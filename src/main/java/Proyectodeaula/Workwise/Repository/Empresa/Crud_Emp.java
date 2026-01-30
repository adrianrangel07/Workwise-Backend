package Proyectodeaula.Workwise.Repository.Empresa;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import Proyectodeaula.Workwise.Model.Empresas.Empresa;

@Repository
public interface Crud_Emp extends CrudRepository <Empresa, Long> {
    
}
