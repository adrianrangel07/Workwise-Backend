package Proyectodeaula.Workwise.Model.Notificacion;

import java.time.LocalDateTime;

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
@AllArgsConstructor
@Setter
@NoArgsConstructor
@Table(name = "notificaciones")
public class Notificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "mensaje", nullable = false)
    private String mensaje;

    @Column(name = "rol", nullable = false)
    private String rol;

    @Column(name = "titulo", nullable = false)
    private String titulo;

    private String tipo;

    private String link;

    @Column(name = "leido", nullable = false)
    private boolean leido;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion = LocalDateTime.now();

}
