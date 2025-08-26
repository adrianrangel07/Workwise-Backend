package Proyectodeaula.Workwise.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "personas")
public class Persona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", columnDefinition = "varchar(50)", nullable = false)
    private String nombre;
    @Column(name = "apellido", columnDefinition = "varchar(50)", nullable = false)
    private String apellido;
    @Column(name = "numero_documento", columnDefinition = "int", nullable = false, unique = true)
    private int numero_documento;
    @Column(name = "tipo_Documento", columnDefinition = "varchar(20)", nullable = false)
    private String tipo_Documento;
    @Column(name = "fecha_Nacimiento", columnDefinition = "int", nullable = false)
    private int fecha_Nacimiento;
    @Column(name = "genero", columnDefinition = "varchar(20)", nullable = false)
    private String genero;
    @Column(name = "direccion", columnDefinition = "varchar(100)", nullable = false)
    private String direccion;
    @Column(name = "telefono", columnDefinition = "varchar(50)", nullable = false, unique = true)
    private String telefono;
    @Column(name = "tipo_telefono", columnDefinition = "varchar(20)", nullable = false)
    private String tipo_telefono;
    @Column(name = "profesion", columnDefinition = "varchar(50)", nullable = false)
    private String profesion;
    @Column(name = "photo", columnDefinition = "blob")
    private byte[] photo;
    @Column(name = "activo", columnDefinition = "boolean", nullable = false)
    private boolean activo;

    @Lob
    @Column(name = "cv", columnDefinition = "blob")
    private byte[] cv;

    @OneToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;
}
