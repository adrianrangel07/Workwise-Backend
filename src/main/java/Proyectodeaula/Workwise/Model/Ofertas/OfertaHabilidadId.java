package Proyectodeaula.Workwise.Model.Ofertas;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class OfertaHabilidadId implements Serializable {

    @Column(name = "oferta_id")
    private Long ofertaId;

    @Column(name = "habilidad_id")
    private Long habilidadId;
}


