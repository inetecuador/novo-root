package com.base.repository;

import com.base.common.JPAQueryDslBaseRepository;
import com.base.entity.PersonEntity;
import com.base.vo.BeneficiarioDTO;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * AfiliacionRepository.
 *
 * @author vsangucho on 20/3/2023.
 * @version 1.0
 */
@Lazy
@Repository
public class AfiliacionRepository extends JPAQueryDslBaseRepository<PersonEntity> implements
        IAfiliacionRepository {

    /**
     * Constructor.
     */
    public AfiliacionRepository() {
        super(PersonEntity.class);
    }

    @Override
    public List<BeneficiarioDTO> list() {
        return null;
    }
}
