package Proyectodeaula.Workwise.Model;

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
    @Column(name = "fecha_postulacion", columnDefinition = "varchar(50)", nullable = false)
    private String fecha_Postulacion;
    @Column(name = "activo", columnDefinition = "boolean", nullable = false)
    private boolean activo;

    @ManyToOne
    @JoinColumn(name = "persona_id")
    private Persona persona;
    
    @ManyToOne
    @JoinColumn(name = "oferta_id")
    private Oferta oferta;
}
