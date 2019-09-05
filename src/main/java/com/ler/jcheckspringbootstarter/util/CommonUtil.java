package com.ler.jcheckspringbootstarter.util;

import com.ler.jcheckspringbootstarter.config.UserFriendlyException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.chrono.ChronoZonedDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

/**
 * @author lww
 * @date 2019-09-04 16:23
 */
public class CommonUtil {
	/**
	 * 日期转为字符串
	 */
	public static String date2String(Date date) {

		if (date == null) {
			return null;
		}

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return simpleDateFormat.format(date);
	}

	/**
	 * 日期转为字符串
	 */
	public static String date2SimpleString(Date date) {

		if (date == null) {
			return null;
		}

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		return simpleDateFormat.format(date);
	}

	/**
	 * 字符串转为日期
	 */
	public static Date getStringDate(String dateString) {
		Date stopDate = null;
		try {
			if (StringUtils.isNotBlank(dateString)) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				stopDate = sdf.parse(dateString);
			}
		} catch (ParseException e) {
			Assert.isTrue(false, "日期格式错误！");
		}
		return stopDate;
	}

	public static Integer getYear(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.YEAR);
	}

	public static Integer getMonth(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.MONTH) + 1;
	}

	public static Integer getDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.DAY_OF_MONTH);
	}

	public static String deleteHMS(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(date);
	}

	public static Date deleteHMSD(Date date) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String format = sdf.format(date);
			return sdf.parse(format);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 是否比今天早30天 true是 false 不是 等于30天也是 false
	 *
	 * @param start
	 * @return
	 */
	public static Boolean isBefore30(Date start) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.DAY_OF_MONTH, -30);
		Date nowDate_30 = deleteHMSD(calendar.getTime());
		Date startDate = deleteHMSD(start);
		Assert.isTrue(nowDate_30 != null, "！");
		Assert.isTrue(startDate != null, "开始日期解析错误！");
		return nowDate_30.compareTo(startDate) > 0;
	}

	/**
	 * 与当前月差几个月， 正数 传入日期比当前日期大几个月，负数，传入日期比当前日期小几个月
	 */
	public static Integer monthBetweenNow(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		Calendar n = Calendar.getInstance();
		n.setTime(new Date());
		return (c.get(Calendar.YEAR) * 12 + c.get(Calendar.MONTH)) - (n.get(Calendar.YEAR) * 12 + n.get(Calendar.MONTH));
	}

	public static Integer betweenMonths(Date d1, Date d2) {
		if (d1.compareTo(d2) > 0) {
			return 0;
		}
		Calendar c = Calendar.getInstance();
		c.setTime(d1);
		Calendar n = Calendar.getInstance();
		n.setTime(d2);
		int i = (c.get(Calendar.YEAR) * 12 + c.get(Calendar.MONTH)) - (n.get(Calendar.YEAR) * 12 + n.get(Calendar.MONTH));
		return i;
	}


	public static Integer betweenDays(Date d1, Date d2) {
		if (d1.compareTo(d2) > 0) {
			return 0;
		}
		Calendar instance1 = Calendar.getInstance();
		Calendar instance2 = Calendar.getInstance();
		instance1.setTime(deleteHMSD(d1));
		instance2.setTime(deleteHMSD(d2));
		Long time1 = instance1.getTimeInMillis();
		Long time2 = instance2.getTimeInMillis();
		Boolean res = (time2 - time1) % (1000 * 3600 * 24) == 0;
		Long betweenDays = res ? (time2 - time1) / (1000 * 3600 * 24) : (time2 - time1) / (1000 * 3600 * 24) + 1;
		return betweenDays.intValue() + 1;
	}

	public static Integer betweenYears(Date d1, Date d2) {
		if (d1.compareTo(d2) > 0) {
			return 0;
		}
		Calendar instance1 = Calendar.getInstance();
		Calendar instance2 = Calendar.getInstance();
		instance1.setTime(d1);
		instance2.setTime(d2);
		int i = instance1.get(Calendar.YEAR) - instance2.get(Calendar.YEAR);
		return i;
	}

	/**
	 * 传入开始日期和结束日期，获取这两个日期所在年的总天数，
	 * 如2013-2014，就是 两个自然年所有天数，2013-2013就是一个自然年所有时间
	 */
	public static Integer getAllYearDay(Date d1, Date d2) {
		Calendar c = Calendar.getInstance();
		c.setTime(d1);
		Calendar n = Calendar.getInstance();
		n.setTime(d2);
		int y1 = c.get(Calendar.YEAR);
		int y2 = n.get(Calendar.YEAR);
		if (y1 == y2) {
			return runYear(y1);
		} else {
			if (y1 > y2) {
				int temp = y1;
				y1 = y2;
				y2 = temp;
			}
			int sumDay = 0;
			for (int i = y1; i <= y2; i++) {
				sumDay += runYear(y1);
			}
			return sumDay;
		}
	}

	private static Integer runYear(int y1) {
		if (y1 % 4 == 0 || y1 % 400 == 0) {
			return 366;
		} else {
			return 365;
		}
	}

	/**
	 * 两个日期之间的月，带小数
	 *
	 * @param d1 前一个
	 * @param d2 后一个
	 * @return 月数
	 */
	public static Double getBetweenMonth(Date d1, Date d2) {
		if (d2.after(d1)) {
			Date temp = d1;
			d1 = d2;
			d2 = temp;
		}
		return getSmallMonth(d1) - getSmallMonth(d2);
	}

	/**
	 * 获取带精度月份
	 */
	public static Double getSmallMonth(Date d) {
		Calendar instance = Calendar.getInstance();
		instance.setTime(d);

		List<Integer> l28 = new ArrayList<>();
		l28.add(2);
		List<Integer> l30 = new ArrayList<>();
		l30.add(4);
		l30.add(6);
		l30.add(9);
		l30.add(11);
		List<Integer> l31 = new ArrayList<>();
		l31.add(1);
		l31.add(3);
		l31.add(5);
		l31.add(7);
		l31.add(8);
		l31.add(10);
		l31.add(12);
		Boolean run = instance.get(Calendar.YEAR) % 4 == 0 || instance.get(Calendar.YEAR) % 400 == 0;

		Integer month = instance.get(Calendar.MONTH) + 1;
		Integer day = instance.get(Calendar.DAY_OF_MONTH);
		if (l31.contains(month)) {
			double v = day / 31.0;
			return month + v;
		} else if (l30.contains(month)) {
			double v = day / 30.0;
			return month + v;
		} else if (l28.contains(month) && run) {
			double v = day / 29.0;
			return month + v;
		} else if (l28.contains(month) && !run) {
			double v = day / 28.0;
			return month + v;
		} else {
			return null;
		}
	}


	/**
	 * 获取月份自然月天数
	 */
	public static Integer getMonthDay(Date d) {
		Calendar instance = Calendar.getInstance();
		instance.setTime(d);

		List<Integer> l28 = new ArrayList<>();
		l28.add(2);
		List<Integer> l30 = new ArrayList<>();
		l30.add(4);
		l30.add(6);
		l30.add(9);
		l30.add(11);
		List<Integer> l31 = new ArrayList<>();
		l31.add(1);
		l31.add(3);
		l31.add(5);
		l31.add(7);
		l31.add(8);
		l31.add(10);
		l31.add(12);
		Boolean run = instance.get(Calendar.YEAR) % 4 == 0 || instance.get(Calendar.YEAR) % 400 == 0;

		Integer month = instance.get(Calendar.MONTH) + 1;
		if (l31.contains(month)) {
			return 31;
		} else if (l30.contains(month)) {
			return 30;
		} else if (l28.contains(month) && run) {
			return 29;
		} else if (l28.contains(month) && !run) {
			return 28;
		} else {
			return null;
		}
	}

	/**
	 * 获取指定月份最后一天日期
	 */
	public static Date getLastDay(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int i = c.get(Calendar.MONTH) + 1;
		String dateString = c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH) + 1) + "-";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List<Integer> l28 = new ArrayList<>();
		List<Integer> l30 = new ArrayList<>();
		List<Integer> l31 = new ArrayList<>();
		Boolean run = (c.get(Calendar.YEAR) % 4 == 0) || (c.get(Calendar.YEAR) % 400 == 0);
		l28.add(2);
		l30.add(4);
		l30.add(9);
		l30.add(6);
		l30.add(11);
		l31.add(1);
		l31.add(3);
		l31.add(5);
		l31.add(7);
		l31.add(8);
		l31.add(10);
		l31.add(12);
		try {
			if (l28.contains(i)) {
				if (run) {
					dateString = dateString + "29";
				} else {
					dateString = dateString + "28";
				}
			} else if (l30.contains(i)) {
				dateString = dateString + "30";
			} else if (l31.contains(i)) {
				dateString = dateString + "31";
			}
			return sdf.parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取指定月份第一天日期
	 */
	public static Date getFirstDay(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		try {
			String dateString = c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH) + 1) + "-" + "01";
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			return sdf.parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Date getYearFirstDay(Date date) {
		try {
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			String dateString = c.get(Calendar.YEAR) + "-01-01";
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			return sdf.parse(dateString);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Date getYearLastDay(Date date) {
		try {
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			String dateString = c.get(Calendar.YEAR) + "-12-31";
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			return sdf.parse(dateString);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}


	/**
	 * 整个月
	 */
	public static Boolean fullMonth(Date start, Date end) {
		if (end == null) {
			return true;
		}
		Calendar sc = Calendar.getInstance();
		sc.setTime(start);
		sc.add(Calendar.MONTH, 1);
		sc.add(Calendar.DAY_OF_MONTH, -1);
		Calendar ec = Calendar.getInstance();
		ec.setTime(end);
		return CommonUtil.deleteHMS(sc.getTime()).equalsIgnoreCase(CommonUtil.deleteHMS(ec.getTime()));
	}

	/**
	 * 整年
	 */
	public static Boolean fullYear(Date start, Date end) {
		if (end == null) {
			return true;
		}
		Calendar sc = Calendar.getInstance();
		sc.setTime(start);
		sc.add(Calendar.YEAR, 1);
		sc.add(Calendar.DAY_OF_MONTH, -1);
		Calendar ec = Calendar.getInstance();
		ec.setTime(end);
		return CommonUtil.deleteHMS(sc.getTime()).equalsIgnoreCase(CommonUtil.deleteHMS(ec.getTime()));
	}

	public static Date addDate(Date start, Integer value, Integer filed) {

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(start);
		if (filed == Calendar.YEAR) {
			calendar.add(Calendar.YEAR, value);
			calendar.add(Calendar.DAY_OF_MONTH, -1);
		} else if (filed == Calendar.MONTH) {
			calendar.add(Calendar.MONTH, value);
			calendar.add(Calendar.DAY_OF_MONTH, -1);
		} else if (filed == Calendar.DAY_OF_MONTH) {
			calendar.add(Calendar.DAY_OF_MONTH, value);
		} else if (filed == Calendar.HOUR) {
			calendar.add(Calendar.HOUR, value);
		} else if (filed == Calendar.MINUTE) {
			calendar.add(Calendar.MINUTE, value);
		} else if (filed == Calendar.SECOND) {
			calendar.add(Calendar.SECOND, value);
		} else {
			Assert.isTrue(false, "不支持的类型！");
		}
		return calendar.getTime();
	}

	public static boolean bigThan(Date startDate) {
		return startDate.compareTo(CommonUtil.deleteHMSD(new Date())) >= 0;
	}

	public static Boolean dayAfterStart(Date date, Date startDate) {
		Calendar dc = Calendar.getInstance();
		dc.setTime(date);
		Calendar sc = Calendar.getInstance();
		sc.setTime(startDate);
		return dc.get(Calendar.DAY_OF_MONTH) > sc.get(Calendar.DAY_OF_MONTH);
	}

	public static Boolean monthAfterStart(Date date, Date startDate) {
		Calendar dc = Calendar.getInstance();
		dc.setTime(date);
		Calendar sc = Calendar.getInstance();
		sc.setTime(startDate);
		return dc.get(Calendar.MONTH) > sc.get(Calendar.MONTH);
	}

	public static String getYearMonth(Date date) {
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		return month > 9 ? year + "年" + month + "月 " : year + "年" + "0" + month + "月 ";
	}

	public static String getYearMonthDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		String monthString = month > 9 ? month + "月" : "0" + month + "月";
		String dayString = day > 9 ? day + "日" : "0" + day + "日";
		return year + "年" + monthString + dayString;
	}

	public static Integer getBetweenDays(Date d1, Date d2) {
		Calendar instance1 = Calendar.getInstance();
		Calendar instance2 = Calendar.getInstance();
		instance1.setTime(d1);
		instance2.setTime(d2);
		long time1 = instance1.getTimeInMillis();
		long time2 = instance2.getTimeInMillis();
		boolean res = (time2 - time1) % (1000 * 3600 * 24) == 0;
		long betweenDays = res ? (time2 - time1) / (1000 * 3600 * 24) : (time2 - time1) / (1000 * 3600 * 24) + 1;
		return Integer.parseInt(String.valueOf(betweenDays));
	}


	public static String getDataString() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		return sdf.format(new Date());
	}

	public static String getDateStringCH(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
		return sdf.format(date);
	}

	/**
	 * LocalDate转Date
	 */
	public static Date convertLocatDateToDate(LocalDate localDate) {
		ZoneId zoneId = ZoneId.systemDefault();
		ChronoZonedDateTime<LocalDate> zonedDateTime = localDate.atStartOfDay(zoneId);
		return Date.from(zonedDateTime.toInstant());
	}

	/**
	 * Date转LocalDate
	 */
	public static LocalDate convertDateToLocatDate(Date date) {
		Instant instant = date.toInstant();
		ZoneId zoneId = ZoneId.systemDefault();
		return instant.atZone(zoneId).toLocalDate();
	}

	/**
	 * Date转LocalDateTime
	 */
	public static LocalDateTime convertDateToLocatDateTime(Date date) {
		Instant instant = date.toInstant();
		ZoneId zoneId = ZoneId.systemDefault();
		return instant.atZone(zoneId).toLocalDateTime();
	}

	/**
	 * DateString转LocalDateTime
	 */
	public static LocalDateTime convertDateStringToLocatDateTime(String dateString) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try {
			date = sdf.parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
			Assert.isTrue(false, "日期格式错误！");
		}
		Instant instant = date.toInstant();
		ZoneId zoneId = ZoneId.systemDefault();
		return instant.atZone(zoneId).toLocalDateTime();
	}

	/**
	 * DateString转LocalDateTime
	 */
	public static LocalDate convertDateStringToLocatDate(String dateString){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date parse = null;
		try {
			parse = sdf.parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
			Assert.isTrue(false, "日期格式错误！");
		}
		Instant instant = parse.toInstant();
		ZoneId zoneId = ZoneId.systemDefault();
		return instant.atZone(zoneId).toLocalDate();
	}

}
