package com.base.service;

import com.base.common.BaseService;
import com.base.entity.PersonEntity;
import com.base.repository.IAfiliadoRepository;
import com.base.repository.IContratoRepository;
import com.base.repository.IPreexistenciaRepository;
import com.base.repository.ITitularRepository;
import com.base.vo.AfiliadoPrexistenciaDTO;
import com.base.vo.ContratoTitularDTO;
import com.base.vo.TitularDTO;
import org.springframework.beans.factory.annotation.Autowired;
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
public class ContratoService extends BaseService<PersonEntity, IContratoRepository> implements
        IContratoService {

    @Lazy
    @Autowired
    private ITitularRepository titularRepository;

    @Lazy
    @Autowired
    private IAfiliadoRepository afiliadoRepository;

    @Lazy
    @Autowired
    private IPreexistenciaRepository preexistenciaRepository;

    /**
     * Constructor.
     *
     * @param repository repository
     * @author vsangucho on 2023/03/20
     */
    public ContratoService(IContratoRepository repository) {
        super(repository);
    }

    @Override
    public List<ContratoTitularDTO> obtenerContratoCobertura(String numeroDocumento) {
        TitularDTO titularDTO = this.titularRepository.titularPorCedula(numeroDocumento);
        if (null == titularDTO) {
            return null;
        }
        List<ContratoTitularDTO> contratoTitularDTOS = this.repository.contratosPorTitular(titularDTO);
        if (null == contratoTitularDTOS) {
            return null;
        }
        // String numeroFamiliaTitular = titularDTO.getNumero() +"";
        for (ContratoTitularDTO contratoTitularDTO : contratoTitularDTOS) {
            // String numero = contratoTitularDTO.getNumeroContrato()+"" + numeroFamiliaTitular;
            // titularDTO.setNumero(Integer.parseInt(numero));
            contratoTitularDTO.setTitularDTO(titularDTO);
            contratoTitularDTO.setAfiliados(this.afiliadoRepository.afiliadosPorContrato(numeroDocumento, contratoTitularDTO.getNumeroContrato()));
            for (AfiliadoPrexistenciaDTO afiliado : contratoTitularDTO.getAfiliados()) {
                afiliado.setPreexistencias(this.preexistenciaRepository.prexistenciasPorAfiliado(contratoTitularDTO.getNumeroContrato(), afiliado.getNumeroFamilia(), afiliado.getNumeroAfiliado()));
            }
        }
        return contratoTitularDTOS;
    }


}
