package Proyectodeaula.Workwise.Controller.General;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Proyectodeaula.Workwise.Model.Notificacion.Notificacion;
import Proyectodeaula.Workwise.Model.Personas.Persona;
import Proyectodeaula.Workwise.Repository.Persona.Repository_Persona;
import Proyectodeaula.Workwise.Service.Config.NotificacionService;

@RestController
@RequestMapping("/api/notificaciones")
@PreAuthorize("isAuthenticated()")
public class NotificacionController {

    @Autowired
    private NotificacionService service;

    @Autowired
    private Repository_Persona personaRepository;

    @GetMapping("/no-leidas/count")
    public ResponseEntity<?> contarNoLeidas(Authentication authentication) {
        try {
            String email = authentication.getName();
            System.out.println("📧 Email del usuario: " + email);

            Persona persona = personaRepository.findByEmail(email);

            if (persona == null) {
                System.out.println("❌ Persona no encontrada con email: " + email);
                return ResponseEntity.ok(0L); // Retornar 0 en lugar de error
            }

            Long count = service.contarNoLeidas(persona.getId());
            System.out.println("📊 Notificaciones no leídas para persona " + persona.getId() + ": " + count);

            return ResponseEntity.ok(count);
        } catch (Exception e) {
            System.err.println("❌ Error al contar notificaciones: " + e.getMessage());
            return ResponseEntity.ok(0L);
        }
    }

    @GetMapping("/no-leidas")
    public ResponseEntity<?> obtenerNoLeidas(Authentication authentication) {
        try {
            String email = authentication.getName();
            System.out.println("Obteniendo notificaciones para: " + email);

            Persona persona = personaRepository.findByEmail(email);

            if (persona == null) {
                System.out.println("❌ Persona no encontrada");
                return ResponseEntity.ok(new ArrayList<>());
            }

            List<Notificacion> notificaciones = service.obtenerNoLeidas(persona.getId());
            System.out.println("📋 Notificaciones encontradas: " + notificaciones.size());

            return ResponseEntity.ok(notificaciones);
        } catch (Exception e) {
            System.err.println("❌ Error: " + e.getMessage());
            return ResponseEntity.ok(new ArrayList<>());
        }
    }

    @PutMapping("/{id}/leida")
    public void marcarLeida(@PathVariable Long id) {
        service.marcarComoLeida(id);
    }
}
