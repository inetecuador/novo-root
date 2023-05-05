/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.base.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 *
 * @author Sistemas
 */
public class TimeUtil {
    private static final String ELAPSED_TIME_PATTERN = "%1s, %2s y %3s";

    public static final String DATE_PATTERN = "yyyy-MM-dd";
    public static final DateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat(DATE_PATTERN);
    public static final int DIAS_SEMANA = 7;
    public static final String DATE_PATTERN_YYYY_MMMMMM = "yyyy-MMMMM";
    public static final String DATE_PATTERN_FULL = "yyyy-MMM-dd hh:mm";
    private static final String DATE_PATTERN_YYYY_MM = "yyyy-MM";
    
    public static Date truncate(Date date) {
        Calendar calendar = getTruncatedInstance(date);
        return calendar.getTime();
    }

    public static Calendar getTruncatedInstance(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar;
    }

    public static Date fijarPrimerDiaMes(Date date) {
        Calendar calendar = getTruncatedInstance(date);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return calendar.getTime();
    }

    public static Date fijarAnio(Date anioNuevo, Date fecha) {
        Calendar fechaNueva = Calendar.getInstance();
        fechaNueva.setTime(anioNuevo);
        int aNuevo = fechaNueva.get(Calendar.YEAR);
        Calendar fechaConAnioNuevo = Calendar.getInstance();
        fechaConAnioNuevo.setTime(fecha);
        fechaConAnioNuevo.set(Calendar.YEAR, aNuevo);
        return fechaConAnioNuevo.getTime();
    }

    public static Date fijarMes(Date mesNuevo, Date fecha) {
        Calendar fechaNueva = Calendar.getInstance();
        fechaNueva.setTime(mesNuevo);
        int mesNuevo_ = fechaNueva.get(Calendar.MONTH);
        Calendar fechaConAnioNuevo = Calendar.getInstance();
        fechaConAnioNuevo.setTime(fecha);
        fechaConAnioNuevo.set(Calendar.MONTH, mesNuevo_);
        return fechaConAnioNuevo.getTime();
    }

    public static Date fijarDia(Date date, int dia) {
        Calendar calendar = getTruncatedInstance(date);
        calendar.set(Calendar.DAY_OF_MONTH, dia);
        return calendar.getTime();
    }

//    public static Date fijarUltimoDiaMes(Date date) {
//        LocalDate now = getAsLocalDate(date);
//        LocalDate lastDay = now.with(TemporalAdjusters.lastDayOfMonth());
//        return getAsDate(lastDay);
//    }

