package com.chinawiserv.deepone.manager.core.util;

/**
 * 转大写工具类
 * <pre>
 * 本类是提供静态的 "将日期转化为大写" 与 "转化数字为大写"的方法，主要方法有：
 * 1、将日期转化为大写 toCapitalDateTime()
 * 2、转化数字为大写 toCapitalNumber()
 * </pre>
 * @author zengpzh
 * @version 0.1
 */
public class ConvertUtil { 
	
	/**
	 * 编码转换，将一字符串重新编码为 UTF-8
	 * @param uniStr 待重新编码字符串
	 * @return 重新编码成UTF-8的字符串
	 * @author zengpzh
	 */
	public static String toUTF8(String uniStr){
		String utf8Str = "";
		if (uniStr == null) {
			uniStr = "";
		}
		try {
			byte[] tempByte = uniStr.getBytes("ISO-8859-1");
			utf8Str = new String(tempByte, "utf-8");
		}
		catch (Exception e) {
		}
		return utf8Str;
	}	
	
	/**
	 * 将日期转化为大写
	 * @param dateTime 待转化日期，格式：yyyy年MM月dd日
	 * @return 转化后的日期
	 * @author Allen Zhang
	 */
	public static String toCapitalDateTime(String dateTime) {
		if ((dateTime != null) && (!"".equals(dateTime))) {
			String datestr = dateTime; 
			String tmp_char = datestr.substring(0, 5); 
			String tmp_Str = datestr.substring(5, datestr.length()); 
			   
			String [] number_lowercase = {"0","1","2","3","4","5","6","7","8","9"}; 
			String [] number_capital = {"零","一","二","三","四","五","六","七","八","九"}; 
			   
			for(int i=0; i<number_lowercase.length; i++) { 
				for(int j=0;j<tmp_char.length()-1;j++) { 
					if(tmp_char.substring(j,j+1).equals(number_lowercase[i])) {
						tmp_char = tmp_char.substring(0,j) + 
						           number_capital[i] + 
						           tmp_char.substring(j+1, 
						           tmp_char.length()); 
					}
				} 
			} 
			    
			String [] dateTime_lowercase = {"01","02","03","04","05","06","07","08","09","10", 
			                                "11","12","13","14","15","16","17","18","19","20", 
			                                "21","22","23","24","25","26","27","28","29","30","31"}; 

			String [] dateTime_capital = {"一","二","三","四","五","六","七","八","九","十", 
			                              "十一","十二","十三","十四","十五","十六","十七","十八","十九","二十", 
			                              "二十一","二十二","二十三","二十四","二十五","二十六","二十七","二十八", 
			                              "二十九","三十","三十一"}; 
			   
			for(int i=0;i<dateTime_lowercase.length;i++) { 
				for(int j=0;j<tmp_Str.length()-2 ; j++) { 
					if(tmp_Str.substring(j,j+2).equals(dateTime_lowercase[i])) {
						tmp_Str=tmp_Str.substring(0,j)+dateTime_capital[i]+tmp_Str.substring(j+2,tmp_Str.length()); 
					}
				} 
			} 
			String dateend=tmp_char + tmp_Str;
			return dateend;
		 }
		 else {
			 return "" ;
		 }
	} 
	
	
	/**
	 * 转化数字为大写
	 * @param number 数字，只能为[0..9]
	 * @return 转化后数字大写
	 * @author Allen Zhang
	 */
	public static String toCapitalNumber(String number) {
		String capitalNum = "";
		if (number == null || "".equals(number)) {
			return capitalNum;
		}
		int number_tmp = -1 ;
		try {
			number_tmp = Integer.parseInt(number);
		} catch (Exception e) {
			return capitalNum;
		}
		if (number_tmp < 0) {
			return capitalNum;
		}
		capitalNum = capitalNumber(number_tmp);
		return capitalNum;
	 }
	
	/**
	 * 转化数字为大写
	 * @param number 数字
	 * @return 转化后数字大写
	 * @author Allen Zhang
	 */
	private static String capitalNumber(int number) {
		String capitalNum = "";
		switch(number) {
		case 0: {
			capitalNum="零";
			break;
		}
		case 1: {
			capitalNum="一";
			break;
		}
		case 2: {
			capitalNum="二";
			break;
		}
		case 3: {
			capitalNum="三";
			break;
		}
		case 4: {
			capitalNum="四";
			break;
		}
		case 5: {
			capitalNum="五";
			break;
		}
		case 6: {
			capitalNum="六";
			break;
		}
		case 7: {
			capitalNum="七";
			break;
		}
		case 8: {
			capitalNum="八";
			break;
		}
		case 9: {
			capitalNum="九";
			break;
		}
		}
	    return capitalNum;
	}
}