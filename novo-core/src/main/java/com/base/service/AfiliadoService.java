package com.base.service;

import com.base.common.BaseService;
import com.base.entity.PersonEntity;
import com.base.repository.IAfiliadoRepository;
import com.base.vo.AfiliadoPrexistenciaDTO;

import java.util.List;

public class AfiliadoService extends BaseService<PersonEntity, IAfiliadoRepository> implements
        IAfiliadoService {


    /**
     * Constructor.
     *
     * @param repository repository
     * @author vsangucho on 2023/03/17
     */
    public AfiliadoService(IAfiliadoRepository repository) {
        super(repository);
    }

    @Override
    public List<AfiliadoPrexistenciaDTO> list() {
        return null;
    }
}
