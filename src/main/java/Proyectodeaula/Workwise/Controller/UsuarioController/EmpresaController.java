package Proyectodeaula.Workwise.Controller.UsuarioController;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

import Proyectodeaula.Workwise.Model.CategoriaProfesional;
import Proyectodeaula.Workwise.Model.Dto.VerificarPasswordDTO;
import Proyectodeaula.Workwise.Model.Empresa;
import Proyectodeaula.Workwise.Model.Habilidad;
import Proyectodeaula.Workwise.Model.Oferta;
import Proyectodeaula.Workwise.Model.OfertaHabilidad;
import Proyectodeaula.Workwise.Model.Usuario;
import Proyectodeaula.Workwise.Repository.Empresa.Crud_Emp;
import Proyectodeaula.Workwise.Repository.Empresa.Repository_Emp;
import Proyectodeaula.Workwise.Repository.Persona.HabilidadRepository;
import Proyectodeaula.Workwise.RepositoryService.Ofertas.IofertaService;
import Proyectodeaula.Workwise.Security.Token.JwtUtil;
import Proyectodeaula.Workwise.Service.Usuarios.ClasificacionProfesionalService;
import Proyectodeaula.Workwise.Service.Usuarios.EmpresaService;

@RestController
@RequestMapping("/api/empresas")
public class EmpresaController {

    private static final Logger logger = LoggerFactory.getLogger(EmpresaController.class);

    @Autowired
    private Repository_Emp uEmp;

    @Autowired
    private Crud_Emp empresaRepository;

    @Autowired
    private IofertaService ofertaService;

    @Autowired
    private EmpresaService empresaService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private ClasificacionProfesionalService clasificacionService;

    @Autowired
    private HabilidadRepository habilidadRepository;

    @Autowired
    private JwtUtil jwtUtil;

    // ==================== REGISTRO ====================

    // registrar empresa
    @PostMapping("/registrar")
    @SuppressWarnings("CallToPrintStackTrace")
    public ResponseEntity<?> registrarEmpresa(@RequestBody Empresa empresa) {
        try {
            if (empresa.getUsuario() == null || empresa.getUsuario().getPassword().isBlank()) {
                return ResponseEntity.badRequest().body("El usuario/contraseña no puede estar vacío");
            }

            empresa.getUsuario().setPassword(passwordEncoder.encode(empresa.getUsuario().getPassword()));
            empresa.getUsuario().setRol("EMPRESA"); // siempre asignar rol

            Empresa saved = empresaRepository.save(empresa);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al registrar empresa: " + e.getClass().getSimpleName() + " - " + e.getMessage());
        }

    }

    // ==================== LOGIN ====================

