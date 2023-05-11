package com.base.repository;

import com.base.common.JPAQueryDslBaseRepository;
import com.base.entity.PersonEntity;
import com.base.vo.ContratoTitularDTO;
import com.base.vo.TitularDTO;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * ContratoRepository.
 *
 * @author vsangucho on 16/3/2023.
 * @version 1.0
 */
@Lazy
@Repository
public class ContratoRepository extends JPAQueryDslBaseRepository<PersonEntity> implements
        IContratoRepository {

    /**
     * Constructor.
     */
    public ContratoRepository() {
        super(PersonEntity.class);
    }

    /**
     *
     * @param titularDTO
     * @return
     * en base a queryFamiliares y queryAfiliacionTitular
     */
    @Override
    public List<ContratoTitularDTO> contratosPorTitular(TitularDTO titularDTO) {
        List<ContratoTitularDTO> contratoTitularDTOS = new ArrayList<>();
        String sqlString = "SELECT DISTINCT"
                // 0region
                + " (CASE WHEN CONTRATO.SUCURSAL_ID = 1 THEN 'SIERRA'	" // Quito
                + "		WHEN CONTRATO.SUCURSAL_ID = 2 THEN 'COSTA'	" // Guayaquil
                + "		WHEN CONTRATO.SUCURSAL_ID = 3 THEN 'SIERRA'	" // Riobamba
                + "		WHEN CONTRATO.SUCURSAL_ID = 4 THEN 'COSTA'	" // Manta
                + "		WHEN CONTRATO.SUCURSAL_ID = 5 THEN 'COSTA'	" // Portoviejo
                + "		WHEN CONTRATO.SUCURSAL_ID = 6 THEN 'SIERRA'	" // Cuenca
                + "		WHEN CONTRATO.SUCURSAL_ID = 7 THEN 'SIERRA'	" // Ambato
                + "		WHEN CONTRATO.SUCURSAL_ID = 8 THEN 'COSTA'	" // Sto.Domingo
                + "		WHEN CONTRATO.SUCURSAL_ID = 9 THEN 'COSTA'	" // Esmeraldas
                + "		WHEN CONTRATO.SUCURSAL_ID = 10 THEN 'SIERRA' " // Ibarra
                + "		WHEN CONTRATO.SUCURSAL_ID = 11 THEN 'SIERRA' " // Loja
                + "		WHEN CONTRATO.SUCURSAL_ID = 3300825 THEN 'COSTA' " // Machala
                + "		ELSE '' END) AS REGION,"
                // 1CONTRATO.PRODUCTO, 2AFILIACION.NUMEROCONTRATO,
                + "		CONTRATO.PRODUCTO, AFILIACION.NUMEROCONTRATO,"
                // 3PLANSALUD.CODIGOPLAN, 4CONTRATO.ESTADO,  5 PLANSALUD.NOMBRE AS PLANMEDICO NombrePlan
                + "		PLANSALUD.CODIGOPLAN, CONTRATO.ESTADO, PLANSALUD.NOMBRE AS PLANMEDICO,"
                // 6PLANESPECIFICO.MONTOCONTRATADO
                + "		PLANESPECIFICO.MONTOCONTRATADO,"
                // 7CONTRATO.FECHAINICIO
                + "		TO_CHAR(CONTRATO.FECHAINICIO,'dd/MM/yyyy') AS FECHAINICIOCONTRATO,"
                // 8CONTRATO.FECHAVENCIMIENTO
                + "		TO_CHAR(CONTRATO.FECHAVENCIMIENTO,'dd/MM/yyyy') AS FECHAVENCIMIENTO,"
                // 9BRANCH.NAME AS NOMBRESUCURSALCONTRATO
                + "		BRANCH.NAME AS NOMBRESUCURSALCONTRATO,"
                // 10BRANCH.CODIGO AS CODIGOSUCURSALCONTRATO
                + "		BRANCH.CODIGO AS CODIGOSUCURSALCONTRATO,"
                // 11CONTRATO.TIPO AS CONTRATOTIPO NombreLista
                + "		CONTRATO.TIPO AS CONTRATOTIPO"
                + " FROM CONTRATO"
                + " INNER JOIN PLANCONTRATO ON PLANCONTRATO.CONTRATO_ID = CONTRATO.ID"
                + " INNER JOIN PLANSALUD ON PLANCONTRATO.PLANESPECIFICO_ID = PLANSALUD.ID"
                + " INNER JOIN PLANESPECIFICO ON PLANCONTRATO.PLANESPECIFICO_ID = PLANESPECIFICO.ID"
                + " INNER JOIN AFILIACION ON CONTRATO.NUMERO = AFILIACION.NUMEROCONTRATO"
                + " INNER JOIN AFILIADO ON AFILIACION.AFILIADO_ID = AFILIADO.ID"
                + " INNER JOIN ENTITYROLE ON AFILIADO.ID = ENTITYROLE.ID"
                + " INNER JOIN ENTITY ON ENTITY.ID = ENTITYROLE.ENTITY_ID"
                + " INNER JOIN PERSON ON PERSON.ID = ENTITY.ID"
                + " INNER JOIN TIPOAFILIADO ON AFILIACION.TIPOAFILIADO_ID = TIPOAFILIADO.ID"
                + " INNER JOIN BRANCH ON CONTRATO.SUCURSAL_ID = BRANCH.ID"
                + " INNER JOIN (SELECT A.NUMEROCONTRATO, A.NUMEROFAMILIA, A.NUMERO"
                + " 			FROM AFILIACION A, FAMILIA F,  ENTITY E, CONTRATO C, TIPOAFILIADO TA"
                + " 			WHERE A.NUMEROFAMILIA = F.NUMERO"
                + " 			AND F.TITULAR_ID = E.ID"
                + " 			AND A.NUMEROCONTRATO = C.NUMERO"
                + " 			AND F.CONTRATO_ID = C.ID"
                + " 			AND A.TIPOAFILIADO_ID = TA.ID"
                + " 			AND E.PIN = :valCedula"
                + " 			AND C.ESTADO = 'A'"
                + " 			AND A.ESTADO = 'ACTIVO'"
                + " 			AND F.TITULAR_ID IN (SELECT DISTINCT F1.TITULAR_ID"
                + "                       FROM AFILIACION A1, FAMILIA F1,  ENTITY E1, CONTRATO C1, TIPOAFILIADO TA1"
                + "                       WHERE A1.NUMEROFAMILIA = F1.NUMERO"
                + "                       AND F1.TITULAR_ID = E1.ID"
                + "                       AND A1.NUMEROCONTRATO = C1.NUMERO"
                + "                       AND F1.CONTRATO_ID = C1.ID"
                + "                       AND A1.TIPOAFILIADO_ID = TA1.ID"
                + "                       AND E1.PIN = :valCedula"
                + "                       AND C1.ESTADO = 'A'"
                + "                       AND A1.ESTADO = 'ACTIVO')) x"
                // se quita para los casos en los el titular tiene contratos solo para sus familiares
                // el titular no tiene contrato para si mismo solo para sus familiares
                // + "                       AND TA1.CODIGO = 'CT')) x"
                + " ON (x.NUMEROCONTRATO = AFILIACION.NUMEROCONTRATO AND x.NUMEROFAMILIA = AFILIACION.NUMEROFAMILIA AND x.NUMERO = AFILIACION.NUMERO)"
                + " WHERE"
                // + " AFILIACION.NUMEROCONTRATO = :numContrato AND"
                // + " AFILIACION.NUMEROFAMILIA = :codFamilia AND"
                // + " AFILIACION.NUMERO = :numAfiliacion AND"
                + " CONTRATO.ESTADO = 'A' AND AFILIACION.ESTADO = 'ACTIVO' AND"
                + " PLANCONTRATO.FECHAVIGENCIA = QUERYMAXIMAFECHA(AFILIACION.NUMEROCONTRATO, AFILIACION.NUMEROFAMILIA, AFILIACION.NUMERO)";
        Query query =  this.getEntityManager().createNativeQuery(sqlString);
        query.setParameter("valCedula", titularDTO.getIdentificacion());
        List<Object[]> results = query.getResultList();
        if(results.size() == 0) {
            return null;
        }
        contratoTitularDTOS = this.setContratosPorTitular(query.getResultList(), titularDTO);
        return contratoTitularDTOS;
    }

    @Override
    public String validaContrato(Integer numContrato, Integer numFamilia, Integer numAfiliado) {
        String vServicio;
        String sqlString = "SELECT FN_VALIDACONTRATO(:numContrato,:numFamilia,:numAfiliado) TAREA FROM DUAL";
        Query query =  this.getEntityManager().createNativeQuery(sqlString);
        query.setParameter("numContrato", numContrato);
        query.setParameter("numFamilia", numFamilia);
        query.setParameter("numAfiliado", numAfiliado);
        Object result = query.getSingleResult();
        String validaContrato = (String) result;
        return "prueba";
    }

    private List<ContratoTitularDTO> setContratosPorTitular(List<Object[]> results, TitularDTO titular) {
        List<ContratoTitularDTO> contratoTitularDTOS = new ArrayList<>();
        for (Object[] contratoTitular : results) {
            ContratoTitularDTO contratoTitularDTO = new ContratoTitularDTO();
            // 0CONTRATO.SUCURSAL_ID
            contratoTitularDTO.setRegion(contratoTitular[0].toString().trim());
            // 1CONTRATO.PRODUCTO
            contratoTitularDTO.setProducto(contratoTitular[1].toString().trim());
            // 2AFILIACION.NUMEROCONTRATO
            contratoTitularDTO.setNumeroContrato(Integer.parseInt(contratoTitular[2].toString().trim()));
            // 3PLANSALUD.CODIGOPLAN,
            contratoTitularDTO.setCodigoPlan(Integer.parseInt(contratoTitular[3].toString().trim()));
            // 2AFILIACION.NUMEROCONTRATO
            contratoTitularDTO.setCodigo(Integer.parseInt(contratoTitular[2].toString().trim()));
            // 4CONTRATO.ESTADO,
            contratoTitularDTO.setEstadoContrato("A".equals(contratoTitular[4].toString().trim()) ? "ACTIVO" : contratoTitular[4].toString().trim());
            // 4CONTRATO.ESTADO,
            contratoTitularDTO.setCodigoEstado("A".equals(contratoTitular[4].toString().trim()) ? 1 : 0);
            // 5PLANSALUD.NOMBRE AS PLANMEDICO NombrePlan
            contratoTitularDTO.setNombrePlanSalud(contratoTitular[5].toString().trim());
            contratoTitularDTO.setVersion(0);
            // 6PLANESPECIFICO.MONTOCONTRATADO CoberturaMaxima
            contratoTitularDTO.setMontoContratado(new BigDecimal(contratoTitular[6].toString().trim()));
            contratoTitularDTO.setNivel(0);
            contratoTitularDTO.setDeducibleTotal(new BigDecimal(0));
            contratoTitularDTO.setEsMoroso(false);
            // 5PLANSALUD.NOMBRE AS PLANMEDICO NombrePlan
            contratoTitularDTO.setNombreComercialPlan(contratoTitular[5].toString().trim());
            // 7CONTRATO.FECHAINICIO
            contratoTitularDTO.setFechaInicioContrato(contratoTitular[7].toString().trim());
            // 8CONTRATO.FECHAVENCIMIENTO FechaVigencia
            contratoTitularDTO.setFechaVencimiento(contratoTitular[8].toString().trim());
            contratoTitularDTO.setCodigoPmf(null);
            contratoTitularDTO.setEmpresa("ecuasanitas");
            contratoTitularDTO.setNumeroEmpresa(0);
            // 9BRANCH.NAME AS NOMBRESUCURSALCONTRATO
            contratoTitularDTO.setNombreSucursalContrato(contratoTitular[9].toString().trim());
            // 10BRANCH.CODIGO AS CODIGOSUCURSALCONTRATO CodigoSucursal
            contratoTitularDTO.setCodigoSucursalContrato(contratoTitular[10].toString().trim());
            // 11CONTRATO.TIPO AS CONTRATOTIPO NombreLista
            contratoTitularDTO.setContratoTipo(contratoTitular[11].toString().trim());

            contratoTitularDTO.setNumeroLista(0);
            contratoTitularDTO.setRuc("RUC".equals(titular.getTipoDocumento()) ?  titular.getIdentificacion() : "");
            contratoTitularDTO.setRazonSocial("RUC".equals(titular.getTipoDocumento()) ?  titular.getNombreTitular() : "");
            contratoTitularDTO.setEsSmartPlan(false);
            contratoTitularDTO.setObservaciones("");
            contratoTitularDTO.setDeducibles(new ArrayList<>());
            contratoTitularDTOS.add(contratoTitularDTO);
        }
        return contratoTitularDTOS;
    }
}
