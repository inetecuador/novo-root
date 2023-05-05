package com.base.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * TitularDTO.
 *
 * @author vsangucho on 15/03/2023
 * @version 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TitularDTO {

    // numero familia FAMILIA.NUMERO
    @JsonProperty("Numero")
    private Integer numero;

    @JsonProperty("TipoDocumento")
    private String tipoDocumento;

    @JsonProperty("NumeroDocumento")
    private String identificacion;

    @JsonProperty("Nombres")
    private String nombres;

    @JsonProperty("Apellidos")
    private String apellidos;

    @JsonProperty("FechaNacimiento")
    private String fechaNacimiento;

    @JsonProperty("Genero")
    private String genero;

    // ENTITY.NAME
    @JsonProperty("nombreTitular")
    private String nombreTitular;
}