    /**
     * Convierte una fecha java.time.LocalDate en un objeto tipo java.util.Date
     *
     * @param current
     * @return
     */
    public static Date getAsDate(LocalDate current) {
        return Date.from(current.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    /**
     * Convierte una fecha java.time.LocalDate en un objeto tipo java.util.Date
     *
     * @param current
     * @return
     */
    public static Date getAsDate(LocalDateTime current) {
        return Date.from(current.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * Convierte una fecha java.util.Date en un objeto tipo java.time.LocalDate
     *
     * @param date
     * @return
     */
    public static LocalDate getAsLocalDate(Date date) {
        Date utilDate = new Date(date.getTime());
        LocalDate localDate = utilDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return localDate;
    }

    private static Period getPeriod(Date reference) {
        LocalDate startDate = getAsLocalDate(reference);
        LocalDate today = LocalDate.now();
        return Period.between(startDate, today);
    }

    private static Period getPeriod(Date fechaInicio, Date fechaFin) {
        LocalDate startDate = getAsLocalDate(fechaInicio);
        LocalDate today = getAsLocalDate(fechaFin);
        return Period.between(startDate, today);
    }

    /**
     *
     * @param fechaNacimiento
     * @return Numero entero con la edad en años
     */
    public static Integer getEdad(Date fechaNacimiento) {
        if (fechaNacimiento != null) {
            Period p = getPeriod(fechaNacimiento);
            return p.getYears();
        }
        return null;
    }

    /**
     *
     * @param fechaNacimiento
     * @param fechaCalculo Fecha de referencia para el calculo de la edad
     * @return Numero entero con la edad en años
     */
    public static Integer getEdad(Date fechaNacimiento, Date fechaCalculo) {
        if (fechaNacimiento != null) {
            Period p = getPeriod(fechaNacimiento, fechaCalculo);
            return p.getYears();
        }
        return null;
    }

    /**
     *
     * @param fechaInicio,
     * @param fechaFin
     * @
     * param fechaFin
     * @return Numero entero con la edad en años
     */
    public static Integer getTiempoTranscurridoMeses(Date fechaInicio, Date fechaFin) {
        if (fechaInicio != null && fechaFin != null) {
            Period p = getPeriod(fechaInicio, fechaFin);
//            return p.getMonths();
            Integer anios = p.getYears();
//            System.out.println("AÑOS: " + anios);
            Integer meses = p.getMonths();
            if (anios > 0) {
                meses += (anios * 12);
            }
//            System.out.println("MESES: " + p.getMonths());
            return meses;
        }
        return null;
    }

    /**
     *
//     * @param fechaInicio,
//     * @param fechaFin
     *
     * @return Numero entero con la edad en años
     */
//    public static Integer getTiempoTranscurridoDias(Date fechaInicio, Date fechaFin) {
//        if (fechaInicio != null && fechaFin != null) {
//            LocalDate startDate = getAsLocalDate(fechaInicio);
//            LocalDate endDate = getAsLocalDate(fechaFin);
//            Long p2 = ChronoUnit.DAYS.between(startDate, endDate);
//            return p2.intValue();
//        }
//        return null;
//    }

    public static String getElapsedTime(Date fechaReferencia, String formatPattern) {
        if (fechaReferencia != null) {
            Period p = getPeriod(fechaReferencia);
            Object[] amd = new Object[3];
            amd[0] = p.getYears() + (p.getYears() == 1 ? " año" : " años");
            amd[1] = p.getMonths() + (p.getMonths() == 1 ? " mes" : " meses");
            amd[2] = p.getDays() + (p.getDays() == 1 ? " dia" : " dias");

            if (formatPattern == null) {
                formatPattern = ELAPSED_TIME_PATTERN;
            }
            String result = String.format(formatPattern, amd);
            return result;
        }
        return null;
    }

    public static String formatDate(Date date) {
        return formatDate(date, DATE_PATTERN);
    }

    public static String formatDate(Date date, String formatPattern) {
        if (date != null) {
            if (formatPattern == null) {
                formatPattern = DATE_PATTERN;
            }
            SimpleDateFormat format1 = new SimpleDateFormat(formatPattern, new Locale("es", "EC"));
            String result = format1.format(date);
            return result;
        }
        return null;

    }

    public static String formatBlankDate(Date date, String formatPattern) {
        if (date != null && formatPattern != null) {
            SimpleDateFormat format = new SimpleDateFormat(formatPattern, new Locale("es", "EC"));
            String result = format.format(date);
            return result;
        }
        return " ";

    }

    /**
     * Permite obtener la fecha enviada como un arreglo de enteros
     *
     * @param date Fecha a dividir
     * @return [0] anio, [1] mes, [2] dia
     */
    public static Integer[] splitDate(Date date) {
        Integer[] sd = new Integer[3];
        LocalDate localDate = getAsLocalDate(date);
        sd[0] = localDate.getYear();
        sd[1] = localDate.getMonth().getValue();
        sd[2] = localDate.getDayOfMonth();
        return sd;
    }

    public static boolean isDateFuture(Date date) {
        Date now = Calendar.getInstance().getTime();
        return date.after(now);
    }

    public static boolean isDatePast(Date date) {
        Date now = Calendar.getInstance().getTime();
        return date.before(now);
    }

    /**
     *
     * @param date Fecha a verificar
     * @param initial Fecha inicial
     * @param end Fecha Final
     * @return (True) si se encuentra en el rango, (False) si esta fuera del
     * rango
     */
    public static boolean isInRangeOfDates(Date date, Date initial, Date end) {
        return date.after(initial) && date.before(end);
    }

    /**
     *
     * @param date1
     * @param date2
     * @return (True) si el Año-mes-día de las fechas son iguales
     */
    public static boolean isEquals(Date date1, Date date2) {
        Calendar c1 = Calendar.getInstance();
        c1.setTime(date1);
        Calendar c2 = Calendar.getInstance();
        c2.setTime(date2);
        boolean b = c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR) && c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH) && c1.get(Calendar.DAY_OF_MONTH) == c2.get(Calendar.DAY_OF_MONTH);
        return b;
    }

    public static boolean isEqualsAnioMes(Date date1, Date date2) {
        Calendar c1 = Calendar.getInstance();
        c1.setTime(date1);
        Calendar c2 = Calendar.getInstance();
        c2.setTime(date2);
        boolean b = c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR) && c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH);
        return b;
    }

    public static boolean isAnioMenor(Date date1, Date date2) {
        Calendar c1 = Calendar.getInstance();
        c1.setTime(date1);
        Calendar c2 = Calendar.getInstance();
        c2.setTime(date2);
        boolean b = c1.get(Calendar.YEAR) < c2.get(Calendar.YEAR);
        return b;
    }

//    public static Date getDate(String fecha) {
//        return getDate(fecha, DATE_PATTERN);
//    }

//    public static Date getDate(String fechaNacimiento, String pattern) {
//        try {
//            SimpleDateFormat formatter = new SimpleDateFormat(pattern);
//            Date utilDate = formatter.parse(fechaNacimiento);
//            return utilDate;
//        } catch (ParseException ex) {
//            //Logger.getLogger(TimeUtilTest.class.getName()).log(Level.SEVERE, null, ex);            
//            return null;
//        }
//    }

    public static String getElapsedTime(Date fechaAfiliacion) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public static Date getNewDate() {
        return Calendar.getInstance().getTime();
    }

