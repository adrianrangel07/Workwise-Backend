package Proyectodeaula.Workwise.Controller.General;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import Proyectodeaula.Workwise.Service.Config.PasswordResetService;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class PasswordResetController {

    private final PasswordResetService resetService;

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestParam String email) {
        resetService.sendPasswordResetToken(email);
        return ResponseEntity.ok("Código enviado al correo");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(
            @RequestParam String email,
            @RequestParam String token,
            @RequestParam String newPassword) {
        resetService.resetPassword(email, token, newPassword);
        return ResponseEntity.ok("Contraseña actualizada correctamente");
    }
}
