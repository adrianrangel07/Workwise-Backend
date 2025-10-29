package Proyectodeaula.Workwise.Model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "password_reset_tokens")
@Getter
@Setter
@NoArgsConstructor
public class PasswordResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email; // usuario o empresa
    private String token; // UUID o código alfanumérico
    private LocalDateTime expirationDate;

    private boolean used = false;

    public boolean isExpired() {
        return expirationDate.isBefore(LocalDateTime.now());
    }
}
