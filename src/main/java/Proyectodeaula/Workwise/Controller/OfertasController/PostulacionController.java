package Proyectodeaula.Workwise.Controller.OfertasController;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Proyectodeaula.Workwise.Model.Dto.PostulacionDTO;
import Proyectodeaula.Workwise.Model.Oferta;
import Proyectodeaula.Workwise.Model.Persona;
import Proyectodeaula.Workwise.Model.Postulacion;
import Proyectodeaula.Workwise.Repository.Oferta.OfertaRepository;
import Proyectodeaula.Workwise.Repository.Persona.Repository_Persona;
import Proyectodeaula.Workwise.Repository.Postulacion.Repository_Postulacion;

@RestController
@RequestMapping("/api/postulaciones")
public class PostulacionController {

    @Autowired
    private Repository_Postulacion postulacionRepository;

    @Autowired
    private OfertaRepository ofertaRepository;

    @Autowired
    private Repository_Persona personaRepository;

    /**
     * POST /api/postulaciones/postularse
     * El usuario autenticado (ROLE_USER) se postula a una oferta.
     */
    @PreAuthorize("hasRole('PERSONA')")
    @PostMapping("/postularse")
    public ResponseEntity<Map<String, Object>> postularse(
            @RequestBody Map<String, Long> request,
            Authentication authentication) {

        Map<String, Object> response = new HashMap<>();

        try {
            Long ofertaId = request.get("ofertaId");
            if (ofertaId == null) {
                response.put("success", false);
                response.put("message", "Falta el ID de la oferta.");
                return ResponseEntity.badRequest().body(response);
            }

            // Usuario autenticado desde JWT
            String correoUsuario = authentication.getName();
            Optional<Persona> personaOpt = personaRepository.findOptionalByEmail(correoUsuario);

            if (personaOpt.isEmpty()) {
                response.put("success", false);
                response.put("message", "Usuario no encontrado.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

            Persona persona = personaOpt.get();

            // Verificar si ya está postulado
            Optional<Postulacion> existente = postulacionRepository.findByOfertaIdAndPersonaId(ofertaId,
                    persona.getId());
            if (existente.isPresent()) {
                response.put("success", false);
                response.put("message", "Ya estás postulado a esta oferta.");
                return ResponseEntity.ok(response);
            }

            Optional<Oferta> ofertaOpt = ofertaRepository.findById(ofertaId);
            if (ofertaOpt.isEmpty()) {
                response.put("success", false);
                response.put("message", "Oferta no encontrada.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            // Crear postulación
            Postulacion postulacion = new Postulacion();
            postulacion.setOferta(ofertaOpt.get());
            postulacion.setPersona(persona);
            postulacion.setEstado("Pendiente");
            postulacion.setN_personas(1);
            postulacion.setFecha_postulacion(LocalDate.now());
            postulacionRepository.save(postulacion);

            response.put("success", true);
            response.put("message", "Postulación exitosa.");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al procesar la postulación.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * DELETE /api/postulaciones/eliminar/{id}
     * Solo administradores pueden eliminar postulaciones.
     */
    @PreAuthorize("hasAnyRole('ADMIN','PERSONA')")
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Map<String, Object>> eliminarPostulacion(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();

        Optional<Postulacion> postulacionOpt = postulacionRepository.findById(id);
        if (postulacionOpt.isEmpty()) {
            response.put("success", false);
            response.put("message", "Postulación no encontrada.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        postulacionRepository.delete(postulacionOpt.get());
        response.put("success", true);
        response.put("message", "Postulación eliminada con éxito.");
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/postulaciones/ofertas/{id}
     * Solo empresas o admin pueden ver postulaciones de una oferta.
     */
    @PreAuthorize("hasAnyRole('EMPRESA', 'ADMIN')")
    @GetMapping("/ofertas/{id}")
    public ResponseEntity<List<PostulacionDTO>> obtenerPostulacionesPorOferta(@PathVariable Long id) {
        List<Postulacion> postulaciones = postulacionRepository.findByOfertaId(id);

        if (postulaciones.isEmpty())
            return ResponseEntity.ok(Collections.emptyList());

        List<PostulacionDTO> resultado = postulaciones.stream()
                .filter(p -> p.getPersona() != null)
                .map(p -> new PostulacionDTO(
                        p.getId(),
                        p.getPersona().getNombre(),
                        p.getPersona().getApellido(),
                        p.getEstado(),
                        p.getPersona().getHabilidades()
                                .stream()
                                .map(h -> h.getHabilidad().getNombre()) 
                                .collect(Collectors.joining(", ")) 
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(resultado);
    }

    /**
     * GET /api/postulaciones/{id}/cv
     * Permite a empresas o admin ver el CV del postulante.
     */
    @PreAuthorize("hasAnyRole('EMPRESA', 'ADMIN')")
    @GetMapping("/{id}/cv")
    public ResponseEntity<byte[]> verCVDesdePostulacion(@PathVariable Long id) {
        Optional<Postulacion> postulacionOpt = postulacionRepository.findById(id);

        if (postulacionOpt.isPresent()) {
            Persona persona = postulacionOpt.get().getPersona();

            if (persona != null && persona.getCv() != null) {
                byte[] cvBytes = persona.getCv();

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_PDF);
                headers.setContentDisposition(ContentDisposition.inline()
                        .filename("CV_" + persona.getId() + ".pdf")
                        .build());

                return new ResponseEntity<>(cvBytes, headers, HttpStatus.OK);
            }
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * PUT /api/postulaciones/{id}/estado
     * Solo empresas o admin pueden cambiar el estado de una postulación.
     */
    @PreAuthorize("hasAnyRole('EMPRESA', 'ADMIN')")
    @PutMapping("/{id}/estado")
    public ResponseEntity<Map<String, Object>> actualizarEstado(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {

        Map<String, Object> response = new HashMap<>();
        Optional<Postulacion> postulacionOpt = postulacionRepository.findById(id);

        if (postulacionOpt.isEmpty()) {
            response.put("error", "Postulación no encontrada");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        String nuevoEstado = body.get("estado");
        if (nuevoEstado == null || nuevoEstado.isEmpty()) {
            response.put("error", "El estado no puede estar vacío");
            return ResponseEntity.badRequest().body(response);
        }

        Postulacion postulacion = postulacionOpt.get();
        postulacion.setEstado(nuevoEstado);
        postulacionRepository.save(postulacion);

        response.put("success", true);
        response.put("message", "Estado actualizado correctamente");
        response.put("estado", nuevoEstado);
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/postulaciones/mis-postulaciones
     * Permite a una persona ver sus propias postulaciones.
     */
    @PreAuthorize("hasRole('PERSONA')")
    @GetMapping("/mis-postulaciones")
    public ResponseEntity<List<Map<String, Object>>> obtenerMisPostulaciones(Authentication authentication) {
        String correoUsuario = authentication.getName();
        Optional<Persona> personaOpt = personaRepository.findOptionalByEmail(correoUsuario);

        if (personaOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Persona persona = personaOpt.get();
        List<Postulacion> postulaciones = postulacionRepository.findByPersonaId(persona.getId());

        List<Map<String, Object>> resultado = postulaciones.stream()
                .map(p -> {
                    Map<String, Object> data = new HashMap<>();
                    data.put("postulacionId", p.getId());
                    data.put("estado", p.getEstado());
                    data.put("fecha", p.getFecha_postulacion());

                    Oferta oferta = p.getOferta();
                    Map<String, Object> ofertaData = new HashMap<>();

                    ofertaData.put("id", oferta.getId());
                    ofertaData.put("titulo", oferta.getTitulo());
                    ofertaData.put("descripcion", oferta.getDescripcion());
                    ofertaData.put("salario", oferta.getSalario());
                    ofertaData.put("moneda", oferta.getMoneda());
                    ofertaData.put("ubicacion", oferta.getUbicacion());
                    ofertaData.put("tipoContrato", oferta.getTipo_Contrato());
                    ofertaData.put("tipoEmpleo", oferta.getTipo_Empleo());
                    ofertaData.put("modalidad", oferta.getModalidad());
                    ofertaData.put("fechaPublicacion", oferta.getFecha_Publicacion());
                    ofertaData.put("fechaCierre", oferta.getFecha_Cierre());
                    ofertaData.put("sectorOferta", oferta.getSector_oferta());
                    ofertaData.put("experiencia", oferta.getExperiencia());
                    ofertaData.put("nivelEducacion", oferta.getNivel_Educacion());
                    ofertaData.put("activo", oferta.isActivo());
                    ofertaData.put("empresa", oferta.getEmpresa().getNombre());

                    data.put("oferta", ofertaData);

                    return data;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(resultado);
    }

}
