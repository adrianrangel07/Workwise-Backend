package Proyectodeaula.Workwise.Service.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Cliente HTTP hacia la API Python v2.
 * Envía las 7 variables ya calculadas por PrediccionController.
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

    public record PrediccionRequest(
        int    experiencia_candidato,
        int    cumple_experiencia,
        int    brecha_experiencia,
        int    nivel_candidato,
        int    cumple_nivel,
        double match_habilidades,
        int    match_categoria
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