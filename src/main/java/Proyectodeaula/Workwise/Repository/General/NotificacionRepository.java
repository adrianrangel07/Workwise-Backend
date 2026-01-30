package Proyectodeaula.Workwise.Repository.General;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import Proyectodeaula.Workwise.Model.Notificacion.Notificacion;

public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {

    List<Notificacion> findByUserIdAndLeidoFalseOrderByFechaCreacionDesc(Long userId);

    Long countByUserIdAndLeidoFalse(Long userId);
}