    // inicio de sesion de empresa
    @PostMapping("/login")
    public ResponseEntity<?> loginEmpresa(@RequestBody Map<String, String> loginData) {
        String email = loginData.get("email");
        String password = loginData.get("password");

        Empresa empresa = uEmp.findByEmail(email);

        if (empresa == null || !passwordEncoder.matches(password, empresa.getUsuario().getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales incorrectas");
        }

        if (!empresa.isActivo()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("La cuenta está desactivada");
        }

        String token = jwtUtil.generateToken(empresa.getUsuario().getEmail(), empresa.getUsuario().getRol());

        return ResponseEntity.ok(Map.of(
                "message", "Login exitoso",
                "token", token,
                "empresaId", empresa.getId()));
    }

    // ==================== PERFIL ====================
    @PreAuthorize("hasAnyRole('EMPRESA', 'ADMIN')")
    @GetMapping("/perfil")
    public ResponseEntity<?> obtenerPerfil(@RequestHeader("Authorization") String authHeader) {
        String email = jwtUtil.extractEmailFromHeader(authHeader);

        Empresa empresa = uEmp.findByEmail(email);
        if (empresa == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido o usuario no encontrado");
        }
        return ResponseEntity.ok(empresa);
    }

    @PreAuthorize("hasAnyRole('EMPRESA', 'ADMIN')")
    @PutMapping("/perfil")
    public ResponseEntity<?> actualizarPerfil(@RequestHeader("Authorization") String authHeader,
            @RequestBody Empresa datos) {
        String email = jwtUtil.extractEmailFromHeader(authHeader);
        Empresa empresa = uEmp.findByEmail(email);

        if (empresa == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido o usuario no encontrado");
        }

        if (datos.getNombre() != null)
            empresa.setNombre(datos.getNombre());
        if (datos.getDireccion() != null)
            empresa.setDireccion(datos.getDireccion());
        if (datos.getRazonSocial() != null)
            empresa.setRazonSocial(datos.getRazonSocial());
        if (datos.getDescripcion() != null)
            empresa.setDescripcion(datos.getDescripcion());
        if (datos.getSector() != null)
            empresa.setSector(datos.getSector());
        if (datos.getTelefono() != null && !datos.getTelefono().isBlank())
            empresa.setTelefono(datos.getTelefono());
        if (datos.getTipo_telefono() != null)
            empresa.setTipo_telefono(datos.getTipo_telefono());
        if (datos.getNit() != null && !datos.getNit().isBlank())
            empresa.setNit(datos.getNit());
        if (datos.getPhoto() != null)
            empresa.setPhoto(datos.getPhoto());

        try {
            empresaService.actualizarPerfil(empresa);
            return ResponseEntity.ok(empresa);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al actualizar perfil");
        }
    }

    // ==================== FOTO ====================
    @PreAuthorize("hasAnyRole('EMPRESA', 'ADMIN')")
    @PostMapping("/foto")
    public ResponseEntity<?> subirFoto(@RequestHeader("Authorization") String authHeader,
            @RequestParam("file") MultipartFile file) {
        String email = jwtUtil.extractEmailFromHeader(authHeader);
        Empresa empresa = uEmp.findByEmail(email);

        if (empresa == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido o usuario no encontrado");
        }

        try {
            empresa.setPhoto(file.getBytes());
            uEmp.save(empresa);
            return ResponseEntity.ok("Foto actualizada");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al guardar la foto");
        }
    }

    @PreAuthorize("hasAnyRole('EMPRESA', 'ADMIN')")
    @GetMapping("/foto/{id}")
    public ResponseEntity<byte[]> obtenerFoto(@PathVariable Long id) {
        Optional<Empresa> empresa = uEmp.findById(id);
        if (empresa.isPresent() && empresa.get().getPhoto() != null) {
            byte[] imagen = empresa.get().getPhoto();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG);
            return new ResponseEntity<>(imagen, headers, HttpStatus.OK);
        }
        return ResponseEntity.notFound().build();
    }

    // ==================== OFERTAS ====================

    // listar ofertas por empresa con paginación
    @GetMapping("/ofertas")
    @PreAuthorize("hasAnyRole('EMPRESA', 'ADMIN')")
    public ResponseEntity<?> listarOfertasEmpresa(@RequestHeader("Authorization") String authHeader,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size) {
        String email = jwtUtil.extractEmailFromHeader(authHeader);
        Empresa empresa = uEmp.findByEmail(email);

        if (empresa == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido o usuario no encontrado");
        }

        Page<Oferta> ofertasPage = ofertaService.listarOfertasPorEmpresaPaginado(empresa, PageRequest.of(page, size));
        return ResponseEntity.ok(ofertasPage);
    }

