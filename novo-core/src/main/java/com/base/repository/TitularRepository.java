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
 * TitularRepository.
 *
 * @author vsangucho on 16/3/2023.
 * @version 1.0
 */
@Lazy
@Repository
public class TitularRepository extends JPAQueryDslBaseRepository<PersonEntity> implements
        ITitularRepository {

    /**
     * Constructor.
     */
    public TitularRepository() {
        super(PersonEntity.class);
    }

    @Override
    public TitularDTO titularPorCedula(String numeroDocumento) {
        TitularDTO titular = new TitularDTO();
        // datos
        List<ContratoTitularDTO> contratoTitularDTOS = new ArrayList<>();
        BigDecimal numeroContratosActivosTitular = this.numeroContratosActivosTitular(numeroDocumento);
        /*si 0 es menor a numeroContratosActivosTitular = -1 */
        if (BigDecimal.ZERO.compareTo(numeroContratosActivosTitular) < 0) {
            titular = this.datosTitularConContratoActivo(numeroDocumento);
        } else {
            titular = this.datosTitularContratoActivoConElTitular(numeroDocumento);
        }
        return titular;
    }

    private BigDecimal numeroContratosActivosTitular(String numeroDocumento) {
        String sqlString = "SELECT count(1) contador"
                + " FROM AFILIACION A, FAMILIA F,  ENTITY E, CONTRATO C, TIPOAFILIADO TA"
                + " WHERE A.NUMEROFAMILIA = F.NUMERO"
                + " AND F.TITULAR_ID = E.ID"
                + " AND A.NUMEROCONTRATO = C.NUMERO"
                + " AND F.CONTRATO_ID = C.ID"
                + " AND A.TIPOAFILIADO_ID = TA.ID"
                + " AND E.PIN = :cedulaTitular"
                + " AND C.ESTADO = 'A'" //
                + " AND A.ESTADO = 'ACTIVO'" //
                + " AND F.TITULAR_ID IN (SELECT DISTINCT F1.TITULAR_ID" //
                + "                       FROM AFILIACION A1, FAMILIA F1,  ENTITY E1, CONTRATO C1, TIPOAFILIADO TA1" //
                + "                       WHERE A1.NUMEROFAMILIA = F1.NUMERO" //
                + "                       AND F1.TITULAR_ID = E1.ID" //
                + "                       AND A1.NUMEROCONTRATO = C1.NUMERO" //
                + "                       AND F1.CONTRATO_ID = C1.ID" //
                + "                       AND A1.TIPOAFILIADO_ID = TA1.ID" //
                + "                       AND E1.PIN = :cedulaTitular" //
                + "                       AND C1.ESTADO = 'A'" //
                + "                       AND A1.ESTADO = 'ACTIVO'" //
                + "                       AND TA1.CODIGO = 'CT')";
        Query query = this.getEntityManager().createNativeQuery(sqlString);
        query.setParameter("cedulaTitular", numeroDocumento);
        Object result1 = query.getSingleResult();
        // no se necesita parsear de null a 0 ya que si no hay registros devuelve 0
        // decide = (BigDecimal) (result1 == null ? BigDecimal.ZERO : result1);
        return (BigDecimal) result1;
    }

    /* queryTitular en base a queryAfiliacionTitular */
    private TitularDTO datosTitularConContratoActivo(String cedulaTitular) {
        TitularDTO titular = new TitularDTO();
        String sqlString = "SELECT DISTINCT E.PIN, PERSON.FIRSTNAME, PERSON.MIDDLENAME, PERSON.LASTNAME," //
                // 5E.DOCUMENTTYPE 6BIRTHDATE 7PERSON.GENDER 8E.NAME
                + " PERSON.SECONDLASTNAME, E.DOCUMENTTYPE, TO_CHAR(PERSON.BIRTHDATE,'dd/MM/yyyy') AS BIRTHDATE, " //
                + "	PERSON.GENDER, E.NAME,"
                // 9E.ID o F.TITULAR_ID  antes A.ID AS AFILIACIONID A.NUMERO AS NUMEROAFILIACION
                + "	E.ID AS TITULARID"
                + " FROM AFILIACION A, FAMILIA F,  ENTITY E, CONTRATO C, TIPOAFILIADO TA, PERSON" //
                + " WHERE A.NUMEROFAMILIA = F.NUMERO" //
                + " AND F.TITULAR_ID = E.ID" //
                + " AND A.NUMEROCONTRATO = C.NUMERO" //
                + " AND F.CONTRATO_ID = C.ID" //
                + " AND A.TIPOAFILIADO_ID = TA.ID"
                + " AND E.ID = PERSON.ID" //
                + " AND E.PIN = :cedulaTitular" //
                + " AND C.ESTADO = 'A'" //
                + " AND A.ESTADO = 'ACTIVO'" //
                + " AND F.TITULAR_ID IN (SELECT DISTINCT F1.TITULAR_ID" //
                + "                       FROM AFILIACION A1, FAMILIA F1,  ENTITY E1, CONTRATO C1, TIPOAFILIADO TA1" //
                + "                       WHERE A1.NUMEROFAMILIA = F1.NUMERO" //
                + "                       AND F1.TITULAR_ID = E1.ID" //
                + "                       AND A1.NUMEROCONTRATO = C1.NUMERO" //
                + "                       AND F1.CONTRATO_ID = C1.ID" //
                + "                       AND A1.TIPOAFILIADO_ID = TA1.ID" //
                + "                       AND E1.PIN = :cedulaTitular" //
                + "                       AND C1.ESTADO = 'A'" //
                + "                       AND A1.ESTADO = 'ACTIVO'" //
                + "                       AND TA1.CODIGO = 'CT')";
        Query query = this.getEntityManager().createNativeQuery(sqlString);
        query.setParameter("cedulaTitular", cedulaTitular);
        List<Object[]> results = query.getResultList();
        if(results.size() == 0) {
            return null;
        }
        titular = this.setTitularDTO(results);
        return titular;
    }

    /* queryTitular en base a queryAfiliacionTitular */
    private TitularDTO datosTitularContratoActivoConElTitular(String cedulaTitular) {
        TitularDTO titular = new TitularDTO();
        String sqlString = "SELECT DISTINCT E.PIN, PERSON.FIRSTNAME, PERSON.MIDDLENAME, PERSON.LASTNAME,"
                + " PERSON.SECONDLASTNAME, E.DOCUMENTTYPE, TO_CHAR(PERSON.BIRTHDATE,'dd/MM/yyyy') AS BIRTHDATE, "
                + "	PERSON.GENDER, E.NAME,"
                // 9E.ID o F.TITULAR_ID  antes A.ID AS AFILIACIONID A.NUMERO AS NUMEROAFILIACION
                + "	E.ID AS TITULARID"
                + " FROM AFILIACION A, CONTRATO C,  FAMILIA F,  ENTITY E, TIPOAFILIADO TA, PERSON"
                + " WHERE A.NUMEROFAMILIA = F.NUMERO "
                + " AND F.TITULAR_ID = E.ID "
                + " AND A.NUMEROCONTRATO = C.NUMERO "
                + " AND F.CONTRATO_ID = C.ID "
                + " AND A.TIPOAFILIADO_ID = TA.ID "
                + " AND E.ID = PERSON.ID"
                + " AND E.PIN = :cedulaTitular "
                + " AND C.ESTADO = 'A' "
                + " AND A.ESTADO = 'ACTIVO'";
        Query query = this.getEntityManager().createNativeQuery(sqlString);
        query.setParameter("cedulaTitular", cedulaTitular);
        List<Object[]> results = query.getResultList();
        if(results.size() == 0) {
            return null;
        }
        titular = this.setTitularDTO(results);
        return titular;
    }

    private TitularDTO setTitularDTO(List<Object[]> results) {
        TitularDTO titularOBJ = new TitularDTO();
        // 1PERSON.FIRSTNAME
        String priNombreTitular = results.get(0)[1].toString().trim() == null ? "" : results.get(0)[1].toString().trim();
        // 2PERSON.MIDDLENAME
        String segNombreTitular = results.get(0)[2].toString().trim() == null ? "" : results.get(0)[2].toString().trim();
        // 3PERSON.LASTNAME
        String priApelliTitular = results.get(0)[3].toString().trim() == null ? "" : results.get(0)[3].toString().trim();
        // 4PERSON.SECONDLASTNAME
        String segApelliTitular = results.get(0)[4].toString().trim() == null ? "" : results.get(0)[4].toString().trim();
        // 1 5E.DOCUMENTTYPE
        titularOBJ.setTipoDocumento(results.get(0)[5].toString().trim());
        // 0E.PIN NumeroDocumento
        titularOBJ.setIdentificacion(results.get(0)[0].toString().trim());
        titularOBJ.setNombres(priNombreTitular + ("".equals(segNombreTitular) ? "" : (" " + segNombreTitular)));
        titularOBJ.setApellidos(priApelliTitular + ("".equals(segApelliTitular) ? "" : (" " + segApelliTitular)));
        // 6PERSON.BIRTHDATE
        titularOBJ.setFechaNacimiento(results.get(0)[6].toString().trim());
        // 7PERSON.GENDER
        titularOBJ.setGenero(results.get(0)[7].toString().trim());
        // 8ENTITY.NAME
        titularOBJ.setNombreTitular(results.get(0)[8].toString().trim());
        // 9 Titular Numero antes A.NUMERO AS NUMEROAFILIACION
        // 9 Titular Numero antes A.NUMERO AS NUMEROAFILIACION
        // 9E.ID = F.TITULAR_ID  antes A.ID AS AFILIACIONID A.NUMERO AS NUMEROAFILIACION antes A.ID AS AFILIACIONID
        // no puede ir afilaicon A.ID por que el titular puede ser que no sea beneficiario solo sus familiares
        titularOBJ.setEntityId(Long.parseLong(results.get(0)[9].toString().trim()));
        return titularOBJ;
    }
}
