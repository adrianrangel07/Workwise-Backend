package Proyectodeaula.Workwise.Controller.OfertasController;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Proyectodeaula.Workwise.Model.CategoriaProfesional;
import Proyectodeaula.Workwise.Model.Dto.OfertaPublicaDTO;
import Proyectodeaula.Workwise.Model.Empresa;
import Proyectodeaula.Workwise.Model.Habilidad;
import Proyectodeaula.Workwise.Model.Oferta;
import Proyectodeaula.Workwise.Model.OfertaHabilidad;
import Proyectodeaula.Workwise.Model.OfertaHabilidadId;
import Proyectodeaula.Workwise.Model.Persona;
import Proyectodeaula.Workwise.Repository.Oferta.OfertaHabilidadRepository;
import Proyectodeaula.Workwise.Repository.Oferta.OfertaRepository;
import Proyectodeaula.Workwise.Repository.Persona.HabilidadRepository;
import Proyectodeaula.Workwise.Repository.Persona.Repository_Persona;
import Proyectodeaula.Workwise.Security.Token.JwtUtil;
import Proyectodeaula.Workwise.Service.Ofertas.OfertaService;

@RestController
@RequestMapping("/api/ofertas")
public class OfertaController {

    @Autowired
    private OfertaService ofertaService;

    @Autowired
    private OfertaRepository ofertaRepository;

    @Autowired
    private Repository_Persona personaRepository;

    @Autowired
    private HabilidadRepository habilidadRepository;

    @Autowired
    private OfertaHabilidadRepository ofertaHabilidadRepository;

    @Autowired
    private JwtUtil jwtUtil;

    // ✅ Listar todas las ofertas activas
    @GetMapping
    public ResponseEntity<List<Oferta>> listarOfertas() {
        return ResponseEntity.ok(ofertaService.listarOfertas());
    }

    @GetMapping("/home")
    public ResponseEntity<List<Oferta>> listarOfertasinvitado() {
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
    @PreAuthorize("hasAnyRole('EMPRESA', 'ADMIN')")
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
    @PreAuthorize("hasAnyRole('EMPRESA', 'ADMIN')")
    @PutMapping("/{id}/toggle")
    public ResponseEntity<Oferta> toggleActivo(@PathVariable Long id) {
        Optional<Oferta> oferta = ofertaService.toggleActivo(id);
        return oferta.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/compatibles/{personaId}")
    public ResponseEntity<?> obtenerOfertasCompatibles(@PathVariable Long personaId) {
        Persona persona = personaRepository.findById(personaId)
                .orElseThrow(() -> new RuntimeException("Persona no encontrada"));

        CategoriaProfesional categoria = persona.getCategoria();

        List<Oferta> ofertas = ofertaRepository.findByCategoriaNombreIgnoreCase(categoria.getNombre());

        // Convertir cada oferta a DTO
        List<OfertaPublicaDTO> ofertasDTO = ofertas.stream()
                .map(OfertaPublicaDTO::new)
                .toList();

        return ResponseEntity.ok(ofertasDTO);
    }
    
    /**
     * Agrega una habilidad a una oferta específica
     */
    @PreAuthorize("hasAnyRole('EMPRESA', 'ADMIN')")
    @PostMapping("/{id}/habilidades")
    public ResponseEntity<?> agregarHabilidadAOferta(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {

        String email = jwtUtil.extractEmailFromHeader(authHeader);
        Oferta oferta = ofertaRepository.findById(id).orElse(null);

        if (oferta == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("La oferta no existe");

        // ✅ Validar que la empresa dueña de la oferta es la que está autenticada
        Empresa empresa = oferta.getEmpresa();
        if (!empresa.getUsuario().getEmail().equals(email)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No puedes modificar ofertas de otra empresa");
        }

        String nombreHabilidad = body.get("nombre");
        if (nombreHabilidad == null || nombreHabilidad.isBlank())
            return ResponseEntity.badRequest().body("El nombre de la habilidad no puede estar vacío");

        // Buscar o crear la habilidad
        Habilidad habilidad = habilidadRepository.findByNombreIgnoreCase(nombreHabilidad)
                .orElseGet(() -> habilidadRepository.save(new Habilidad(null, nombreHabilidad)));

        // Verificar si ya existe la relación
        boolean yaExiste = oferta.getHabilidades().stream()
                .anyMatch(oh -> oh.getHabilidad().getId().equals(habilidad.getId()));
        if (yaExiste)
            return ResponseEntity.badRequest().body("La habilidad ya está asociada a esta oferta");

        // Asociar
        OfertaHabilidad oh = new OfertaHabilidad(oferta, habilidad);
        oferta.getHabilidades().add(oh);
        ofertaRepository.save(oferta);

        return ResponseEntity.ok("Habilidad agregada exitosamente a la oferta");
    }

    /**
     * Listar habilidades de una oferta
     */
    @PreAuthorize("hasAnyRole('EMPRESA', 'ADMIN')")
    @GetMapping("/{id}/habilidades")
    public ResponseEntity<?> listarHabilidadesDeOferta(@PathVariable Long id) {
        Oferta oferta = ofertaRepository.findById(id).orElse(null);
        if (oferta == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("La oferta no existe");

        var habilidades = ofertaHabilidadRepository.findByOferta(oferta)
                .stream()
                .map(oh -> Map.of("id", oh.getHabilidad().getId(), "nombre", oh.getHabilidad().getNombre()))
                .toList();

        return ResponseEntity.ok(habilidades);
    }

    /**
     * Eliminar una habilidad de una oferta
     */
    @PreAuthorize("hasAnyRole('EMPRESA', 'ADMIN')")
    @DeleteMapping("/{id}/habilidades/{habilidadId}")
    public ResponseEntity<?> eliminarHabilidadDeOferta(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id,
            @PathVariable Long habilidadId) {

        String email = jwtUtil.extractEmailFromHeader(authHeader);
        Oferta oferta = ofertaRepository.findById(id).orElse(null);

        if (oferta == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("La oferta no existe");

        Empresa empresa = oferta.getEmpresa();
        if (!empresa.getUsuario().getEmail().equals(email)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No puedes modificar ofertas de otra empresa");
        }

        OfertaHabilidadId ohId = new OfertaHabilidadId(oferta.getId(), habilidadId);
        if (!ofertaHabilidadRepository.existsById(ohId))
            return ResponseEntity.badRequest().body("Esta habilidad no está asociada a la oferta");

        ofertaHabilidadRepository.deleteById(ohId);
        return ResponseEntity.ok("Habilidad eliminada exitosamente de la oferta");
    }
    
}
