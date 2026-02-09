package Proyectodeaula.Workwise.Controller.General;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import Proyectodeaula.Workwise.Model.Dto.VerificationResponse;
import Proyectodeaula.Workwise.Repository.Persona.Repository_Persona;

@RestController
@RequestMapping("/api/verificacion")
public class VerificationController {

    @Autowired
    private Repository_Persona personaRepository;

    // Verificar si el email existe
    @GetMapping("/verificar-email")
    public ResponseEntity<VerificationResponse> verificarEmail(@RequestParam String email) {
        boolean existe = personaRepository.existsByEmail(email);
        String mensaje = existe ? "El correo electrónico ya está registrado" : "Correo electrónico disponible";

        return ResponseEntity.ok(new VerificationResponse(existe, mensaje));
    }

    // Verificar si el documento existe
    @GetMapping("/verificar-documento")
    public ResponseEntity<VerificationResponse> verificarDocumento(@RequestParam int numeroDocumento) {
        boolean existe = personaRepository.existsByNumeroDocumento(numeroDocumento);
        String mensaje = existe ? "El número de documento ya está registrado" : "Documento disponible";

        return ResponseEntity.ok(new VerificationResponse(existe, mensaje));
    }

    // Verificar ambos (email y documento) en una sola llamada
    @GetMapping("/verificar-registro")
    public ResponseEntity<Map<String, Object>> verificarRegistro(
            @RequestParam(required = false) String email,
            @RequestParam(required = false) Integer numeroDocumento) {

        Map<String, Object> response = new HashMap<>();

        if (email != null && !email.trim().isEmpty()) {
            boolean emailExiste = personaRepository.existsByEmail(email);
            response.put("emailExiste", emailExiste);
            response.put("emailMensaje", emailExiste ? "Correo ya registrado" : "Correo disponible");
        }

        if (numeroDocumento != null) {
            boolean documentoExiste = personaRepository.existsByNumeroDocumento(numeroDocumento);
            response.put("documentoExiste", documentoExiste);
            response.put("documentoMensaje", documentoExiste ? "Documento ya registrado" : "Documento disponible");
        }

        return ResponseEntity.ok(response);
    }
}