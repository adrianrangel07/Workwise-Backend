package Proyectodeaula.Workwise.Service.Config;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import Proyectodeaula.Workwise.Model.PasswordResetToken;
import Proyectodeaula.Workwise.Repository.General.PasswordResetTokenRepository;

@Component
public class PasswordResetTokenCleanup {

    private final PasswordResetTokenRepository tokenRepository;

    public PasswordResetTokenCleanup(PasswordResetTokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    // Se ejecuta cada hora (cron: segundo, minuto, hora, día, mes, semana)
    @Scheduled(cron = "0 0 * * * ?") // cada hora
    public void deleteExpiredTokens() {

        LocalDateTime now = LocalDateTime.now();

        // Borrar tokens expirados (usados o no)
        List<PasswordResetToken> expiredTokens = tokenRepository.findByExpirationDateBefore(now);

        if (!expiredTokens.isEmpty()) {
            tokenRepository.deleteAll(expiredTokens);
        }
    }

}
