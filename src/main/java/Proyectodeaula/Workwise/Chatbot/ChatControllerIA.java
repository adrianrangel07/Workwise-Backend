package Proyectodeaula.Workwise.Chatbot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/api")
public class ChatControllerIA {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${OPENROUTER_API_KEY:}")
    private String apiKeyFromProperties;

    private final String apiKeyFromEnv = System.getenv("OPENROUTER_API_KEY");

    public ChatControllerIA() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    // Endpoint para manejar solicitudes de chat
    @GetMapping("/chat")
    public ResponseEntity<String> chat(@RequestParam("message") String message) {
        try {
            if (message == null || message.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("El mensaje no puede estar vacío");
            }

            String apiKey = getApiKey();
            if (apiKey == null || apiKey.isEmpty()) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("API Key no configurada. Formas de configurar:<br>" +
                                "1. Crear archivo .env con OPENROUTER_API_KEY=tu_key<br>" +
                                "2. Variable de entorno OPENROUTER_API_KEY<br>" +
                                "3. En application.properties: OPENROUTER_API_KEY=tu_key<br>" +
                                "API Key desde propiedades: " + (apiKeyFromProperties != null ? "SÍ" : "NO") + "<br>" +
                                "API Key desde ENV: " + (apiKeyFromEnv != null ? "SÍ" : "NO"));
            }

            String response = callGemmaAPI(message, apiKey);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Error al procesar la solicitud: " + e.getMessage());
        }
    }

    private String getApiKey() {
        // Prioridad 1: Desde properties
        if (apiKeyFromProperties != null && !apiKeyFromProperties.isEmpty()) {
            return apiKeyFromProperties;
        }
        // Prioridad 2: Desde variables de entorno
        if (apiKeyFromEnv != null && !apiKeyFromEnv.isEmpty()) {
            return apiKeyFromEnv;
        }
        return null;
    }

    private String callGemmaAPI(String message, String apiKey) throws Exception {
        String url = "https://openrouter.ai/api/v1/chat/completions";

        // Headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + apiKey);
        headers.set("HTTP-Referer", "https://workwisenav.vercel.app");
        headers.set("X-Title", "Workwise Chatbot");
        
        // Request body
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "google/gemma-3-27b-it:free");

        List<Map<String, String>> messages = new ArrayList<>();
        Map<String, String> userMessage = new HashMap<>();
        userMessage.put("role", "user");
        userMessage.put("content", message);
        messages.add(userMessage);

        requestBody.put("messages", messages);
        requestBody.put("temperature", 0.7);
        requestBody.put("max_tokens", 1000);

        // Create request entity
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        // Make API call
        ResponseEntity<String> response = restTemplate.exchange(
                url, HttpMethod.POST, requestEntity, String.class);

        // Parse response
        JsonNode root = objectMapper.readTree(response.getBody());
        JsonNode choices = root.path("choices");
        if (choices.isArray() && choices.size() > 0) {
            JsonNode messageNode = choices.get(0).path("message");
            return messageNode.path("content").asText();
        } else {
            throw new RuntimeException("No response from AI model: " + response.getBody());
        }
    }

    // Endpoint adicional para POST requests
    @PostMapping("/chat")
    public ResponseEntity<String> chatPost(@RequestBody ChatRequest request) {
        return chat(request.getMessage());
    }

    // Endpoint para verificar la configuración
    @GetMapping("/chat/config")
    public ResponseEntity<String> checkConfig() {
        String apiKey = getApiKey();
        return ResponseEntity.ok("API Key configurada: " + (apiKey != null && !apiKey.isEmpty() ? "SÍ" : "NO") +
                "<br>Longitud API Key: " + (apiKey != null ? apiKey.length() : "0") +
                "<br>Desde properties: " + (apiKeyFromProperties != null ? "SÍ" : "NO") +
                "<br>Desde ENV: " + (apiKeyFromEnv != null ? "SÍ" : "NO"));
    }

    // Clase para el request body
    public static class ChatRequest {
        private String message;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}