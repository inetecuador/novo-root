package com.base.repository;

import com.base.common.IQueryDslBaseRepository;
import com.base.entity.PersonEntity;
import com.base.vo.BeneficiarioDTO;

import java.util.List;

/**
 * Contrato repository specification.
 *
 * @author vsangucho on 16/03/2023
 * @version 1.0
 */
public interface IBeneficiarioRepository extends IQueryDslBaseRepository<PersonEntity> {

    /**
     * Find persons active list.
     *
     * @return An array of persons
     */
    List<BeneficiarioDTO> list();
}
