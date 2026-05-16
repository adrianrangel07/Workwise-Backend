package Proyectodeaula.Workwise.Controller.General;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Proyectodeaula.Workwise.Model.Ofertas.Oferta;
import Proyectodeaula.Workwise.Model.Otros.CategoriaProfesional;
import Proyectodeaula.Workwise.Model.Personas.Persona;
import Proyectodeaula.Workwise.Repository.Oferta.OfertaRepository;
import Proyectodeaula.Workwise.Repository.Persona.Repository_Persona;
import Proyectodeaula.Workwise.Security.Token.JwtUtil;
import Proyectodeaula.Workwise.Service.Config.MlClient;
import Proyectodeaula.Workwise.Service.Config.MlClient.PrediccionResponse;





/**
 * POST /api/ofertas/{id}/predecir
 *
 * Body:
 * {
 *   "experiencia":   3,
 *   "nivel_estudio": "Universitario"
 * }
 *
 * Spring Boot calcula las 7 variables y llama a la API Python.
 */
@RestController
@RequestMapping("/api/ofertas")
public class PrediccionController {

    // Mapa de nivel de estudio → peso numérico (igual que en Python)
    private static final Map<String, Integer> NIVEL_PESO = Map.of(
        "Sin_estudios",            0,
        "Bachiller",               1,
        "Tecnico_Tecnologo",       2,
        "Tecnologo_Universitario", 3,
        "Universitario",           4,
        "Master",                  5,
        "Doctorado",               6
    );

    private final MlClient         mlClient;
    private final OfertaRepository ofertaRepo;
    private final Repository_Persona personaRepo;
    private final JwtUtil          jwtUtil;

    public PrediccionController(MlClient mlClient,
                                OfertaRepository ofertaRepo,
                                Repository_Persona personaRepo,
                                JwtUtil jwtUtil) {
        this.mlClient    = mlClient;
        this.ofertaRepo  = ofertaRepo;
        this.personaRepo = personaRepo;
        this.jwtUtil     = jwtUtil;
    }

    // ─── DTO entrada ─────────────────────────────────────────────────────────

    public record CandidatoInput(
        int    experiencia,    // años de experiencia del candidato
        String nivel_estudio   // nivel de estudio del candidato
    ) {}

    // ─── Endpoint ────────────────────────────────────────────────────────────

    @PostMapping("/{id}/predecir")
    public ResponseEntity<?> predecir(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authHeader,
            @RequestBody CandidatoInput candidato) {

        // 1. Obtener la oferta
        Oferta oferta = ofertaRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("Oferta no encontrada: " + id));

        // 2. Obtener el perfil del candidato autenticado
        String email   = jwtUtil.extractEmailFromHeader(authHeader);
        Persona persona = personaRepo.findByEmail(email);
        if (persona == null) {
            return ResponseEntity.status(401).body("Usuario no encontrado");
        }

        // 3. Calcular las 7 variables

        // 3a. Experiencia
        int expCandidato   = candidato.experiencia();
        int expOferta      = oferta.getExperiencia();
        int cumpleExp      = expCandidato >= expOferta ? 1 : 0;
        int brechaExp      = expCandidato - expOferta;

        // 3b. Nivel de estudio
        int nivelCandidato = NIVEL_PESO.getOrDefault(candidato.nivel_estudio(), 0);
        int nivelOferta    = NIVEL_PESO.getOrDefault(normalizar(oferta.getNivel_Educacion()), 0);
        int cumpleNivel    = nivelCandidato >= nivelOferta ? 1 : 0;

        // 3c. Match de habilidades
        //     Habilidades que requiere la oferta
        Set<Long> idsOferta = oferta.getHabilidades().stream()
            .map(oh -> oh.getHabilidad().getId())
            .collect(Collectors.toSet());

        //     Habilidades que tiene el candidato
        Set<Long> idsPersona = persona.getHabilidades().stream()
            .map(ph -> ph.getHabilidad().getId())
            .collect(Collectors.toSet());

        double matchHabilidades = 0.0;
        if (!idsOferta.isEmpty()) {
            long enComun = idsPersona.stream().filter(idsOferta::contains).count();
            matchHabilidades = (double) enComun / idsOferta.size();
        }

        // 3d. Match de categoría
        int matchCategoria = 0;
        CategoriaProfesional catPersona = persona.getCategoria();
        CategoriaProfesional catOferta  = oferta.getCategoria();
        if (catPersona != null && catOferta != null
                && catPersona.getId().equals(catOferta.getId())) {
            matchCategoria = 1;
        }

        // 4. Construir request para Python
        MlClient.PrediccionRequest request = new MlClient.PrediccionRequest(
            expCandidato,
            cumpleExp,
            brechaExp,
            nivelCandidato,
            cumpleNivel,
            matchHabilidades,
            matchCategoria
        );

        // 5. Llamar a la API Python
        PrediccionResponse response = mlClient.predecir(request);

        // 6. Enriquecer la respuesta con el detalle calculado
        return ResponseEntity.ok(Map.of(
            "aceptado",     response.aceptado(),
            "probabilidad", response.probabilidad(),
            "confianza",    response.confianza(),
            "mensaje",      response.mensaje(),
            "detalle", Map.of(
                "cumple_experiencia",    cumpleExp == 1,
                "brecha_experiencia",    brechaExp,
                "cumple_nivel_estudio",  cumpleNivel == 1,
                "match_habilidades_pct", (int)(matchHabilidades * 100),
                "match_categoria",       matchCategoria == 1
            )
        ));
    }

    /**
     * Normaliza texto para comparar con las claves del mapa NIVEL_PESO.
     * Ej: "Técnico" → "Tecnico_Tecnologo" no aplica aquí,
     * pero sí elimina tildes para que "Técnico" no falle.
     *
     * ⚠ Los valores en tu BD deben coincidir exactamente con las claves del mapa.
     * Si usas "Universitario" en la BD → funciona.
     * Si usas "universitario" → falla. Ajusta según tus datos reales.
     */
    private String normalizar(String valor) {
        if (valor == null) return "";
        return java.text.Normalizer
            .normalize(valor, java.text.Normalizer.Form.NFD)
            .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }
}