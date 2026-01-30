package Proyectodeaula.Workwise.Model.Personas;

import java.io.Serializable;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class PersonaHabilidadId implements Serializable {

    @Column(name = "persona_id")
    private Long personaId;

    @Column(name = "habilidad_id")
    private Long habilidadId;
}
