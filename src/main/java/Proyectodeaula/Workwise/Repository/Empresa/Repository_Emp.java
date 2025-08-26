package Proyectodeaula.Workwise.Repository.Empresa;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import Proyectodeaula.Workwise.Model.Empresa;

public interface Repository_Emp extends JpaRepository<Empresa, Long> {

    @Query("SELECT e FROM Empresa e WHERE e.usuario.email = :email")
    Empresa findByEmail(@Param("email") String email);

    @Query(value = "SELECT * FROM empresas ORDER BY id DESC LIMIT 3", nativeQuery = true)
    List<Empresa> findTopNByOrderByIdDesc(int limit);

    @Query("SELECT e FROM Empresa e WHERE " +
            "LOWER(e.nombre) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "CAST(e.nit AS string) LIKE CONCAT('%', :query, '%') OR " +
            "LOWER(e.usuario.email) LIKE LOWER(CONCAT('%', :query, '%'))")
    Page<Empresa> buscarPorNombreNitOEmail(@Param("query") String query, Pageable pageable);

}
