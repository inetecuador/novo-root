package com.base.repository;

import com.base.common.JPAQueryDslBaseRepository;
import com.base.entity.PersonEntity;
import com.base.vo.PreexistenciaDTO;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * PreexistenciaRepository.
 *
 * @author vsangucho on 16/3/2023.
 * @version 1.0
 */
@Lazy
@Repository
public class PreexistenciaRepository extends JPAQueryDslBaseRepository<PersonEntity> implements
        IPreexistenciaRepository {

    /**
     * Constructor.
     */
    public PreexistenciaRepository() {
        super(PersonEntity.class);
    }

    @Override
    /* en base al metodo queryPreexistencia1(  */
    public List<PreexistenciaDTO> prexistenciasPorAfiliado(Integer numeroContrato, Integer numeroFamilia, Integer numeroAfiliado) {
        List<PreexistenciaDTO> preexistenciaDTOS = new ArrayList<>();
        List<Object[]> results = new ArrayList<>();
        BigDecimal numeroPreexistenciasConMonto = this.numeroPreexistenciasConMonto(numeroContrato, numeroFamilia, numeroAfiliado);
        /*si 0 es menor a numeroContratosActivosTitular = -1 */
        if (BigDecimal.ZERO.compareTo(numeroPreexistenciasConMonto) < 0) {
            preexistenciaDTOS = this.prexistenciasConMonto(numeroContrato, numeroFamilia, numeroAfiliado);
        } else {
            preexistenciaDTOS = this.prexistenciasSinMonto(numeroContrato, numeroFamilia, numeroAfiliado);
        }
        return preexistenciaDTOS;
    }

    private BigDecimal numeroPreexistenciasConMonto(Long afiliacioId) {
        String sqlString = "SELECT count(1) total " + "FROM CONTRATO " + "INNER JOIN AFILIACION ON CONTRATO.NUMERO = AFILIACION.NUMEROCONTRATO " //
                + "INNER JOIN PLANCONTRATO ON PLANCONTRATO.CONTRATO_ID = CONTRATO.ID " //
                + "INNER JOIN PLANESPECIFICO ON PLANCONTRATO.PLANESPECIFICO_ID = PLANESPECIFICO.ID " //
                + "INNER JOIN MONTOPREEXISTENCIA ON MONTOPREEXISTENCIA.PLAN_ID = PLANESPECIFICO.ID " //
                + "INNER JOIN AFILIADO ON AFILIACION.AFILIADO_ID = AFILIADO.ID " + "INNER JOIN PREEXISTENCIA ON PREEXISTENCIA.AFILIADO_ID = AFILIADO.ID " //
                + "INNER JOIN DIAGNOSTICO ON PREEXISTENCIA.DIAGNOSTICO_ID = DIAGNOSTICO.ID " + "INNER JOIN ITEMMEDICO ON DIAGNOSTICO.ID = ITEMMEDICO.ID " //
                + "WHERE AFILIACION.ID = :afiliacioId AND " //
                + "TRUNC(PLANCONTRATO.FECHAVIGENCIA) = QUERYMAXIMAFECHA(AFILIACION.NUMEROCONTRATO, AFILIACION.NUMEROFAMILIA, AFILIACION.NUMERO)";
        Query query = this.getEntityManager().createNativeQuery(sqlString);
        query.setParameter("afiliacioId", afiliacioId);
        Object result1 = query.getSingleResult();
        // no se necesita parsear de null a 0 ya que si no hay registros devuelve 0
        // decide = (BigDecimal) (result1 == null ? BigDecimal.ZERO : result1);
        return (BigDecimal) result1;
    }

    private BigDecimal numeroPreexistenciasConMonto(Integer numeroContrato, Integer numeroFamilia, Integer numeroAfiliado) {
        String sqlString = "SELECT count(1) total " + "FROM CONTRATO " + "INNER JOIN AFILIACION ON CONTRATO.NUMERO = AFILIACION.NUMEROCONTRATO " //
                + "INNER JOIN PLANCONTRATO ON PLANCONTRATO.CONTRATO_ID = CONTRATO.ID " //
                + "INNER JOIN PLANESPECIFICO ON PLANCONTRATO.PLANESPECIFICO_ID = PLANESPECIFICO.ID " //
                + "INNER JOIN MONTOPREEXISTENCIA ON MONTOPREEXISTENCIA.PLAN_ID = PLANESPECIFICO.ID " //
                + "INNER JOIN AFILIADO ON AFILIACION.AFILIADO_ID = AFILIADO.ID " + "INNER JOIN PREEXISTENCIA ON PREEXISTENCIA.AFILIADO_ID = AFILIADO.ID " //
                + "INNER JOIN DIAGNOSTICO ON PREEXISTENCIA.DIAGNOSTICO_ID = DIAGNOSTICO.ID " + "INNER JOIN ITEMMEDICO ON DIAGNOSTICO.ID = ITEMMEDICO.ID " //
                + "WHERE " + "AFILIACION.NUMEROCONTRATO = :numContrato AND  " + "AFILIACION.NUMEROFAMILIA = :codFamilia AND " //
                + "AFILIACION.NUMERO = :numAfiliacion AND " //
                + "TRUNC(PLANCONTRATO.FECHAVIGENCIA) = QUERYMAXIMAFECHA(AFILIACION.NUMEROCONTRATO, AFILIACION.NUMEROFAMILIA, AFILIACION.NUMERO)";
        Query query = this.getEntityManager().createNativeQuery(sqlString);
        query.setParameter("numContrato", numeroContrato);
        query.setParameter("codFamilia", numeroFamilia);
        query.setParameter("numAfiliacion", numeroAfiliado);
        Object result1 = query.getSingleResult();
        // no se necesita parsear de null a 0 ya que si no hay registros devuelve 0
        // decide = (BigDecimal) (result1 == null ? BigDecimal.ZERO : result1);
        return (BigDecimal) result1;
    }

    public List<PreexistenciaDTO> prexistenciasConMonto(Integer numeroContrato, Integer numeroFamilia, Integer numeroAfiliado) {
        String sqlString = "SELECT DISTINCT DIAGNOSTICO.CODIGO, ITEMMEDICO.NOMBRE, "
                // 2FECHAINICIO
                + "TO_CHAR(CASE WHEN PREEXISTENCIA.FECHARIGE IS NULL THEN PREEXISTENCIA.FECHADECLARACION ELSE PREEXISTENCIA.FECHARIGE END,'dd/MM/yyyy') AS FECHAINICIO, " //
                // 3ESTADO
                + "decode(DIAGNOSTICO.ESTADO,'1','ACTIVO', DIAGNOSTICO.ESTADO) ESTADO "
                // 4CUBRE
                // + "fn_coberturaap(MONTOPREEXISTENCIA.PERIODOCARENCIA,MONTOPREEXISTENCIA.UNIDADPERIODOCARENCIA, "
                // + "               MONTOPREEXISTENCIA.TIPOAFILIADO_ID,PREEXISTENCIA.FECHAREGISTRO) CUBRE "
                + "FROM " + "CONTRATO "
                + "INNER JOIN AFILIACION ON CONTRATO.NUMERO = AFILIACION.NUMEROCONTRATO "
                + "INNER JOIN PLANCONTRATO ON PLANCONTRATO.CONTRATO_ID = CONTRATO.ID "
                + "INNER JOIN PLANESPECIFICO ON PLANCONTRATO.PLANESPECIFICO_ID = PLANESPECIFICO.ID "
                + "INNER JOIN MONTOPREEXISTENCIA ON MONTOPREEXISTENCIA.PLAN_ID = PLANESPECIFICO.ID "
                + "INNER JOIN AFILIADO ON AFILIACION.AFILIADO_ID = AFILIADO.ID " + "INNER JOIN PREEXISTENCIA ON PREEXISTENCIA.AFILIADO_ID = AFILIADO.ID "
                + "INNER JOIN DIAGNOSTICO ON PREEXISTENCIA.DIAGNOSTICO_ID = DIAGNOSTICO.ID " + "INNER JOIN ITEMMEDICO ON DIAGNOSTICO.ID = ITEMMEDICO.ID "
                + "WHERE " + "AFILIACION.NUMEROCONTRATO = :numContrato AND  " + "AFILIACION.NUMEROFAMILIA = :codFamilia AND "
                + "AFILIACION.NUMERO = :numAfiliacion AND "
                + "TRUNC(PLANCONTRATO.FECHAVIGENCIA) = QUERYMAXIMAFECHA(AFILIACION.NUMEROCONTRATO, AFILIACION.NUMEROFAMILIA, AFILIACION.NUMERO) ";
        Query query = this.getEntityManager().createNativeQuery(sqlString);
        query.setParameter("numContrato", numeroContrato);
        query.setParameter("codFamilia", numeroFamilia);
        query.setParameter("numAfiliacion", numeroAfiliado);
        List<Object[]> results = query.getResultList();
        if(results.size() == 0) {
            return null;
        }
        return setPreexistenciasDTOS(results);
    }

    public List<PreexistenciaDTO> prexistenciasConMonto(Long afiliacionId) {
        String sqlString = "SELECT DISTINCT DIAGNOSTICO.CODIGO, ITEMMEDICO.NOMBRE, "
                // 2FECHAINICIO
                + "TO_CHAR(CASE WHEN PREEXISTENCIA.FECHARIGE IS NULL THEN PREEXISTENCIA.FECHADECLARACION ELSE PREEXISTENCIA.FECHARIGE END,'dd/MM/yyyy') AS FECHAINICIO, " //
                // 3ESTADO
                + "decode(DIAGNOSTICO.ESTADO,'1','ACTIVO', DIAGNOSTICO.ESTADO) ESTADO "
                // 4CUBRE
                // + "fn_coberturaap(MONTOPREEXISTENCIA.PERIODOCARENCIA,MONTOPREEXISTENCIA.UNIDADPERIODOCARENCIA, "
                // + "               MONTOPREEXISTENCIA.TIPOAFILIADO_ID,PREEXISTENCIA.FECHAREGISTRO) CUBRE "
                + "FROM " + "CONTRATO "
                + "INNER JOIN AFILIACION ON CONTRATO.NUMERO = AFILIACION.NUMEROCONTRATO "
                + "INNER JOIN PLANCONTRATO ON PLANCONTRATO.CONTRATO_ID = CONTRATO.ID "
                + "INNER JOIN PLANESPECIFICO ON PLANCONTRATO.PLANESPECIFICO_ID = PLANESPECIFICO.ID "
                + "INNER JOIN MONTOPREEXISTENCIA ON MONTOPREEXISTENCIA.PLAN_ID = PLANESPECIFICO.ID "
                + "INNER JOIN AFILIADO ON AFILIACION.AFILIADO_ID = AFILIADO.ID " + "INNER JOIN PREEXISTENCIA ON PREEXISTENCIA.AFILIADO_ID = AFILIADO.ID "
                + "INNER JOIN DIAGNOSTICO ON PREEXISTENCIA.DIAGNOSTICO_ID = DIAGNOSTICO.ID " + "INNER JOIN ITEMMEDICO ON DIAGNOSTICO.ID = ITEMMEDICO.ID "
                + "WHERE " + "AFILIACION.ID = :afiliacionId AND  "
                + "TRUNC(PLANCONTRATO.FECHAVIGENCIA) = QUERYMAXIMAFECHA(AFILIACION.NUMEROCONTRATO, AFILIACION.NUMEROFAMILIA, AFILIACION.NUMERO) ";
        Query query = this.getEntityManager().createNativeQuery(sqlString);
        query.setParameter("afiliacionId", afiliacionId);
        List<Object[]> results = query.getResultList();
        if(results.size() == 0) {
            return null;
        }
        return setPreexistenciasDTOS(results);
    }

    public List<PreexistenciaDTO> prexistenciasSinMonto(Integer numeroContrato, Integer numeroFamilia, Integer numeroAfiliado) {
        List<PreexistenciaDTO> preexistenciaDTOS = new ArrayList<>();
        String sqlString = "SELECT distinct DIAGNOSTICO.CODIGO, ITEMMEDICO.NOMBRE, "
                // 2FECHAINICIO
                + "TO_CHAR(CASE WHEN PREEXISTENCIA.FECHARIGE IS NULL THEN PREEXISTENCIA.FECHADECLARACION ELSE PREEXISTENCIA.FECHARIGE END,'dd/MM/yyyy') AS FECHAINICIO, "
                + "decode(DIAGNOSTICO.ESTADO,'1','ACTIVO', DIAGNOSTICO.ESTADO) ESTADO "
                // + "'N' CUBRE "
                + "FROM " + "CONTRATO "
                + "INNER JOIN AFILIACION ON CONTRATO.NUMERO = AFILIACION.NUMEROCONTRATO "
                + "INNER JOIN PLANCONTRATO ON PLANCONTRATO.CONTRATO_ID = CONTRATO.ID "
                + "INNER JOIN PLANESPECIFICO ON PLANCONTRATO.PLANESPECIFICO_ID = PLANESPECIFICO.ID "
                + "INNER JOIN AFILIADO ON AFILIACION.AFILIADO_ID = AFILIADO.ID "
                + "INNER JOIN PREEXISTENCIA ON PREEXISTENCIA.AFILIADO_ID = AFILIADO.ID "
                + "INNER JOIN DIAGNOSTICO ON PREEXISTENCIA.DIAGNOSTICO_ID = DIAGNOSTICO.ID "
                + "INNER JOIN ITEMMEDICO ON DIAGNOSTICO.ID = ITEMMEDICO.ID "
                + "WHERE " + "AFILIACION.NUMEROCONTRATO = :numContrato AND " + "AFILIACION.NUMEROFAMILIA = :codFamilia AND "
                + "AFILIACION.NUMERO = :numAfiliacion AND "
                + "TRUNC(PLANCONTRATO.FECHAVIGENCIA) = QUERYMAXIMAFECHA(AFILIACION.NUMEROCONTRATO, AFILIACION.NUMEROFAMILIA, AFILIACION.NUMERO) ";
        Query query = this.getEntityManager().createNativeQuery(sqlString);
        query.setParameter("numContrato", numeroContrato);
        query.setParameter("codFamilia", numeroFamilia);
        query.setParameter("numAfiliacion", numeroAfiliado);
        List<Object[]> results = query.getResultList();
        if(results.size() == 0) {
            return null;
        }
        return setPreexistenciasDTOS(results);
    }

    public List<PreexistenciaDTO> prexistenciasSinMonto(Long afiliacionId) {
        List<PreexistenciaDTO> preexistenciaDTOS = new ArrayList<>();
        String sqlString = "SELECT distinct DIAGNOSTICO.CODIGO, ITEMMEDICO.NOMBRE, "
                // 2FECHAINICIO
                + "TO_CHAR(CASE WHEN PREEXISTENCIA.FECHARIGE IS NULL THEN PREEXISTENCIA.FECHADECLARACION ELSE PREEXISTENCIA.FECHARIGE END,'dd/MM/yyyy') AS FECHAINICIO, "
                + "decode(DIAGNOSTICO.ESTADO,'1','ACTIVO', DIAGNOSTICO.ESTADO) ESTADO "
                // + "'N' CUBRE "
                + "FROM " + "CONTRATO "
                + "INNER JOIN AFILIACION ON CONTRATO.NUMERO = AFILIACION.NUMEROCONTRATO "
                + "INNER JOIN PLANCONTRATO ON PLANCONTRATO.CONTRATO_ID = CONTRATO.ID "
                + "INNER JOIN PLANESPECIFICO ON PLANCONTRATO.PLANESPECIFICO_ID = PLANESPECIFICO.ID "
                + "INNER JOIN AFILIADO ON AFILIACION.AFILIADO_ID = AFILIADO.ID "
                + "INNER JOIN PREEXISTENCIA ON PREEXISTENCIA.AFILIADO_ID = AFILIADO.ID "
                + "INNER JOIN DIAGNOSTICO ON PREEXISTENCIA.DIAGNOSTICO_ID = DIAGNOSTICO.ID "
                + "INNER JOIN ITEMMEDICO ON DIAGNOSTICO.ID = ITEMMEDICO.ID "
                + "WHERE AFILIACION.ID = :afiliacionId AND "
                + "TRUNC(PLANCONTRATO.FECHAVIGENCIA) = QUERYMAXIMAFECHA(AFILIACION.NUMEROCONTRATO, AFILIACION.NUMEROFAMILIA, AFILIACION.NUMERO) ";
        Query query = this.getEntityManager().createNativeQuery(sqlString);
        query.setParameter("afiliacionId", afiliacionId);
        List<Object[]> results = query.getResultList();
        if(results.size() == 0) {
            return null;
        }
        return setPreexistenciasDTOS(results);
    }

    private List<PreexistenciaDTO> setPreexistenciasDTOS(List<Object[]> results) {
        List<PreexistenciaDTO> preexistenciaDTOS = new ArrayList<>();
        for (Object[] preexistenciaResult : results) {
            PreexistenciaDTO preexistenciaDTO = new PreexistenciaDTO();
            // 0DIAGNOSTICO.CODIGO, setCie10 CodigoDiagnostico
            preexistenciaDTO.setCie10(preexistenciaResult[0].toString().trim());
            // 1ITEMMEDICO.NOMBRE,
            preexistenciaDTO.setNombre(preexistenciaResult[1].toString().trim());
            // 2FECHAINICIO
            preexistenciaDTO.setFechaInicio(preexistenciaResult[2].toString().trim());
            // 3ESTADO
            preexistenciaDTO.setCodigo(preexistenciaResult[3].toString().trim());
            // 4CUBRE
            // preexistenciaDTO.setCubre(preexistenciaResult[4].toString().trim());
            preexistenciaDTOS.add(preexistenciaDTO);
        }
        return preexistenciaDTOS;
    }

    @Override
    /* en base al metodo queryPreexistencia1(  */
    public List<PreexistenciaDTO> prexistenciasPorAfiliado(Long afiliacionId) {
        List<PreexistenciaDTO> preexistenciaDTOS = new ArrayList<>();
        List<Object[]> results = new ArrayList<>();
        BigDecimal numeroPreexistenciasConMonto = this.numeroPreexistenciasConMonto(afiliacionId);
        /*si 0 es menor a numeroContratosActivosTitular = -1 */
        if (BigDecimal.ZERO.compareTo(numeroPreexistenciasConMonto) < 0) {
            preexistenciaDTOS = this.prexistenciasConMonto(afiliacionId);
        } else {
            preexistenciaDTOS = this.prexistenciasSinMonto(afiliacionId);
        }
        return preexistenciaDTOS;
    }
}
