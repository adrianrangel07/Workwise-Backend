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
import Proyectodeaula.Workwise.Model.Personas.Persona;
import Proyectodeaula.Workwise.Repository.Oferta.OfertaRepository;
import Proyectodeaula.Workwise.Repository.Persona.Repository_Persona;
import Proyectodeaula.Workwise.Security.Token.JwtUtil;
import Proyectodeaula.Workwise.Service.Config.MlClient;
import Proyectodeaula.Workwise.Service.Config.MlClient.PrediccionResponse;

/**
 * POST /api/ofertas/{id}/predecir
 *
 * Body JSON que envía Flutter:
 * {
 * "experiencia": 3,
 * "nivel_estudio": "Universitario",
 * "sector": "Tecnología"
 * }
 *
 * Spring Boot calcula las 8 variables y llama a la API Python v3.
 */
@RestController
@RequestMapping("/api/ofertas")
public class PrediccionController {

    // ── Nivel educativo → índice numérico (0-6) ───────────────────────────────
    // Claves normalizadas (sin tildes, sin espacios extra, lowercase handled en
    // normalizar())
    private static final Map<String, Integer> NIVEL_PESO = Map.of(
            "Sin_estudios", 0,
            "Bachiller", 1,
            "Tecnico_Tecnologo", 2,
            "Tecnologo_Universitario", 3,
            "Universitario", 4,
            "Master", 5,
            "Doctorado", 6);

    // ── Categoría unificada → índice numérico ────────────────────────────────
    // Este mapa cubre AMBOS vocabularios:
    // · sector_oferta (texto libre que pone la empresa al crear la oferta)
    // · categoria_profesion.nombre (asignado por ClasificacionProfesionalService)
    // Ambos se normalizan con normalizarCategoria() antes de buscar aquí.
    // Debe coincidir con SECTOR_PESO en model.py / api.py.
    private static final Map<String, Integer> CATEGORIA_IDX = Map.ofEntries(
            Map.entry("tecnologia", 0), // sector: "Tecnología" | cat: "Tecnología", "Desarrollo"
            Map.entry("desarrollo", 0), // cat: "Desarrollo" → mismo grupo que Tecnología
            Map.entry("salud", 1), // sector: "Salud" | cat: "Salud"
            Map.entry("educacion", 2), // sector: "Educación" | cat: "Educación"
            Map.entry("finanzas", 3), // sector: "Finanzas" | cat: "Administración", "Finanzas"
            Map.entry("administracion", 3), // cat: "Administración" → mismo grupo que Finanzas
            Map.entry("construccion", 4), // sector: "Construcción"| cat: "Ingeniería"
            Map.entry("ingenieria", 4), // cat: "Ingeniería" → mismo grupo que Construcción
            Map.entry("marketing", 5), // sector: "Marketing" | cat: "Marketing y Ventas", "Diseño"
            Map.entry("diseno", 5), // cat: "Diseño" → mismo grupo que Marketing
            Map.entry("logistica", 6), // sector: "Logística" | cat: "Logística"
            Map.entry("ecommerce", 7), // sector: "E-commerce"
            Map.entry("turismo", 8), // sector: "Turismo" | cat: "Gastronomía y Turismo"
            Map.entry("gastronomia", 8), // cat: "Gastronomía y Turismo"
            Map.entry("industria", 9), // sector: "Industria" | cat: "Industria y Producción"
            Map.entry("agricultura", 10) // cat: "Agricultura"
    // cualquier otro → 11 (Otros, default en normalizarCategoria)
    );

    private final MlClient mlClient;
    private final OfertaRepository ofertaRepo;
    private final Repository_Persona personaRepo;
    private final JwtUtil jwtUtil;

    public PrediccionController(MlClient mlClient,
            OfertaRepository ofertaRepo,
            Repository_Persona personaRepo,
            JwtUtil jwtUtil) {
        this.mlClient = mlClient;
        this.ofertaRepo = ofertaRepo;
        this.personaRepo = personaRepo;
        this.jwtUtil = jwtUtil;
    }

