package Proyectodeaula.Workwise.Controller.OfertasController;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Proyectodeaula.Workwise.Model.Oferta;
import Proyectodeaula.Workwise.Service.Ofertas.OfertaService;

@RestController
@RequestMapping("/api/ofertas")
public class OfertaController {

    @Autowired
    private OfertaService ofertaService;

    // ✅ Listar todas las ofertas activas
    @GetMapping
    public ResponseEntity<List<Oferta>> listarOfertas() {
        return ResponseEntity.ok(ofertaService.listarOfertas());
    }

    // ✅ Buscar oferta por ID
    @GetMapping("/{id}")
    public ResponseEntity<Oferta> obtenerOferta(@PathVariable Long id) {
        Optional<Oferta> oferta = ofertaService.obtenerPorId(id);
        return oferta.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ Actualizar oferta
    @PutMapping("/{id}")
    public ResponseEntity<Oferta> actualizarOferta(@PathVariable Long id, @RequestBody Oferta ofertaActualizada) {
        Optional<Oferta> ofertaOpt = ofertaService.actualizar(id, ofertaActualizada);
        return ofertaOpt.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ Eliminar oferta
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarOferta(@PathVariable Long id) {
        if (ofertaService.eliminar(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // ✅ Listar ofertas por empresa
    @GetMapping("/empresa/{empresaId}")
    public ResponseEntity<List<Oferta>> listarPorEmpresa(@PathVariable Long empresaId) {
        return ResponseEntity.ok(ofertaService.listarPorEmpresa(empresaId));
    }

    // ✅ Cambiar estado activo/inactivo
    @PutMapping("/{id}/toggle")
    public ResponseEntity<Oferta> toggleActivo(@PathVariable Long id) {
        Optional<Oferta> oferta = ofertaService.toggleActivo(id);
        return oferta.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
