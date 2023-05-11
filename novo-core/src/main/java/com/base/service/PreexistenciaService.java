package com.base.service;

import com.base.common.BaseService;
import com.base.entity.PersonEntity;
import com.base.repository.IPreexistenciaRepository;
import com.base.vo.PreexistenciaDTO;
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
public class PreexistenciaService extends BaseService<PersonEntity, IPreexistenciaRepository> implements
        IPreexistenciaService {


    /**
     * Constructor.
     *
     * @param repository repository
     * @author vsangucho on 2022/09/26
     */
    public PreexistenciaService(IPreexistenciaRepository repository) {
        super(repository);
    }

    @Override
    public List<PreexistenciaDTO> list() {
        return null;
    }
}
