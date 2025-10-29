package Proyectodeaula.Workwise.Repository.Persona;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import Proyectodeaula.Workwise.Model.Persona;
import Proyectodeaula.Workwise.Model.PersonaHabilidad;
import Proyectodeaula.Workwise.Model.PersonaHabilidadId;

public interface PersonaHabilidadRepository extends JpaRepository<PersonaHabilidad, PersonaHabilidadId> {

    List<PersonaHabilidad> findByPersona(Persona persona);

}

