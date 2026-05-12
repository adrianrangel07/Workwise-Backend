package Proyectodeaula.Workwise.Service.Config;

import java.text.Normalizer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Cliente HTTP que llama a la API Python desplegada en Render.
 * Normaliza automáticamente los valores antes de enviarlos
 * (elimina tildes para compatibilidad con la API Python).
 */
@Service
public class MlClient {

    @Value("${ml.api.url}")
    private String mlApiUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    // ─── DTOs ────────────────────────────────────────────────────────────────

    public record PrediccionRequest(
        String tipo_empleo,
        String modalidad,
        String tipo_contrato,
        int    experiencia,
        String nivel_estudio,
        String sector
    ) {}

    public record PrediccionResponse(
        boolean aceptado,
        double  probabilidad,
        String  confianza,
        String  mensaje
    ) {}

    // ─── Método principal ─────────────────────────────────────────────────────

    public PrediccionResponse predecir(PrediccionRequest request) {
        // Normalizar antes de enviar (quita tildes)
        PrediccionRequest normalizado = new PrediccionRequest(
            normalizar(request.tipo_empleo()),
            normalizar(request.modalidad()),
            normalizar(request.tipo_contrato()),
            request.experiencia(),
            normalizar(request.nivel_estudio()),
            normalizar(request.sector())
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<PrediccionRequest> entity = new HttpEntity<>(normalizado, headers);

        ResponseEntity<PrediccionResponse> response = restTemplate.postForEntity(
            mlApiUrl + "/predict",
            entity,
            PrediccionResponse.class
        );
        System.out.println("ML API URL = " + mlApiUrl);
        return response.getBody();
    }

    // ─── Normalización ────────────────────────────────────────────────────────

    /**
     * Elimina tildes y diacríticos para compatibilidad con la API Python.
     * Ej: "Tecnología" → "Tecnologia"
     *     "Construcción" → "Construccion"
     *     "Diseño" → "Diseno"
     */
    private String normalizar(String valor) {
        if (valor == null) return null;
        return Normalizer
            .normalize(valor, Normalizer.Form.NFD)
            .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }
}
