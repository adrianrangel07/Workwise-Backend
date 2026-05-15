package Proyectodeaula.Workwise.Controller.General;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Proyectodeaula.Workwise.Model.Ofertas.Oferta;
import Proyectodeaula.Workwise.Repository.Oferta.OfertaRepository;
import Proyectodeaula.Workwise.Service.Config.MlClient;
import Proyectodeaula.Workwise.Service.Config.MlClient.PrediccionRequest;
import Proyectodeaula.Workwise.Service.Config.MlClient.PrediccionResponse;

/**
 * Endpoint que el frontend (Angular/Flutter) llama
 * para obtener la predicción de aceptación de un candidato.
 *
 * El candidato ingresa manualmente su experiencia y nivel de estudio
 * a través del modal en la app, y esos datos llegan en el body.
 */
@RestController
@RequestMapping("/api/prediccion/ofertas")
public class PrediccionController {

    private final MlClient mlClient;
    private final OfertaRepository ofertaRepo;

    public PrediccionController(MlClient mlClient, OfertaRepository ofertaRepo) {
        this.mlClient   = mlClient;
        this.ofertaRepo = ofertaRepo;
    }

    // ─── DTO del request del candidato ───────────────────────────────────────

    public record CandidatoInput(
        int    experiencia,   // Años de experiencia que ingresa el usuario
        String nivel_estudio  // Nivel de estudio que selecciona el usuario
    ) {}

    // ─── Endpoint principal ───────────────────────────────────────────────────

    /**
     * POST /api/prediccion/ofertas/{id}/predecir
     *
     * Body (JSON):
     * {
     *   "experiencia": 3,
     *   "nivel_estudio": "Universitario"
     * }
     *
     * Toma los datos de la oferta desde la BD y los combina con
     * los datos del candidato para llamar a la API Python.
     */
    @PostMapping("/{id}/predecir")
    public ResponseEntity<PrediccionResponse> predecir(
            @PathVariable Long id,
            @RequestBody CandidatoInput candidato) {

        // 1. Obtener la oferta desde la BD
        Oferta oferta = ofertaRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("Oferta no encontrada: " + id));

        // 2. Construir el request combinando oferta + datos del candidato
        //    MlClient se encarga de normalizar tildes automáticamente
        PrediccionRequest request = new PrediccionRequest(
            oferta.getTipo_Empleo(),    // ej: "Tiempo_Completo"
            oferta.getModalidad(),      // ej: "Presencial"
            oferta.getTipo_Contrato(),  // ej: "Fijo"
            candidato.experiencia(),    // ej: 3
            candidato.nivel_estudio(),  // ej: "Universitario"
            oferta.getSector_oferta()   // ej: "Tecnología" → se normaliza a "Tecnologia"
        );

        // 3. Llamar a la API Python y retornar la respuesta
        PrediccionResponse response = mlClient.predecir(request);
        return ResponseEntity.ok(response);
    }
}