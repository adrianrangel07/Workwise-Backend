package Proyectodeaula.Workwise.Service.Usuarios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Proyectodeaula.Workwise.Model.Otros.CategoriaProfesional;
import Proyectodeaula.Workwise.Repository.Persona.CategoriaProfesionalRepository;

@Service
public class ClasificacionProfesionalService {

    @Autowired
    private CategoriaProfesionalRepository categoriaRepo;

    public CategoriaProfesional obtenerCategoriaPorProfesion(String profesion) {

        if (profesion == null || profesion.isBlank()) {
            profesion = "";
        }

        String p = profesion.toLowerCase().trim();
        String categoria;

        // 🏗️ Construcción / Ingeniería técnica
        if (p.contains("civil") || p.contains("arquitect") || p.contains("constru")
                || p.contains("obra") || p.contains("estructur") || p.contains("topogr")
                || p.contains("plomer") || p.contains("electric") || p.contains("soldad")
                || p.contains("carpint") || p.contains("maquinaria") || p.contains("ambiental")
                || p.contains("industrial") || p.contains("mecánic") || p.contains("hidrául")
                || p.contains("obrero")) {

            categoria = "Construcción e Ingeniería";

            // 💻 Tecnología / Informática
        } else if (p.contains("sistemas") || p.contains("informát") || p.contains("software")
                || p.contains("program") || p.contains("desarroll") || p.contains("frontend")
                || p.contains("backend") || p.contains("fullstack") || p.contains("devops")
                || p.contains("qa") || p.contains("tester") || p.contains("datos")
                || p.contains("data") || p.contains("ml") || p.contains("ai")
                || p.contains("ciber") || p.contains("seguridad informática")
                || p.contains("bases de datos") || p.contains("db") || p.contains("servidor")
                || p.contains("ux") || p.contains("ui") || p.contains("soporte")
                || p.contains("redes") || p.contains("infraestructura")) {

            categoria = "Tecnología";

            // 🏥 Salud / Medicina
        } else if (p.contains("doctor") || p.contains("médic") || p.contains("ciruj")
                || p.contains("enfermer") || p.contains("odont") || p.contains("dent")
                || p.contains("psic") || p.contains("fisioter") || p.contains("terapeu")
                || p.contains("nutric") || p.contains("farmac") || p.contains("laboratorio")
                || p.contains("quirof") || p.contains("paraméd") || p.contains("clínic")) {

            categoria = "Salud";

            // 🎓 Educación / Docencia
        } else if (p.contains("profesor") || p.contains("docent") || p.contains("educa")
                || p.contains("pedag") || p.contains("instructor") || p.contains("tutor")
                || p.contains("maestro")) {

            categoria = "Educación";

            // 💰 Finanzas / Economía
        } else if (p.contains("finanz") || p.contains("econom") || p.contains("contad")
                || p.contains("auditor") || p.contains("fiscal") || p.contains("tribut")
                || p.contains("banca") || p.contains("riesgo") || p.contains("inversion")
                || p.contains("tesorer")) {

            categoria = "Finanzas";

            // 🧑‍💼 Administración / Gestión / RRHH
        } else if (p.contains("administra") || p.contains("gerent") || p.contains("director")
                || p.contains("coordinador") || p.contains("supervisor") || p.contains("analista")
                || p.contains("asistente") || p.contains("talento") || p.contains("recursos humanos")
                || p.contains("project") || p.contains("pm") || p.contains("gestor")) {

            categoria = "Administración";

            // 🎨 Arte / Diseño / Creatividad
        } else if (p.contains("diseñ") || p.contains("art") || p.contains("creativ")
                || p.contains("fotograf") || p.contains("video") || p.contains("audiovisual")
                || p.contains("animac") || p.contains("3d") || p.contains("ilustr")
                || p.contains("moda") || p.contains("editor") || p.contains("dibujo")) {

            categoria = "Arte y Creatividad";

            // 📈 Marketing / Publicidad
        } else if (p.contains("market") || p.contains("public") || p.contains("ventas")
                || p.contains("comercial") || p.contains("community") || p.contains("digital")
                || p.contains("seo") || p.contains("sem") || p.contains("social media")
                || p.contains("branding") || p.contains("copy")) {

            categoria = "Marketing y Ventas";

            // 🚚 Logística / Transporte
        } else if (p.contains("logíst") || p.contains("inventario") || p.contains("bodega")
                || p.contains("almac") || p.contains("transpor") || p.contains("conductor")
                || p.contains("ruta") || p.contains("operac") || p.contains("carga")) {

            categoria = "Logística";

            // ⚙️ Industria / Producción
        } else if (p.contains("producc") || p.contains("industrial") || p.contains("operario")
                || p.contains("planta") || p.contains("mantenimiento") || p.contains("electromec")
                || p.contains("manufactura") || p.contains("montaje")) {

            categoria = "Industria y Producción";

            // 🧪 Ciencia / Laboratorios
        } else if (p.contains("cient") || p.contains("biolog") || p.contains("quím")
                || p.contains("microb") || p.contains("físic") || p.contains("investig")
                || p.contains("analista de laboratorio")) {

            categoria = "Ciencia y Laboratorios";

            // 🛡️ Seguridad / Defensa
        } else if (p.contains("segurid") || p.contains("vigil") || p.contains("escolta")
                || p.contains("guardia") || p.contains("cctv") || p.contains("patrull")) {

            categoria = "Seguridad";

            // ⚖️ Legal
        } else if (p.contains("abog") || p.contains("juríd") || p.contains("legal")
                || p.contains("notar") || p.contains("fiscal")) {

            categoria = "Legal";

            // 🍳 Gastronomía / Turismo
        } else if (p.contains("chef") || p.contains("cocin") || p.contains("mesero")
                || p.contains("bartender") || p.contains("hotel") || p.contains("turism")
                || p.contains("recepcion") || p.contains("hosteler")) {

            categoria = "Gastronomía y Turismo";

            // 🛠️ Reparación / Técnicos
        } else if (p.contains("técnic") || p.contains("repar") || p.contains("mecán")
                || p.contains("electrón") || p.contains("celular") || p.contains("automotr")
                || p.contains("electrodom")) {

            categoria = "Reparación y Mantenimiento";

            // 🐾 Veterinaria
        } else if (p.contains("veterin") || p.contains("zootec")) {

            categoria = "Veterinaria";

            // ♿ Servicio social / Bienestar
        } else if (p.contains("trabajador social") || p.contains("bienestar")
                || p.contains("orientador") || p.contains("consejero")) {

            categoria = "Asistencia y Bienestar";

            // 🌱 Agricultura / Agro
        } else if (p.contains("agrón") || p.contains("agric") || p.contains("agropecu")) {

            categoria = "Agricultura";

            // 🔧 Energía / Minería / Petróleo
        } else if (p.contains("petrol") || p.contains("min") || p.contains("solar")
                || p.contains("eólic") || p.contains("energ")) {

            categoria = "Energía y Minería";

            // 📢 Comunicación / Medios
        } else if (p.contains("period") || p.contains("locut") || p.contains("comunic")
                || p.contains("redact") || p.contains("report")) {

            categoria = "Comunicación y Medios";

            // 💇 Belleza / Estética / Moda
        } else if (p.contains("estilista") || p.contains("manicur") || p.contains("barber")
                || p.contains("maquill") || p.contains("moda")) {

            categoria = "Belleza y Estética";

            // Fallback
        } else {
            categoria = "Otros";
        }

        // --- Buscar o crear categoría ---
        final String c = categoria;

        return categoriaRepo.findByNombreIgnoreCase(c)
                .orElseGet(() -> {
                    CategoriaProfesional nueva = new CategoriaProfesional();
                    nueva.setNombre(c);
                    nueva.setDescripcion("Categoría generada automáticamente");
                    return categoriaRepo.save(nueva);
                });
    }

}
