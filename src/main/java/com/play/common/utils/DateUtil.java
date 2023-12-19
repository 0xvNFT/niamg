package com.play.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

public class DateUtil {

	public static final String DATETIME_FORMAT_STR = "yyyy-MM-dd HH:mm:ss";
	public static final String DATETIME_FORMAT_STR_1 = "yyyy-MM-dd HH:mm";
	public static final String DATE_FORMAT_STR = "yyyy-MM-dd";
	public static final String DATE_YEAR_MONTH_FORMAT = "yyyyMM";
	public static final String YMD_FORMAT = "yyyyMMdd";
	public static final String TIME_FORMAT = "HH:mm:ss";

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
			e.printStackTrace();
			return null;
		}
	}

	public static Date toDateMDtime(String time) {
		Date UTCDate;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		String localTimeStr = null;
		try {
			UTCDate = format.parse(time);
			format.setTimeZone(TimeZone.getTimeZone("GMT-4"));
			localTimeStr = format.format(UTCDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return DateUtil.parseDate(localTimeStr, DATETIME_FORMAT_STR);
	}

	/**
	 * YYYY-MM-DD HH:SS
	 * 
	 * @param dateStr
	 * @return
	 */
	public static Date toDatetime_1(String dateStr) {
		if (StringUtils.isEmpty(dateStr)) {
			return null;
		}

		try {
			SimpleDateFormat datetimeFormat = new SimpleDateFormat(DATETIME_FORMAT_STR_1);
			return datetimeFormat.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
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
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Date转 str yyyy-MM-dd HH:mm:ss
	 * 
	 * @param dateStr
	 * @return
	 */
	public static String toDatetimeStr(Date date) {
		if (date == null) {
			return "";
		}
		SimpleDateFormat datetimeFormat = new SimpleDateFormat(DATETIME_FORMAT_STR);
		return datetimeFormat.format(date);
	}

	/**
	 * Date转 str yyyy-MM-dd
	 * 
	 * @param dateStr
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
	 * @param a
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
	 * @param a
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
		String[] weekDays = { "周天", "周一", "周二", "周三", "周四", "周五", "周六" };
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

	public static String getDateInterval(Date endDate, Date startDate) {

		long nd = 1000 * 24 * 60 * 60;
		long nh = 1000 * 60 * 60;
		long nm = 1000 * 60;
		long ns = 1000;
		// long ns = 1000;
		// 获得两个时间的毫秒时间差异
		long diff = endDate.getTime() - startDate.getTime();
		// 计算差多少天
		long day = diff / nd;
		// 计算差多少小时
		long hour = diff % nd / nh;
		String hourStr = hour < 10 ? "0" + hour : String.valueOf(hour);
		// 计算差多少分钟
		long min = diff % nd % nh / nm;
		String minStr = min < 10 ? "0" + min : String.valueOf(min);
		// 计算差多少秒//输出结果
		 long sec = diff % nd % nh % nm / ns;
		String secStr = sec < 10 ? "0" + sec : String.valueOf(sec);
//		return day + "D " + hourStr + "H " + minStr + "M " + secStr+"S";
		return day + "D " + hourStr + "H " + minStr + "M ";
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
		long now = new Date().getTime();
		if (now - date.getTime() < 24 * 60 * 60 * 1000) {
			return true;
		}
		return false;
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
	 * @param aMask
	 *              输入字符串的格式
	 * @param str
	 *              一个按aMask格式排列的日期的字符串描述
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
	 * @param Date
	 *                 传入的时间
	 * @param dayIndex
	 *                 负数代表往后 证书往前
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
	 * 日期字符串转datetime，且是每天的第一时刻
	 * 
	 * @param startTime
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
	 * @param startTime
	 * @return
	 */
	public static Date validEndDateTime(String endDate) {
		return validEndDateTime(endDate, new Date());
	}

	/**
	 * 日期字符串转datetime，且是每天的第一时刻
	 * 
	 * @param startDate
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

	public static int getDiscrepantDays(Date dateStart, Date dateEnd) {
		return (int) ((dateEnd.getTime() - dateStart.getTime()) / 1000 / 60 / 60 / 24);
	}

	/**
	 * 取得当月天数
	 */
	public static int getCurrentMonthLastDay() {
		Calendar a = Calendar.getInstance();
		a.set(Calendar.DATE, 1);// 把日期设置为当月第一天
		a.roll(Calendar.DATE, -1);// 日期回滚一天，也就是最后一天
		int maxDate = a.get(Calendar.DATE);
		return maxDate;
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
			e.printStackTrace();
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

	/**
	 * 获取当前的时间戳
	 * 
	 * @return
	 */
	public static String timeStamp() {
		long time = System.currentTimeMillis();
		String t = String.valueOf(time / 1000);
		return t;
	}

	// 获得本周一0点时间
	public static Date getTimesWeekmorning() {
		Calendar cal = Calendar.getInstance();
		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		return cal.getTime();
	}

	// 获得本周日24点时间
	public static Date getTimesWeeknight() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(getTimesWeekmorning());
		cal.add(Calendar.DAY_OF_WEEK, 7);
		return cal.getTime();
	}

	public static String diffStr(Date endTime, Date startTime) {
		if (endTime == null) {
			return "结束时间为空";
		}
		if (startTime == null) {
			return "开始时间为空";
		}

		long diff = endTime.getTime() - startTime.getTime();
		long days = diff / (1000 * 60 * 60 * 24);
		diff = diff - days * (1000 * 60 * 60 * 24);
		long hours = diff / (1000 * 60 * 60);
		diff = diff - hours * (1000 * 60 * 60);
		long minutes = diff / (1000 * 60);
		diff = diff - minutes * (1000 * 60);
		long seconds = diff / 1000;
		diff = diff - seconds * 1000;
		StringBuilder sb = new StringBuilder();
		if (days > 0) {
			sb.append(days).append("天");
		}
		if (hours > 0) {
			sb.append(hours).append("小时");
		}
		if (minutes > 0) {
			sb.append(minutes).append("分");
		}
		if (seconds > 0) {
			sb.append(seconds).append("秒");
		}
		if (diff > 0) {
			sb.append(diff).append("毫秒");
		}
		return sb.toString();
	}

	/**
	 * 获取
	 * @param date
	 * @param datetimeFormat
	 * @param timezone
	 * @return
	 */
	public static String getTzDateTimeStr(Date date, String timezone, String datetimeFormat) {
		if (null == datetimeFormat || "".equals(datetimeFormat)) {
			datetimeFormat = DATETIME_FORMAT_STR;
		}
		// 设置日期格式
		SimpleDateFormat formatter = new SimpleDateFormat(datetimeFormat);
		// 设置时区
		formatter.setTimeZone(TimeZone.getTimeZone(timezone));

		return formatter.format(date);
	}

	public static String getTzDateTimeStr(Date date, String timezone) {
		return getTzDateTimeStr(date, timezone, DATETIME_FORMAT_STR);
	}

	public static Date getTzDateTime(String dateTimeStr, String timezone, String datetimeFormat) {
		if (null == dateTimeStr || "".equals(dateTimeStr)) {
			return null;
		}
		if (null == datetimeFormat || "".equals(datetimeFormat)) {
			datetimeFormat = DATETIME_FORMAT_STR;
		}
		// 设置日期格式
		SimpleDateFormat formatter = new SimpleDateFormat(datetimeFormat);
		// 设置时区
		formatter.setTimeZone(TimeZone.getTimeZone(timezone));

		try {
			return formatter.parse(dateTimeStr);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}

	}
	public static Date getTzDateTime(String dateTimeStr, String timezone) {
		return getTzDateTime(dateTimeStr, timezone, DATETIME_FORMAT_STR);
	}

	public static void main(String[] args) {
		// Date d = DateUtil.toDate("2015-08-26 15:00:00");
		// System.out.println(DateUtil.isToday(d));
		// System.out.println(afterMonth(d, 8));
		// System.out.println();
		System.out.println(afterMonth(new Date(), -3));
	}
}
