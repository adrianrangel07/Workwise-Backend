package Proyectodeaula.Workwise.Model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@Entity

@Table(name = "postulacion")
public class Postulacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "n_personas", columnDefinition = "int", nullable = false)
    private int n_personas;

    @Column(name = "fecha_postulacion", nullable = false)
    private LocalDate fecha_postulacion;
    
    @Column(name = "estado", columnDefinition = "varchar(20)", nullable = false)
    private String estado;

    @ManyToOne
    @JoinColumn(name = "persona_id")
    private Persona persona;

    @ManyToOne
    @JoinColumn(name = "oferta_id")
    private Oferta oferta;
}