    // ─── DTO de entrada desde Flutter ────────────────────────────────────────

    public record CandidatoInput(
            int experiencia, // años de experiencia del candidato
            String nivel_estudio // e.g. "Universitario"
    // sector NO viene del formulario — se lee de categoria_profesion de la persona
    ) {
    }

    // ─── Endpoint ────────────────────────────────────────────────────────────

    @PostMapping("/{id}/predecir")
    public ResponseEntity<?> predecir(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authHeader,
            @RequestBody CandidatoInput candidato) {

        // 1. Oferta
        Oferta oferta = ofertaRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Oferta no encontrada: " + id));

        // 2. Persona autenticada
        String email = jwtUtil.extractEmailFromHeader(authHeader);
        Persona persona = personaRepo.findByEmail(email);
        if (persona == null) {
            return ResponseEntity.status(401).body("Usuario no encontrado");
        }

        // ── 3. Calcular las 8 variables ──────────────────────────────────────

        // 3a. Habilidades — cuantitativas
        Set<Long> idsOferta = oferta.getHabilidades().stream()
                .map(oh -> oh.getHabilidad().getId())
                .collect(Collectors.toSet());

        Set<Long> idsPersona = persona.getHabilidades().stream()
                .map(ph -> ph.getHabilidad().getId())
                .collect(Collectors.toSet());

        // Si la oferta no requiere habilidades, se trata como requisito cumplido (1/1)
        // evitando que el modelo reciba 0/0, combinación fuera del rango de
        // entrenamiento.
        // Si el usuario no tiene habilidades, habMatch queda en 0 y penaliza
        // correctamente.
        int habOferta = idsOferta.isEmpty() ? 1 : idsOferta.size();
        int habMatch = idsOferta.isEmpty() ? 1
                : (int) idsPersona.stream().filter(idsOferta::contains).count();

        // 3b. Experiencia
        int expOferta = oferta.getExperiencia();
        int expCandidato = candidato.experiencia();

        // 3c. Nivel educativo (0-6)
        int nivelOferta = NIVEL_PESO.getOrDefault(normalizarNivel(oferta.getNivel_Educacion()), 0);
        int nivelCandidato = NIVEL_PESO.getOrDefault(candidato.nivel_estudio(), 0);

        // 3d. Categoría / Sector
        // La oferta tiene un campo sector_oferta (texto libre de la empresa).
        // La persona tiene categoria_profesion asignada por
        // ClasificacionProfesionalService.
        // Ambos se convierten al mismo índice numérico con normalizarCategoria().
        int sectorOferta = normalizarCategoria(oferta.getSector_oferta());
        int sectorCandidato;
        if (persona.getCategoria() != null && persona.getCategoria().getNombre() != null) {
            sectorCandidato = normalizarCategoria(persona.getCategoria().getNombre());
        } else {
            // Persona sin categoría asignada → índice 11 (Otros), no penaliza
            // extremadamente
            sectorCandidato = 11;
        }

        // 4. Request a Python
        MlClient.PrediccionRequest request = new MlClient.PrediccionRequest(
                habOferta,
                habMatch,
                expOferta,
                expCandidato,
                nivelOferta,
                nivelCandidato,
                sectorOferta,
                sectorCandidato);

        // 5. Llamar a Python
        PrediccionResponse response = mlClient.predecir(request);

        // 6. Respuesta enriquecida al frontend
        // Nota: Map.of() acepta máximo 10 pares; usamos Map.ofEntries() para el
        // detalle.
        int habPct = habOferta > 0 ? (int) ((habMatch * 100.0) / habOferta) : 0;

        Map<String, Object> detalle = Map.ofEntries(
                Map.entry("habilidades_oferta", habOferta),
                Map.entry("habilidades_match", habMatch),
                Map.entry("habilidades_pct", habPct),
                Map.entry("experiencia_oferta", expOferta),
                Map.entry("experiencia_candidato", expCandidato),
                Map.entry("cumple_experiencia", expCandidato >= expOferta),
                Map.entry("nivel_oferta", nivelOferta),
                Map.entry("nivel_candidato", nivelCandidato),
                Map.entry("cumple_nivel", nivelCandidato >= nivelOferta),
                Map.entry("sector_oferta", sectorOferta),
                Map.entry("sector_candidato", sectorCandidato),
                Map.entry("match_sector", sectorOferta == sectorCandidato));

        return ResponseEntity.ok(Map.of(
                "aceptado", response.aceptado(),
                "probabilidad", response.probabilidad(),
                "confianza", response.confianza(),
                "mensaje", response.mensaje(),
                "detalle", detalle));
    }

