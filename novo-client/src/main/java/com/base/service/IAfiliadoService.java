package com.base.service;

import com.base.vo.AfiliadoPrexistenciaDTO;

import java.util.List;

public interface IAfiliadoService {

    /**
     * Find persons active list.
     *
     * @return List AfiliadoPrexistenciaDTO
     */
    List<AfiliadoPrexistenciaDTO> list();
}
