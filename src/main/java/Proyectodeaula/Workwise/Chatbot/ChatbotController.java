package Proyectodeaula.Workwise.Chatbot;

import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/chatbot")
public class ChatbotController {

    private final ChatbotService chatbotService;

    public ChatbotController(ChatbotService chatbotService) {
        this.chatbotService = chatbotService;
    }

    // Endpoint para manejar solicitudes de chat
    @PostMapping("/message")
    public Map<String, Object> procesarMensaje(@RequestBody Map<String, String> request) {
        String mensaje = request.get("mensaje");
        return chatbotService.obtenerRespuesta(mensaje);
    }
}