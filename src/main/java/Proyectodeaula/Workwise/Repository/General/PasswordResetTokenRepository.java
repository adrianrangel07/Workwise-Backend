package Proyectodeaula.Workwise.Repository.General;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import Proyectodeaula.Workwise.Model.Otros.PasswordResetToken;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

    // Tokens activos que NO han sido usados
    List<PasswordResetToken> findByEmailAndUsedFalseOrderByExpirationDateDesc(String email);

    // Tokens expirados (no importa si fueron usados o no)
    List<PasswordResetToken> findByExpirationDateBefore(LocalDateTime time);

    // Tokens usados que ya pasaron la expiración (para limpieza)
    List<PasswordResetToken> findByUsedTrueAndExpirationDateBefore(LocalDateTime time);
}
