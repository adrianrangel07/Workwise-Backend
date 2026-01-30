package Proyectodeaula.Workwise.RepositoryService.Ofertas;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import Proyectodeaula.Workwise.Model.Empresas.Empresa;
import Proyectodeaula.Workwise.Model.Ofertas.Oferta;

public interface IofertaService {
    Page<Oferta> listarOfertasPorEmpresaPaginado(Empresa empresa, Pageable pageable);
    List<Oferta> listarOfertasPorEmpresa(Empresa empresa);

    public List<Oferta>listar_ofertas();
    public Optional<Oferta>listarId(int id);
    public int save(Oferta O);
    public void delete (long Id);
    Oferta guardar(Oferta oferta);
}
