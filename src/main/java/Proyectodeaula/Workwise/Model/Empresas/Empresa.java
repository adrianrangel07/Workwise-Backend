package Proyectodeaula.Workwise.Model.Empresas;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import Proyectodeaula.Workwise.Model.Ofertas.Oferta;
import Proyectodeaula.Workwise.Model.Otros.Usuario;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "empresas")
public class Empresa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", columnDefinition = "varchar(50)", nullable = false)
    private String nombre;
    @Column(name = "direccion", columnDefinition = "varchar(100)", nullable = false)
    private String direccion;
    @Column(name = "razon_social", columnDefinition = "varchar(100)", nullable = false)
    private String razonSocial;
    @Column(name = "nit", columnDefinition = "varchar(20)", nullable = false)
    private String nit;
    @Column(name = "telefono", columnDefinition = "varchar(20)", nullable = false)
    private String telefono;
    @Column(name = "tipo_telefono", columnDefinition = "varchar(20)", nullable = false)
    private String tipo_telefono;
    @Column(name = "sector", columnDefinition = "varchar(50)", nullable = false)
    private String sector;
    @Column(name = "descripcion", columnDefinition = "varchar(255)", nullable = false)
    private String descripcion;
    @Column(name = "photo", columnDefinition = "blob")
    private byte[] photo;
    @Column(name = "activo", nullable = false)
    private boolean activo;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "usuario_id", referencedColumnName = "id")
    private Usuario usuario;

    @OneToMany(mappedBy = "empresa", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Oferta> ofertas;

}
