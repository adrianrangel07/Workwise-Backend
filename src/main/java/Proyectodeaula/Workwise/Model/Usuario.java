package Proyectodeaula.Workwise.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "usuarios")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", columnDefinition = "varchar(100)", nullable = false, unique = true)
    private String email;
    @Column(name = "password", columnDefinition = "varchar(100)", nullable = false)
    private String password;
    @Column(name = "rol", columnDefinition = "varchar(50)", nullable = false)
    private String rol;
}

