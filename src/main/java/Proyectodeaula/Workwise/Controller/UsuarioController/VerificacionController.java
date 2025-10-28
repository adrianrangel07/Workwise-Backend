package Proyectodeaula.Workwise.Controller.UsuarioController;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Proyectodeaula.Workwise.Model.Persona;
import Proyectodeaula.Workwise.Repository.Persona.Repository_Persona;
import Proyectodeaula.Workwise.RepositoryService.VerificacionCodeGenerator;
import Proyectodeaula.Workwise.Security.Token.JwtUtil;
import Proyectodeaula.Workwise.Service.Config.EmailService;

@RestController
@RequestMapping("/api/verificacion")
public class VerificacionController {

    private final JwtUtil jwtUtil;
    private final Repository_Persona personaRepository;
    private final VerificacionCodeGenerator codeGenerator;
    private final EmailService emailService;

    public VerificacionController(JwtUtil jwtUtil,
            Repository_Persona personaRepository,
            VerificacionCodeGenerator codeGenerator,
            EmailService emailService) {
        this.jwtUtil = jwtUtil;
        this.personaRepository = personaRepository;
        this.codeGenerator = codeGenerator;
        this.emailService = emailService;
    }
    
    // ✅ Enviar código de verificación
    @PostMapping("/enviar")
    public ResponseEntity<?> enviarCodigo(@RequestHeader("Authorization") String authHeader) {
        // Extraer el email directamente del token
        String email = jwtUtil.extractEmailFromHeader(authHeader);
        if (email == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido o ausente");
        }

        Persona persona = personaRepository.findByEmail(email);
        if (persona == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        }

        if (Boolean.TRUE.equals(persona.getVerificado())) {
            return ResponseEntity.badRequest().body("El correo ya fue verificado");
        }

        // Generar y guardar el código
        String codigo = codeGenerator.generarCodigo();
        persona.setCodigoVerificacion(codigo);
        personaRepository.save(persona);

        // Enviar el email
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

        if (persona.getCodigoVerificacion() != null &&
                persona.getCodigoVerificacion().equals(codigoIngresado)) {

            persona.setVerificado(true);
            persona.setCodigoVerificacion(null);
            personaRepository.save(persona);

            return ResponseEntity.ok("Correo verificado exitosamente");
        }

        return ResponseEntity.badRequest().body("Código incorrecto o expirado");
    }

}
