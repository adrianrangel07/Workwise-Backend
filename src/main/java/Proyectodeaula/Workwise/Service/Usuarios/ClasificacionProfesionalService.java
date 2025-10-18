package Proyectodeaula.Workwise.Service.Usuarios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Proyectodeaula.Workwise.Model.CategoriaProfesional;
import Proyectodeaula.Workwise.Repository.Persona.CategoriaProfesionalRepository;

@Service
public class ClasificacionProfesionalService {

    @Autowired
    private CategoriaProfesionalRepository categoriaRepo;

    public CategoriaProfesional obtenerCategoriaPorProfesion(String profesion) {
        String p = profesion.toLowerCase();

        String nombreCategoria;
        if (p.contains("civil") || p.contains("arquitect") || p.contains("constru")) {
            nombreCategoria = "Construcción";
        } else if (p.contains("sistemas") || p.contains("software") || p.contains("informat")) {
            nombreCategoria = "Tecnología";
        } else if (p.contains("doctor") || p.contains("medic") || p.contains("salud")) {
            nombreCategoria = "Salud";
        } else if (p.contains("profesor") || p.contains("docent") || p.contains("educa")) {
            nombreCategoria = "Educación";
        } else if (p.contains("finanz") || p.contains("econom")) {
            nombreCategoria = "Finanzas";
        } else {
            nombreCategoria = "Otros";
        }

        //Busca la categoría existente, o la crea si no existe
        return categoriaRepo.findByNombreIgnoreCase(nombreCategoria)
                .orElseGet(() -> {
                    CategoriaProfesional nueva = new CategoriaProfesional();
                    nueva.setNombre(nombreCategoria);
                    nueva.setDescripcion("Categoría generada automáticamente");
                    return categoriaRepo.save(nueva);
                });
    }
}

