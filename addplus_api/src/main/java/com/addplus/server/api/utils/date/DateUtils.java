package com.addplus.server.api.utils.date;


import com.addplus.server.api.constant.TimeStampConstant;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 类描述:
 *
 * @author ljt
 * @version V1.0
 * @date 2016年11月04日 16:50
 */
public class DateUtils {

    public static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static SimpleDateFormat yyyy_MM_dd = new SimpleDateFormat("yyyy-MM-dd");
    private static SimpleDateFormat yyyy_MM_dd_HH_mm_ss = new SimpleDateFormat("yyyy年MM月dd日HH:mm:ss");

    private static DateTimeFormatter datetimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * 获取当前日期时间
     *
     * @return Date类型
     */
    public static Date nowDate() {
        return new Date();
    }

    /**
     * 获取当前日期时间
     *
     * @return String类型
     */
    public static String nowDateString() {
        return LocalDateTime.now().format(datetimeFormat);
    }

    /**
     * 获取传入时间的开始时间
     *
     * @param time Date类型
     * @param day  the value to be set for the given calendar field.
     * @return Date类型
     */
    public static Date getDateStart(Date time, int day) {
        return customTime(time, day, 0, 0, 0);
    }

    /**
     * 获取传入时间的结束时间
     *
     * @param time Date类型
     * @param day  the value to be set for the given calendar field.
     * @return Date类型
     */
    public static Date getDateEnd(Date time, int day) {
        return customTime(time, day, 23, 59, 59);
    }

    /**
     * 自定义日期
     *
     * @param time      传入时间
     * @param day       天数 正数为传入时间后几天，负数为传入时间前几天
     * @param hourOfDay 固定小时
     * @param minute    固定分钟
     * @param second    固定秒
     * @return 生成后时间
     */
    public static Date customTime(Date time, int day, int hourOfDay, int minute, int second) {
        Calendar currentDate = new GregorianCalendar();
        currentDate.setTime(time);
        currentDate.add(Calendar.DATE, day);
        currentDate.set(Calendar.HOUR_OF_DAY, hourOfDay);
        currentDate.set(Calendar.MINUTE, minute);
        currentDate.set(Calendar.SECOND, second);
        return (Date) currentDate.getTime().clone();
    }


    /**
     * 将日期格式字符串转成日期
     *
     * @param strDate 日期格式字符串
     * @return
     * @author ljt
     */
    public static Date getDateFromStr(String strDate) {
        Date date = null;
        try {
            date = yyyy_MM_dd.parse(strDate);
        } catch (Exception e) {
            date = null;
        }
        return date;
    }

    /**
     * 将日期格式字符串转成日期
     *
     * @param strDate 日期格式字符串
     * @return
     * @author ljt
     */
    public static Date getDateFromStrYMDHMS(String strDate) {
        Date date = null;
        try {
            date = simpleDateFormat.parse(strDate);
        } catch (Exception e) {
            date = null;
        }
        return date;
    }

    /**
     * 以“yyyy年MM月dd日HH:mm:ss”的格式得到当前日期时间
     *
     * @return
     * @author ljt
     */
    public static String getNowDate() {
        return yyyy_MM_dd_HH_mm_ss.format(new Date());
    }

    /**
     * 方法描述:对传人时间进行计算
     *
     * @autor ljt
     * @date 2017年03月21日 09:47:19
     */

    public static Date calculateTime(Date time, int day) {
        Calendar currentDate = new GregorianCalendar();
        currentDate.setTime(time);
        currentDate.add(Calendar.DATE, day);
        return (Date) currentDate.getTime().clone();
    }


    /**
     * 方法描述: 字符串类型日期加一天并转成日期类型
     *
     * @param
     * @return
     * @author ljt
     * @date 2017年03月15日  11:12:46
     */
    public static Date addOneDay(String day) {
        Date date = null;
        try {
            date = yyyy_MM_dd.parse(day);
            Calendar c = Calendar.getInstance();
            c.setTime(date);   //设置当前日期
            c.add(Calendar.DATE, 1); //日期加1天
            date = c.getTime();
        } catch (Exception e) {
            date = null;
        }
        return date;
    }

