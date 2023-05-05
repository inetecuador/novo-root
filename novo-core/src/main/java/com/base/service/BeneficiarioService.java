package com.base.service;

import com.base.common.BaseService;
import com.base.entity.PersonEntity;
import com.base.repository.IBeneficiarioRepository;
import com.base.vo.BeneficiarioDTO;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * ContratoService.
 *
 * @author vsangucho on 16/03/2023
 * @version 1.0
 */
@Transactional(readOnly = true)
@Lazy
@Service
public class BeneficiarioService extends BaseService<PersonEntity, IBeneficiarioRepository> implements
        IBeneficiarioService {

    /**
     * Constructor.
     *
     * @param repository repository
     * @author vsangucho on 2023/03/16
     */
    public BeneficiarioService(IBeneficiarioRepository repository) {
        super(repository);
    }

    @Override
    public List<BeneficiarioDTO> list() {
        return null;
    }
}
