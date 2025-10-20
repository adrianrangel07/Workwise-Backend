package Proyectodeaula.Workwise.Security;

import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class PublicRoutes {

    public List<String> getPublicPaths() {
        return List.of(
                "/api/personas/login",
                "/api/personas/registrar",
                "/api/empresas/login",
                "/api/empresas/registrar",
                "/api/chatbot/**",
                "/api/ofertas/home");
    }
}