    // ─── Helpers ──────────────────────────────────────────────────────────────

    /**
     * Mapea el valor de nivel_educacion de la BD a las claves de NIVEL_PESO.
     * Elimina tildes y maneja variaciones comunes.
     */
    private String normalizarNivel(String valor) {
        if (valor == null)
            return "Sin_estudios";
        String s = java.text.Normalizer
                .normalize(valor.trim(), java.text.Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");

        return switch (s.toLowerCase()) {
            case "sin estudios", "sin_estudios" -> "Sin_estudios";
            case "bachiller" -> "Bachiller";
            // "Tecnologo" solo (sin "universitario") → lo tratamos como Tecnico_Tecnologo
            // porque en Flutter no existe nivel "Tecnólogo" puro, solo
            // "Tecnico_Tecnologo" (idx 2) y "Tecnologo_Universitario" (idx 3).
            case "tecnico", "tecnologo",
                    "tecnico_tecnologo", "tecnico/tecnologo",
                    "tecnico tecnologo" ->
                "Tecnico_Tecnologo";
            case "tecnologo universitario",
                    "tecnologo_universitario" ->
                "Tecnologo_Universitario";
            case "universitario" -> "Universitario";
            case "master", "maestria", "magister" -> "Master";
            case "doctorado" -> "Doctorado";
            default -> "Sin_estudios";
        };
    }

    /**
     * Convierte sector_oferta (texto de la empresa) O categoria_profesion.nombre
     * (texto del servicio de clasificación) al mismo índice numérico del modelo.
     *
     * Normalización: elimina tildes, pasa a lowercase, elimina caracteres
     * especiales.
     * Luego busca en CATEGORIA_IDX. Si no hay coincidencia → 11 (Otros).
     */
    private int normalizarCategoria(String valor) {
        if (valor == null || valor.isBlank())
            return 11;

        String s = java.text.Normalizer
                .normalize(valor.trim(), java.text.Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                .toLowerCase()
                // quitar caracteres especiales para unificar "e-commerce" → "ecommerce"
                .replaceAll("[^a-z0-9]", "");

        // Buscar coincidencia exacta normalizada
        Integer idx = CATEGORIA_IDX.get(s);
        if (idx != null)
            return idx;

        // Coincidencias parciales para nombres compuestos del servicio de clasificación
        // ej: "industria y produccion", "marketing y ventas", "gastronomiayturismo"
        if (s.contains("tecnolog") || s.contains("software") || s.contains("sistem"))
            return 0;
        if (s.contains("salud") || s.contains("medic") || s.contains("clinic"))
            return 1;
        if (s.contains("educ") || s.contains("docen") || s.contains("pedagog"))
            return 2;
        if (s.contains("finanz") || s.contains("adminis") || s.contains("contad"))
            return 3;
        if (s.contains("construc") || s.contains("ingenier") || s.contains("infraestr"))
            return 4;
        if (s.contains("market") || s.contains("venta") || s.contains("diseno")
                || s.contains("publicid") || s.contains("creativi"))
            return 5;
        if (s.contains("logist") || s.contains("transport") || s.contains("almac"))
            return 6;
        if (s.contains("ecommerc") || s.contains("comercio"))
            return 7;
        if (s.contains("turism") || s.contains("hotel") || s.contains("gastron"))
            return 8;
        if (s.contains("industr") || s.contains("manufact") || s.contains("producc"))
            return 9;
        if (s.contains("agricul") || s.contains("agropec") || s.contains("agron"))
            return 10;

        return 11; // Otros
    }
}