package Proyectodeaula.Workwise.Model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "categoria_profesion")
public class CategoriaProfesional {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", columnDefinition = "varchar(100)")
    private String nombre;

    @Column(name = "descripcion", columnDefinition = "varchar(100)")
    private String descripcion;

    @OneToMany(mappedBy = "categoria")
    @JsonIgnore
    private List<Persona> personas;

    @OneToMany(mappedBy = "categoria")
    @JsonIgnore
    private List<Oferta> ofertas;
}
