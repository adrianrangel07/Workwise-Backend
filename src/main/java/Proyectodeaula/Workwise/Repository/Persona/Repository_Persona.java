package Proyectodeaula.Workwise.Repository.Persona;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import Proyectodeaula.Workwise.Model.Personas.Persona;

public interface Repository_Persona extends JpaRepository<Persona, Long> {

    @Query("SELECT p FROM Persona p WHERE p.usuario.email = :email")
    Persona findByEmail(@Param("email") String email);

    @Query("SELECT p FROM Persona p WHERE " +
            "LOWER(p.nombre) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(p.apellido) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "CAST(p.numero_documento AS string) LIKE CONCAT('%', :query, '%') OR " +
            "LOWER(p.usuario.email) LIKE LOWER(CONCAT('%', :query, '%'))")
    Page<Persona> buscarPorNombreApellidoDocumentoOEmail(@Param("query") String query, Pageable pageable);

    @Query("SELECT p FROM Persona p WHERE p.usuario.email = :email")
    Optional<Persona> findOptionalByEmail(@Param("email") String email);

     // Verifica si existe un email
    @Query("SELECT COUNT(p) > 0 FROM Persona p WHERE p.usuario.email = :email")
    boolean existsByEmail(@Param("email") String email);

    // Verifica si existe un número de documento
    @Query("SELECT COUNT(p) > 0 FROM Persona p WHERE p.numero_documento = :numeroDocumento")
    boolean existsByNumeroDocumento(@Param("numeroDocumento") int numeroDocumento);
}
