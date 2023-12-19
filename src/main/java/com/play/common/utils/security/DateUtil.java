package com.play.common.utils.security;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;

public class DateUtil {

	public static final String DATETIME_FORMAT_STR = "yyyy-MM-dd HH:mm:ss";
	public static final String DATE_FORMAT_STR = "yyyy-MM-dd";
	public static final String DATE_YEAR_MONTH_FORMAT = "yyyyMM";
	public static final String YMD_FORMAT = "yyyyMMdd";
	public static final String HMS_FORMAT = "hhMMss";
	public static final String TIME_FORMAT = "HH:mm:ss";
	public static final String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

	// public static final SimpleDateFormat datetimeFormat = new
	// SimpleDateFormat(DATETIME_FORMAT_STR);
	// public static final SimpleDateFormat dateFormat = new
	// SimpleDateFormat(DATE_FORMAT_STR);
	// public static final SimpleDateFormat monthFormat = new
	// SimpleDateFormat(DATE_YEAR_MONTH_FORMAT);
	
	/**
	 * 根据参数格式化时间
	 * @param time 时间
	 * @param format 格式化类型
	 * @return
	 */
	public static String customFormatDate(Date time,String format) {
		try {
			SimpleDateFormat datetimeFormat = new SimpleDateFormat(format);
			return datetimeFormat.format(time);
		} catch (Exception e) {

		}
		return null;
	}
	public static Date toDateMDtime(String time) {
		Date UTCDate;  
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		  
		String localTimeStr = null ;  
		try {  
		UTCDate = format.parse(time);  
		format.setTimeZone(TimeZone.getTimeZone("GMT-4")) ;  
		localTimeStr = format.format(UTCDate) ;  
		} catch (ParseException e) {  

		}  
		  
		return DateUtil.parseDate(localTimeStr, DATETIME_FORMAT_STR);  
	}

	/**
	 * str转Date yyyy-MM-dd HH:mm:ss
	 * 
	 * @param dateStr
	 * @return
	 */
	public static Date toDatetime(String dateStr) {
		if (StringUtils.isEmpty(dateStr)) {
			return null;
		}

		try {
			SimpleDateFormat datetimeFormat = new SimpleDateFormat(DATETIME_FORMAT_STR);
			return datetimeFormat.parse(dateStr);
		} catch (ParseException e) {

			return null;
		}
	}

	/**
	 * str转Date
	 *
	 * @param dateStr
	 * @return
	 */
	public static Date toDatetime(String dateStr, String format) {
		if (StringUtils.isEmpty(dateStr)) {
			return null;
		}

		try {
			SimpleDateFormat datetimeFormat = new SimpleDateFormat(format);
			return datetimeFormat.parse(dateStr);
		} catch (ParseException e) {

			return null;
		}
	}

	/**
	 * str转Date yyyy-MM-dd
	 * 
	 * @param dateStr
	 * @return
	 */
	public static Date toDate(String dateStr) {

		if (StringUtils.isEmpty(dateStr)) {
			return null;
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_STR);
		try {
			return dateFormat.parse(dateStr);
		} catch (ParseException e) {

			return null;
		}
	}


	/**
	 * Date转 str yyyy-MM-dd HH:mm:ss
	 * 
	 * @param
	 * @return
	 */
	public static String toDatetimeStr(Date date) {
		SimpleDateFormat datetimeFormat = new SimpleDateFormat(DATETIME_FORMAT_STR);
		if(date == null) {
			return null;
		}
		return datetimeFormat.format(date);
	}