    public static Date stringToDateTime(String strDateTime) {
        Date date = null;
        try {
            date = TimeStampConstant.formatterDate5.parse(strDateTime);
        } catch (ParseException e) {
            date = null;
        }
        return date;
    }

    /**
     * LocalDate类型转Date类型
     *
     * @param localDate
     * @return Date
     */
    public static Date localDateToDate(LocalDate localDate) {
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zdt = localDate.atStartOfDay(zoneId);
        Date date = Date.from(zdt.toInstant());
        return date;
    }

    /**
     * LocalDateTime类型转Date类型
     *
     * @param localDateTime
     * @return Date
     */
    public static Date localDateToDateTime(LocalDateTime localDateTime) {
        ZoneId zoneId = ZoneId.of("Asia/Shanghai");
        ZonedDateTime zdt = localDateTime.atZone(zoneId);
        Date date = Date.from(zdt.toInstant());
        return date;
    }

    /**
     * 方法描述：date转LocalDateTime
     *
     * @throws Exception
     * @author zhangjiehang
     * @date 2018/11/8 4:06 PM
     */
    public static LocalDateTime dateToLocalDateTime(Date date) {
        Instant instant = date.toInstant();
        ZoneId zone = ZoneId.systemDefault();
        return LocalDateTime.ofInstant(instant, zone);
    }


    /**
     * 方法描述：date转LocalDateTime
     *
     * @throws Exception
     * @author zhangjiehang
     * @date 2018/11/8 4:06 PM
     */
    public static LocalDateTime dateToLocalDateTimeSecondZero(Date date) {
        Instant instant = date.toInstant();
        ZoneId zone = ZoneId.systemDefault();
        LocalDateTime localDateTimeOld = LocalDateTime.ofInstant(instant, zone);
        LocalDateTime localDateTimeNew = LocalDateTime.of(localDateTimeOld.getYear(), localDateTimeOld.getMonth(), localDateTimeOld.getDayOfMonth(), localDateTimeOld.getHour(), localDateTimeOld.getMinute(), 0);
        return localDateTimeNew;
    }

    /**
     * 方法描述：date转LocalDate
     *
     * @throws Exception
     * @author zhangjiehang
     * @date 2018/11/8 4:06 PM
     */
    public static LocalDate dateToLocalDate(Date date) {
        return dateToLocalDateTime(date).toLocalDate();
    }

    /**
     * 秒数增加后时间
     *
     * @param date
     * @return
     */
    public static Date addOneSecond(Date date, Integer seconds) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.SECOND, seconds);
        return calendar.getTime();
    }

    /**
     * 两个时间相差距离多少天多少小时多少分多少秒
     *
     * @param one 时间参数 1
     * @param two 时间参数 2
     * @return String 返回值为：xx天xx小时xx分xx秒
     */
    public static List<Long> getDistanceTime(Date one, Date two) {
        List<Long> result = new ArrayList<>();
        long day = 0;
        long hour = 0;
        long min = 0;
        long sec = 0;
        long ms = 0;
        long time1 = one.getTime();
        long time2 = two.getTime();
        long diff;
        if (time1 < time2) {
            diff = time2 - time1;
        } else {
            diff = time1 - time2;
        }
        day = diff / (24 * 60 * 60 * 1000);
        hour = (diff / (60 * 60 * 1000) - day * 24);
        min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
        sec = (diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        //ms = (diff - day * 24 * 60 * 60 * 1000 - hour * 60 * 60 * 1000 - min * 60 * 1000 - sec * 1000);
        result.add(day);
        result.add(hour);
        result.add(min);
        result.add(sec);
        return result;
    }


    public static void main(String[] args) {
        System.out.println(DateUtils.nowDateString());
    }
}
