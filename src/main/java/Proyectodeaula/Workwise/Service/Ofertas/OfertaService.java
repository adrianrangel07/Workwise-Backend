package Proyectodeaula.Workwise.Service.Ofertas;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import Proyectodeaula.Workwise.Model.Empresa;
import Proyectodeaula.Workwise.Model.Oferta;
import Proyectodeaula.Workwise.Repository.Oferta.OfertaRepository;
import Proyectodeaula.Workwise.RepositoryService.Ofertas.IofertaService;

@Service
public class OfertaService implements IofertaService {

    @Autowired
    private OfertaRepository ofertaRepository; // tu JpaRepository<Oferta, Long>

    @Override
    public Page<Oferta> listarOfertasPorEmpresaPaginado(Empresa empresa, Pageable pageable) {
        return ofertaRepository.findByEmpresa(empresa, pageable);
    }

    @Override
    public List<Oferta> listarOfertasPorEmpresa(Empresa empresa) {
        return ofertaRepository.findByEmpresa(empresa);
    }

    @Override
    public List<Oferta> listar_ofertas() {
        return ofertaRepository.findAll();
    }

    @Override
    public Optional<Oferta> listarId(int id) {
        return ofertaRepository.findById((long) id);
    }

    @Override
    public int save(Oferta O) {
        Oferta oferta = ofertaRepository.save(O);
        return oferta != null ? 1 : 0;
    }

    @Override
    public void delete(long Id) {
        ofertaRepository.deleteById(Id);
    }
}