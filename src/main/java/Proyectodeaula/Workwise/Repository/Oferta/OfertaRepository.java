package Proyectodeaula.Workwise.Repository.Oferta;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import Proyectodeaula.Workwise.Model.Empresa;
import Proyectodeaula.Workwise.Model.Oferta;

public interface OfertaRepository extends JpaRepository<Oferta, Long> {
    List<Oferta> findByEmpresa(Empresa empresa);

    List<Oferta> findByActivoTrue();

    Page<Oferta> findByEmpresa(Empresa empresa, Pageable pageable);
}
