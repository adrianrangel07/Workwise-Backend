package Proyectodeaula.Workwise.Controller.UsuarioController;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import Proyectodeaula.Workwise.Model.Dto.VerificarPasswordDTO;
import Proyectodeaula.Workwise.Model.Dto.VerificationResponse;
import Proyectodeaula.Workwise.Model.Otros.CategoriaProfesional;
import Proyectodeaula.Workwise.Model.Otros.Habilidad;
import Proyectodeaula.Workwise.Model.Otros.Usuario;
import Proyectodeaula.Workwise.Model.Personas.Persona;
import Proyectodeaula.Workwise.Model.Personas.PersonaHabilidad;
import Proyectodeaula.Workwise.Model.Personas.PersonaHabilidadId;
import Proyectodeaula.Workwise.Repository.Persona.HabilidadRepository;
import Proyectodeaula.Workwise.Repository.Persona.PersonaHabilidadRepository;
import Proyectodeaula.Workwise.Repository.Persona.Repository_Persona;
import Proyectodeaula.Workwise.Security.Token.JwtUtil;
import Proyectodeaula.Workwise.Service.Usuarios.ClasificacionProfesionalService;
import Proyectodeaula.Workwise.Service.Usuarios.PersonaService;

@RestController
@RequestMapping("/api/personas")
public class PersonaController {

    @Autowired
    private Repository_Persona personaRepository;

    @Autowired
    private PersonaService personaService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private ClasificacionProfesionalService clasificacionService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private HabilidadRepository habilidadRepository;

    @Autowired
    private PersonaHabilidadRepository personaHabilidadRepository;

    // ==================== REGISTRO ====================
    @PostMapping("/registrar")
    public ResponseEntity<?> registrarPersona(@RequestBody Persona persona) {
        try {
            // Validaciones iniciales
            if (persona.getUsuario() == null || persona.getUsuario().getPassword().isBlank()) {
                return ResponseEntity.badRequest()
                        .body("El usuario/contraseña no puede estar vacío");
            }

            String email = persona.getUsuario().getEmail();

            // 🔍 Buscar persona existente por email
            Persona existente = personaRepository.findByEmail(email);

            // ==================== CASO: EMAIL YA EXISTE ====================
            if (existente != null) {

                // 🟢 Reactivar cuenta si estaba desactivada
                if (!existente.isActivo()) {

                    existente.setActivo(true);
                    existente.setNombre(persona.getNombre());
                    existente.setApellido(persona.getApellido());
                    existente.setDireccion(persona.getDireccion());
                    existente.setTelefono(persona.getTelefono());
                    existente.setTipo_telefono(persona.getTipo_telefono());
                    existente.setProfesion(persona.getProfesion());
                    existente.setNumero_documento(persona.getNumero_documento());

                    // Recalcular categoría
                    CategoriaProfesional categoria = clasificacionService
                            .obtenerCategoriaPorProfesion(persona.getProfesion());
                    existente.setCategoria(categoria);

                    // Actualizar contraseña
                    existente.getUsuario().setPassword(
                            passwordEncoder.encode(persona.getUsuario().getPassword()));

                    personaRepository.save(existente);

                    return ResponseEntity.ok("Cuenta reactivada correctamente");
                }

                // 🔴 Email existe y está activo
                return ResponseEntity.badRequest()
                        .body(new VerificationResponse(true, "El correo electrónico ya está registrado"));
            }

            // ==================== CASO: DOCUMENTO YA EXISTE ====================
            if (personaRepository.existsByNumeroDocumento(persona.getNumero_documento())) {
                return ResponseEntity.badRequest()
                        .body(new VerificationResponse(true, "El número de documento ya está registrado"));
            }

            // ==================== REGISTRO NORMAL ====================
            persona.getUsuario().setPassword(
                    passwordEncoder.encode(persona.getUsuario().getPassword()));
            persona.getUsuario().setRol("PERSONA");
            persona.setActivo(true);

            CategoriaProfesional categoria = clasificacionService.obtenerCategoriaPorProfesion(persona.getProfesion());
            persona.setCategoria(categoria);

            Persona saved = personaRepository.saveAndFlush(persona);

            return ResponseEntity.status(HttpStatus.CREATED).body(saved);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al registrar persona: " + e.getMessage());
        }
    }

