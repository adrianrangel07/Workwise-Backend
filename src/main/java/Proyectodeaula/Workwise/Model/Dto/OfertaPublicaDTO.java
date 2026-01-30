package Proyectodeaula.Workwise.Model.Dto;

import java.time.LocalDate;

import Proyectodeaula.Workwise.Model.Ofertas.Oferta;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OfertaPublicaDTO {
    private Long id;
    private String titulo;
    private String descripcion;
    private int salario;
    private String moneda;
    private String ubicacion;
    private String tipoContrato;
    private String tipoEmpleo;
    private String modalidad;
    private LocalDate fechaPublicacion;
    private LocalDate fechaCierre;
    private String sectorOferta;
    private int experiencia;
    private String nivelEducacion;
    private boolean activo;
    private String empresaNombre;
    private String categoriaNombre;

    public OfertaPublicaDTO(Oferta oferta) {
        this.id = oferta.getId();
        this.titulo = oferta.getTitulo();
        this.descripcion = oferta.getDescripcion();
        this.salario = oferta.getSalario();
        this.moneda = oferta.getMoneda();
        this.ubicacion = oferta.getUbicacion();
        this.tipoContrato = oferta.getTipo_Contrato();
        this.tipoEmpleo = oferta.getTipo_Empleo();
        this.modalidad = oferta.getModalidad();
        this.fechaPublicacion = oferta.getFecha_Publicacion();
        this.fechaCierre = oferta.getFecha_Cierre();
        this.sectorOferta = oferta.getSector_oferta();
        this.experiencia = oferta.getExperiencia();
        this.nivelEducacion = oferta.getNivel_Educacion();
        this.activo = oferta.isActivo();
        this.empresaNombre = oferta.getEmpresa() != null ? oferta.getEmpresa().getNombre() : null;
        this.categoriaNombre = oferta.getCategoria() != null ? oferta.getCategoria().getNombre() : null;
    }
}
