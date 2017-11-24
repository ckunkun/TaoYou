package com.ad.taoyou.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class DateUtil {
	public static final SimpleDateFormat HHmmssSSS=new SimpleDateFormat("HHmmssSSS");
	public static final SimpleDateFormat yyyyMMdd=new SimpleDateFormat("yyyyMMdd");
	public static final SimpleDateFormat yyyy_MM_dd=new SimpleDateFormat("yyyy-MM-dd");
	public static final SimpleDateFormat yyyy_MM_dd2=new SimpleDateFormat("yyyy.MM.dd");
	public static final SimpleDateFormat HH_mm_ss=new SimpleDateFormat("HH:mm:ss");
	public static final SimpleDateFormat yyyy_MM_dd_HH_mm_ss=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static final SimpleDateFormat yyyy_MM_dd_HH_mm2=new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
	public static final SimpleDateFormat yyyyMMddHHmmss=new SimpleDateFormat("yyyyMMddHHmmss");
	/**
	 * MM/dd/yyyy HH:mm:ss
	 */
	public static final SimpleDateFormat df1=new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
	
	/**
	 * 4.把日期转换为字符串
	 * 
	 * @param date
	 *            要转换的日期
	 * @return 转换后的字符串，格式"yyyy-MM-dd HH:mm:ss"
	 */
	public static String dateToString(Date date) {
		String string = "";

		if (date == null)
			return string;

		SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		string = dateformat.format(date);

		return string;
	}
	
	/**
	 * 相差多少天
	 * @param time1
	 * @param time2
	 * @return
	 */
	public static Long getDistanceDay(long time1, long time2) { 
        long day = 0; 
        try { 
            long diff ; 
            if(time1<time2) { 
                diff = time2 - time1; 
            } else { 
                diff = time1 - time2; 
            } 
            day = diff / (24 * 60 * 60 * 1000); 
        } catch (Exception e) { 
            e.printStackTrace(); 
        } 
        return day; 
    } 

	/**
	 * 得到本月的第一天
	 * 
	 * @return
	 */
	public static String getMonthFirstDay() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, calendar
				.getActualMinimum(Calendar.DAY_OF_MONTH));
		SimpleDateFormat df   =   new   SimpleDateFormat( "yyyy-MM-dd");
		return df.format(calendar.getTime());
	}

	/**
	 * 得到本月的最后一天
	 * 
	 * @return
	 */
	public static String getMonthLastDay() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, calendar
				.getActualMaximum(Calendar.DAY_OF_MONTH));
		SimpleDateFormat df   =   new   SimpleDateFormat( "yyyy-MM-dd");
		return df.format(calendar.getTime());
	}
	
	/**
	 * 获取前一天的日期
	 * @param date
	 * @return
	 * @author WangWenGuang/Mar 29, 2012/9:12:04 PM
	 */
	public static Date getBeforeDate(Date date) {
		
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.DATE, c.get(Calendar.DATE)-1);
		return c.getTime();
	}
	
	  /** 
     * 两个时间相差距离多少天多少小时多少分多少秒 
     * @param time1 
     * @param time2  
     * @return String 返回值为：xx天xx小时xx分xx秒 
     */ 
    public static String getDistanceTime(long time1, long time2) { 
        long day = 0; 
        long hour = 0; 
        long min = 0; 
        long sec = 0; 
        try { 
            long diff ; 
            if(time1<time2) { 
                diff = time2 - time1; 
            } else { 
                diff = time1 - time2; 
            } 
            day = diff / (24 * 60 * 60 * 1000); 
            hour = (diff / (60 * 60 * 1000) - day * 24); 
            min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60); 
            sec = (diff/1000-day*24*60*60-hour*60*60-min*60); 
        } catch (Exception e) { 
            e.printStackTrace(); 
        } 
        return day + "天" + hour + "小时" + min + "分" + sec + "秒"; 
    } 
    
    /** 
     * 两个时间相差距离多少天多少小时多少分多少秒 
     * @param time1 
     * @param time2  
     * @return String 返回值为：xx天||xx小时||xx分||xx秒 
     */ 
	public static String getDistanceTime2(long time1, long time2) { 
        long day = 0; 
        long hour = 0; 
        long min = 0; 
        long sec = 0; 
        try { 
            long diff ; 
            if(time1<time2) { 
                diff = time2 - time1; 
            } else { 
                diff = time1 - time2; 
            } 
            day = diff / (24 * 60 * 60 * 1000); 
            hour = (diff / (60 * 60 * 1000) - day * 24); 
            min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60); 
            sec = (diff/1000-day*24*60*60-hour*60*60-min*60); 
        } catch (Exception e) { 
            e.printStackTrace(); 
        }
        if(day>0){
        	return day + "天";
        }
        if(hour>0){
        	return hour + "小时";
        }
        if(min>0){
        	return min + "分";
        }
        return sec+"秒"; 
    } 
    
    /** 
     * 两个时间相差距离多少小时
     * @param time1 
     * @param time2  
     * @return String 返回值为：xx小时 
     */ 
    public static String getDistanceHour(long time1, long time2) {
    	long day = 0;
        long hour = 0; 
        long min = 0; 
        try { 
            long diff ; 
            if(time1<time2) { 
                diff = time2 - time1; 
            } else { 
                diff = time1 - time2; 
            } 
            day = diff / (24 * 60 * 60 * 1000); 
            hour = (diff / (60 * 60 * 1000)); 
            min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60); 
            if(min >= 55){
            	hour +=1 ;
            }
        } catch (Exception e) { 
            e.printStackTrace(); 
        } 
        return hour + "小时"; 
    }
    
	public static void main(String[] args) {
		long long1 = dateToLong("2014-09-04 11:37:35.630","yyyy-MM-dd HH:mm:ss");
		long long2 = dateToLong("2014-09-04 12:37:15.000","yyyy-MM-dd HH:mm:ss");
		System.out.println(DateUtil.getDistanceTime(long1,long2));
		System.out.println(DateUtil.getMonthLastDay());
	}
	/**
	 * 获取格式化日期
	 * @param format
	 * @param date
	 * @return
	 */
	public static String getFormat(String format,Date date){
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}
	/**
	 * 获取几天以后的时间
	 * @param date
	 * @param n
	 * @return
	 * @author WangWenGuang/Nov 1, 2012/11:00:26 AM
	 */
	public static Date getAfterNDay(Date date, int n) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.DATE, c.get(Calendar.DATE)+n);
		return c.getTime();
	}
	
	/**
     * 当得到两个日期相差天数。
     *
     * @param begin     第一个日期
     * @param end    第二个日期      
     * @return          相差的天数
     */
    public static int selectDateDiff(long begin, long end) {
        int dif = 0;
        try {
            long fDate = (begin>end?begin:end);
            long sDate = (begin>end?end:begin);
            dif = (int) ((fDate- sDate) / 86400000);
        } catch (Exception e) {
            dif = 0;
        }
        return dif;
    }
    /**
	 * 日期加（减）月份后的日期
	 * @param date
	 * @param month
	 * @return
	 */
	public static Date addMonth(Date date, int month) {		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, month);
		return calendar.getTime();
	}
	/**
	 *  日期加（减）天数后的日期
	 * @param date
	 * @param day
	 * @return
	 */
	public static Date addDay(Date date, int day) {		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, day);
		return calendar.getTime();
	}
	/**
	 *  日期加（减）天数后的日期
	 * @param date
	 * @param day
	 * @return
	 */
	public static Date addDayOfMohth(Date date, int day) {		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, day);
		return calendar.getTime();
	}
	/**
	 * 日期加（减）年数后的日期
	 * @param date
	 * @param year
	 * @return
	 */
	public static Date addYear(Date date, int year) {		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.YEAR, year);
		return calendar.getTime();
	}
	/**
	 *  日期加（减）小时后的日期
	 * @param date
	 * @param day
	 * @return
	 */
	public static Date addHour(Date date, int hour) {		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.HOUR, hour);
		return calendar.getTime();
	}

	/**
	 *  日期加（减）秒后的日期
	 * @param date
	 * @param addSeconds
	 * @return
	 */
	public static Date addSeconds(Date date, int seconds) {		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.SECOND, seconds);
		return calendar.getTime();
	}
	
	/**
	 * 计算签约时间使用，如2014-1-31加1月，返回2014-3-3，会加上天数差
	 * @param date
	 * @return
	 */
	public static Date signingDate(Date date,int month){
		Date d2=DateUtil.addMonth(date, month);
		//计算日差
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int day1=c.get(Calendar.DAY_OF_MONTH);
		c.setTime(d2);
		int day2=c.get(Calendar.DAY_OF_MONTH);
		//31日>28日，在上月基础上加3天
		if(day1>day2){
			c.setTime(d2);
			c.add(Calendar.DAY_OF_MONTH, day1-day2);
		}
		return c.getTime();
	}
	
	/**
	 * 将字符串型的时间按照定义的格式
	 * 转化为时间戳，long型
	 * @param datestr
	 * @param format
	 * @return
	 */
	public static long dateToLong(String datestr, String format) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			Date date = sdf.parse(datestr);
			return date.getTime();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
	/**
	 * 将时间戳型的时间按照定义的格式
	 * 转化为时间的字符串
	 * @param datelong
	 * @param format
	 * @return
	 */
	public static String longToDate(long datelong, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Date date = new Date();
		date.setTime(datelong);
		return sdf.format(date);
	}
	
	/**
	 * 3.把日期字符串转换成按指定格式的具体日期
	 * 
	 * @param dateString
	 *            字符串
	 * @param pattern
	 *            格式1:yyyy-MM-dd HH:mm:ss(默认)
	 *            格式2:yyyy-MM-dd
	 *            格式3:yyyy
	 * @return 日期类型
	 */
	public static Date stringToDate(String dateString, String pattern) {
		Date tempDate = null;
		if (pattern == null || pattern.equals("")) {
			pattern = "yyyy-MM-dd HH:mm:ss";
		}
		if (dateString == null || dateString.equals("")) {
			return tempDate;
		}
		SimpleDateFormat dateformat = new SimpleDateFormat(pattern);
		try {
			tempDate = dateformat.parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return tempDate;
	}

	/**
	 * 将时间按照定义格式转化为字符串型
	 * @param date
	 * @param format
	 * @return
	 */
	public static String dateToString(Date date, String format) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format);

			return sdf.format(date);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	



	/**
	 * 获取时间列表
	 * @param day 几天前的列表
	 * @return 20141021,20141022
	 */
	public static List<String> getDateList(int day){
		List<String> list=new ArrayList<String>();
		Date curDate=new Date();
		
		for (int i = 1; i <= day; i++) {
			Calendar c = Calendar.getInstance();
			c.setTime(curDate);
			c.add(Calendar.DAY_OF_MONTH, -i);
			
			list.add(yyyyMMdd.format(c.getTime()));
		}
		return list;
	}
	/**
	 * 获取一个月的时间列表
	 * @param year 年
	 * @param month 月
	 * @return 20141021,20141022
	 */
	@SuppressWarnings("deprecation")
	public static List<String> getDateList(int year,int month){
		List<String> list=new ArrayList<String>();
		Date date=new Date(year-1900,month-1,1);
		list.add(yyyyMMdd.format(date));
		for (int i = 1; i <31; i++) {
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			c.add(Calendar.DAY_OF_MONTH, i);
			if(c.getTime().getMonth()>month-1){
				break;
			}
			list.add(yyyyMMdd.format(c.getTime()));
		}
		return list;
	}
	
	/**
	 * 获取前两个月的第一天的日期 Index 0 ，当月最后一天的日期 Index 1
	 * @return
	 */
	public static List<String> getLastThreeMonth(){
	  List<String> list = new ArrayList<String>();
	  //获得当前月最后一天的数据
  	  Calendar c = Calendar.getInstance();
  	  c.set(Calendar.DATE, c.getActualMaximum(Calendar.DATE)); 
  	  list.add(yyyyMMdd.format(c.getTime()));
  	  

  	  //获得当前月的前两个月第一天日期
  	  Calendar c2 = Calendar.getInstance();
  	  c2.add(Calendar.MONTH, -2);
  	  c2.set(Calendar.DATE, c2.getActualMinimum(Calendar.DATE)); 
  	  list.add(yyyyMMdd.format(c2.getTime()));
  	  return list;
	}
}
