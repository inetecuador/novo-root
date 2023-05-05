package com.base.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * BeneficiarioDTO.
 *
 * @author vsangucho on 15/03/2023
 * @version 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BeneficiarioDTO {

    private Integer numeroAfiliado;

    private String nombreParentesco;

    private String nombres;

    private String apellidos;

    private String genero;

    private String tipoDocumento;

    private String identificacion;

    private String fechaNacimiento;

    private Integer edad;

    private String fechaInclusion;

    private BigDecimal deducibleCubierto;

    private Boolean enCarencia;

    private Integer diasFinCarencia;

    private Boolean enCarenciaHospitalaria;

    private Integer diasFinCarenciaHospitalaria;

    private List<PreexistenciaDTO> preexistencias;

    private Boolean beneficioOda;

    private String observaciones;

    private Boolean maternidad;

    private Integer numeroFamilia;

}