    // crear oferta por empresa
    @PostMapping("/ofertas")
    @PreAuthorize("hasAnyRole('EMPRESA', 'ADMIN')")
    @SuppressWarnings("CallToPrintStackTrace")
    public ResponseEntity<?> crearOfertaEmpresa(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody Map<String, Object> body) {
        try {
            // 🔹 Extraer email del token
            String email = jwtUtil.extractEmailFromHeader(authHeader);
            Empresa empresa = uEmp.findByEmail(email);

            if (empresa == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Token inválido o usuario no encontrado");
            }

            // 🔹 Crear nueva oferta
            Oferta oferta = new Oferta();
            oferta.setTitulo((String) body.get("titulo"));
            oferta.setDescripcion((String) body.get("descripcion"));
            oferta.setSalario((Integer) body.get("salario"));
            oferta.setMoneda((String) body.get("moneda"));
            oferta.setUbicacion((String) body.get("ubicacion"));
            oferta.setTipo_Contrato((String) body.get("tipo_Contrato"));
            oferta.setTipo_Empleo((String) body.get("tipo_Empleo"));
            oferta.setModalidad((String) body.get("modalidad"));
            oferta.setFecha_Publicacion(LocalDate.now());
            oferta.setFecha_Cierre(LocalDate.parse((String) body.get("fecha_Cierre")));
            oferta.setSector_oferta((String) body.get("sector_oferta"));
            oferta.setExperiencia((Integer) body.get("experiencia"));
            oferta.setNivel_Educacion((String) body.get("nivel_Educacion"));
            oferta.setActivo(true);
            oferta.setEmpresa(empresa);

            // 🔹 Clasificar automáticamente según el título
            CategoriaProfesional categoria = clasificacionService.obtenerCategoriaPorProfesion(oferta.getTitulo());
            oferta.setCategoria(categoria);

            // 🔹 Guardar oferta inicialmente para obtener su ID
            oferta = ofertaService.guardar(oferta);

            // 🔹 Procesar habilidades si se incluyen en el JSON
            if (body.containsKey("habilidades")) {
                @SuppressWarnings("unchecked")
                List<String> habilidades = (List<String>) body.get("habilidades");

                for (String nombreHab : habilidades) {
                    if (nombreHab == null || nombreHab.isBlank())
                        continue;

                    // Buscar o crear la habilidad
                    Habilidad habilidad = habilidadRepository.findByNombreIgnoreCase(nombreHab)
                            .orElseGet(() -> habilidadRepository.save(new Habilidad(null, nombreHab)));

                    // Asociar la habilidad con la oferta
                    OfertaHabilidad oh = new OfertaHabilidad(oferta, habilidad);
                    oferta.getHabilidades().add(oh);
                }

                ofertaService.guardar(oferta);
            }

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of(
                            "mensaje", "Oferta creada correctamente con habilidades",
                            "ofertaId", oferta.getId()));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al crear oferta: " + e.getMessage());
        }
    }

    // ==================== CONTRASEÑA ====================

    @PostMapping("/verificar-contrasena")
    public ResponseEntity<Map<String, Object>> verificarContrasena(@RequestBody VerificarPasswordDTO dto) {
        Empresa empresa = uEmp.findByEmail(dto.getEmail());

        boolean valido = false;
        String mensaje = "Usuario no encontrado";

        if (empresa != null && empresa.getUsuario() != null) {
            String hashedPassword = empresa.getUsuario().getPassword();
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

    @PutMapping("/cambiar-contrasena")
    public ResponseEntity<?> cambiarContraseña(@RequestHeader("Authorization") String authHeader,
            @RequestBody Map<String, String> requestData) {
        String email = jwtUtil.extractEmailFromHeader(authHeader);
        Empresa empresa = uEmp.findByEmail(email);

        if (empresa == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido o usuario no encontrado");

        Usuario usuario = empresa.getUsuario();
        if (usuario == null)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("La empresa no tiene usuario asociado");

        String nuevaContraseña = requestData.get("nuevaContraseña");
        if (nuevaContraseña == null || nuevaContraseña.isBlank()) {
            return ResponseEntity.badRequest().body("La nueva contraseña no puede estar vacía");
        }

        usuario.setPassword(passwordEncoder.encode(nuevaContraseña));
        empresa.setUsuario(usuario);
        try {
            empresaService.actualizarPerfil(empresa);
            return ResponseEntity.ok("Contraseña cambiada con éxito");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al cambiar la contraseña");
        }
    }

    // ==================== ELIMINAR CUENTA ====================
    @PreAuthorize("hasAnyRole('EMPRESA', 'ADMIN')")
    @DeleteMapping("/eliminar")
    public ResponseEntity<?> eliminarCuenta(@RequestHeader("Authorization") String authHeader,
            @RequestBody Map<String, String> requestData) {
        String email = jwtUtil.extractEmailFromHeader(authHeader);
        Empresa empresa = uEmp.findByEmail(email);

        if (empresa == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido o usuario no encontrado");

        Usuario usuario = empresa.getUsuario();
        if (usuario == null)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("La empresa no tiene usuario asociado");

        boolean contraseñaValida = passwordEncoder.matches(requestData.get("password"), usuario.getPassword());
        if (!contraseñaValida) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Contraseña incorrecta");
        }

        try {
            List<Oferta> ofertas = ofertaService.listarOfertasPorEmpresa(empresa);
            for (Oferta oferta : ofertas) {
                ofertaService.delete(oferta.getId());
            }

            empresaService.delete(empresa.getId());
            return ResponseEntity.ok("Cuenta eliminada exitosamente");
        } catch (Exception e) {
            logger.error("Error al eliminar cuenta", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar la cuenta");
        }
    }

}