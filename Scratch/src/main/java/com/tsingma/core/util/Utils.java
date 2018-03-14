package com.tsingma.core.util;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

public class Utils {

	/**
	 * 判断对象是否Empty(null或元素为0)<br>
	 * 实用于对如下对象做判断:String Collection及其子类 Map及其子类
	 * 
	 * @param pObj
	 *            待检查对象
	 * @return boolean 返回的布尔值
	 */
	public static boolean isEmpty(Object pObj) {
		if (pObj == null)
			return true;
		if (pObj == "")
			return true;
		if (pObj instanceof String) {
			if (((String) pObj).length() == 0) {
				return true;
			}
		} else if (pObj instanceof Collection) {
			if (((Collection) pObj).size() == 0) {
				return true;
			}
		} else if (pObj instanceof Map) {
			if (((Map) pObj).size() == 0) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 半角转全角
	 * 
	 * @param input
	 *            String.
	 * @return 全角字符串.
	 */
	public static String ToSBC(String input) {
		char c[] = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == ' ') {
				c[i] = '\u3000';
			} else if (c[i] < '\177') {
				c[i] = (char) (c[i] + 65248);

			}
		}
		return new String(c);
	}

	/**
	 * 全角转半角
	 * 
	 * @param input
	 *            String.
	 * @return 半角字符串
	 */
	public static String ToDBC(String input) {

		char c[] = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == '\u3000') {
				c[i] = ' ';
			} else if (c[i] > '\uFF00' && c[i] < '\uFF5F') {
				c[i] = (char) (c[i] - 65248);

			}
		}
		String returnString = new String(c);

		return returnString;
	}

	/**
	 * 获取详细的异常信息
	 * 
	 * @param e
	 * @return
	 */
	public static String getErrorMessage(Exception e) {
		StringBuffer errorBuffer = new StringBuffer();
		StackTraceElement[] stackTraceElements = e.getStackTrace();
		errorBuffer.append("\n").append(String.valueOf(e)).append("\n");
		for (StackTraceElement target : stackTraceElements) {
			errorBuffer.append(target.toString()).append("\n");
		}
		return errorBuffer.toString();
	}

	/**
	 * 获取系统当前时间
	 * @return
	 */
	public static String getSysTime() {
		// 设置日期格式
		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		return df.format(new Date());
	}
	
	/**
	 * 根据指定格式获取系统当前时间
	 * @return
	 */
	public static String getSysTime(String formatStr) {
		// 设置日期格式
		SimpleDateFormat df = new SimpleDateFormat(formatStr);
		return df.format(new Date());
	}
	
	/**
	 * 解析出参数url中的键值对 如 "Action=del&id=123"
	 * @param URL url地址
	 * @return url请求参数部分
	 */
	public static Map<String, String> URLToMap(String URL) {

		Map<String, String> mapRequest = new HashMap<String, String>();
		String[] arrSplit = null;

		if (URL == null)
			return mapRequest;
		if(URL.indexOf("&") > 0){
			arrSplit = URL.split("&");
			for (String strSplit : arrSplit) {
				String[] arrSplitEqual = null;
				arrSplitEqual = strSplit.split("=");
				if (arrSplitEqual.length > 1)
					mapRequest.put(arrSplitEqual[0], arrSplitEqual[1]);
				else
					if (arrSplitEqual[0] != "")
						mapRequest.put(arrSplitEqual[0], "");
			}
		} else {
			String[] arrSplitEqual = null;
			arrSplitEqual = URL.split("=");
			if (arrSplitEqual.length > 1)
				mapRequest.put(arrSplitEqual[0], arrSplitEqual[1]);
			else
				if (arrSplitEqual[0] != "")
					mapRequest.put(arrSplitEqual[0], "");
			
		}
		return mapRequest;
	}
	
    /**  
     * 计算两个日期之间相差的天数  
     * @param date1 时间1
     * @param date2 时间2
     * @return 相差天数  
     */
    public static int daysBetween(Date date1,Date date2) {    
        Calendar cal = Calendar.getInstance();    
        cal.setTime(date1);    
        long time1 = cal.getTimeInMillis();   
        cal.setTime(date2);    
        long time2 = cal.getTimeInMillis();       
        
        long between_days=(time2-time1)/(1000*3600*24);  
            
        return Integer.parseInt(String.valueOf(between_days));           
    }
    
    /**  
     * 计算两个日期之间相差的天数  （日期字符串）
     * @param date1 时间1
     * @param date2 时间2
     * @param format 日期格式
     * @return 相差天数  date2-date1绝对值
     */
    public static int daysBetween(String date1,String date2, String format) {  
        SimpleDateFormat sdf=new SimpleDateFormat(format);  
        Calendar cal = Calendar.getInstance();    
        try {
			cal.setTime(sdf.parse(date1));
			long time1 = cal.getTimeInMillis();                 
			cal.setTime(sdf.parse(date2));    
			long time2 = cal.getTimeInMillis();         
			long between_days=(time2-time1)/(1000*3600*24);  
			return Math.abs(Integer.parseInt(String.valueOf(between_days)));     
		} catch (ParseException e) {
			e.printStackTrace();
		}    
        return 0;
    } 
    
    /**  
     * 计算两个日期之间相差的天数  （日期字符串）非绝对值
     * @param date1 时间1
     * @param date2 时间2
     * @param format 日期格式
     * @return 相差天数  date2-date1绝对值
     */
    public static int daysBetween2(String date1,String date2, String format) {  
        SimpleDateFormat sdf=new SimpleDateFormat(format);  
        Calendar cal = Calendar.getInstance();    
        try {
			cal.setTime(sdf.parse(date1));
			long time1 = cal.getTimeInMillis();                 
			cal.setTime(sdf.parse(date2));    
			long time2 = cal.getTimeInMillis();         
			long between_days=(time2-time1)/(1000*3600*24);  
			return Integer.parseInt(String.valueOf(between_days));     
		} catch (ParseException e) {
			e.printStackTrace();
		}    
        return 0;
    } 
    
    /**  
     * 计算两个日期之间相差的小时数  （日期字符串）
     * @param date1 时间1
     * @param date2 时间2
     * @param format 日期格式
     * @return 相差天数  date2-date1绝对值
     */
    public static int hoursBetween(String date1,String date2, String format) {  
        SimpleDateFormat sdf=new SimpleDateFormat(format);  
        Calendar cal = Calendar.getInstance();    
        try {
			cal.setTime(sdf.parse(date1));
			long time1 = cal.getTimeInMillis();                 
			cal.setTime(sdf.parse(date2));    
			long time2 = cal.getTimeInMillis();         
			long between_days=(time2-time1)/(1000*3600);  
			return Math.abs(Integer.parseInt(String.valueOf(between_days)));     
		} catch (ParseException e) {
			e.printStackTrace();
		}    
        return 0;
    }
    
    /**  
     * 计算两个日期之间相差的秒数  （日期字符串）
     * @param date1 时间1
     * @param date2 时间2
     * @param format 日期格式
     * @return 相差秒数  date2-date1绝对值
     */
    public static int secondBetween(String date1,String date2, String format) {  
        SimpleDateFormat sdf=new SimpleDateFormat(format);  
        Calendar cal = Calendar.getInstance();    
        try {
			cal.setTime(sdf.parse(date1));
			long time1 = cal.getTimeInMillis();                 
			cal.setTime(sdf.parse(date2));    
			long time2 = cal.getTimeInMillis();         
			long between_days=(time2-time1)/1000;  
			return Math.abs(Integer.parseInt(String.valueOf(between_days)));     
		} catch (ParseException e) {
			e.printStackTrace();
		}    
        return 0;
    }
    
    /**
     * 得到制定日期，几月前或几月后的日期，负数为之前的日期，正数为之后的日期
     * @param date
     * @param fewMonth
     * @param formatStr 日期格式
     * @return
     */
    public static String getDateForMonth(String dateStr, int fewMonth, String formatStr) {
    	SimpleDateFormat sdf=new SimpleDateFormat(formatStr);
    	Calendar calendar = Calendar.getInstance();
    	//获取制定时间的前几月或后几月日期
    	try {
			calendar.setTime(sdf.parse(dateStr));
		} catch (ParseException e) {
			e.printStackTrace();
		}
    	calendar.add(Calendar.MONTH,fewMonth);
    	Date date = calendar.getTime();
    	SimpleDateFormat format = new SimpleDateFormat(formatStr);
    	String fewMonthFordateStr = format.format(date);
    	return fewMonthFordateStr;
    }
   
    /**
     * 将日期转化成制定格式
     * @param sourceDate 需要被转换的日期
     * @param sourceFormat 需要被转换日期现有格式
     * @param targetFormat 转化后的格式
     * @return 转化后的日期
     */
    public static String changeDateFormat(String sourceDate, String sourceFormat, String targetFormat) {
    	SimpleDateFormat format1 = new SimpleDateFormat(sourceFormat);
		SimpleDateFormat format2 = new SimpleDateFormat(targetFormat);
		String targetDate = sourceDate;
		try {
			targetDate = format2.format(format1.parse(sourceDate));
		} catch (ParseException e) {
			e.printStackTrace();
		}
    	return targetDate;
    }
    
    /**
     * 返回长度为【strLength】的随机数，在前面补0 
     * @param strLength
     * @return
     */
    public static String getFixLenthString(int strLength) {  
          
        Random rm = new Random();  
          
        // 获得随机数  
        double next = (rm.nextDouble());
        double pross = (next) * Math.pow(10, strLength);  
        long code = Math.round(pross);
        // 将获得的获得随机数转化为字符串  
        DecimalFormat decimalFormat = new DecimalFormat("###0");//格式化设置  
        String fixLenthString = decimalFormat.format(pross);  
        // 返回固定的长度的随机数  
        int num = strLength-fixLenthString.length();
        for(int i=0; i<num; i++){
        	fixLenthString = "0" + fixLenthString;
        }
        
        return fixLenthString;  
    } 
    
    /**
     * 得到request请求参数字符串
     * @param request
     * @return
     */
    public static String getRequestParams(HttpServletRequest request) {
    	StringBuffer buffer = new StringBuffer();
    	Enumeration enum_term = request.getParameterNames();
    	while (enum_term.hasMoreElements()) {
	    	String paramName = (String) enum_term.nextElement();
	    	String paramValue = request.getParameter(paramName);
	    	if("sid".equalsIgnoreCase(paramName)) continue;
	    	buffer.append(paramName).append("=").append(paramValue).append("&");
    	}
    	String result = "";
    	if(buffer.length() > 0)
    		result = buffer.substring(0, buffer.length()-1);
    	return result;
    }
    
    /**
     * 转换服务域名，将缺失www.部分进行补全。异于浏览器访问，部分网络接口调用需补全
     * @param serverName
     * @return
     */
    public static String convertServerName(String serverName) {
    	int length = serverName.split("\\.").length;
    	if(length <= 2 && serverName.indexOf("www") == -1) {
    		serverName = "www." + serverName;
    	}
    	return serverName;
    } 
    
    /**  
     * 计算相距今天若干天之后或之前的日期
     * @param days 相差天数  
     * @return 日期
     */
    public static String dateApart(int days) {
    	if(days == 0)
    		return "";
    	else if(days == -1)
    		return "9999/12/30";
    	else {
    		Calendar now = Calendar.getInstance();
    		now.add(Calendar.DAY_OF_MONTH, days);
    		return new SimpleDateFormat("yyyy/MM/dd").format(now.getTime());           
    	}
    }
    
    /**  
     * 计算相距今天若干天之后或之前的日期(指定格式)
     * @param days 相差天数  
     * @return 日期
     */
    public static String dateApart(int days, String formatStr) {
    	if(days == 0)
    		return "";
    	else if(days == -1)
    		return "9999/12/30";
    	else {
    		Calendar now = Calendar.getInstance();
    		now.add(Calendar.DAY_OF_MONTH, days);
    		return new SimpleDateFormat(formatStr).format(now.getTime());           
    	}
    }
    
    /**  
     * 计算相距指定日期若干天之后或之前的日期
     * @param days 相差天数  
     * @return 日期
     * @throws ParseException 
     */
    public static String dateApart(String date, int days) throws ParseException {
    	if(days == 0)
    		return "";
    	else if(days == -1)
    		return "9999/12/30";
    	else {
    		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
    		Date date1 = format.parse(date);
			Calendar now = Calendar.getInstance();
    		now.setTime(date1);
    		now.add(Calendar.DAY_OF_MONTH, days);
    		return new SimpleDateFormat("yyyy/MM/dd").format(now.getTime());    
    	}
    }

	public static void main(String[] args) throws Exception {
		// String QJstr = "wch";
		// String QJstr1 = "ｈｅｌｌｏ";
		// String result = ToSBC(QJstr);
		// String result1 = ToDBC(QJstr1);
		// System.out.println(QJstr + "\n" + result);
		// System.out.println(QJstr1 + "\n" + result1);
		// DecimalFormat df = new DecimalFormat("###.00");
		// System.out.println(df.format(Double.parseDouble("12354.21") /
		// Double.parseDouble("254634.60") * 100));
		System.out.println(dateApart("2017/06/30", 15));
		System.out.println(daysBetween2("2017/09/08", "2017/09/12", "yyyy/MM/dd"));
	}

}
