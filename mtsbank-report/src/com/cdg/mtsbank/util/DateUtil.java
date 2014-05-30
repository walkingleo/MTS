package com.cdg.mtsbank.util;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;
/**
 * 
 * 时间工具 
 * 有新的时间，请在这里写
 * @author lihui
 *
 */
public class DateUtil {
   
	private static final String  DATE_FORMAT ="yyyyMMdd";
	private static final String  DATE_FORMAT_THREE = "yyyyMMddHHmmss";
	private static final long   DAY_LONG = 1000*60*60*24;
	private static final String DATE_FORMAT_TWO = "yyyy-MM-dd HH:mm:ss";
	private static final String CERTAIN_TIME = "1530";
	private static final String  DATE_FORMAT_FOUR = "HHmm";
	private static final String DATE_FORMAT_YYYY_MM_DD = "yyyy-MM-dd";
	private static final String DATE_FORMAT_YYYY_MM = "yyyy-MM";
	
	private static Logger log = Logger.getLogger(DateUtil.class);
	/**
	 * 
	 * 
	 * 把Date 对象转换成格式为'yyyyMMdd'的字符串
	 * @param nowDate
	 * @return nowDateStr
	 */
	public static String getDateToString(Date  nowDate){
		String nowDateStr = null;
		if(nowDate != null){
			nowDateStr = new SimpleDateFormat(DATE_FORMAT).format(nowDate);
		}
		return nowDateStr;	
	}
	
	public static String getDateToStringThree(Date nowDate){
		String nowDateStr = null;
		if(nowDate != null){
			nowDateStr = new SimpleDateFormat(DATE_FORMAT_THREE).format(nowDate);
		}
		return nowDateStr;	
	}
	
	public static int getImportDateToNowDate(String importDate){
		SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
		int day = 0;
		try {
			Date date = dateFormat.parse(importDate);
			Calendar calendar =  Calendar.getInstance();
			Date nowDate = dateFormat.parse(dateFormat.format(calendar.getTime()));
			day = (int)((nowDate.getTime() - date.getTime())/(1000*60*60*24));
		} catch (ParseException e) {
			log.info(e.getMessage());
		}
		return day;
	}
	
	public static String getDatePlusDay(String importDate,int day){
		SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
		String dateStr = "";
		try {
			Date date = dateFormat.parse(importDate);
			date.setTime(date.getTime()+ DAY_LONG*day);
			dateStr = dateFormat.format(date);
		} catch (ParseException e) {
			log.info(e.getMessage());
		}
		
		return dateStr;
	}
	
