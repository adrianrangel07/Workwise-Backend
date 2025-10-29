package Proyectodeaula.Workwise.Model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "Ofertas")
public class Oferta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "titulo", columnDefinition = "varchar(100)", nullable = false)
    private String titulo;
    @Column(name = "descripcion", columnDefinition = "text", nullable = false)
    private String descripcion;
    @Column(name = "salario", columnDefinition = "int", nullable = false)
    private int salario;
    @Column(name = "moneda", columnDefinition = "varchar(50)", nullable = false)
    private String moneda;
    @Column(name = "ubicacion", columnDefinition = "varchar(100)", nullable = false)
    private String ubicacion;
    @Column(name = "tipo_Contrato", columnDefinition = "varchar(50)", nullable = false)
    private String tipo_Contrato;
    @Column(name = "tipo_Empleo", columnDefinition = "varchar(50)", nullable = false)
    private String tipo_Empleo;
    @Column(name = "modalidad", columnDefinition = "varchar(50)", nullable = false)
    private String modalidad;
    @Column(name = "fecha_Publicacion", columnDefinition = "Date", nullable = false)
    private LocalDate fecha_Publicacion;
    @Column(name = "fecha_Cierre", columnDefinition = "Date", nullable = false)
    private LocalDate fecha_Cierre;
    @Column(name = "sector_oferta", columnDefinition = "varchar(100)", nullable = false)
    private String sector_oferta;
    @Column(name = "experiencia", columnDefinition = "int", nullable = false)
    private int experiencia;
    @Column(name = "nivel_Educacion", columnDefinition = "varchar(100)", nullable = false)
    private String nivel_Educacion;
    @Column(name = "activo", columnDefinition = "boolean", nullable = false)
    private boolean activo = true;

    @ManyToOne
    @JoinColumn(name = "empresa_id")
    private Empresa empresa;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private CategoriaProfesional categoria;

    @OneToMany(mappedBy = "oferta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OfertaHabilidad> habilidades = new ArrayList<>();

}
