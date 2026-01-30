package Proyectodeaula.Workwise.Controller.OfertasController;

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
import Proyectodeaula.Workwise.Model.Ofertas.Oferta;
import Proyectodeaula.Workwise.Model.Personas.Persona;
import Proyectodeaula.Workwise.Model.Personas.Postulacion;
import Proyectodeaula.Workwise.Repository.Persona.Repository_Persona;
import Proyectodeaula.Workwise.Repository.Postulacion.Repository_Postulacion;
import Proyectodeaula.Workwise.Service.Ofertas.PostulacionService;

@RestController
@RequestMapping("/api/postulaciones")
public class PostulacionController {

    @Autowired
    private Repository_Postulacion postulacionRepository;

    @Autowired
    private PostulacionService postulacionService;

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
            String email = authentication.getName();

            postulacionService.postularse(ofertaId, email);

            response.put("success", true);
            response.put("message", "Postulación exitosa");
            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * DELETE /api/postulaciones/eliminar/{id}
     * Solo administradores pueden eliminar postulaciones.
     */

    @PreAuthorize("hasAnyRole('ADMIN', 'PERSONA')")
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Map<String, Object>> eliminarPostulacion(
            @PathVariable Long id,
            Authentication authentication) {

        Map<String, Object> response = new HashMap<>();

        try {
            boolean esAdmin = authentication.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

            postulacionService.eliminarPostulacion(
                    id,
                    authentication.getName(),
                    esAdmin);

            response.put("success", true);
            response.put("message", "Postulación eliminada con éxito");
            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }
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
                                .collect(Collectors.joining(", "))))
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

        try {
            String nuevoEstado = body.get("estado");
            postulacionService.cambiarEstado(id, nuevoEstado);

            response.put("success", true);
            response.put("estado", nuevoEstado);
            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
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
