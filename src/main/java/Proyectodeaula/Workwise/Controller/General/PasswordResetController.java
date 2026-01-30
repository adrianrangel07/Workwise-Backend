package Proyectodeaula.Workwise.Controller.General;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import Proyectodeaula.Workwise.Service.Config.PasswordResetService;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/password")
@AllArgsConstructor
public class PasswordResetController {

    private final PasswordResetService resetService;

    // Endpoint para solicitar el restablecimiento de contraseña
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestParam String email) {
        resetService.sendPasswordResetToken(email);
        return ResponseEntity.ok(Map.of("message", "Código enviado al correo"));

    }

    // Endpoint para restablecer la contraseña
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> body) {

        String email = body.get("email");
        String token = body.get("token");
        String newPassword = body.get("newPassword");

        resetService.resetPassword(email, token, newPassword);

        return ResponseEntity.ok(Map.of("message", "Contraseña actualizada correctamente"));
    }

}
