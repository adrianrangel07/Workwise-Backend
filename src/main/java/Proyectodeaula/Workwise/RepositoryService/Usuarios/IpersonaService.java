package Proyectodeaula.Workwise.RepositoryService.Usuarios;


import java.util.List;
import java.util.Optional;

import Proyectodeaula.Workwise.Model.Personas.Persona;

public interface IpersonaService {

    public List<Persona> listar();

    public Optional<Persona> listarId(Long id);

    public int save(Persona persona);

    public void delete(Long Id);
}

