package Proyectodeaula.Workwise.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
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
@Table(name = "persona_habilidad")
public class PersonaHabilidad {

    @EmbeddedId
    private PersonaHabilidadId id = new PersonaHabilidadId();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("personaId")
    @JoinColumn(name = "persona_id")
    @JsonIgnore
    private Persona persona;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("habilidadId")
    @JoinColumn(name = "habilidad_id")
    private Habilidad habilidad;

    public PersonaHabilidad(Persona persona, Habilidad habilidad) {
        this.persona = persona;
        this.habilidad = habilidad;
        this.id = new PersonaHabilidadId(persona.getId(), habilidad.getId());
    }

    @Override
    public String toString() {
        return habilidad != null ? habilidad.getNombre() : "Habilidad desconocida";
    }
}
