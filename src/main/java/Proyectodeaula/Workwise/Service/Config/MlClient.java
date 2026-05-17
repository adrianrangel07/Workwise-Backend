package Proyectodeaula.Workwise.Service.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Cliente HTTP hacia la API Python v3.
 * Envía las 8 variables cuantitativas calculadas por PrediccionController.
 */
@Service
public class MlClient {

    @Value("${ml.api.url}")
    private String mlApiUrl;

    private final RestTemplate restTemplate;

    public MlClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // ─── DTOs ────────────────────────────────────────────────────────────────

    /**
     * Las 8 variables que recibe la red neuronal.
     *
     * @param habilidades_oferta    cuántas habilidades pide la oferta
     * @param habilidades_match     cuántas de esas tiene el candidato
     * @param experiencia_oferta    años mínimos requeridos por la oferta
     * @param experiencia_candidato años del candidato (formulario)
     * @param nivel_oferta          nivel educativo requerido (0-6)
     * @param nivel_candidato       nivel educativo del candidato (0-6)
     * @param sector_oferta         sector de la oferta (índice numérico)
     * @param sector_candidato      sector del candidato (índice numérico)
     */
    public record PrediccionRequest(
        int habilidades_oferta,
        int habilidades_match,
        int experiencia_oferta,
        int experiencia_candidato,
        int nivel_oferta,
        int nivel_candidato,
        int sector_oferta,
        int sector_candidato
    ) {}

    public record PrediccionResponse(
        boolean aceptado,
        double  probabilidad,
        String  confianza,
        String  mensaje
    ) {}

    // ─── Llamada a Python ─────────────────────────────────────────────────────

    public PrediccionResponse predecir(PrediccionRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<PrediccionRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<PrediccionResponse> response = restTemplate.postForEntity(
            mlApiUrl + "/predict",
            entity,
            PrediccionResponse.class
        );

        return response.getBody();
    }
}