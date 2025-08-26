package Proyectodeaula.Workwise.Service.Usuarios;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import Proyectodeaula.Workwise.Model.Persona;
import Proyectodeaula.Workwise.Model.Usuario;
import Proyectodeaula.Workwise.Repository.Persona.Repository_Persona;
import Proyectodeaula.Workwise.RepositoryService.Usuarios.IpersonaService;

@Service
public class PersonaService implements IpersonaService {

    @Autowired
    private Repository_Persona repositoryPersona;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public List<Persona> listar() {
        return repositoryPersona.findAll();
    }

    @Override
    public Optional<Persona> listarId(Long id) {
        return repositoryPersona.findById(id);
    }

    @Override
    public int save(Persona persona) {
        Usuario usuario = persona.getUsuario();
        if (usuario != null) {
            usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
            if (usuario.getRol() == null || usuario.getRol().isEmpty()) {
                usuario.setRol("PERSONA");
            }
        }

        Persona saved = repositoryPersona.save(persona);
        return saved != null ? 1 : 0;
    }

    @Override
    public void delete(Long id) {
        repositoryPersona.deleteById(id);
    }

    public void actualizarPerfil(Persona persona) throws Exception {
        Persona per = repositoryPersona.findByEmail(persona.getUsuario().getEmail());
        if (per == null) {
            throw new Exception("Persona no encontrada");
        }

        per.setNombre(persona.getNombre());
        per.setApellido(persona.getApellido());
        per.setDireccion(persona.getDireccion());
        per.setTelefono(persona.getTelefono());
        per.setTipo_telefono(persona.getTipo_telefono());
        per.setProfesion(persona.getProfesion());
        per.setPhoto(persona.getPhoto());
        per.setCv(persona.getCv());

        if (persona.getUsuario() != null) {
            Usuario usuario = per.getUsuario();
            usuario.setEmail(persona.getUsuario().getEmail());
            if (persona.getUsuario().getPassword() != null && !persona.getUsuario().getPassword().isEmpty()) {
                usuario.setPassword(passwordEncoder.encode(persona.getUsuario().getPassword()));
            }
            per.setUsuario(usuario);
        }

        repositoryPersona.save(per);
    }
}

