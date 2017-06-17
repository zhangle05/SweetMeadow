/**
 * 
 */
package com.octopusdio.api.common.service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * @author zhangle
 *
 */
public class TimeUtils {

    public static final SimpleDateFormat sCommonFormat = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");

    /**
     * get term start time according to term code 根据学期编码获取本学期开学时间 (1月15日或7月15日)
     * 
     * @param termCode:
     *            学期编码，编码方式为"学年开始年份"+"学期序号",
     *            比如"201601"表示2016-2017学年第一学期(2016.07.15-2017.01.15),
     *            "201602"表示2016-2017学年第二学期(2017.01.15-2017.07.15)
     * @return
     */
    public static Date getTermStart(String termCode) throws Exception {
        int year = Integer.parseInt(termCode.substring(0, 4));
        String dateStr = "";
        if (termCode.endsWith("01")) {
            // N~(N+1)年度第一学期
            dateStr = year + "-07-15 00:00:00";
        } else {
            dateStr = (year + 1) + "-01-15 00:00:00";
        }
        return sCommonFormat.parse(dateStr);
    }

    /**
     * get term end time according to term code 根据当前时间获取本学期结束时间 (1月15日或7月15日)
     * 
     * @param termCode:
     *            学期编码，编码方式为"学年开始年份"+"学期序号",
     *            比如"201601"表示2016-2017学年第一学期(2016.07.15-2017.01.15),
     *            "201602"表示2016-2017学年第二学期(2017.01.15-2017.07.15)
     * @return
     */
    public static Date getTermEnd(String termCode) throws Exception {
        int year = Integer.parseInt(termCode.substring(0, 4));
        String dateStr = "";
        if (termCode.endsWith("01")) {
            // N~(N+1)年度第一学期
            dateStr = (year + 1) + "-01-15 00:00:00";
        } else {
            dateStr = (year + 1) + "-07-15 00:00:00";
        }
        return sCommonFormat.parse(dateStr);
    }

    /**
     * 根据时间戳获取学期编码
     * 
     * @param currentTimeMil
     * @return
     * @throws Exception
     */
    public static String getTermCode(long currentTimeMil) throws Exception {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(currentTimeMil);
        int intYear = cal.get(Calendar.YEAR);
        String startStr = intYear + "-01-15 00:00:00";
        String endStr = intYear + "-07-15 00:00:00";
        System.out.println("startStr:" + startStr + ", endStr:" + endStr);
        Date tmpStart = sCommonFormat.parse(startStr);
        Date tmpEnd = sCommonFormat.parse(endStr);
        if (currentTimeMil < tmpStart.getTime()) {
            // 当前时间在xxxx年1月1日至xxxx年1月15日之间
            return (intYear - 1) + "01";
        } else if (currentTimeMil < tmpEnd.getTime()) {
            // 当前时间在xxxx年1月15日至xxxx年7月15日之间
            return (intYear - 1) + "02";
        } else {
            // 当前时间在xxxx年7月15日至xxxx年12月31日之间
            return intYear + "01";
        }
    }

    /**
     * 根据时间戳获取过去最近一周的周日
     * 
     * @param currentTimeMil
     * @return
     */
    public static Date getLastSunday(long currentTimeMil) {
        Calendar c = Calendar.getInstance(Locale.CHINA);
        c.setTimeInMillis(currentTimeMil);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        currentTimeMil = c.getTimeInMillis();
        long oneDayMil = 3600 * 1000 * 24L;
        for (int i = 1; i <= 7; i++) {
            long past = currentTimeMil - i * oneDayMil;
            c.setTimeInMillis(past);
            int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
            if (dayOfWeek == Calendar.SUNDAY) {
                return c.getTime();
            }
        }
        return c.getTime();
    }
}
