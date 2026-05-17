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

        // ── Sector → índice numérico ───────────────────────────────────────────────
        // Debe coincidir EXACTAMENTE con el orden en model.py / api.py
        private static final Map<String, Integer> SECTOR_PESO = Map.of(
                        "Tecnología", 0,
                        "Salud", 1,
                        "Educación", 2,
                        "Finanzas", 3,
                        "Construcción", 4,
                        "Marketing", 5,
                        "Logística", 6,
                        "E-commerce", 7,
                        "Turismo", 8,
                        "Industria", 9
        // "Otro" → 10 (default en normalizarSector)
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
                        String nivel_estudio, // e.g. "Universitario"
                        String sector // e.g. "Tecnología"
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

                int habOferta = idsOferta.size();
                int habMatch = (int) idsPersona.stream().filter(idsOferta::contains).count();

                // 3b. Experiencia
                int expOferta = oferta.getExperiencia();
                int expCandidato = candidato.experiencia();

                // 3c. Nivel educativo (0-6)
                int nivelOferta = NIVEL_PESO.getOrDefault(normalizarNivel(oferta.getNivel_Educacion()), 0);
                int nivelCandidato = NIVEL_PESO.getOrDefault(candidato.nivel_estudio(), 0);

                // 3d. Sector (índice numérico)
                int sectorOferta = normalizarSector(oferta.getSector_oferta());
                int sectorCandidato = normalizarSector(candidato.sector());

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
                        case "tecnico", "tecnologo",
                                        "tecnico_tecnologo", "tecnico/tecnologo" ->
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
         * Mapea el sector (texto libre) al índice numérico del modelo.
         * Devuelve 10 ("Otro") si no coincide con ninguno conocido.
         */
        private int normalizarSector(String sector) {
                if (sector == null)
                        return 10;
                // Intenta coincidencia directa primero
                Integer idx = SECTOR_PESO.get(sector.trim());
                if (idx != null)
                        return idx;

                // Coincidencia sin tildes, lowercase
                String s = java.text.Normalizer
                                .normalize(sector.trim(), java.text.Normalizer.Form.NFD)
                                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                                .toLowerCase();

                return switch (s) {
                        case "tecnologia", "tech", "software", "ti" -> 0;
                        case "salud", "health", "medicina" -> 1;
                        case "educacion", "education" -> 2;
                        case "finanzas", "finance", "banca", "fintech" -> 3;
                        case "construccion", "infraestructura" -> 4;
                        case "marketing", "publicidad" -> 5;
                        case "logistica", "transporte" -> 6;
                        case "e-commerce", "ecommerce", "comercio electronico" -> 7;
                        case "turismo", "hoteleria" -> 8;
                        case "industria", "manufactura" -> 9;
                        default -> 10;
                };
        }
}