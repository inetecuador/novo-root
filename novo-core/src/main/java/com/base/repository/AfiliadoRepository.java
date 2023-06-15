package com.base.repository;

import com.base.common.JPAQueryDslBaseRepository;
import com.base.entity.PersonEntity;
import com.base.util.TimeUtil;
import com.base.vo.AfiliadoPrexistenciaDTO;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * AfiliadoRepository.
 *
 * @author vsangucho on 20/3/2023.
 * @version 1.0
 */
@Lazy
@Repository
public class AfiliadoRepository extends JPAQueryDslBaseRepository<PersonEntity> implements
        IAfiliadoRepository {

    /**
     * Constructor.
     */
    public AfiliadoRepository() {
        super(PersonEntity.class);
    }

    /**
     *
     * @param numeroDocumento
     * @param numeroContrato
     * @return
     * en base a queryFamiliares y queryAfiliacionTitular
     */
    @Override
    public List<AfiliadoPrexistenciaDTO> afiliadosPorContrato(String numeroDocumento, Integer numeroContrato) {
        List<AfiliadoPrexistenciaDTO> afiliadosPorContrato = new ArrayList<>();
        String sqlString = "SELECT"
                // 0AFILIACION.ID, 1UPPER(TIPOAFILIADO.NOMBRE) AS NOMBREPARENTESCO
                + " AFILIACION.ID, UPPER(TIPOAFILIADO.NOMBRE) AS NOMBREPARENTESCO,"
                // 2PERSON.FIRSTNAME, 3PERSON.MIDDLENAME, 4PERSON.LASTNAME, 5PERSON.SECONDLASTNAME,
                + "	PERSON.FIRSTNAME, PERSON.MIDDLENAME, PERSON.LASTNAME, PERSON.SECONDLASTNAME,"
                // 6PERSON.GENDER, 7ENTITY.DOCUMENTTYPE, 8ENTITY.PIN, 9TO_CHAR(PERSON.BIRTHDATE,'dd/MM/yyyy') AS BIRTHDATE
                + "	PERSON.GENDER, ENTITY.DOCUMENTTYPE, ENTITY.PIN, TO_CHAR(PERSON.BIRTHDATE,'dd/MM/yyyy') AS BIRTHDATE,"
                // 10fn_edades(PERSON.BIRTHDATE) AS EDAD
                + "	fn_edades(PERSON.BIRTHDATE) AS EDAD,"
                // 11AFILIACION.FECHACAPTURA < AFILIACION.FECHAINGRESO THEN AFILIACION.FECHACAPTURA AS FECHAINCLUSION
                + "	TO_CHAR(CASE WHEN AFILIACION.FECHACAPTURA < AFILIACION.FECHAINGRESO THEN AFILIACION.FECHACAPTURA ELSE AFILIACION.FECHAINGRESO END,'dd/MM/yyyy') AS FECHAINCLUSION,"
                // 12AFILIACION.NUMEROFAMILIA
                // + "	AFILIACION.NUMEROFAMILIA,"
                // 12FN_VALIDACONTRATO
                + " FN_VALIDACONTRATO(:numContrato, AFILIACION.NUMEROFAMILIA, AFILIACION.NUMERO) AS VALIDACONTRATO"
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
                + " AFILIACION.NUMEROCONTRATO = :numContrato AND"
                // + " AFILIACION.NUMEROFAMILIA = :codFamilia AND"
                // + " AFILIACION.NUMERO = :numAfiliacion AND"
                + " CONTRATO.ESTADO = 'A' AND AFILIACION.ESTADO = 'ACTIVO' AND"
                + " PLANCONTRATO.FECHAVIGENCIA = QUERYMAXIMAFECHA(AFILIACION.NUMEROCONTRATO, AFILIACION.NUMEROFAMILIA, AFILIACION.NUMERO) ORDER BY AFILIACION.NUMERO";
        Query query = this.getEntityManager().createNativeQuery(sqlString);
        query.setParameter("valCedula", numeroDocumento);
        query.setParameter("numContrato", numeroContrato);
        List<Object[]> results = query.getResultList();
        if(results.size() == 0) {
            return null;
        }
        afiliadosPorContrato = this.setAfiliadosPorContrato(query.getResultList(), numeroContrato);
        return afiliadosPorContrato;
    }

    private List<AfiliadoPrexistenciaDTO> setAfiliadosPorContrato(List<Object[]> results, Integer numeroContrato) {
        List<AfiliadoPrexistenciaDTO> afiliadosPorContrato = new ArrayList<>();
        for (Object[] afiliadoResults : results) {
            AfiliadoPrexistenciaDTO afiliadoPrexistenciaDTO = new AfiliadoPrexistenciaDTO();
            // 0AFILIACION.ID usuario numUsuario numeroAfiliado
            // mascara NumeroPersona
            afiliadoPrexistenciaDTO.setAfiliacionId(Long.parseLong(afiliadoResults[0].toString().trim()));
            // antes afiliadoPrexistenciaDTO.setNumeroAfiliado(Integer.parseInt(afiliadoResults[0].toString().trim()));

            // 1UPPER(TIPOAFILIADO.NOMBRE) AS NOMBREPARENTESCO
            afiliadoPrexistenciaDTO.setNombreParentesco(afiliadoResults[1].toString().trim());
            // 2PERSON.FIRSTNAME
            String priNombreBeneficiario = afiliadoResults[2].toString().trim() == null ? "" : afiliadoResults[2].toString().trim();
            // 3PERSON.MIDDLENAME
            String segNombreBeneficiario = afiliadoResults[3].toString().trim() == null ? "" : afiliadoResults[3].toString().trim();
            // 4PERSON.LASTNAME
            String priApelliBeneficiario = afiliadoResults[4].toString().trim() == null ? "" : afiliadoResults[4].toString().trim();
            // 5PERSON.SECONDLASTNAME
            String segApelliBeneficiario = afiliadoResults[5].toString().trim() == null ? "" : afiliadoResults[5].toString().trim();
            afiliadoPrexistenciaDTO.setNombres(priNombreBeneficiario + ("".equals(segNombreBeneficiario) ? "" : (" " + segNombreBeneficiario)));
            afiliadoPrexistenciaDTO.setApellidos(priApelliBeneficiario + ("".equals(segApelliBeneficiario) ? "" : (" " + segApelliBeneficiario)));
            // 6PERSON.GENDER,
            afiliadoPrexistenciaDTO.setGenero(afiliadoResults[6].toString().trim());
            // 7ENTITY.DOCUMENTTYPE,
            afiliadoPrexistenciaDTO.setTipoDocumento(afiliadoResults[7].toString().trim());
            // 8ENTITY.PIN,
            afiliadoPrexistenciaDTO.setIdentificacion(afiliadoResults[8].toString().trim());
            // 9TO_CHAR(PERSON.BIRTHDATE,'dd/MM/yyyy') AS BIRTHDATE
            afiliadoPrexistenciaDTO.setFechaNacimiento(afiliadoResults[9].toString().trim());
            // 10fn_edades(PERSON.BIRTHDATE) AS EDAD
            afiliadoPrexistenciaDTO.setEdad(Integer.parseInt(afiliadoResults[10].toString().trim()));
            // 11AFILIACION.FECHACAPTURA < AFILIACION.FECHAINGRESO THEN
            // AFILIACION.FECHACAPTURA AS FECHAINCLUSION
            afiliadoPrexistenciaDTO.setFechaInclusion(afiliadoResults[11].toString().trim());
            afiliadoPrexistenciaDTO.setDeducibleCubierto(new BigDecimal(0));
            // 12AFILIACION.NUMEROFAMILIA
            // afiliadoPrexistenciaDTO.setNumeroFamilia(Integer.parseInt(afiliadoResults[12].toString().trim()));

            // Long afiliacionId (antes Integer numContrato, Integer numFamilia,Integer numeroUsuario)
            afiliadoPrexistenciaDTO.setEnCarencia(this.enCarenciaBnf(afiliadoPrexistenciaDTO.getAfiliacionId()));

            afiliadoPrexistenciaDTO.setEnCarenciaHospitalaria(afiliadoPrexistenciaDTO.getEnCarencia());
            afiliadoPrexistenciaDTO.setDiasFinCarencia(this.diasFinCarenciaBnf(afiliadoPrexistenciaDTO.getFechaInclusion(), afiliadoPrexistenciaDTO.getEnCarencia()));

            afiliadoPrexistenciaDTO.setDiasFinCarenciaHospitalaria(0);
            afiliadoPrexistenciaDTO.setBeneficioOda(false);
            // 12FN_VALIDACONTRATO
            afiliadoPrexistenciaDTO.setObservaciones(afiliadoResults[12].toString().trim());
            afiliadosPorContrato.add(afiliadoPrexistenciaDTO);
        }
        return afiliadosPorContrato;
    }

    private Integer diasFinCarenciaBnf(String fechaIngresoAfiliado, Boolean enCarencia) {
        int dias = 0;
        if (enCarencia) {
            try {
                Date fechaIngresoAfiliadoDate = new SimpleDateFormat("dd/MM/yyyy").parse(fechaIngresoAfiliado);
                Date now = new Date();
                dias = (int) ((TimeUtil.addDays(fechaIngresoAfiliadoDate, 30).getTime() - now.getTime()) / 86400000);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return dias;
        } else {
            return 0;
        }
    }

    /**
     * @param afiliacionId antes Integer numeroUsuario
     * @return
     */
    private Boolean enCarenciaBnf(Long afiliacionId) {
        Date now = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String varCita = dateFormat.format(now);
        if ("S".equals(this.validaCarencia(varCita, afiliacionId))) {
            // SIN CARENCIA
            return false;
        } else {
            // No cumple periodo de carencia // CARENCIA 30 DIAS
            return true;
        }
    }
    private Boolean enCarenciaBnf(Integer numeroContrato, Integer numeroFamilia, Integer numeroUsuario) {
        Date now = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String varCita = dateFormat.format(now);
        if ("S".equals(this.validaCarencia(varCita, numeroContrato, numeroFamilia, numeroUsuario))) {
            // SIN CARENCIA
            return false;
        } else {
            // No cumple periodo de carencia // CARENCIA 30 DIAS
            return true;
        }
    }

    /*deberia estar en afiliacion AFILIACION.TIPOCARENCIA*/
    /**
     *
     * @param varCita
     * @param afiliacionId
     * @return
     */
    public String validaCarencia(String varCita, Long afiliacionId) {
        String aplica = "X";
        if ("SIN_CARENCIA".equals(this.tipoCarencia(afiliacionId))) {
            aplica = "S";
        } else {
            String sqlString = "SELECT distinct FN_CARENCIA(CASE WHEN afiliacion.fechacaptura <= AFILIACION.Fechaingreso then afiliacion.fechacaptura else AFILIACION.Fechaingreso end + 30, TO_DATE(:varCita,'YYYY-MM-DD')) RESULTADO "
                    + "FROM CONTRATO "
                    + "INNER JOIN AFILIACION ON CONTRATO.NUMERO = AFILIACION.NUMEROCONTRATO "
                    + "INNER JOIN PLANCONTRATO ON PLANCONTRATO.CONTRATO_ID = CONTRATO.ID "
                    + "WHERE "
                    + "  AFILIACION.ID = :afiliacionId AND "
                    + "  PLANCONTRATO.FECHAVIGENCIA = QUERYMAXIMAFECHA(AFILIACION.NUMEROCONTRATO, AFILIACION.NUMEROFAMILIA, AFILIACION.NUMERO)";
            Query query = this.getEntityManager().createNativeQuery(sqlString);
            query.setParameter("varCita", varCita);
            query.setParameter("afiliacionId", afiliacionId);
            Object result = query.getSingleResult();
            aplica = (String) (result == null ? "" : result);
        }
        return aplica;
    }
    /*deberia estar en afiliacion AFILIACION.TIPOCARENCIA*/
    public String validaCarencia(String varCita, Integer numContrato, Integer numFamilia, Integer numUsuario) {
        String aplica = "X";
        if ("SIN_CARENCIA".equals(this.tipoCarencia(numContrato, numFamilia, numUsuario))) {
            aplica = "S";
        } else {
            String sqlString = "SELECT distinct FN_CARENCIA(CASE WHEN afiliacion.fechacaptura <= AFILIACION.Fechaingreso then afiliacion.fechacaptura else AFILIACION.Fechaingreso end + 30, TO_DATE(:varCita,'YYYY-MM-DD')) RESULTADO "
                    + "FROM CONTRATO "
                    + "INNER JOIN AFILIACION ON CONTRATO.NUMERO = AFILIACION.NUMEROCONTRATO "
                    + "INNER JOIN PLANCONTRATO ON PLANCONTRATO.CONTRATO_ID = CONTRATO.ID "
                    + "WHERE CONTRATO.NUMERO = :numContrato AND "
                    + "  AFILIACION.NUMEROFAMILIA = :numFamilia AND "
                    + "  AFILIACION.NUMERO = :numUsuario AND "
                    + "  PLANCONTRATO.FECHAVIGENCIA = QUERYMAXIMAFECHA(AFILIACION.NUMEROCONTRATO, AFILIACION.NUMEROFAMILIA, AFILIACION.NUMERO)";
            Query query = this.getEntityManager().createNativeQuery(sqlString);
            query.setParameter("varCita", varCita);
            query.setParameter("numContrato", numContrato);
            query.setParameter("numFamilia", numFamilia);
            query.setParameter("numUsuario", numUsuario);
            Object result = query.getSingleResult();
            aplica = (String) (result == null ? "" : result);
        }
        return aplica;
    }

    /**
     *
     * @param afiliacionId antes Integer numUsuario
     * @return
     */
    private String tipoCarencia(Long afiliacionId) {
        String tipoCarencia = "";
        List<Object> results = new ArrayList<>();
        String sqlString = "SELECT distinct AFILIACION.TIPOCARENCIA FROM CONTRATO "
                + "INNER JOIN AFILIACION ON CONTRATO.NUMERO = AFILIACION.NUMEROCONTRATO " + "INNER JOIN PLANCONTRATO ON PLANCONTRATO.CONTRATO_ID = CONTRATO.ID "
                + "WHERE AFILIACION.ID = :afiliacionId AND "
                + " PLANCONTRATO.FECHAVIGENCIA = QUERYMAXIMAFECHA(AFILIACION.NUMEROCONTRATO, AFILIACION.NUMEROFAMILIA, AFILIACION.NUMERO)";
        Query query = this.getEntityManager().createNativeQuery(sqlString);
        query.setParameter("afiliacionId", afiliacionId);
        results = query.getResultList();
        if (!results.isEmpty()) {
            tipoCarencia = results.get(0).toString();
        }
        return tipoCarencia;
    }

    /*deberia estar en afiliacion AFILIACION.TIPOCARENCIA*/
    private String tipoCarencia(Integer numContrato, Integer numFamilia, Integer numUsuario) {
        String tipoCarencia = "";
        List<Object> results = new ArrayList<>();
        String sqlString = "SELECT distinct AFILIACION.TIPOCARENCIA FROM CONTRATO "
                + "INNER JOIN AFILIACION ON CONTRATO.NUMERO = AFILIACION.NUMEROCONTRATO " + "INNER JOIN PLANCONTRATO ON PLANCONTRATO.CONTRATO_ID = CONTRATO.ID "
                + "WHERE CONTRATO.NUMERO = :numContrato AND" + " AFILIACION.NUMEROFAMILIA = :numFamilia AND " + " AFILIACION.NUMERO = :numUsuario AND "
                + " PLANCONTRATO.FECHAVIGENCIA = QUERYMAXIMAFECHA(AFILIACION.NUMEROCONTRATO, AFILIACION.NUMEROFAMILIA, AFILIACION.NUMERO)";
        Query query = this.getEntityManager().createNativeQuery(sqlString);
        query.setParameter("numContrato", numContrato);
        query.setParameter("numFamilia", numFamilia);
        query.setParameter("numUsuario", numUsuario);
        results = query.getResultList();
        if (!results.isEmpty()) {
            tipoCarencia = results.get(0).toString();
        }
        return tipoCarencia;
    }
}
