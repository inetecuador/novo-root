package com.base.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;


/**
 * ContratoTitularDTO.
 *
 * @author vsangucho on 15/03/2023
 * @version 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ContratoTitularDTO {

    @JsonProperty("Region")
    private String region;

    @JsonProperty("Producto")
    private String producto;

    @JsonProperty("Numero")
    private int numeroContrato;

    @JsonProperty("CodigoPlan")
    private Integer codigoPlan;

    /**
     * numeroContrato
     */
    @JsonProperty("Codigo")
    private Integer codigo;

    @JsonProperty("Estado")
    private String estadoContrato;

    @JsonProperty("CodigoEstado")
    private Integer codigoEstado;

    @JsonProperty("NombrePlan")
    private String nombrePlanSalud;

    @JsonProperty("Version")
    private Integer version;

    @JsonProperty("CoberturaMaxima")
    private BigDecimal montoContratado;

    @JsonProperty("Nivel")
    private Integer nivel;

    @JsonProperty("Titular")
    private TitularDTO titularDTO;

    @JsonProperty("DeducibleTotal")
    private BigDecimal deducibleTotal;

    @JsonProperty("EsMoroso")
    private Boolean esMoroso;

    @JsonProperty("NombreComercialPlan")
    private String nombreComercialPlan;

    @JsonProperty("FechaInicio")
    private String fechaInicioContrato;

    @JsonProperty("FechaVigencia")
    private String fechaVencimiento;

    @JsonProperty("CodigoPmf")
    private String codigoPmf;

    @JsonProperty("Empresa")
    private String empresa;

    @JsonProperty("NumeroEmpresa")
    private Integer numeroEmpresa;

    @JsonProperty("Sucursal")
    private String nombreSucursalContrato;

    @JsonProperty("CodigoSucursal")
    private String codigoSucursalContrato;

    @JsonProperty("NombreLista")
    private String contratoTipo;

    @JsonProperty("NumeroLista")
    private Integer numeroLista;

    @JsonProperty("EsDeducibleAnual")
    private Boolean esDeducibleAnual;

    @JsonProperty("Ruc")
    private String ruc;

    @JsonProperty("RazonSocial")
    private String razonSocial;

    @JsonProperty("EsSmartPlan")
    private Boolean esSmartPlan;

    @JsonProperty("Observaciones")
    private String observaciones;

    @JsonProperty("Deducibles")
    private List<DeducibleDTO> deducibles;

    @JsonProperty("Beneficiarios")
    private List<AfiliadoPrexistenciaDTO> afiliados;
}
