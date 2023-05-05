package com.base.service;

import com.base.common.IBaseService;
import com.base.entity.PersonEntity;
import com.base.vo.PreexistenciaDTO;

import java.util.List;

/**
 * Preexistencia services specification.
 *
 * @author vsangucho on 17/03/2023
 * @version 1.0
 */
public interface IPreexistenciaService extends IBaseService<PersonEntity> {

    /**
     * Find persons active list.
     *
     * @return List PreexistenciaDTO
     */
    List<PreexistenciaDTO> list();
}
