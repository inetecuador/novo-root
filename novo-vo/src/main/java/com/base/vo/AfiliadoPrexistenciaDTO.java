package com.base.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * AfiliadoPrexistenciaDTO.
 *
 * @author vsangucho on 17/03/2023
 * @version 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AfiliadoPrexistenciaDTO {

    @JsonProperty("NumeroPersona")
    private Integer numeroAfiliado;

    @JsonProperty("RelacionDependiente")
    private String nombreParentesco;

    @JsonProperty("Nombres")
    private String nombres;

    @JsonProperty("Apellidos")
    private String apellidos;

    @JsonProperty("Genero")
    private String genero;

    @JsonProperty("TipoDocumento")
    private String tipoDocumento;

    @JsonProperty("NumeroDocumento")
    private String identificacion;

    @JsonProperty("FechaNacimiento")
    private String fechaNacimiento;

    @JsonProperty("Edad")
    private Integer edad;

    @JsonProperty("FechaInclusion")
    private String fechaInclusion;

    @JsonProperty("DeducibleCubierto")
    private BigDecimal deducibleCubierto;

    @JsonProperty("EnCarencia")
    private Boolean enCarencia;

    @JsonProperty("DiasFinCarencia")
    private Integer diasFinCarencia;

    @JsonProperty("EnCarenciaHospitalaria")
    private Boolean enCarenciaHospitalaria;

    @JsonProperty("DiasFinCarenciaHospitalaria")
    private Integer diasFinCarenciaHospitalaria;

    @JsonProperty("Preexistencias")
    private List<PreexistenciaDTO> preexistencias;

    @JsonProperty("BeneficioOda")
    private Boolean beneficioOda;

    @JsonProperty("Observaciones")
    private String observaciones;

    @JsonProperty("Maternidad")
    private Boolean maternidad;

    private Integer numeroFamilia;
}
