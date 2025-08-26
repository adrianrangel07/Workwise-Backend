package Proyectodeaula.Workwise.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
    @Column(name = "requisitos", columnDefinition = "text", nullable = false)
    private String requisitos;
    @Column(name = "salario", columnDefinition = "int", nullable = false)
    private int salario;
    @Column(name = "moneda", columnDefinition = "varchar(50)", nullable = false)
    private String moneda;
    @Column(name = "ubicacion", columnDefinition = "varchar(100)", nullable = false)
    private String ubicacion;
    @Column(name = "tipo_Contrato", columnDefinition = "varchar(50)", nullable = false)
    private String tipo_Contrato;
    @Column(name = "modalidad", columnDefinition = "varchar(50)", nullable = false)
    private String modalidad;
    @Column(name = "fecha_Publicacion", columnDefinition = "varchar(50)", nullable = false)
    private String fecha_Publicacion;
    @Column(name = "fecha_Cierre", columnDefinition = "varchar(50)", nullable = false)
    private String fecha_Cierre;
    @Column(name = "sector_oferta", columnDefinition = "varchar(100)", nullable = false)
    private String sector_oferta;
    @Column(name = "experiencia", columnDefinition = "int", nullable = false)
    private int experiencia;
    @Column(name = "nivel_Educacion", columnDefinition = "varchar(100)", nullable = false)
    private String nivel_Educacion;
    @Column(name = "activo", columnDefinition = "boolean", nullable = false)
    private String activo;
    @Column(name = "comentarios", columnDefinition = "text", nullable = true)
    private String comentarios;


    @ManyToOne
    @JoinColumn(name = "empresa_id")
    private Empresa empresa;
}