    // ==================== LOGIN ====================
    @PostMapping("/login")
    public ResponseEntity<?> loginPersona(@RequestBody Map<String, String> loginData) {
        String email = loginData.get("email");
        String password = loginData.get("password");

        Persona persona = personaRepository.findByEmail(email);

        if (persona == null || !passwordEncoder.matches(password, persona.getUsuario().getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales incorrectas");
        }

        if (!persona.isActivo()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("La cuenta está desactivada");
        }

        String token = jwtUtil.generateToken(persona.getUsuario().getEmail(), persona.getUsuario().getRol());

        return ResponseEntity.ok(Map.of(
                "message", "Login exitoso",
                "token", token,
                "rol", persona.getUsuario().getRol(),
                "personaId", persona.getId(),
                "nombre", persona.getNombre()));

    }

    // ==================== ELIMINAR ====================
    @PreAuthorize("hasAnyRole('PERSONA', 'ADMIN')")
    @DeleteMapping("/eliminar")
    public ResponseEntity<?> eliminarMiCuenta(@RequestHeader("Authorization") String authHeader) {
        String email = jwtUtil.extractEmailFromHeader(authHeader);
        Persona persona = personaRepository.findByEmail(email);

        persona.setActivo(false);
        personaRepository.save(persona);
        return ResponseEntity.ok("Cuenta desactivada");
    }

    // ==================== PERFIL ====================
    @PreAuthorize("hasAnyRole('PERSONA', 'ADMIN')")
    @GetMapping("/perfil")
    public ResponseEntity<?> obtenerPerfil(@RequestHeader("Authorization") String authHeader) {
        String email = jwtUtil.extractEmailFromHeader(authHeader);

        Persona persona = personaRepository.findByEmail(email);
        if (persona == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido o usuario no encontrado");
        }
        return ResponseEntity.ok(persona);
    }

    @PreAuthorize("hasAnyRole('PERSONA', 'ADMIN')")
    @PutMapping("/perfil")
    public ResponseEntity<?> actualizarPerfil(@RequestHeader("Authorization") String authHeader,
            @RequestBody Persona datos) {
        String email = jwtUtil.extractEmailFromHeader(authHeader);
        Persona persona = personaRepository.findByEmail(email);

        if (persona == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido o usuario no encontrado");
        }

        if (datos.getNombre() != null)
            persona.setNombre(datos.getNombre());
        if (datos.getApellido() != null)
            persona.setApellido(datos.getApellido());
        if (datos.getDireccion() != null)
            persona.setDireccion(datos.getDireccion());
        if (datos.getProfesion() != null)
            persona.setProfesion(datos.getProfesion());
        if (datos.getTelefono() != null && !datos.getTelefono().isBlank())
            persona.setTelefono(datos.getTelefono());
        if (datos.getTipo_telefono() != null)
            persona.setTipo_telefono(datos.getTipo_telefono());
        if (datos.getPhoto() != null)
            persona.setPhoto(datos.getPhoto());
        if (datos.getCv() != null)
            persona.setCv(datos.getCv());

        try {
            personaService.actualizarPerfil(persona);
            return ResponseEntity.ok(persona);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al actualizar perfil");
        }
    }

    // ==================== FOTO ====================
    @PreAuthorize("hasAnyRole('PERSONA', 'ADMIN')")
    @PostMapping("/foto")
    public ResponseEntity<?> subirFoto(@RequestHeader("Authorization") String authHeader,
            @RequestParam("file") MultipartFile file) {
        String email = jwtUtil.extractEmailFromHeader(authHeader);
        Persona persona = personaRepository.findByEmail(email);

        if (persona == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido o usuario no encontrado");
        }

        try {
            persona.setPhoto(file.getBytes());
            personaRepository.save(persona);
            return ResponseEntity.ok("Foto actualizada");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al guardar la foto");
        }
    }

    @PreAuthorize("hasAnyRole('PERSONA', 'ADMIN')")
    @GetMapping("/foto/{id}")
    public ResponseEntity<byte[]> obtenerFoto(@PathVariable Long id) {
        Optional<Persona> persona = personaRepository.findById(id);
        if (persona.isPresent() && persona.get().getPhoto() != null) {
            byte[] imagen = persona.get().getPhoto();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG);
            return new ResponseEntity<>(imagen, headers, HttpStatus.OK);
        }
        return ResponseEntity.notFound().build();
    }

