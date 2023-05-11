package com.base.repository;

import com.base.common.IQueryDslBaseRepository;
import com.base.entity.PersonEntity;
import com.base.vo.PersonVo;

import java.util.List;

/**
 * Person repository specification.
 *
 * @author vsangucho on 10/03/2023
 * @version 1.0
 */
public interface IPersonRepository extends IQueryDslBaseRepository<PersonEntity> {


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
     * @return An array of persons
     */
    List<PersonVo> list();


    /**
     * Update Person.
     *
     * @param personVo person Object
     * @return PersonVo updated in DB
     */
    PersonVo updateValues(PersonVo personVo);
}
