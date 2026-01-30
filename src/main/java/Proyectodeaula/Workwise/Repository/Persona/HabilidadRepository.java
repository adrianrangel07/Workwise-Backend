package Proyectodeaula.Workwise.Repository.Persona;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import Proyectodeaula.Workwise.Model.Otros.Habilidad;

public interface HabilidadRepository extends JpaRepository<Habilidad, Long> {
    Optional<Habilidad> findByNombreIgnoreCase(String nombre);
}
