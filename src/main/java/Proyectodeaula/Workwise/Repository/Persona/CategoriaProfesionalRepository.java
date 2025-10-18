package Proyectodeaula.Workwise.Repository.Persona;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import Proyectodeaula.Workwise.Model.CategoriaProfesional;

@Repository
public interface CategoriaProfesionalRepository extends JpaRepository<CategoriaProfesional, Long> {
    Optional<CategoriaProfesional> findByNombreIgnoreCase(String nombre);
}

