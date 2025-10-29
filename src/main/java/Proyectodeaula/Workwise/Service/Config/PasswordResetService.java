package Proyectodeaula.Workwise.Service.Config;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import Proyectodeaula.Workwise.Model.Empresa;
import Proyectodeaula.Workwise.Model.PasswordResetToken;
import Proyectodeaula.Workwise.Model.Persona;
import Proyectodeaula.Workwise.Repository.Empresa.Repository_Emp;
import Proyectodeaula.Workwise.Repository.General.PasswordResetTokenRepository;
import Proyectodeaula.Workwise.Repository.Persona.Repository_Persona;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PasswordResetService {

    private final PasswordResetTokenRepository tokenRepository;
    private final JavaMailSender mailSender;
    private final BCryptPasswordEncoder passwordEncoder;
    private final Repository_Persona personaRepository;
    private final Repository_Emp empresaRepository;

    private static final SecureRandom secureRandom = new SecureRandom();

    // Generar código numérico seguro de N dígitos
    private String generateNumericCode(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(secureRandom.nextInt(10));
        }
        return sb.toString();
    }

    public void sendPasswordResetToken(String email) {
        // Validar que el correo exista
        boolean exists = (personaRepository.findByEmail(email) != null
                && personaRepository.findByEmail(email).isActivo()) ||
                (empresaRepository.findByEmail(email) != null && empresaRepository.findByEmail(email).isActivo());

        if (!exists) {
            throw new RuntimeException("No existe usuario o empresa con ese correo");
        }

        // Generar código de 6 dígitos
        String rawCode = generateNumericCode(6);
        String hashedCode = passwordEncoder.encode(rawCode);

        // Guardar token con hash
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setEmail(email);
        resetToken.setToken(hashedCode);
        resetToken.setExpirationDate(LocalDateTime.now().plusMinutes(15));
        resetToken.setUsed(false);
        tokenRepository.save(resetToken);

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(email);
            helper.setSubject("Recuperación de contraseña - Workwise");

            String htmlContent = String.format(
                    """
                            <div style="font-family: 'Segoe UI', Arial, sans-serif; background-color: #f5f7fa; padding: 40px;">
                                <div style="max-width: 600px; margin: auto; background: #ffffff; border-radius: 16px; box-shadow: 0 0 20px rgba(0,0,0,0.05); overflow: hidden;">

                                    <div style="background: linear-gradient(135deg, #2f80ed, #1c5fbf); text-align: center; padding: 25px;">
                                        <h2 style="color: white; margin: 0;">Recuperación de contraseña</h2>
                                    </div>

                                    <div style="padding: 35px; text-align: center;">
                                        <p style="font-size: 16px; color: #333;">Hola 👋,</p>
                                        <p style="font-size: 15px; color: #555; margin: 15px 0;">
                                            Solicitaste recuperar tu contraseña en <strong>Workwise</strong>.<br>
                                            Usa el siguiente código para restablecer tu contraseña. Este código expirará en 15 minutos.
                                        </p>

                                        <div style="margin: 25px 0;">
                                            <span style="font-size: 28px; font-weight: bold; letter-spacing: 3px; color: #224abe; background: #eef2ff; padding: 12px 24px; border-radius: 10px; display: inline-block;">
                                                %s
                                            </span>
                                        </div>

                                        <hr style="margin: 35px 0; border: none; border-top: 1px solid #eee;">

                                        <p style="font-size: 12px; color: #999;">
                                            © 2025 Workwise. Todos los derechos reservados.<br>
                                            Este correo fue enviado automáticamente, por favor no respondas.
                                        </p>
                                    </div>
                                </div>
                            </div>
                            """,
                    rawCode);

            helper.setText(htmlContent, true);
            mailSender.send(message);

        } catch (MessagingException | MailException e) {
            System.err.println("❌ Error al enviar correo: " + e.getMessage());
        }
    }

    public void resetPassword(String email, String rawCode, String newPassword) {
        // Obtener tokens no usados y ordenados por fecha descendente
        List<PasswordResetToken> tokens = tokenRepository.findByEmailAndUsedFalseOrderByExpirationDateDesc(email);
        if (tokens.isEmpty()) {
            throw new RuntimeException("Token inválido");
        }

        PasswordResetToken resetToken = tokens.get(0);

        if (resetToken.isExpired()) {
            throw new RuntimeException("Token expirado");
        }

        // Verificar código con hash
        if (!passwordEncoder.matches(rawCode, resetToken.getToken())) {
            throw new RuntimeException("Token inválido");
        }

        // Actualizar contraseña en Usuario
        Persona persona = personaRepository.findByEmail(email);
        if (persona != null) {
            persona.getUsuario().setPassword(passwordEncoder.encode(newPassword));
            personaRepository.save(persona);
        } else {
            Empresa empresa = empresaRepository.findByEmail(email);
            if (empresa != null) {
                empresa.getUsuario().setPassword(passwordEncoder.encode(newPassword));
                empresaRepository.save(empresa);
            } else {
                throw new RuntimeException("No se encontró un usuario o empresa con ese correo");
            }
        }

        // Marcar token como usado
        resetToken.setUsed(true);
        tokenRepository.save(resetToken);
    }
}