package Proyectodeaula.Workwise.Service.Ofertas;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Proyectodeaula.Workwise.Model.Ofertas.Oferta;
import Proyectodeaula.Workwise.Model.Personas.Persona;
import Proyectodeaula.Workwise.Model.Personas.Postulacion;
import Proyectodeaula.Workwise.Repository.Oferta.OfertaRepository;
import Proyectodeaula.Workwise.Repository.Persona.Repository_Persona;
import Proyectodeaula.Workwise.Repository.Postulacion.Repository_Postulacion;
import Proyectodeaula.Workwise.Service.Config.NotificacionService;
import jakarta.transaction.Transactional;

@Service
public class PostulacionService {

        @Autowired
        private Repository_Postulacion postulacionRepository;

        @Autowired
        private OfertaRepository ofertaRepository;

        @Autowired
        private Repository_Persona personaRepository;

        @Autowired
        private NotificacionService notificacionService;

        public void postularse(Long ofertaId, String emailUsuario) {

                Persona persona = personaRepository.findOptionalByEmail(emailUsuario)
                                .orElseThrow(() -> new RuntimeException("Persona no encontrada"));

                // Validar si ya está postulado
                postulacionRepository.findByOfertaIdAndPersonaId(ofertaId, persona.getId())
                                .ifPresent(p -> {
                                        throw new RuntimeException("Ya estás postulado a esta oferta");
                                });

                Oferta oferta = ofertaRepository.findById(ofertaId)
                                .orElseThrow(() -> new RuntimeException("Oferta no encontrada"));

                Postulacion postulacion = new Postulacion();
                postulacion.setOferta(oferta);
                postulacion.setPersona(persona);
                postulacion.setEstado("Pendiente");
                postulacion.setN_personas(1);
                postulacion.setFecha_postulacion(LocalDate.now());

                postulacionRepository.save(postulacion);

                // 🔔 NOTIFICACIÓN: postulación creada
                notificacionService.crearNotificacion(
                                persona.getId(),
                                "PERSONA",
                                "Postulación enviada",
                                "Te postulaste a la oferta '" + oferta.getTitulo() + "'",
                                "POSTULACION",
                                "/postulaciones-pendientes");
        }

        public void cambiarEstado(Long postulacionId, String nuevoEstado) {

                Postulacion postulacion = postulacionRepository.findById(postulacionId)
                                .orElseThrow(() -> new RuntimeException("Postulación no encontrada"));

                postulacion.setEstado(nuevoEstado);
                postulacionRepository.save(postulacion);

                Persona persona = postulacion.getPersona();
                Oferta oferta = postulacion.getOferta();

                // 🔔 NOTIFICACIÓN: cambio de estado
                notificacionService.crearNotificacion(
                                persona.getId(),
                                "PERSONA",
                                "Estado de postulación actualizado",
                                "Tu postulación a la oferta '" + oferta.getTitulo() +
                                                "' cambió a estado: " + nuevoEstado,
                                "POSTULACION",
                                "/postulaciones-resueltas");
        }

        @Transactional
        public void eliminarPostulacion(Long postulacionId, String emailUsuario, boolean esAdmin) {

                Postulacion postulacion = postulacionRepository.findById(postulacionId)
                                .orElseThrow(() -> new RuntimeException("Postulación no encontrada"));

                System.out.println("ID persona dueña: " +
                                (postulacion.getPersona() != null ? postulacion.getPersona().getId() : "null"));

                if (!esAdmin) {
                        Persona persona = personaRepository.findOptionalByEmail(emailUsuario)
                                        .orElseThrow(() -> new RuntimeException("Persona no encontrada"));

                        if (!postulacion.getPersona().getId().equals(persona.getId())) {
                                System.out.println("ERROR: IDs no coinciden - No puede eliminar");
                                throw new RuntimeException("No puedes eliminar esta postulación");
                        }
                }

                // 🔥 DELETE REAL
                postulacionRepository.deleteById(postulacionId);

                // 🔔 Notificación opcional
                if (!esAdmin) {
                        notificacionService.crearNotificacion(
                                        postulacion.getPersona().getId(),
                                        "PERSONA",
                                        "Postulación eliminada",
                                        "Eliminaste tu postulación a la oferta '" +
                                                        postulacion.getOferta().getTitulo() + "'",
                                        "POSTULACION",
                                        "/postulaciones-pendientes");
                }
        }

}