	/**
	 * Date转 str yyyy-MM-dd
	 * 
	 * @param
	 * @return
	 */
	public static String toDateStr(Date date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_STR);
		return dateFormat.format(date);
	}

	/**
	 * date转成时间 HH:mm:ss
	 * 
	 * @param date
	 * @return
	 */
	public static String toTimeStr(Date date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(TIME_FORMAT);
		return dateFormat.format(date);
	}

	/**
	 * 获取格式化后的当前日期字符串
	 * 
	 * @return yyyy
	 */
	public static String getCurrentYear() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		return sdf.format(new Date());
	}

	/**
	 * 获取格式化后的当前日期字符串
	 * 
	 * @return yyyy-MM-dd
	 */
	public static String getCurrentDate() {
		SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_STR);
		return dateFormat.format(new Date());
	}

	/**
	 * 获取指定格式的当前日期字符串
	 * 
	 * @return 自定义日期
	 */
	public static String getCurrentDate(String formatString) {
		SimpleDateFormat sdf = new SimpleDateFormat(formatString);
		return sdf.format(new Date());
	}

	/**
	 * 获取格式化后的当前时间字符串
	 * 
	 * @return yyyy-MM-dd HH:mm:ss
	 */
	public static String getCurrentTime() {
		SimpleDateFormat datetimeFormat = new SimpleDateFormat(DATETIME_FORMAT_STR);
		return datetimeFormat.format(new Date());
	}

	/**
	 * 获取指定格式的当前时间字符串
	 * 
	 * @return 自定义时间
	 */
	public static String getCurrentTime(String formatString) {
		SimpleDateFormat sdf = new SimpleDateFormat(formatString, Locale.US);
		return sdf.format(new Date());
	}

	/**
	 * 获取指定日期的指定格式的时间字符串
	 * 
	 * @return 自定义日期
	 */
	public static String getCurrentTime(String formatString, Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat(formatString, Locale.US);
		return sdf.format(date);
	}

	/**
	 * 将传入的日期向前（或向后）滚动|amount|天
	 * 
	 * @param date
	 * @param
	 * @return yyyy-MM-dd
	 */
	public static String getRollDay(Date date, int amount) {
		Date newDate = DateUtils.addDays(date, amount);
		SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_STR);
		return dateFormat.format(newDate);
	}

	/**
	 * 将传入的日期向前（或向后）滚动|amount|天
	 * 
	 * @param date
	 * @param
	 * @return yyyy-MM-dd HH:ss:mm
	 */
	public static String getRollDayTime(Date date, int amount) {
		Date newDate = DateUtils.addDays(date, amount);
		SimpleDateFormat datetimeFormat = new SimpleDateFormat(DATETIME_FORMAT_STR);
		return datetimeFormat.format(newDate);
	}

	/**
	 * 获取当前日期是星期几<br>
	 * 
	 * @param dt
	 * @return 当前日期是星期几
	 */
	public static String getWeekOfDate(Date dt) {
		String[] weekDays = { "星期天", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
		Calendar cal = Calendar.getInstance();
		cal.setTime(dt);
		int day = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (day < 0) {
			day = 0;
		}
		return weekDays[day];
	}

	/**
	 * 取得两个日期相差的天数
	 * 
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	public static int getDayMargin(Date beginDate, Date endDate) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(beginDate);
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 1,
				0, 0);
		long beginTime = calendar.getTime().getTime();
		calendar.setTime(endDate);
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 1,
				0, 0);
		long endTime = calendar.getTime().getTime();
		return (int) ((endTime - beginTime) / (1000 * 60 * 60 * 24));
	}

	/**
	 * 取得两个日期相差的天数
	 * 
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	public static int getDayMargin(String beginDate, String endDate) {

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(DateUtil.toDate(beginDate));
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 1,
				0, 0);
		long beginTime = calendar.getTime().getTime();
		calendar.setTime(DateUtil.toDate(endDate));
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 1,
				0, 0);
		long endTime = calendar.getTime().getTime();
		return (int) ((endTime - beginTime) / (1000 * 60 * 60 * 24));
	}

	/**
	 * 获取当前所在的年月
	 * 
	 * @return
	 */
	public static String getYearMonth() {

		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		return String.valueOf(year) + (month < 10 ? "0" + month : month);
	}

	/**
	 * yyyy-MM 转yyyyMM格式
	 * 
	 * @return
	 */
	public static String getYearMonth(Date strDate) {
		if (strDate == null) {
			strDate = new Date();
		}
		SimpleDateFormat monthFormat = new SimpleDateFormat(DATE_YEAR_MONTH_FORMAT);
		return monthFormat.format(strDate);
	}

	public static boolean isToday(Date date) {
		if (date == null) {
			return false;
		}
		Calendar now = Calendar.getInstance();
		Calendar d = Calendar.getInstance();
		d.setTime(date);
		return now.get(Calendar.YEAR) == d.get(Calendar.YEAR) && now.get(Calendar.MONTH) == d.get(Calendar.MONTH)
				&& now.get(Calendar.DAY_OF_MONTH) == d.get(Calendar.DAY_OF_MONTH);
	}

	public static boolean isToYear(String year) {
		if (StringUtils.isEmpty(year)) {
			return false;
		}
		Calendar now = Calendar.getInstance();
		return StringUtils.equals(year, String.valueOf(now.get(Calendar.YEAR)));
	}

	public static Date getTomorrow() {
		return DateUtils.addDays(new Date(), 1);
		// Calendar calendar = Calendar.getInstance();
		// calendar.setTime();
		// calendar.add(Calendar.DATE, 1);// 把日期往后增加一天.整数往后推,负数往前移动
		// return calendar.getTime();
	}

	public static Date getTomorrow(Date curDate) {
		if (curDate == null) {
			return null;
		}
		return DateUtils.addDays(curDate, 1);
	}

	public static Date getTomorrow(String curDate) {
		return getTomorrow(toDate(curDate));
	}

	public static String formatDate(Date date, String pattern) {
		if (date != null) {
			SimpleDateFormat df = new SimpleDateFormat(pattern);
			return df.format(date);
		}
		return "";
	}

	// public static Date addDays(Date date,int days){
	// return DateUtils.addDays(date, amount)
	// }

	// public static Date addTimeOfCalendarType(Date date, int value, int
	// dateType) {
	// Calendar.DAY_OF_MONTH
	// if (date == null) {
	// return null;
	// }
	// calendar.setTime(date);
	// calendar.add(dateType, value);
	// return calendar.getTime();
	// }

	/**
	 * 按照日期格式，将字符串解析为日期对象
	 * 
	 * @param str
	 *            一个按aMask格式排列的日期的字符串描述
	 * @return Date 对象
	 */
	public static Date parseDate(String str, String pattern) {
		try {
			SimpleDateFormat df = new SimpleDateFormat(pattern);
			return df.parse(str);
		} catch (Exception pe) {
			return null;
		}
	}

	/**
	 * 获得dayIndex之前(后)的日期
	 * 
	 * @param date
	 *            传入的时间
	 * @param dayIndex
	 *            负数代表往后 证书往前
	 * @return
	 */
	public static String afterMonth(Date date, int dayIndex) {
		try {
			return toDatetimeStr(DateUtils.addMonths(date, dayIndex));
		} catch (Exception e) {
			return null;
		}

	}

	public static boolean afterByPattern(String str, String pattern, Date date) {
		if (StringUtils.isEmpty(str) || StringUtils.isEmpty(pattern) || date == null) {
			return false;
		}
		SimpleDateFormat datetimeFormat = new SimpleDateFormat(pattern);
		Date strDate = null;
		try {
			strDate = datetimeFormat.parse(str);
			date = datetimeFormat.parse(datetimeFormat.format(date));
			return date.after(strDate);
		} catch (ParseException e) {
			return false;
		}
	}

	public static boolean beforeByPattern(String str, String pattern, Date date) {
		if (StringUtils.isEmpty(str) || StringUtils.isEmpty(pattern) || date == null) {
			return false;
		}
		SimpleDateFormat datetimeFormat = new SimpleDateFormat(pattern);
		Date strDate = null;
		try {
			strDate = datetimeFormat.parse(str);
			date = datetimeFormat.parse(datetimeFormat.format(date));
			return date.before(strDate);
		} catch (ParseException e) {
			return false;
		}
	}

	/**
	 * 每天的第一时刻
	 * 
	 * @param date
	 * @return
	 */
	public static Date dayFirstTime(Date date, int day) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DAY_OF_MONTH, day);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTime();
	}

	/**
	 * 每天的最后时刻
	 *
	 * @param date
	 * @return
	 */
	public static Date dayEndTime(Date date, int day) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DAY_OF_MONTH, day);
		c.set(Calendar.HOUR_OF_DAY, 23);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.SECOND, 59);
		c.set(Calendar.MILLISECOND, 999);
		return c.getTime();
	}

	/**
	 * 该月第几天
	 * 
	 * @param date
	 * @return
	 */
	public static Date monthOfDays(Date date, int day) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.DAY_OF_MONTH, day);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTime();
	}
	
	/**
	 * 是否本月第一天
	 * @return
	 */
	public static boolean isFirstDayOfMonth() {
        Calendar c = Calendar.getInstance();
        int date = c.get(Calendar.DATE); 
        if(date == 1)
        	return true;
        else
		return false;
	}

	/**
	 * 日期字符串转datetime，且是每天的第一时刻
	 * 
	 * @param startDate
	 * @return
	 */
	public static Date validStartDateTime(String startDate) {
		return validStartDateTime(startDate, new Date());
	}

	/**
	 * 日期字符串转datetime，且是每天的第一时刻
	 * 
	 * @param startDate
	 * @param defaultDate
	 * @return
	 */
	public static Date validStartDateTime(String startDate, Date defaultDate) {
		if (StringUtils.isEmpty(startDate)) {
			if (defaultDate != null) {
				return dayFirstTime(defaultDate, 0);
			}
		} else {
			return dayFirstTime(toDate(startDate), 0);
		}
		return null;
	}

	/**
	 * 日期字符串转datetime，且是第二天的第一时刻
	 * 
	 * @param endDate
	 * @return
	 */
	public static Date validEndDateTime(String endDate) {
		return validEndDateTime(endDate, new Date());
	}

	/**
	 * 日期字符串转datetime，且是每天的第一时刻
	 * 
	 * @param endDate
	 * @param defaultDate
	 * @return
	 */
	public static Date validEndDateTime(String endDate, Date defaultDate) {
		if (StringUtils.isEmpty(endDate)) {
			if (defaultDate != null) {
				return dayFirstTime(defaultDate, 1);
			}
		} else {
			return dayFirstTime(toDate(endDate), 1);
		}
		return null;
	}

	public static int getCurMonthByDate(Date date) {
		if (date == null) {
			return -1;
		}
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.MONTH);
	}

	public static int getCurYearByDate(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.YEAR);
	}

	public static Date parseDate(String str) {
		if (str == null) {
			return null;
		}
		str = str.trim();
		if (str.matches("[\\d]{4}-[\\d]{2}-[\\d]{2}")) {
			SimpleDateFormat df = new SimpleDateFormat(DATE_FORMAT_STR);
			try {
				return df.parse(str);
			} catch (ParseException e) {
			}
		}
		if (str.matches("[\\d]{4}-[\\d][\\d]-[\\d][\\d] [\\d][\\d]:[\\d][\\d]:[\\d][\\d]")) {
			SimpleDateFormat df = new SimpleDateFormat(DATETIME_FORMAT_STR);
			try {
				return df.parse(str);
			} catch (ParseException e) {
			}
		}
		return null;
	}

	public static String getSuffixBySecond(Calendar c) {
		return c.get(Calendar.MINUTE) % 10 + "_" + c.get(Calendar.SECOND) / 30;
	}
	/**
	 * 生成随机时间
	 * 
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	public static Date randomDate(String beginDate, String endDate) {
		try {
			SimpleDateFormat format = new SimpleDateFormat(DATETIME_FORMAT_STR);
			Date start = format.parse(beginDate);// 构造开始日期
			Date end = format.parse(endDate);// 构造结束日期
			// getTime()表示返回自 1970 年 1 月 1 日 00:00:00 GMT 以来此 Date 对象表示的毫秒数。
			if (start.getTime() >= end.getTime()) {
				return null;
			}
			long date = random(start.getTime(), end.getTime());
			return new Date(date);
		} catch (Exception e) {

		}
		return null;
	}
	private static long random(long begin, long end) {
		long rtn = begin + (long) (Math.random() * (end - begin));
		// 如果返回的是开始时间和结束时间，则递归调用本函数查找随机值
		if (rtn == begin || rtn == end) {
			return random(begin, end);
		}
		return rtn;
	}

	public static Date getBeginDayOfLastWeek() {
		Date date = new Date();
		if (date == null) {
			return null;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int dayofweek = cal.get(Calendar.DAY_OF_WEEK);
		if (dayofweek == 1) {
			dayofweek += 7;
		}
		cal.add(Calendar.DAY_OF_MONTH, 2 - dayofweek - 7);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

	public static Date getEndDayOfLastWeek(){
		Calendar cal = Calendar.getInstance();
		cal.setTime(getBeginDayOfLastWeek());
		cal.add(Calendar.DAY_OF_MONTH, 7);
		cal.set(Calendar.HOUR_OF_DAY,23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		return cal.getTime();
	}

	public static Date getBeginDayOfWeek() {
		Date date = new Date();
		if (date == null) {
			return null;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int dayofweek = cal.get(Calendar.DAY_OF_WEEK);
		if (dayofweek == 1) {
			dayofweek += 7;
		}
		cal.add(Calendar.DAY_OF_MONTH, 2 - dayofweek);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

	public static Date getEndDayOfWeek(){
		Calendar cal = Calendar.getInstance();
		cal.setTime(getBeginDayOfWeek());
		cal.add(Calendar.DAY_OF_MONTH,6);
		cal.set(Calendar.HOUR_OF_DAY,23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		return cal.getTime();
	}

    /**
     * 获取当前月的第一天
     *
     * @return date
     */
    public static String getFirstDayOfMonth() {
        Calendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTime(new Date());
        gregorianCalendar.set(Calendar.DAY_OF_MONTH, 1);
        return toDateStr(gregorianCalendar.getTime());
    }

    /**
     * 获取当前月的最后一天
     *
     * @return
     */
    public static String getLastDayOfMonth() {
        Calendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTime(new Date());
        gregorianCalendar.set(Calendar.DAY_OF_MONTH, 1);
        gregorianCalendar.add(Calendar.MONTH, 1);
        gregorianCalendar.add(Calendar.DAY_OF_MONTH, -1);
        return toDateStr(gregorianCalendar.getTime());
    }

    /**
     * 根据month获取月份的第一天
     *
     * @return
     */
	public static String getFirstDayOfMonth(int month) {
		Calendar gregorianCalendar = new GregorianCalendar();
		gregorianCalendar.setTime(new Date());
		gregorianCalendar.set(Calendar.DAY_OF_MONTH, 1);
		gregorianCalendar.add(Calendar.MONTH, month);
		return toDateStr(gregorianCalendar.getTime());
	}


	public static void main(String[] args) {
		System.out.println(getBeginDayOfWeek());
		LocalDate today = LocalDate.now();
		LocalDate toweekMonday = today.with(DayOfWeek.MONDAY);
		System.out.println(toweekMonday);
        System.out.println(DateUtil.parseDate(DateUtil.getLastDayOfMonth() + " 00:00:00",DateUtil.DATETIME_FORMAT_STR));
        System.out.println(getFirstDayOfMonth(-1));
	}
}
