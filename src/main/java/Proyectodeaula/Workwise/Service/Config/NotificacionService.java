package Proyectodeaula.Workwise.Service.Config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Proyectodeaula.Workwise.Model.Notificacion.Notificacion;
import Proyectodeaula.Workwise.Repository.General.NotificacionRepository;

@Service
public class NotificacionService {
    
    @Autowired
    private NotificacionRepository repository;

    public void crearNotificacion(
            Long userId,
            String rol,
            String titulo,
            String mensaje,
            String tipo,
            String link) {
        Notificacion n = new Notificacion();
        n.setUserId(userId);
        n.setRol(rol);
        n.setTitulo(titulo);
        n.setMensaje(mensaje);
        n.setTipo(tipo);
        n.setLink(link);
        n.setLeido(false);

        repository.save(n);
    }

    public List<Notificacion> obtenerNoLeidas(Long userId) {
        return repository.findByUserIdAndLeidoFalseOrderByFechaCreacionDesc(userId);
    }

    public void marcarComoLeida(Long id) {
        Notificacion n = repository.findById(id).orElseThrow();
        n.setLeido(true);
        repository.save(n);
    }
}