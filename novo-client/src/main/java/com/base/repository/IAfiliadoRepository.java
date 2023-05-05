package com.base.repository;

import com.base.common.IQueryDslBaseRepository;
import com.base.entity.PersonEntity;
import com.base.vo.AfiliadoPrexistenciaDTO;
import com.base.vo.BeneficiarioDTO;
import com.base.vo.ContratoTitularDTO;
import com.base.vo.TitularDTO;

import java.util.List;

/**
 * Afiliado repository specification.
 *
 * @author vsangucho on 17/03/2023
 * @version 1.0
 */
public interface IAfiliadoRepository extends IQueryDslBaseRepository<PersonEntity> {

    /**
     * Find persons active list.
     *
     * @return An array of persons
     */
    List<AfiliadoPrexistenciaDTO> afiliadosPorContrato(String numeroDocumento, Integer numeroContrato);

}
