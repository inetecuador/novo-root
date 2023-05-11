package com.base.service;

import com.base.common.IBaseService;
import com.base.entity.PersonEntity;
import com.base.vo.PersonVo;

import java.util.List;

/**
 * Person services specification.
 *
 * @author vsangucho on 10/03/2023
 * @version 1.0
 */
public interface IPersonService extends IBaseService<PersonEntity> {

    /**
     * Save Person.
     *
     * @param personVo person object
     */
    void save(PersonVo personVo);

    /**
     * Verify existing person.
     *
     * @param documentNumber document number
     * @return Boolean
     */
    Boolean exist(String documentNumber);

    /**
     * Find persons active list.
     *
     * @return List personVo
     */
    List<PersonVo> list();


}
