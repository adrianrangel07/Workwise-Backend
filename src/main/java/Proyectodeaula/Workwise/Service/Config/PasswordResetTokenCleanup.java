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
    @Scheduled(cron = "0 0 * * * ?")
    public void deleteExpiredTokens() {
        List<PasswordResetToken> expiredTokens = tokenRepository.findByExpirationDateBefore(LocalDateTime.now());
        if (!expiredTokens.isEmpty()) {
            tokenRepository.deleteAll(expiredTokens);
        }
    }
}

