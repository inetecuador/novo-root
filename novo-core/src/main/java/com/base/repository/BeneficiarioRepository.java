package com.base.repository;

import com.base.common.JPAQueryDslBaseRepository;
import com.base.entity.PersonEntity;
import com.base.vo.BeneficiarioDTO;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * BeneficiarioRepository.
 *
 * @author vsangucho on 16/3/2023.
 * @version 1.0
 */
@Lazy
@Repository
public class BeneficiarioRepository extends JPAQueryDslBaseRepository<PersonEntity> implements
        IBeneficiarioRepository {

    /**
     * Constructor.
     */
    public BeneficiarioRepository() {
        super(PersonEntity.class);
    }

    @Override
    public List<BeneficiarioDTO> list() {
        return null;
    }
}
