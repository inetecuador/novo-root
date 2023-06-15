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

    // no puede ir afilaicon A.ID ni FAMILIA.NUMERO por que el titular puede ser que no sea beneficiario solo sus familiares
    // 9E.ID = F.TITULAR_ID  antes A.ID AS AFILIACIONID A.NUMERO AS NUMEROAFILIACION antes A.ID AS AFILIACIONID
    @JsonProperty("Numero")
    private Long entityId;

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
