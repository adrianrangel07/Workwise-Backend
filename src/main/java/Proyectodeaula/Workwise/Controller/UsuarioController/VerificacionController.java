package Proyectodeaula.Workwise.Controller.UsuarioController;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Proyectodeaula.Workwise.Model.EmailVerification;
import Proyectodeaula.Workwise.Model.Persona;
import Proyectodeaula.Workwise.Repository.General.EmailVerificationRepository;
import Proyectodeaula.Workwise.Repository.Persona.Repository_Persona;
import Proyectodeaula.Workwise.RepositoryService.VerificacionCodeGenerator;
import Proyectodeaula.Workwise.Security.Token.JwtUtil;
import Proyectodeaula.Workwise.Service.Config.EmailService;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/verificacion")
@AllArgsConstructor
public class VerificacionController {

    private final JwtUtil jwtUtil;
    private final Repository_Persona personaRepository;
    private final EmailVerificationRepository verificationRepository;
    private final VerificacionCodeGenerator codeGenerator;
    private final EmailService emailService;

    // ✅ Enviar código de verificación
    @PostMapping("/enviar")
    public ResponseEntity<?> enviarCodigo(@RequestHeader("Authorization") String authHeader) {
        String email = jwtUtil.extractEmailFromHeader(authHeader);
        if (email == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido o ausente");
        }

        Persona persona = personaRepository.findByEmail(email);
        if (persona == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        }

        // Verificar si ya existe un código no usado
        EmailVerification verification = verificationRepository
                .findByPersonaId(persona.getId())
                .orElse(new EmailVerification());

        if (verification.isVerificado()) {
            return ResponseEntity.badRequest().body("El correo ya fue verificado");
        }

        // Generar código
        String codigo = codeGenerator.generarCodigo();
        verification.setCodigo(codigo);
        verification.setPersona(persona);
        verification.setVerificado(false);
        verification.setFechaCreacion(LocalDateTime.now());
        verification.setFechaExpiracion(LocalDateTime.now().plusMinutes(15));

        verificationRepository.save(verification);

        // Enviar email
        emailService.enviarEmail(email, "Código de verificación", codigo);

        return ResponseEntity.ok("Código de verificación enviado al correo " + email);
    }

    // ✅ Verificar código de verificación
    @PostMapping("/verificar")
    public ResponseEntity<?> verificarCodigo(@RequestHeader("Authorization") String authHeader,
            @RequestBody Map<String, String> body) {

        String email = jwtUtil.extractEmailFromHeader(authHeader);
        if (email == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido o ausente");
        }

        String codigoIngresado = body.get("codigo");
        if (codigoIngresado == null || codigoIngresado.isBlank()) {
            return ResponseEntity.badRequest().body("Debe ingresar un código");
        }

        Persona persona = personaRepository.findByEmail(email);
        if (persona == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        }

        EmailVerification verification = verificationRepository.findByPersonaId(persona.getId())
                .orElse(null);

        if (verification == null || verification.isVerificado()
                || verification.getFechaExpiracion().isBefore(LocalDateTime.now())) {
            return ResponseEntity.badRequest().body("Código incorrecto o expirado");
        }

        if (verification.getCodigo().equals(codigoIngresado)) {
            verification.setVerificado(true);
            verificationRepository.save(verification);
            return ResponseEntity.ok("Correo verificado exitosamente");
        }

        return ResponseEntity.badRequest().body("Código incorrecto o expirado");
    }
}