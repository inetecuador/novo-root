package com.base.service;

import com.base.common.IBaseService;
import com.base.entity.PersonEntity;
import com.base.vo.TitularDTO;

/**
 * Person services specification.
 *
 * @author vsangucho on 16/03/2023
 * @version 1.0
 */
public interface ITitularService extends IBaseService<PersonEntity> {

    /**
     * Find persons active list.
     *
     * @return List personVo
     */
    TitularDTO titularByCedula(String cedulaTitular);

}
