package Proyectodeaula.Workwise.Model.Dto;

import Proyectodeaula.Workwise.Model.Oferta;
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
    private String ubicacion;
    private int salario;
    private String moneda;
    private String tipo_Contrato;
    private String modalidad;
    private String sector_oferta;
    private String nivel_Educacion;
    private boolean activo;
    private String empresaNombre;
    private String categoriaNombre;

    public OfertaPublicaDTO(Oferta oferta) {
        this.id = oferta.getId();
        this.titulo = oferta.getTitulo();
        this.descripcion = oferta.getDescripcion();
        this.ubicacion = oferta.getUbicacion();
        this.salario = oferta.getSalario();
        this.moneda = oferta.getMoneda();
        this.tipo_Contrato = oferta.getTipo_Contrato();
        this.modalidad = oferta.getModalidad();
        this.sector_oferta = oferta.getSector_oferta();
        this.nivel_Educacion = oferta.getNivel_Educacion();
        this.activo = oferta.isActivo();
        this.empresaNombre = oferta.getEmpresa() != null ? oferta.getEmpresa().getNombre() : null;
        this.categoriaNombre = oferta.getCategoria() != null ? oferta.getCategoria().getNombre() : null;
    }

}