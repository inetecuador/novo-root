package com.base.controller;

import com.base.service.IContratoService;
import com.base.vo.common.BaseResponseVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * PersonController.
 *
 * @author vsangucho on 16/03/2023
 * @version 1.0
 */
@Tag(name = "Contrato", description = "El Contrato API")
@RestController
@RequestMapping("/api/v1/contrato")
@Lazy
public class ContratoController {

    @Lazy
    @Autowired
    private IContratoService contratoService;


    /**
     * Get all persons.
     *
     * @return list personVo
     */
    @GetMapping(path = "/obtenerContratoCobertura")
    @Operation(summary = "Get list of contracts")
    public ResponseEntity<BaseResponseVo> obtenerContratoCobertura(@RequestParam String numeroDocumento) {
        return ResponseEntity.ok(BaseResponseVo.builder().data(this.contratoService.obtenerContratoCobertura(numeroDocumento)).build());
    }
}
