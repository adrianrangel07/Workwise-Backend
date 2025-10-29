package Proyectodeaula.Workwise.Repository.Oferta;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import Proyectodeaula.Workwise.Model.Oferta;
import Proyectodeaula.Workwise.Model.OfertaHabilidad;
import Proyectodeaula.Workwise.Model.OfertaHabilidadId;

public interface OfertaHabilidadRepository extends JpaRepository<OfertaHabilidad, OfertaHabilidadId> {
    List<OfertaHabilidad> findByOferta(Oferta oferta);
}


