package Proyectodeaula.Workwise.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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
@Table(name = "oferta_habilidad")
public class OfertaHabilidad {

    @EmbeddedId
    private OfertaHabilidadId id = new OfertaHabilidadId();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("ofertaId")
    @JoinColumn(name = "oferta_id")
    @JsonIgnore // Ignorar la oferta para evitar ciclos
    private Oferta oferta;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("habilidadId")
    @JoinColumn(name = "habilidad_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) // Esto evita el problema del proxy
    private Habilidad habilidad;

    public OfertaHabilidad(Oferta oferta, Habilidad habilidad) {
        this.oferta = oferta;
        this.habilidad = habilidad;
    }

    @Override
    public String toString() {
        return habilidad != null ? habilidad.getNombre() : "Habilidad desconocida";
    }
}