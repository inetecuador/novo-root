package com.base.service;

import com.base.common.IBaseService;
import com.base.entity.PersonEntity;
import com.base.vo.BeneficiarioDTO;

import java.util.List;

/**
 * Afiliacion services specification.
 *
 * @author vsangucho on 20/03/2023
 * @version 1.0
 */
public interface IAfiliacionService  extends IBaseService<PersonEntity> {

    /**
     * Find persons active list.
     *
     * @return List beneficiarioDTO
     */
    List<BeneficiarioDTO> list();
}
