package Proyectodeaula.Workwise.Service.Config;

import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

  private final JavaMailSender mailSender;

  public EmailService(JavaMailSender mailSender) {
    this.mailSender = mailSender;
  }

  public void enviarEmail(String to, String subject, String codigo) {
    try {
      MimeMessage message = mailSender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

      helper.setTo(to);
      helper.setSubject(subject);

      String linkVerificacion = "http://localhost:5500/verificacion.html?codigo=" + codigo; // enlace de direccion de
                                                                                            // verificacion

      String htmlContent = String.format(
          """
              <div style="font-family: 'Segoe UI', Arial, sans-serif; background-color: #f5f7fa; padding: 40px;">
                <div style="max-width: 600px; margin: auto; background: #ffffff; border-radius: 16px; box-shadow: 0 0 20px rgba(0,0,0,0.05); overflow: hidden;">

                  <!-- HEADER -->
                  <div style="background: linear-gradient(135deg, #2f80ed, #1c5fbf); text-align: center; padding: 25px;">

                    <h2 style="color: white; margin: 0;">Verificación de correo</h2>
                  </div>

                  <!-- CONTENIDO -->
                  <div style="padding: 35px; text-align: center;">
                    <p style="font-size: 16px; color: #333;">Hola 👋,</p>
                    <p style="font-size: 15px; color: #555; margin: 15px 0;">
                      Gracias por registrarte en <strong>Workwise</strong>.<br>
                      Para completar tu registro, usa el siguiente código o haz clic en el botón de abajo:
                    </p>

                    <div style="margin: 25px 0;">
                      <span style="font-size: 28px; font-weight: bold; letter-spacing: 3px; color: #224abe; background: #eef2ff; padding: 12px 24px; border-radius: 10px; display: inline-block;">
                        %s
                      </span>
                    </div>
                    <a href='%s'
                       style="display: inline-block; background: #2f80ed; color: white; text-decoration: none; padding: 12px 28px; border-radius: 8px; font-weight: 600; margin-top: 15px;">
                      Verificar mi correo
                    </a>

                    <p style="font-size: 13px; color: #777; margin-top: 25px;">
                      Si el botón no funciona, copia y pega este enlace en tu navegador:<br>
                      <a href='%s' style="color: #2f80ed; text-decoration: none;">%s</a>
                    </p>

                    <hr style="margin: 35px 0; border: none; border-top: 1px solid #eee;">

                    <p style="font-size: 12px; color: #999;">
                      © 2025 Workwise. Todos los derechos reservados.<br>
                      Este correo fue enviado automáticamente, por favor no respondas.
                    </p>
                  </div>
                </div>
              </div>
              """,
          codigo, linkVerificacion, linkVerificacion, linkVerificacion);

      helper.setText(htmlContent, true);
      mailSender.send(message);

    } catch (MessagingException | MailException e) {
      System.err.println("❌ Error al enviar correo: " + e.getMessage());
    }
  }
}