    public static Date now() {
        return Calendar.getInstance().getTime();
    }

    public static Date getDateOperation(Date date, int field, int amount) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(field, amount);
        return c.getTime();
    }

    public static Date getDateOperationSet(Date date, int field, int amount) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(field, amount);
        return c.getTime();
    }

    //Codigo java obtenido de org.omnifaces.el.functions;
    public static Date addMonths(Date date, int months) {
        return add(date, months, Calendar.MONTH);
    }

    public static int getMonths(Date date) {
        Calendar d = Calendar.getInstance();
        d.setTime(date);
        return d.get(Calendar.MONTH) + 1;
    }

    public static Date addDays(Date date, int days) {
        return add(date, days, Calendar.DAY_OF_MONTH);
    }

    public static Date addYears(Date date, int years) {
        return add(date, years, Calendar.YEAR);
    }
    //UTC zona horaria universal
    //ECT zona horaria para ecuador
    private static final TimeZone TIMEZONE_UTC = TimeZone.getTimeZone("UTC");

    private static Date add(Date date, int units, int field) {
        if (date == null) {
            throw new NullPointerException("date");
        }

        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.setTime(date);
        calendar.setTimeZone(TIMEZONE_UTC);
        calendar.add(field, units);
        return calendar.getTime();
    }

    public static String getMesesImpago(Integer mesesImpago, Date fechaImpago) {
        StringBuilder builder = new StringBuilder();
        for (int i = 1; i <= mesesImpago; i++) {
            Date actual = TimeUtil.addMonths(fechaImpago, i);
            String fechaStr = TimeUtil.formatDate(actual, DATE_PATTERN_YYYY_MM);
            builder.append(fechaStr);
            if (i < mesesImpago) {
                builder.append(", ");
            }
        }

        return builder.toString();
    }

    public static String getMesesImpagoMesAnterior(Integer mesesImpago, Date fechaProduccion) {
        return getMesesImpagoMesAnterior(mesesImpago, fechaProduccion, DATE_PATTERN_YYYY_MM);
    }

    public static String getMesesImpagoMesAnterior(Integer mesesImpago, Date fechaProduccion,
            String pattern) {

        List<String> fechas = new ArrayList<>();
        for (int i = mesesImpago; i > 0; i--) {
            Date actual = TimeUtil.addMonths(fechaProduccion, -i);
            String fechaStr = TimeUtil.formatDate(actual, pattern);
            fechas.add(fechaStr);
        }

        if (fechas.isEmpty()) {
            return "";
        }
        String fechasStr = fechas.stream().reduce((x, y) -> x + ", " + y).get();

        return fechasStr;
    }

    /**
     * Corta o trunca una cadena dependiendo de la longitud enviada
     *
     * @param str
     * @param longitud
     * @return
     */
    public static String truncar(String str, int longitud) {
        if (str == null || str.isEmpty()) {
            str = " ";
        }
        if (str.length() > longitud) {
            str = str.substring(0, longitud);
        }
        return str;
    }

    /**
     * Alinia a la derecha una cadena
     *
     * @param val
     * @return
     */
    public static String alinearDerecha(String val) {
        String respuesta = " ";
        if (val != null) {
            respuesta = String.format("%10s", val.trim());//%10s  //derecha  //%-10s
        }
        return respuesta;
    }

    /**
     * LLena con espacios String dependiendo de la longitud enviada
     *
     * @param datos
     * @param longitud
     * @param espacio caracter a llenar
     * @return
     */
    public static String fillWith(String datos, int longitud, String espacio) {
        if (datos == null || datos.isEmpty()) {
            datos = espacio;
        }
        StringBuilder linea = new StringBuilder();
        linea.append(datos);
        while (linea.toString().length() < longitud) {
            linea.append(espacio);
        }
        return linea.toString();
    }

    public static int getYear(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);
        return year;
    }

    public static int getMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int month = cal.get(Calendar.MONTH);
        return month;
    }

    public static List<Date> listaEntreFechasPorMes(Date fechaDesde, Date fechaHasta) {
        Calendar c1 = Calendar.getInstance();
        c1.setTime(fechaDesde);
        Calendar c2 = Calendar.getInstance();
        c2.setTime(fechaHasta);
        List<Date> listaFechas = new ArrayList<>();
        while (!c1.after(c2)) {
            listaFechas.add(c1.getTime());
            c1.add(Calendar.MONTH, 1);
        }
        return listaFechas;
    }
   
    /**
     * Verifica si una fecha esta dentro de un inicio y un fin
     * tomando encuenta incluso el inicio y el fin
     * @param start
     * @param stop
     * @param now
     * @return
     */
    public static Boolean contains(Date start, Date stop, Date now) {
        if (start != null && stop != null) {
            start = addDays(start, -1);
            stop = addDays(stop, 1);
            return (!now.before(start) && (now.before(stop)));
        }
        return Boolean.FALSE;
    }
}
