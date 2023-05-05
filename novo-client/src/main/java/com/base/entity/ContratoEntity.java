package com.base.entity;

import com.base.common.AbstractBaseAuditable;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

/**
 * Contrato entity.
 *
 * @author vsangucho on 23/03/2023
 * @version 1.0
 */
@Getter
@Setter
@SuperBuilder
@Entity(name = "CONTRATO")
public class ContratoEntity extends AbstractBaseAuditable<Long> {

    private static final long serialVersionUID = 2L;

    @Id
    @Column(name = "ID")
    private Long id;

    @Override
    public Long getId() {
        return this.id;
    }

    /**
     * Identificador del contratop, debe ser secuencial y que se diferencie de cada ciudad en caso de
     * cambio de ciudad Guayaquil o Quito
     */
    @Column(nullable=false, unique=true)
    //@Range(min=-1, message="Por favor ingrese numeros mayores a cero" )
    private Long numero;

    /**
     * Fecha a partir de la cual inicia el contrato
     */
    @Column (nullable=false)
    private Date fechaInicio;

    /**
     * Fecha en la que se renueva el contrato
     */
    @Column (nullable=false)
    private Date fechaRenovacion;

    /**
     * Fecha hasta cuando est vigente el contrato
     */
    @Column (nullable=false)
    private Date fechaVencimiento;

    /**
     * Es la Fecha en la que se termina el contrato entre el cliente y la empresa.
     */
    @Column (nullable=true)
    private Date fechaCancelacion;

    /**
     * Fecha en la que se van realizando los pagos mensuales
     */
    @Column (nullable=false)
    private Date fechaPago;

    /**
     * Es la parte del valor de la cuota mensual que debe pagar el cliente
     */
    @Column (nullable=true)
    private Double abono;

    /**
     * Es el numero que identifica como se va a calcular el valor de la cuota.
     */
    @Column (nullable=false)
    private Integer codigoTarifa; // contemplar varias tarifas para emision de contratos

    /**
     * Estado del contrato, sirve para dar servicio a los usuarios
     * A: activo, C:cancelado, S:supendido (10 primeros dias), N: anulado.
     * Contemplar el motivo de cancelacion para emision de contratos
     */
    @Column(length=1,nullable=false)
    private String estado;

    /**
     * TODO:
     * Persona que va al domicilio a cobrar la cuota, queda pendiente para analizar
     */
    @Column(length=40, nullable=true)
    private String recaudador;

    /**
     * Contiene los tipos de contrato que tiene Ecuasanitas
     */
    /*@Enumerated(EnumType.STRING)
    @Column(length=10, nullable=false)
    private TipoContrato tipo;*/

    @Column (nullable=true)
    private String descripcion;

    @Column (length=5,nullable=true)
    private String creditoHospitalario;

    /*estaba GrupoFacturacion*/
    @Column
    private String grupoFacturacion;

    /*@Enumerated(EnumType.STRING)
    @Column(length=12, nullable=false)
    private SubTipoContrato subTipo;*/

    @Column
    private Double porcentajeIncremento;

    @Column
    private Double valorTarifaCheque;

    /*@Enumerated(EnumType.STRING)
    @Column(length=12, nullable=false)
    private Producto producto;*/

    //20180802
    @Column
    private Boolean desglosaCopago;

    @Column
    private Boolean convenioEspecial;

    //2022-10-24 CM para correo fijo de Global Llantas
    @Column(length=150)
    private String correo;

    /**
     * Contiene la sucursal donde se realizo el contrato
     */
    /*@ManyToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="SUCURSAL_ID",nullable=true)
    private Branch sucursal;*/

    /**
     * La persona que responde por la realizacion del contrato entre Ecuasanitas y el benficiario.
     * Puede ser el mismo titular del contrato
     */
    /*@ManyToOne(fetch=FetchType.LAZY)
    private Entity representanteLegal;*/

    /**
     * El beneficiario del contrato
     */
    /*@ManyToOne(fetch=FetchType.LAZY)
    private Entity titular;*/

    /**
     * La persona a quien se le va a enviar las facturas del cobro del seguro.
     */
    /*@ManyToOne(fetch=FetchType.LAZY)
    private Entity titularCobro;*/

    /*
    @OneToOne
    private Convenio convenio;*/

    /*
    //30-07-2021 victor cardenas
    @ManyToOne//(fetch=FetchType.LAZY)
    @JoinColumn(name="motivosuspencion_ID",nullable=true)
    private MotivoSuspencion motivoSuspencion;*/

    /*no hay el campo codconvenio*/

    //2022-10-20 CM para controlar el tiempo de servicio
    /*@OneToOne PoolContrato pool;*/

}
