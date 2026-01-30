package Proyectodeaula.Workwise.Controller.General;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("/no-leidas")
    public List<Notificacion> obtenerNoLeidas(Authentication authentication) {

        String email = authentication.getName();
        Persona persona = personaRepository.findByEmail(email);
        if (persona == null) {
            throw new RuntimeException("Persona no encontrada");
        }

        return service.obtenerNoLeidas(persona.getId());
    }

    @PutMapping("/{id}/leida")
    public void marcarLeida(@PathVariable Long id) {
        service.marcarComoLeida(id);
    }
}
