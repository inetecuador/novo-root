package com.base.repository;

import com.base.common.IQueryDslBaseRepository;
import com.base.entity.PersonEntity;
import com.base.vo.PreexistenciaDTO;

import java.util.List;

/**
 * Preexistencia repository specification.
 *
 * @author vsangucho on 16/03/2023
 * @version 1.0
 */
public interface IPreexistenciaRepository extends IQueryDslBaseRepository<PersonEntity> {

    /**
     * Find persons active list.
     *
     * @return An array of persons
     */
    List<PreexistenciaDTO> prexistenciasPorAfiliado(Integer numeroContrato, Integer numeroFamilia, Integer numeroAfiliado);

}
