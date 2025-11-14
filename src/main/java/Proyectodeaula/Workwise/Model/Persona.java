package Proyectodeaula.Workwise.Model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
    @Column(name = "fecha_Nacimiento", columnDefinition = "date", nullable = false)
    private LocalDate fecha_Nacimiento;
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
    @Column(name = "activo", nullable = false)
    private boolean activo;

    @OneToOne(mappedBy = "persona", cascade = CascadeType.ALL, orphanRemoval = true)
    private EmailVerification emailVerification;

    @Lob
    @Column(name = "cv", columnDefinition = "Longblob")
    private byte[] cv;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "usuario_id", referencedColumnName = "id")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private CategoriaProfesional categoria;

    @OneToMany(mappedBy = "persona", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<PersonaHabilidad> habilidades = new ArrayList<>();

}