	public static String getDateFormatTwo(){
		Calendar nowDate = Calendar.getInstance();
		return new SimpleDateFormat(DATE_FORMAT_TWO).format(nowDate.getTime());	
	}
	
	
	public static boolean isAfterCertainTime(String importTime){
		boolean result = false ;
		SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_THREE);
		try {
			Date date = dateFormat.parse(importTime);
			String certainImportTime = new SimpleDateFormat(DATE_FORMAT_FOUR).format(date);
			if(certainImportTime.compareTo(CERTAIN_TIME) > 0){ // 15点半以后
				result = true;
			}else{
				result = false;
			}
		} catch (ParseException e) {
			log.info(e.getMessage());
		}
		return result ;
	}
	
	public static long getBetweenDays(String importTime){
		Calendar calendar =  Calendar.getInstance();
		Date today = calendar.getTime();
		SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_TWO);
		Date importDate = null;
		long days = 0;
		try {
			importDate = dateFormat.parse(importTime);
			days = Math.abs((today.getTime()-importDate.getTime()))/(24*60*60*1000);
		} catch (ParseException e) {
			log.error(e.getMessage());
		}
		return days;
	}
	
	public static long getInterval(String startTime,String endTime){
		SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_TWO);
		Date startDate = null;
		Date endDate = null;
		long days = 0;
		try {
			startDate = dateFormat.parse(startTime);
			endDate = dateFormat.parse(endTime);
			days = Math.abs((endDate.getTime()-startDate.getTime()))/(1000);
		} catch (ParseException e) {
			log.error(e.getMessage());
		}
		return days;
	}
	
	public static String getTodayYYYY_MM_DD(){
		SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_YYYY_MM_DD);
		Calendar calendar =  Calendar.getInstance();
		Date today = calendar.getTime();
		return dateFormat.format(today);
	}
	
	public static String getTodayNextYYYY_MM_DD(int days){
		SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_YYYY_MM_DD);
		Calendar calendar =  Calendar.getInstance();
		Date today = calendar.getTime();
		today.setTime(today.getTime()+ DAY_LONG*days);
		return dateFormat.format(today);
	}
	
	public static String getCurrentMonthYYYY_MM(){
		SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_YYYY_MM);
		Calendar calendar =  Calendar.getInstance();
		Date today = calendar.getTime();
		return dateFormat.format(today);
	}
	
	public static String getCurrentMonthFirstDayYYYY_MM_DD(){
		SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_YYYY_MM_DD);
		Calendar calendar =  Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		Date day = calendar.getTime();
		return dateFormat.format(day);
	}
	
	public static String getNextMonthFirstDayYYYY_MM_DD(int months){
		SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_YYYY_MM_DD);
		Calendar calendar =  Calendar.getInstance();
		calendar.add(Calendar.MONTH, months);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		Date today = calendar.getTime();
		return dateFormat.format(today);
	}
	
	
	public static int compareTwoDayYYYY_MM_DD(String dateOne , String dateTwo){
		SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_YYYY_MM_DD);
		int flag = 2;
		try {
			Date beginDate= dateFormat.parse(dateOne);
			Date endDate= dateFormat.parse(dateTwo);
			if(endDate.getTime()- beginDate.getTime() > 0){
				flag = 1;
			}else if(endDate.getTime()- beginDate.getTime() < 0){
				flag = -1;
			}else if(endDate.getTime()- beginDate.getTime() == 0){
				flag = 0;
			}
		} catch (ParseException e) {
			log.error(e.getMessage());
		}
		return flag;
	}
	
	public static String getIntervalMinutes(String sTime,String eTime) {
		Long s = DateUtil.getInterval(sTime, eTime);
		float p = 60;
		float t = s/p;
		DecimalFormat fnum=new DecimalFormat("##0.00");
		
		return fnum.format(t);
	}
	
	public static Date getStrToDate(String date) {
		SimpleDateFormat format = null;
		Date date2 = null;
		try {
			format = new SimpleDateFormat(DATE_FORMAT_TWO);
			date2 = format.parse(date);
		} catch (Exception re) {
			re.printStackTrace();
		}
		return date2;
	}
	
	public static String getDateTostr(Date date) {
		SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT_TWO);
		return format.format(date);
	}
	
	public static void main(String [] args){
//		log.info(DateUtil.isAfterCertainTime("2012-08-14 16:00:01.667"));
//		log.info(DateUtil.getBetweenDays("2012-08-14 16:00:01.667"));
//		log.info(DateUtil.getImportDateToNowDate("2012-08-14 16:00:01.667"));
		//log.info(DateUtil.getInterval("2012-08-14 16:00:01.667","2012-08-14 16:02:01.607"));
		//log.info(DateUtil.getTodayYYYY_MM_DD());
		//log.info(DateUtil.getTodayNextYYYY_MM_DD(-1));
		//log.info(DateUtil.getCurrentMonthYYYY_MM());
		//log.info(DateUtil.getCurrentMonthFirstDayYYYY_MM_DD());
		//log.info(DateUtil.getNextMonthFirstDayYYYY_MM_DD(1));
		
//		log.info(DateUtil.compareTwoDayYYYY_MM_DD("2013-10-12","2013-10-12"));
//		log.info(DateUtil.compareTwoDayYYYY_MM_DD("2013-10-12","2013-10-12"));
//		log.info(DateUtil.compareTwoDayYYYY_MM_DD("2013-10-12","2013-10-13"));
//		Float s = new Float("60.0");
//		
//		log.info(DateUtil.getIntervalMinutes("2014-04-21 12:00:00","2014-04-21 12:51:10"));
		//if(!((DateUtil.compareTwoDayYYYY_MM_DD(startDate, busDate) == 0 || DateUtil.compareTwoDayYYYY_MM_DD(startDate, busDate) == 1)
		//&& (DateUtil.compareTwoDayYYYY_MM_DD(busDate, endDate) == -1))
	}
	
}
