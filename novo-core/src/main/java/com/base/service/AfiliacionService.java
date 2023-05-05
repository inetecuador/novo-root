package com.base.service;

import com.base.common.BaseService;
import com.base.entity.PersonEntity;
import com.base.repository.IAfiliacionRepository;
import com.base.vo.BeneficiarioDTO;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * ContratoService.
 *
 * @author vsangucho on 20/03/2023
 * @version 1.0
 */
@Transactional(readOnly = true)
@Lazy
@Service
public class AfiliacionService extends BaseService<PersonEntity, IAfiliacionRepository> implements
        IAfiliacionService {

    /**
     * Constructor.
     *
     * @param repository repository
     * @author vsangucho on 2023/03/20
     */
    public AfiliacionService(IAfiliacionRepository repository) {
        super(repository);
    }

    @Override
    public List<BeneficiarioDTO> list() {
        return null;
    }
}
