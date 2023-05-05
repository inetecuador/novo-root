package com.base.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * PreexistenciaDTO.
 *
 * @author vsangucho on 15/03/2023
 * @version 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PreexistenciaDTO {

    @JsonProperty("CodigoDiagnostico")
    private String cie10;

    @JsonProperty("Descripcion")
    private String nombre;

    @JsonProperty("FechaInicio")
    private String fechaInicio;

    @JsonProperty("estado")
    private String codigo;

}
