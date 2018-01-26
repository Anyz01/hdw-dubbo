package com.hdw.dubbo.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @description Date工具类
 * @author TuMinglong
 * @date 2016-11-28
 * @version v1.0
 *
 */
public class DateUtils {
	/**
	 * 将String类型时间转换成Date类型
	 * 
	 * @param dateStr
	 * @return
	 */
	public static Date toDate(String dateStr) {
		if (dateStr == null) {
			return null;
		}
		try {
			if (dateStr.length() == 10) {
				return new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
			} else if (dateStr.length() == 16) {
				return new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(dateStr);
			} else if (dateStr.length() == 19) {
				return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateStr);
			} else {
				return new Date(Long.parseLong(dateStr));
			}
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

	public static Date toDate(String dateStr, String format) {
		try {
			return new SimpleDateFormat(format).parse(dateStr);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 讲时间类型转换成String类型
	 * 
	 * @param date
	 *            时间
	 * @param format
	 *            yyyy-MM-dd yyyy-MM-dd HH:mm yyyy-MM-dd HH:mm:ss yyyyMMddHHmmss
	 * @return
	 */
	public static String format(Date date, String format) {
		try {
			return new SimpleDateFormat(format).format(date);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static String format(Date date) {
		return format(date, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 得到几天前的时间
	 * 
	 * @param d
	 * @param day
	 * @return
	 */
	public static Date getDateBefore(Date d, int day) {
		Calendar now = Calendar.getInstance();
		now.setTime(d);
		now.set(Calendar.DATE, now.get(Calendar.DATE) - day);
		return now.getTime();
	}

	/**
	 * 得到几天后的时间
	 * 
	 * @param d
	 * @param day
	 * @return
	 */
	public static Date getDateAfter(Date d, int day) {
		Calendar now = Calendar.getInstance();
		now.setTime(d);
		now.set(Calendar.DATE, now.get(Calendar.DATE) + day);
		return now.getTime();
	}

	/**
	 * 获取每个月第一天和最后一天
	 * 
	 * @param i
	 *            从0开始倒序
	 * @return ["2017-05-01","2017-05-31",2017-05,5]
	 */
	public static String[] getFirstandLastDate(int i) {
		String[] date = new String[4];
		Calendar cal_1 = Calendar.getInstance();// 获取当前日期

		cal_1.add(Calendar.MONTH, i);
		int month = cal_1.get(Calendar.MONTH) + 1;// 获取当前月份
		cal_1.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天

		String firstDay = format(cal_1.getTime(), "yyyy-MM-dd ");
		date[0] = firstDay;

		Calendar cal_2 = Calendar.getInstance();
		cal_2.add(Calendar.MONTH, i + 1);
		cal_2.set(Calendar.DAY_OF_MONTH, 0);// 设置为1号,当前日期既为本月第一天
		String lastDay = format(cal_2.getTime(), "yyyy-MM-dd");
		date[1] = lastDay;
		date[2] = format(cal_2.getTime(), "yyyy-MM");
		date[3] = Integer.toString(month);

		return date;

	}

	/**
	 * 获取上月时间
	 * 
	 * @return
	 */
	public static Date lastMonth() {
		Calendar calendar = Calendar.getInstance();
		// 取得上一个时间
		calendar.set(Calendar.MONDAY, calendar.get(Calendar.MONDAY) - 1);
		return calendar.getTime();
	}

	/**
	 * 获取上上月时间
	 * 
	 * @return
	 */
	public static Date aggravateLastMonth() {
		Calendar calendar = Calendar.getInstance();
		// 取得上一个时间
		calendar.set(Calendar.MONDAY, calendar.get(Calendar.MONDAY) - 2);
		return calendar.getTime();
	}

}
