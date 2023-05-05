package com.base.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DeducibleDTO.
 *
 * @author vsangucho on 15/03/2023
 * @version 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeducibleDTO {

    private String codigo;

    private String region;

    private String codigoProducto;

    private String codigoPlan;

    private Integer verisonPlan;

    private String tipoCobertura;

    private BigDecimal monto;

}
