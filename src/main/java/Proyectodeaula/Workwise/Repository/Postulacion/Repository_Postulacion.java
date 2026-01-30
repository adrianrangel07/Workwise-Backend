package Proyectodeaula.Workwise.Repository.Postulacion;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import Proyectodeaula.Workwise.Model.Personas.Postulacion;
@Repository
public interface Repository_Postulacion extends JpaRepository<Postulacion, Long> {

    List<Postulacion> findByPersonaId(Long personaId);

    List<Postulacion> findByOfertaId(Long ofertaId);

    Optional<Postulacion> findByOfertaIdAndPersonaId(Long ofertaId, Long personaId);

    
}