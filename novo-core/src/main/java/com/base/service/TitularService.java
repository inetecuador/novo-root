package com.base.service;

import com.base.common.BaseService;
import com.base.entity.PersonEntity;
import com.base.repository.ITitularRepository;
import com.base.vo.TitularDTO;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * TitularService.
 *
 * @author vsangucho on 16/03/2023
 * @version 1.0
 */
@Transactional(readOnly = true)
@Lazy
@Service
public class TitularService extends BaseService<PersonEntity, ITitularRepository> implements
        ITitularService {

    /**
     * Constructor.
     *
     * @param repository repository
     * @author vsangucho on 2023/03/16
     */
    public TitularService(ITitularRepository repository) {
        super(repository);
    }

    @Override
    public TitularDTO titularByCedula(String cedulaTitular) {
        return null;
    }
}
