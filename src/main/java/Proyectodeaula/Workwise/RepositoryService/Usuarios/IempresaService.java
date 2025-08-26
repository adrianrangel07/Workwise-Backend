package Proyectodeaula.Workwise.RepositoryService.Usuarios;

import java.util.List;
import java.util.Optional;

import Proyectodeaula.Workwise.Model.Empresa;

public interface IempresaService {

    public List<Empresa> listar_Emp();

    public Optional<Empresa> listarId(Long id);

    public int save(Empresa E);

    public void delete(Long Id);
}
