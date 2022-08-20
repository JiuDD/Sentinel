package com.alibaba.csp.sentinel.dashboard.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * description: 日期工具类
 * @author: JiuDongDong
 * date: 2017/12/13.
 */
public class DateUtil {

    /**
     * Description: 根据给定日期获取给定日期往后推n天的时间
     *              返回时、分、秒设置为0的日期
     * @author: JiuDongDong
     * @param date 给定日期
     * @return: java.util.Date 给定日期的开始时间
     * date: 2017/12/14 11:27
     */
    public static synchronized Date getFutureMountDaysStart(Date date, Integer n) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.set(year, month, day + n, 0, 0, 0);
        Date time = calendar.getTime();
        return time;
    }

    /**
     * Description: 根据给定日期获取给定日期往后推n小时的时间
     *              返回分、秒设置为0的日期
     * @author: JiuDongDong
     * @param date 给定日期
     * @return: java.util.Date 给定日期的开始时间
     * date: 2017/12/14 11:27
     */
    public static synchronized Date getFutureMountHoursStart(Date date, Integer n) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        calendar.set(year, month, day, hour + n, 0, 0);
        Date time = calendar.getTime();
        return time;
    }

    /**
     * Description: 用于根据输入的日期格式获取SimpleDateFormat对象
     * @author: JiuDongDong
     * @param pattern 输入的日期格式
     * @return: java.text.SimpleDateFormat
     * date: 2017/12/18 20:22:31
     */
    public static synchronized SimpleDateFormat getSimpleDateFormat(String pattern) {
        SimpleDateFormat simpleDateFormat;
        try {
            simpleDateFormat = new SimpleDateFormat(pattern);
        } catch (Exception e) {
            return null;
        }
        return simpleDateFormat;
    }

}
