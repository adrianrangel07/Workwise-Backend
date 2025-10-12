package Proyectodeaula.Workwise.Service.Ofertas;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import Proyectodeaula.Workwise.Model.Empresa;
import Proyectodeaula.Workwise.Model.Oferta;
import Proyectodeaula.Workwise.Repository.Empresa.Repository_Emp;
import Proyectodeaula.Workwise.Repository.Oferta.OfertaRepository;
import Proyectodeaula.Workwise.RepositoryService.Ofertas.IofertaService;

@Service
public class OfertaService implements IofertaService {

    @Autowired
    private OfertaRepository ofertaRepository; // tu JpaRepository<Oferta, Long>
    

    @Autowired
    private Repository_Emp empresaRepository;

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

    // Listar todas las ofertas activas
    public List<Oferta> listarOfertas() {
        return ofertaRepository.findByActivoTrue();
    }

    // Buscar por id
    public Optional<Oferta> obtenerPorId(Long id) {
        return ofertaRepository.findById(id);
    }

    // Guardar nueva oferta
    @Override
    public Oferta guardar(Oferta oferta) {
        return ofertaRepository.save(oferta);
    }

    // Actualizar oferta
    public Optional<Oferta> actualizar(Long id, Oferta ofertaActualizada) {
        return ofertaRepository.findById(id).map(oferta -> {
            oferta.setTitulo(ofertaActualizada.getTitulo());
            oferta.setDescripcion(ofertaActualizada.getDescripcion());
            oferta.setRequisitos(ofertaActualizada.getRequisitos());
            oferta.setSalario(ofertaActualizada.getSalario());
            oferta.setMoneda(ofertaActualizada.getMoneda());
            oferta.setUbicacion(ofertaActualizada.getUbicacion());
            oferta.setTipo_Contrato(ofertaActualizada.getTipo_Contrato());
            oferta.setModalidad(ofertaActualizada.getModalidad());
            oferta.setFecha_Publicacion(ofertaActualizada.getFecha_Publicacion());
            oferta.setFecha_Cierre(ofertaActualizada.getFecha_Cierre());
            oferta.setSector_oferta(ofertaActualizada.getSector_oferta());
            oferta.setExperiencia(ofertaActualizada.getExperiencia());
            oferta.setNivel_Educacion(ofertaActualizada.getNivel_Educacion());
            oferta.setComentarios(ofertaActualizada.getComentarios());
            return ofertaRepository.save(oferta);
        });
    }

    // Eliminar oferta
    public boolean eliminar(Long id) {
        if (ofertaRepository.existsById(id)) {
            ofertaRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Listar por empresa
    public List<Oferta> listarPorEmpresa(Long empresaId) {
        Empresa empresa = empresaRepository.findById(empresaId)
                .orElseThrow(() -> new RuntimeException("Empresa no encontrada"));
        return ofertaRepository.findByEmpresa(empresa);
    }

    // Cambiar estado activo/inactivo
    public Optional<Oferta> toggleActivo(Long id) {
        return ofertaRepository.findById(id).map(oferta -> {
            oferta.setActivo(!oferta.isActivo());
            return ofertaRepository.save(oferta);
        });
    }
}

