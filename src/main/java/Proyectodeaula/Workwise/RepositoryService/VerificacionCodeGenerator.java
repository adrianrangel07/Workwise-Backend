package Proyectodeaula.Workwise.RepositoryService;

import org.springframework.stereotype.Component;

// Servicio para generar códigos de verificación
@Component
public class VerificacionCodeGenerator {
    public String generarCodigo() {
        int codigo = (int) (Math.random() * 9000000) + 1000000; // 7 dígitos
        return String.valueOf(codigo);
    }
}

