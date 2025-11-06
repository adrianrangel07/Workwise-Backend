package Proyectodeaula.Workwise.Service.Usuarios;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import Proyectodeaula.Workwise.Model.Empresa;
import Proyectodeaula.Workwise.Model.Usuario;
import Proyectodeaula.Workwise.Repository.Empresa.Crud_Emp;
import Proyectodeaula.Workwise.Repository.Empresa.Repository_Emp;
import Proyectodeaula.Workwise.RepositoryService.Usuarios.IempresaService;

@Service
public class EmpresaService implements IempresaService {

    @Autowired
    private Crud_Emp crudEmp;

    @Autowired
    private Repository_Emp repositoryEmp;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    // ================== CRUD BÁSICO ==================

    @Override
    public List<Empresa> listar_Emp() {
        return (List<Empresa>) crudEmp.findAll();
    }

    @Override
    public Optional<Empresa> listarId(Long id) {
        return crudEmp.findById(id);
    }

    @Override
    public int save(Empresa empresa) {
        Usuario usuario = empresa.getUsuario();
        if (usuario != null) {
            usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));

            if (usuario.getRol() == null || usuario.getRol().isEmpty()) {
                usuario.setRol("EMPRESA");
            }
        }

        Empresa saved = crudEmp.save(empresa);
        return saved != null ? 1 : 0;
    }

    @Override
    public void delete(Long id) {
        crudEmp.deleteById((long) id);
    }

    // ================== MÉTODOS ADICIONALES ==================

    public boolean verificarPassword(Long empresaId, String rawPassword) {
        Optional<Empresa> empresaOpt = crudEmp.findById(empresaId);
        if (empresaOpt.isPresent() && empresaOpt.get().getUsuario() != null) {
            String encodedPassword = empresaOpt.get().getUsuario().getPassword();
            return passwordEncoder.matches(rawPassword, encodedPassword);
        }
        return false;
    }

    public Empresa findByEmail(String email) {
        return repositoryEmp.findByEmail(email);
    }

    public void actualizarPerfil(Empresa empresa) throws Exception {
        Empresa emp = repositoryEmp.findByEmail(empresa.getUsuario().getEmail());
        if (emp == null) {
            throw new Exception("Empresa no encontrada");
        }

        // Actualiza campos
        emp.setNombre(empresa.getNombre());
        emp.setDireccion(empresa.getDireccion());
        emp.setRazonSocial(empresa.getRazonSocial());
        emp.setDescripcion(empresa.getDescripcion());
        emp.setSector(empresa.getSector());
        emp.setTelefono(empresa.getTelefono());
        emp.setTipo_telefono(empresa.getTipo_telefono());
        emp.setNit(empresa.getNit());
        emp.setPhoto(empresa.getPhoto());

        // Actualizar usuario
        if (empresa.getUsuario() != null) {
            Usuario usuario = emp.getUsuario();

            usuario.setEmail(empresa.getUsuario().getEmail());

            if (empresa.getUsuario().getPassword() != null &&
                    !empresa.getUsuario().getPassword().isEmpty()) {

                usuario.setPassword(passwordEncoder.encode(empresa.getUsuario().getPassword()));
            }

            emp.setUsuario(usuario);
        }

        crudEmp.save(emp);
    }

    public long contarEmpresas() {
        return crudEmp.count();
    }

    public List<Empresa> obtenerEmpresasRecientes(int limit) {
        return repositoryEmp.findTopNByOrderByIdDesc(limit);
    }

    public Page<Empresa> listarEmpresasPaginadas(Pageable pageable) {
        return repositoryEmp.findAll(pageable);
    }

    public Page<Empresa> buscarEmpresas(String query, Pageable pageable) {
        return repositoryEmp.buscarPorNombreNitOEmail(query, pageable);
    }

    public void cambiarEstadoEmpresa(Long id) {
        Empresa empresa = crudEmp.findById(id)
                .orElseThrow(() -> new RuntimeException("Empresa no encontrada"));
        empresa.setActivo(!empresa.isActivo());
        crudEmp.save(empresa);
    }

}
