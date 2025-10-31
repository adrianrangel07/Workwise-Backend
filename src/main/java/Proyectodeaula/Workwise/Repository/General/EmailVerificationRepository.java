package Proyectodeaula.Workwise.Repository.General;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import Proyectodeaula.Workwise.Model.EmailVerification;

@Repository
public interface EmailVerificationRepository extends JpaRepository<EmailVerification, Long> {
    Optional<EmailVerification> findByCodigoAndVerificadoFalse(String codigo);
    Optional<EmailVerification> findByPersonaId(Long personaId);
}
