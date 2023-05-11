package com.base.repository;

import com.base.common.JPAQueryDslBaseRepository;
import com.base.entity.PersonEntity;
import com.base.vo.PersonVo;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.base.entity.QPersonEntity.personEntity;

/**
 * PersonRepository.
 *
 * @author vsangucho on 13/3/2023.
 * @version 1.0
 */
@Lazy
@Repository
public class PersonRepository extends JPAQueryDslBaseRepository<PersonEntity> implements
    IPersonRepository {

    /**
     * Constructor.
     */
    public PersonRepository() {
        super(PersonEntity.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean exist(String documentNumber) {
        JPQLQuery<String> query = from(personEntity)
            .select(personEntity.personId)
            .where(personEntity.documentNumber.eq(documentNumber));
        String response = query.fetchFirst();
        return StringUtils.isNotBlank(response);
    }

    /**
     * {@inheritDoc}
     *
     */
    @Override
    public List<PersonVo> list() {
        Query query_ =  this.getEntityManager().createNativeQuery("SELECT E.PIN, E.DOCUMENTTYPE FROM ENTITY E WHERE E.PIN = '1718915349'");
        Object result1 = query_.getSingleResult();
        BigDecimal numeroContratosActivosTitular = this.numeroContratosActivosTitular("1710003755");
        /*si 0 es menor a numeroContratosActivosTitular = -1 */
        if (BigDecimal.ZERO.compareTo(numeroContratosActivosTitular) < 0) {
            this.datosTitularConContratoActivo("1710003755");
        } else {
            this.datosTitularContratoActivoConElTitular("1710003755");
        }
        /*una vez consultado los datos del titular consultamos los contratos del titular */
        /* String cedulaTitular, String tipoDocumento, String identificacion, String titularNombre*/
        this.contratosByTitular("1710003755", "CEDULA", "1710003755", "GUAMAN CASA FAUSTO ENRIQUE");
        /*luego se consulta los beneficiarios por contrato 280561 GUAMAN CAZA LAURA SOLEDAD*/
        this.beneficiariosByTitularContrato("1710003755",280561);
        /* preexistencias de los beneficiarios numeroContrato 542720 GUAMAN CASA FAUSTO ENRIQUE numeroFamilia 133 numeroAfiliado 1*/
        this.preexistenciasByBeneficiario(542720,133,1);
        return null;
        /*return from(personEntity)
            .select(bean(PersonVo.class,
                personEntity.personId,
                personEntity.firstName,
                personEntity.lastName,
                personEntity.documentNumber,
                personEntity.email,
                personEntity.createDate,
                personEntity.status)).fetch();*/
    }

    private BigDecimal numeroContratosActivosTitular(String cedulaTitular) {
        String sqlString = "SELECT count(1) contador" //
                + " FROM AFILIACION A, FAMILIA F,  ENTITY E, CONTRATO C, TIPOAFILIADO TA" //
                + " WHERE A.NUMEROFAMILIA = F.NUMERO" //
                + " AND F.TITULAR_ID = E.ID" //
                + " AND A.NUMEROCONTRATO = C.NUMERO" //
                + " AND F.CONTRATO_ID = C.ID" //
                + " AND A.TIPOAFILIADO_ID = TA.ID" //
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
        Query query_ =  this.getEntityManager().createNativeQuery(sqlString);
        query_.setParameter("cedulaTitular", cedulaTitular);
        Object result1 = query_.getSingleResult();
        return (BigDecimal) result1;
    }

    /* queryTitular en base a queryAfiliacionTitular */
    private void datosTitularConContratoActivo(String cedulaTitular) {

        List<Object[]> results = new ArrayList<>();
        String sqlString = "SELECT DISTINCT E.PIN, PERSON.FIRSTNAME, PERSON.MIDDLENAME, PERSON.LASTNAME," //
                // 5E.DOCUMENTTYPE 6BIRTHDATE 7PERSON.GENDER 8E.NAME
                + " PERSON.SECONDLASTNAME, E.DOCUMENTTYPE, TO_CHAR(PERSON.BIRTHDATE,'dd/MM/yyyy') AS BIRTHDATE, " //
                + "	PERSON.GENDER, E.NAME" //
                + " FROM AFILIACION A, FAMILIA F,  ENTITY E, CONTRATO C, TIPOAFILIADO TA, PERSON" //
                + " WHERE A.NUMEROFAMILIA = F.NUMERO" //
                + " AND F.TITULAR_ID = E.ID" //
                + " AND A.NUMEROCONTRATO = C.NUMERO" //
                + " AND F.CONTRATO_ID = C.ID" //
                + " AND A.TIPOAFILIADO_ID = TA.ID" //
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
                + "                       AND TA1.CODIGO = 'CT')"; //
        Query query =  this.getEntityManager().createNativeQuery(sqlString);
        query.setParameter("cedulaTitular", cedulaTitular);
        results = query.getResultList();
    }

    /* queryTitular en base a queryAfiliacionTitular */
    private void datosTitularContratoActivoConElTitular(String cedulaTitular) {
        List<Object[]> results = new ArrayList<>();
        String sqlString = "SELECT DISTINCT E.PIN, PERSON.FIRSTNAME, PERSON.MIDDLENAME, PERSON.LASTNAME," //
                + " PERSON.SECONDLASTNAME, E.DOCUMENTTYPE, TO_CHAR(PERSON.BIRTHDATE,'dd/MM/yyyy') AS BIRTHDATE, " //
                + "	PERSON.GENDER, E.NAME" //
                + " FROM AFILIACION A, CONTRATO C,  FAMILIA F,  ENTITY E, TIPOAFILIADO TA, PERSON" //
                + " WHERE A.NUMEROFAMILIA = F.NUMERO " //
                + " AND F.TITULAR_ID = E.ID " //
                + " AND A.NUMEROCONTRATO = C.NUMERO " //
                + " AND F.CONTRATO_ID = C.ID " //
                + " AND A.TIPOAFILIADO_ID = TA.ID " //
                + " AND E.ID = PERSON.ID" //
                + " AND E.PIN = :cedulaTitular " //
                + " AND C.ESTADO = 'A' " //
                + " AND A.ESTADO = 'ACTIVO'";
        Query query =  this.getEntityManager().createNativeQuery(sqlString);
        query.setParameter("cedulaTitular", cedulaTitular);
        results = query.getResultList();
    }

    /*en base a queryFamiliares y queryAfiliacionTitular*/
    private void contratosByTitular(String cedulaTitular, String tipoDocumento, String identificacion, String titularNombre) {
        List<Object[]> results = new ArrayList<>();
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
                + "		ELSE '' END) AS REGION," //
                // 1CONTRATO.PRODUCTO, 2AFILIACION.NUMEROCONTRATO,
                + "		CONTRATO.PRODUCTO, AFILIACION.NUMEROCONTRATO," //
                // 3PLANSALUD.CODIGOPLAN, 4CONTRATO.ESTADO,  5 PLANSALUD.NOMBRE AS PLANMEDICO NombrePlan
                + "		PLANSALUD.CODIGOPLAN, CONTRATO.ESTADO, PLANSALUD.NOMBRE AS PLANMEDICO," //
                // 6PLANESPECIFICO.MONTOCONTRATADO
                + "		PLANESPECIFICO.MONTOCONTRATADO," //
                // 7CONTRATO.FECHAINICIO
                + "		TO_CHAR(CONTRATO.FECHAINICIO,'dd/MM/yyyy') AS FECHAINICIOCONTRATO," //
                // 8CONTRATO.FECHAVENCIMIENTO
                + "		TO_CHAR(CONTRATO.FECHAVENCIMIENTO,'dd/MM/yyyy') AS FECHAVENCIMIENTO," //
                // 9BRANCH.NAME AS NOMBRESUCURSALCONTRATO
                + "		BRANCH.NAME AS NOMBRESUCURSALCONTRATO," //
                // 10BRANCH.CODIGO AS CODIGOSUCURSALCONTRATO
                + "		BRANCH.CODIGO AS CODIGOSUCURSALCONTRATO" //
                + " FROM CONTRATO" //
                + " INNER JOIN PLANCONTRATO ON PLANCONTRATO.CONTRATO_ID = CONTRATO.ID" //
                + " INNER JOIN PLANSALUD ON PLANCONTRATO.PLANESPECIFICO_ID = PLANSALUD.ID" //
                + " INNER JOIN PLANESPECIFICO ON PLANCONTRATO.PLANESPECIFICO_ID = PLANESPECIFICO.ID" //
                + " INNER JOIN AFILIACION ON CONTRATO.NUMERO = AFILIACION.NUMEROCONTRATO" //
                + " INNER JOIN AFILIADO ON AFILIACION.AFILIADO_ID = AFILIADO.ID" //
                + " INNER JOIN ENTITYROLE ON AFILIADO.ID = ENTITYROLE.ID" //
                + " INNER JOIN ENTITY ON ENTITY.ID = ENTITYROLE.ENTITY_ID" //
                + " INNER JOIN PERSON ON PERSON.ID = ENTITY.ID" //
                + " INNER JOIN TIPOAFILIADO ON AFILIACION.TIPOAFILIADO_ID = TIPOAFILIADO.ID" //
                + " INNER JOIN BRANCH ON CONTRATO.SUCURSAL_ID = BRANCH.ID" //
                + " INNER JOIN (SELECT A.NUMEROCONTRATO, A.NUMEROFAMILIA, A.NUMERO" //
                + " 			FROM AFILIACION A, FAMILIA F,  ENTITY E, CONTRATO C, TIPOAFILIADO TA" //
                + " 			WHERE A.NUMEROFAMILIA = F.NUMERO" //
                + " 			AND F.TITULAR_ID = E.ID" //
                + " 			AND A.NUMEROCONTRATO = C.NUMERO" //
                + " 			AND F.CONTRATO_ID = C.ID" //
                + " 			AND A.TIPOAFILIADO_ID = TA.ID" //
                + " 			AND E.PIN = :cedulaTitular" //
                + " 			AND C.ESTADO = 'A'" //
                + " 			AND A.ESTADO = 'ACTIVO'" //
                + " 			AND F.TITULAR_ID IN (SELECT DISTINCT F1.TITULAR_ID" //
                + "                       FROM AFILIACION A1, FAMILIA F1,  ENTITY E1, CONTRATO C1, TIPOAFILIADO TA1" //
                + "                       WHERE A1.NUMEROFAMILIA = F1.NUMERO" //
                + "                       AND F1.TITULAR_ID = E1.ID" //
                + "                       AND A1.NUMEROCONTRATO = C1.NUMERO" //
                + "                       AND F1.CONTRATO_ID = C1.ID" //
                + "                       AND A1.TIPOAFILIADO_ID = TA1.ID" //
                + "                       AND E1.PIN = :cedulaTitular" //
                + "                       AND C1.ESTADO = 'A'" //
                + "                       AND A1.ESTADO = 'ACTIVO'" //
                + "                       AND TA1.CODIGO = 'CT')) x" //
                + " ON (x.NUMEROCONTRATO = AFILIACION.NUMEROCONTRATO AND x.NUMEROFAMILIA = AFILIACION.NUMEROFAMILIA AND x.NUMERO = AFILIACION.NUMERO)" //
                + " WHERE" //
                // + " AFILIACION.NUMEROCONTRATO = :numContrato AND" //
                // + " AFILIACION.NUMEROFAMILIA = :codFamilia AND" //
                // + " AFILIACION.NUMERO = :numAfiliacion AND" //
                + " CONTRATO.ESTADO = 'A' AND AFILIACION.ESTADO = 'ACTIVO' AND" //
                + " PLANCONTRATO.FECHAVIGENCIA = QUERYMAXIMAFECHA(AFILIACION.NUMEROCONTRATO, AFILIACION.NUMEROFAMILIA, AFILIACION.NUMERO)";
        Query query =  this.getEntityManager().createNativeQuery(sqlString);
        query.setParameter("cedulaTitular", cedulaTitular);
        results = query.getResultList();
    }

    /*en base a queryFamiliares y queryAfiliacionTitular*/
    private void beneficiariosByTitularContrato(String cedulaTitular, Integer numeroContrato) {
        List<Object[]> results = new ArrayList<>();
        String sqlString = "SELECT"
                // 0AFILIACION.NUMERO, 1UPPER(TIPOAFILIADO.NOMBRE) AS NOMBREPARENTESCO
                + " AFILIACION.NUMERO, UPPER(TIPOAFILIADO.NOMBRE) AS NOMBREPARENTESCO," //
                // 2PERSON.FIRSTNAME, 3PERSON.MIDDLENAME, 4PERSON.LASTNAME, 5PERSON.SECONDLASTNAME,
                + "	PERSON.FIRSTNAME, PERSON.MIDDLENAME, PERSON.LASTNAME, PERSON.SECONDLASTNAME," //
                // 6PERSON.GENDER, 7ENTITY.DOCUMENTTYPE, 8ENTITY.PIN, 9TO_CHAR(PERSON.BIRTHDATE,'dd/MM/yyyy') AS BIRTHDATE
                + "	PERSON.GENDER, ENTITY.DOCUMENTTYPE, ENTITY.PIN, TO_CHAR(PERSON.BIRTHDATE,'dd/MM/yyyy') AS BIRTHDATE," //
                // 10fn_edades(PERSON.BIRTHDATE) AS EDAD
                + "	fn_edades(PERSON.BIRTHDATE) AS EDAD," //
                // 11AFILIACION.FECHACAPTURA < AFILIACION.FECHAINGRESO THEN AFILIACION.FECHACAPTURA AS FECHAINCLUSION
                + "	TO_CHAR(CASE WHEN AFILIACION.FECHACAPTURA < AFILIACION.FECHAINGRESO THEN AFILIACION.FECHACAPTURA ELSE AFILIACION.FECHAINGRESO END,'dd/MM/yyyy') AS FECHAINCLUSION," //
                // 12AFILIACION.NUMEROFAMILIA
                + "	AFILIACION.NUMEROFAMILIA" //
                + " FROM CONTRATO" //
                + " INNER JOIN PLANCONTRATO ON PLANCONTRATO.CONTRATO_ID = CONTRATO.ID" //
                + " INNER JOIN PLANSALUD ON PLANCONTRATO.PLANESPECIFICO_ID = PLANSALUD.ID" //
                + " INNER JOIN PLANESPECIFICO ON PLANCONTRATO.PLANESPECIFICO_ID = PLANESPECIFICO.ID" //
                + " INNER JOIN AFILIACION ON CONTRATO.NUMERO = AFILIACION.NUMEROCONTRATO" //
                + " INNER JOIN AFILIADO ON AFILIACION.AFILIADO_ID = AFILIADO.ID" //
                + " INNER JOIN ENTITYROLE ON AFILIADO.ID = ENTITYROLE.ID" //
                + " INNER JOIN ENTITY ON ENTITY.ID = ENTITYROLE.ENTITY_ID" //
                + " INNER JOIN PERSON ON PERSON.ID = ENTITY.ID" //
                + " INNER JOIN TIPOAFILIADO ON AFILIACION.TIPOAFILIADO_ID = TIPOAFILIADO.ID" //
                + " INNER JOIN BRANCH ON CONTRATO.SUCURSAL_ID = BRANCH.ID" //
                + " INNER JOIN (SELECT A.NUMEROCONTRATO, A.NUMEROFAMILIA, A.NUMERO" //
                + " 			FROM AFILIACION A, FAMILIA F,  ENTITY E, CONTRATO C, TIPOAFILIADO TA" //
                + " 			WHERE A.NUMEROFAMILIA = F.NUMERO" //
                + " 			AND F.TITULAR_ID = E.ID" //
                + " 			AND A.NUMEROCONTRATO = C.NUMERO" //
                + " 			AND F.CONTRATO_ID = C.ID" //
                + " 			AND A.TIPOAFILIADO_ID = TA.ID" //
                + " 			AND E.PIN = :cedulaTitular" //
                + " 			AND C.ESTADO = 'A'" //
                + " 			AND A.ESTADO = 'ACTIVO'" //
                + " 			AND F.TITULAR_ID IN (SELECT DISTINCT F1.TITULAR_ID" //
                + "                       FROM AFILIACION A1, FAMILIA F1,  ENTITY E1, CONTRATO C1, TIPOAFILIADO TA1" //
                + "                       WHERE A1.NUMEROFAMILIA = F1.NUMERO" //
                + "                       AND F1.TITULAR_ID = E1.ID" //
                + "                       AND A1.NUMEROCONTRATO = C1.NUMERO" //
                + "                       AND F1.CONTRATO_ID = C1.ID" //
                + "                       AND A1.TIPOAFILIADO_ID = TA1.ID" //
                + "                       AND E1.PIN = :cedulaTitular" //
                + "                       AND C1.ESTADO = 'A'" //
                + "                       AND A1.ESTADO = 'ACTIVO'" //
                + "                       AND TA1.CODIGO = 'CT')) x" //
                + " ON (x.NUMEROCONTRATO = AFILIACION.NUMEROCONTRATO AND x.NUMEROFAMILIA = AFILIACION.NUMEROFAMILIA AND x.NUMERO = AFILIACION.NUMERO)" //
                + " WHERE" //
                + " AFILIACION.NUMEROCONTRATO = :numeroContrato AND" //
                // + " AFILIACION.NUMEROFAMILIA = :codFamilia AND" //
                // + " AFILIACION.NUMERO = :numAfiliacion AND" //
                + " CONTRATO.ESTADO = 'A' AND AFILIACION.ESTADO = 'ACTIVO' AND" //
                + " PLANCONTRATO.FECHAVIGENCIA = QUERYMAXIMAFECHA(AFILIACION.NUMEROCONTRATO, AFILIACION.NUMEROFAMILIA, AFILIACION.NUMERO) ORDER BY AFILIACION.NUMERO";
        Query query =  this.getEntityManager().createNativeQuery(sqlString);
        query.setParameter("cedulaTitular", cedulaTitular);
        query.setParameter("numeroContrato", numeroContrato);
        results = query.getResultList();
    }

    /* en base al metodo queryPreexistencia1(*/
    private void preexistenciasByBeneficiario(Integer numeroContrato, Integer numeroFamilia, Integer numeroAfiliado){
        List<Object[]> results = new ArrayList<>();
        BigDecimal numeroPreexistenciasConMonto = this.numeroPreexistenciasConMonto(numeroContrato, numeroFamilia, numeroAfiliado);
        /*si 0 es menor a numero de prexistencias = -1 */
        if (BigDecimal.ZERO.compareTo(numeroPreexistenciasConMonto) < 0) {
            this.preexistenciasConMonto(numeroContrato, numeroFamilia, numeroAfiliado);
        } else {
            this.preexistenciasSinMonto(numeroContrato, numeroFamilia, numeroAfiliado);
        }
    }

    private BigDecimal numeroPreexistenciasConMonto(Integer numeroContrato, Integer numeroFamilia, Integer numeroAfiliado) {
        String sqlString = "SELECT count(1) total " + "FROM CONTRATO " + "INNER JOIN AFILIACION ON CONTRATO.NUMERO = AFILIACION.NUMEROCONTRATO " //
                + "INNER JOIN PLANCONTRATO ON PLANCONTRATO.CONTRATO_ID = CONTRATO.ID " //
                + "INNER JOIN PLANESPECIFICO ON PLANCONTRATO.PLANESPECIFICO_ID = PLANESPECIFICO.ID " //
                + "INNER JOIN MONTOPREEXISTENCIA ON MONTOPREEXISTENCIA.PLAN_ID = PLANESPECIFICO.ID " //
                + "INNER JOIN AFILIADO ON AFILIACION.AFILIADO_ID = AFILIADO.ID " + "INNER JOIN PREEXISTENCIA ON PREEXISTENCIA.AFILIADO_ID = AFILIADO.ID " //
                + "INNER JOIN DIAGNOSTICO ON PREEXISTENCIA.DIAGNOSTICO_ID = DIAGNOSTICO.ID " + "INNER JOIN ITEMMEDICO ON DIAGNOSTICO.ID = ITEMMEDICO.ID " //
                + "WHERE " + "AFILIACION.NUMEROCONTRATO = :numContrato AND " + "AFILIACION.NUMEROFAMILIA = :codFamilia AND " //
                + "AFILIACION.NUMERO = :numAfiliacion AND " //
                + "TRUNC(PLANCONTRATO.FECHAVIGENCIA) = QUERYMAXIMAFECHA(AFILIACION.NUMEROCONTRATO, AFILIACION.NUMEROFAMILIA, AFILIACION.NUMERO)";
        Query query =  this.getEntityManager().createNativeQuery(sqlString);
        query.setParameter("numContrato", numeroContrato);
        query.setParameter("codFamilia", numeroFamilia);
        query.setParameter("numAfiliacion", numeroAfiliado);
        Object result1 = query.getSingleResult();
        return (BigDecimal) result1;
    }

    private void preexistenciasConMonto(Integer numeroContrato, Integer numeroFamilia, Integer numeroAfiliado) {
        List<Object[]> results = new ArrayList<>();
        String sqlString = "SELECT DISTINCT DIAGNOSTICO.CODIGO, ITEMMEDICO.NOMBRE, " //
                // 2FECHAINICIO
                + "TO_CHAR(CASE WHEN PREEXISTENCIA.FECHARIGE IS NULL THEN PREEXISTENCIA.FECHADECLARACION ELSE PREEXISTENCIA.FECHARIGE END,'dd/MM/yyyy') AS FECHAINICIO, " //
                // 3ESTADO
                + "decode(DIAGNOSTICO.ESTADO,'1','ACTIVO', DIAGNOSTICO.ESTADO) ESTADO, " //
                // 4CUBRE
                + "fn_coberturaap(MONTOPREEXISTENCIA.PERIODOCARENCIA,MONTOPREEXISTENCIA.UNIDADPERIODOCARENCIA, " //
                + "               MONTOPREEXISTENCIA.TIPOAFILIADO_ID,PREEXISTENCIA.FECHAREGISTRO) CUBRE " //
                + "FROM " + "CONTRATO " //
                + "INNER JOIN AFILIACION ON CONTRATO.NUMERO = AFILIACION.NUMEROCONTRATO " + "INNER JOIN PLANCONTRATO ON PLANCONTRATO.CONTRATO_ID = CONTRATO.ID "
                + "INNER JOIN PLANESPECIFICO ON PLANCONTRATO.PLANESPECIFICO_ID = PLANESPECIFICO.ID "
                + "INNER JOIN MONTOPREEXISTENCIA ON MONTOPREEXISTENCIA.PLAN_ID = PLANESPECIFICO.ID "
                + "INNER JOIN AFILIADO ON AFILIACION.AFILIADO_ID = AFILIADO.ID " + "INNER JOIN PREEXISTENCIA ON PREEXISTENCIA.AFILIADO_ID = AFILIADO.ID "
                + "INNER JOIN DIAGNOSTICO ON PREEXISTENCIA.DIAGNOSTICO_ID = DIAGNOSTICO.ID " + "INNER JOIN ITEMMEDICO ON DIAGNOSTICO.ID = ITEMMEDICO.ID "
                + "WHERE " + "AFILIACION.NUMEROCONTRATO = :numContrato AND  " + "AFILIACION.NUMEROFAMILIA = :codFamilia AND "
                + "AFILIACION.NUMERO = :numAfiliacion AND "
                + "TRUNC(PLANCONTRATO.FECHAVIGENCIA) = QUERYMAXIMAFECHA(AFILIACION.NUMEROCONTRATO, AFILIACION.NUMEROFAMILIA, AFILIACION.NUMERO) ";
        Query query =  this.getEntityManager().createNativeQuery(sqlString);
        query.setParameter("numContrato", numeroContrato);
        query.setParameter("codFamilia", numeroFamilia);
        query.setParameter("numAfiliacion", numeroAfiliado);
        results = query.getResultList();
    }

    private void preexistenciasSinMonto(Integer numeroContrato, Integer numeroFamilia, Integer numeroAfiliado) {
        List<Object[]> results = new ArrayList<>();
        String sqlString = "SELECT distinct DIAGNOSTICO.CODIGO, ITEMMEDICO.NOMBRE, " //
                // 2FECHAINICIO
                + "TO_CHAR(CASE WHEN PREEXISTENCIA.FECHARIGE IS NULL THEN PREEXISTENCIA.FECHADECLARACION ELSE PREEXISTENCIA.FECHARIGE END,'dd/MM/yyyy') AS FECHAINICIO, " //
                + "decode(DIAGNOSTICO.ESTADO,'1','ACTIVO', DIAGNOSTICO.ESTADO) ESTADO, " //
                + "'N' CUBRE " //
                + "FROM " + "CONTRATO " //
                + "INNER JOIN AFILIACION ON CONTRATO.NUMERO = AFILIACION.NUMEROCONTRATO " + "INNER JOIN PLANCONTRATO ON PLANCONTRATO.CONTRATO_ID = CONTRATO.ID "
                + "INNER JOIN PLANESPECIFICO ON PLANCONTRATO.PLANESPECIFICO_ID = PLANESPECIFICO.ID "
                + "INNER JOIN AFILIADO ON AFILIACION.AFILIADO_ID = AFILIADO.ID " + "INNER JOIN PREEXISTENCIA ON PREEXISTENCIA.AFILIADO_ID = AFILIADO.ID "
                + "INNER JOIN DIAGNOSTICO ON PREEXISTENCIA.DIAGNOSTICO_ID = DIAGNOSTICO.ID " + "INNER JOIN ITEMMEDICO ON DIAGNOSTICO.ID = ITEMMEDICO.ID "
                + "WHERE " + "AFILIACION.NUMEROCONTRATO = :numContrato AND " + "AFILIACION.NUMEROFAMILIA = :codFamilia AND "
                + "AFILIACION.NUMERO = :numAfiliacion AND "
                + "TRUNC(PLANCONTRATO.FECHAVIGENCIA) = QUERYMAXIMAFECHA(AFILIACION.NUMEROCONTRATO, AFILIACION.NUMEROFAMILIA, AFILIACION.NUMERO) ";
        Query query =  this.getEntityManager().createNativeQuery(sqlString);
        query.setParameter("numContrato", numeroContrato);
        query.setParameter("codFamilia", numeroFamilia);
        query.setParameter("numAfiliacion", numeroAfiliado);
        results = query.getResultList();
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public PersonVo updateValues(PersonVo personVo) {
        BooleanBuilder where = new BooleanBuilder();
        where.and(personEntity.personId.eq(personVo.getPersonId()));
        updateWithAudit(personEntity).where(where)
            .set(personEntity.firstName, personVo.getFirstName())
            .set(personEntity.lastName, personVo.getLastName())
            .set(personEntity.email, personVo.getEmail()).execute();
        return personVo;
    }


}