    // ==================== CONTRASEÑA ====================
    @PostMapping("/verificar-contrasena")
    public ResponseEntity<Map<String, Object>> verificarContrasena(@RequestBody VerificarPasswordDTO dto) {
        Persona persona = personaRepository.findByEmail(dto.getEmail());

        boolean valido = false;
        String mensaje = "Usuario no encontrado";

        if (persona != null && persona.getUsuario() != null) {
            String hashedPassword = persona.getUsuario().getPassword();
            if (passwordEncoder.matches(dto.getPassword(), hashedPassword)) {
                valido = true;
                mensaje = "Credenciales válidas";
            } else {
                mensaje = "Contraseña incorrecta";
            }
        }

        Map<String, Object> response = new HashMap<>();
        response.put("valido", valido);
        response.put("mensaje", mensaje);

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('PERSONA', 'ADMIN')")
    @PutMapping("/cambiar-contrasena")
    public ResponseEntity<?> cambiarContraseña(@RequestHeader("Authorization") String authHeader,
            @RequestBody Map<String, String> requestData) {
        String email = jwtUtil.extractEmailFromHeader(authHeader);
        Persona persona = personaRepository.findByEmail(email);

        if (persona == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido o usuario no encontrado");

        Usuario usuario = persona.getUsuario();
        if (usuario == null)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("La persona no tiene usuario asociado");

        String nuevaContraseña = requestData.get("nuevaContraseña");
        if (nuevaContraseña == null || nuevaContraseña.isBlank()) {
            return ResponseEntity.badRequest().body("La nueva contraseña no puede estar vacía");
        }

        usuario.setPassword(passwordEncoder.encode(nuevaContraseña));
        persona.setUsuario(usuario);
        try {
            personaService.actualizarPerfil(persona);
            return ResponseEntity.ok("Contraseña cambiada con éxito");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al cambiar la contraseña");
        }
    }

    // ==================== CV ====================
    @PreAuthorize("hasAnyRole('PERSONA', 'ADMIN')")
    @PostMapping("/cv")
    public ResponseEntity<?> subirCV(@RequestHeader("Authorization") String authHeader,
            @RequestParam("file") MultipartFile file) {
        String email = jwtUtil.extractEmailFromHeader(authHeader);
        Persona persona = personaRepository.findByEmail(email);

        if (persona == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido o usuario no encontrado");
        }

        try {
            persona.setCv(file.getBytes());
            personaRepository.save(persona);
            return ResponseEntity.ok("CV actualizado correctamente");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al guardar el CV");
        }
    }

    @PreAuthorize("hasAnyRole('PERSONA', 'ADMIN')")
    @GetMapping("/cv/{id}")
    public ResponseEntity<byte[]> obtenerCV(@PathVariable Long id) {
        Optional<Persona> persona = personaRepository.findById(id);
        if (persona.isPresent() && persona.get().getCv() != null) {
            byte[] cv = persona.get().getCv();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "CV_" + persona.get().getNombre() + ".pdf");
            return new ResponseEntity<>(cv, headers, HttpStatus.OK);
        }
        return ResponseEntity.notFound().build();
    }

    @PreAuthorize("hasAnyRole('PERSONA', 'ADMIN')")
    @DeleteMapping("/cv")
    public ResponseEntity<?> eliminarCV(@RequestHeader("Authorization") String authHeader) {
        String email = jwtUtil.extractEmailFromHeader(authHeader);
        Persona persona = personaRepository.findByEmail(email);

        if (persona == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido o usuario no encontrado");
        }

        if (persona.getCv() == null) {
            return ResponseEntity.badRequest().body("La persona no tiene CV guardado");
        }

        persona.setCv(null);
        personaRepository.save(persona);
        return ResponseEntity.ok("CV eliminado correctamente");
    }

    // ==================== HABILIDADES ====================

    /**
     * Agrega una habilidad
     */
    @PreAuthorize("hasAnyRole('PERSONA', 'ADMIN')")
    @PostMapping("/habilidades")
    public ResponseEntity<?> agregarHabilidad(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody Map<String, String> body) {

        String email = jwtUtil.extractEmailFromHeader(authHeader);
        Persona persona = personaRepository.findByEmail(email);

        if (persona == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido o usuario no encontrado");

        String nombreHabilidad = body.get("nombre");
        if (nombreHabilidad == null || nombreHabilidad.isBlank())
            return ResponseEntity.badRequest().body("El nombre de la habilidad no puede estar vacío");

        // Buscar o crear la habilidad
        Habilidad habilidad = habilidadRepository.findByNombreIgnoreCase(nombreHabilidad)
                .orElseGet(() -> habilidadRepository.save(new Habilidad(null, nombreHabilidad)));

        // Verificar si ya la tiene
        boolean yaExiste = persona.getHabilidades().stream()
                .anyMatch(ph -> ph.getHabilidad().getId().equals(habilidad.getId()));
        if (yaExiste) {
            return ResponseEntity.badRequest().body("La habilidad ya está asociada a tu perfil");
        }

        // Asociar la habilidad
        PersonaHabilidad ph = new PersonaHabilidad(persona, habilidad);
        persona.getHabilidades().add(ph);
        personaRepository.save(persona);

        return ResponseEntity.ok("Habilidad agregada exitosamente");
    }

    /**
     * Lista todas las habilidades
     */
    @PreAuthorize("hasAnyRole('PERSONA', 'ADMIN')")
    @GetMapping("/habilidades")
    public ResponseEntity<?> listarHabilidades(@RequestHeader("Authorization") String authHeader) {
        String email = jwtUtil.extractEmailFromHeader(authHeader);
        Persona persona = personaRepository.findByEmail(email);

        if (persona == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido o usuario no encontrado");

        var habilidades = personaHabilidadRepository.findByPersona(persona)
                .stream()
                .map(ph -> Map.of("id", ph.getHabilidad().getId(), "nombre", ph.getHabilidad().getNombre()))
                .toList();

        return ResponseEntity.ok(habilidades);
    }

    /**
     * Elimina una habilidad
     */
    @PreAuthorize("hasAnyRole('PERSONA', 'ADMIN')")
    @DeleteMapping("/habilidades/{id}")
    public ResponseEntity<?> eliminarHabilidad(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id) {

        String email = jwtUtil.extractEmailFromHeader(authHeader);
        Persona persona = personaRepository.findByEmail(email);

        if (persona == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido o usuario no encontrado");

        Optional<Habilidad> habilidadOpt = habilidadRepository.findById(id);
        if (habilidadOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("La habilidad no existe");
        }

        Habilidad habilidad = habilidadOpt.get();
        PersonaHabilidadId phId = new PersonaHabilidadId(persona.getId(), habilidad.getId());

        if (!personaHabilidadRepository.existsById(phId)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Esta habilidad no está asociada a tu perfil");
        }

        personaHabilidadRepository.deleteById(phId);
        return ResponseEntity.ok("Habilidad eliminada exitosamente");
    }

}
