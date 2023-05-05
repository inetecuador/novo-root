package com.base.service;

import com.base.common.IBaseService;
import com.base.entity.PersonEntity;
import com.base.vo.ContratoTitularDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import java.util.List;

/**
 * Contrato services specification.
 *
 * @author vsangucho on 16/03/2023
 * @version 1.0
 */
public interface IContratoService extends IBaseService<PersonEntity> {


    /**
     * Find persons active list.
     *
     * @return List contratoTitularDTO
     */
    List<ContratoTitularDTO> obtenerContratoCobertura(String numeroDocumento);


}
