package com.base.repository;

import com.base.common.IQueryDslBaseRepository;
import com.base.entity.PersonEntity;
import com.base.vo.ContratoTitularDTO;
import com.base.vo.TitularDTO;

import java.util.List;

/**
 * Contrato repository specification.
 *
 * @author vsangucho on 16/03/2023
 * @version 1.0
 */
public interface IContratoRepository extends IQueryDslBaseRepository<PersonEntity> {

    /**
     * Find persons active list.
     *
     * @return An array of persons
     */
    List<ContratoTitularDTO> contratosPorTitular(TitularDTO titular);

    /**
     *
     * @param numContrato
     * @param numFamilia
     * @param numAfiliado
     * @return
     */
    String validaContrato(Integer numContrato, Integer numFamilia, Integer numAfiliado);
}
