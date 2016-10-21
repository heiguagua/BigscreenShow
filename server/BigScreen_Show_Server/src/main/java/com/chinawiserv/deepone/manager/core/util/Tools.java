package com.chinawiserv.deepone.manager.core.util;

import com.chinawiserv.deepone.manager.core.exception.MsgException;

import java.io.File;
import java.io.FilenameFilter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * 综合工具类
 * <pre>
 * 本类提供一些综合的静态方法，主要方法有
 * 1、生成 bit 位 随即数 random()
 * 2、统计字符串所占的字节数 getBytesFromAString()
 * 3、统计字符串的字符数 getWordsFromAString()
 * 4、生成HTML原码：用于 Bean:Write 显示 信息 createAreaHTML()
 * 5、生成段落数组 createAreaArray()
 * 6、生成WORD回车符的字符串 createAreaTEXTForWord()
 * 7、切字符串，把源字符串切为指定长度的字符串 sliceString()
 * 8、判断是否为允许注册的名称 allowRegisterName()
 * 9、判断是否为数字 allowRegisterNumber()
 * 10、纠正domainName（显示） correctDomainNameForShow()
 * 11、纠正domainName（储存）correctDomainName()
 * 12、从Map中取得指定键的值 getObjFromMap()
 * </pre>
 * @author zengpzh
 * @version 0.1
 */

public class Tools {

	/**
	 * 生成 bit 位 随即数
	 * @return  bit 位 随即数
	 * @author Allen Zhang
	 */
	public static String random(int bit) {
		String myRandom = String.valueOf(Math.random());
		myRandom = myRandom.replaceAll("\\.", "").replaceAll("0", "");
		if (myRandom.length() > bit) {
			myRandom = myRandom.substring(0, bit);
		}		
		while (myRandom.length() < bit) {
			myRandom += "0";
		}
		return myRandom;
	}	

	/**
	 * 统计字符串所占的字节数
	 * @param srcStr 待统计的字符串
	 * @return 字符串所占的字节数
	 * @author Allen Zhang
	 */
	public static int getBytesFromAString(String srcStr) {
		int count = 0;
		if (srcStr != null) {
			byte[] bytes = srcStr.getBytes();
			count = bytes.length;
		}
		return count;
	} 	

	/**
	 * 统计字符串的字符数
	 * @param srcStr 待统计的字符串
	 * @return 字符串的字符数
	 * @author Allen Zhang
	 */
	public static int getWordsFromAString(String srcStr) {
		int count = 0;
		if (srcStr != null) {
			count = srcStr.length();
		}
		return count;
	} 	
	
	/**
	 * 生成HTML原码：用于 Bean:Write 显示 信息 <BR>
	 * 注意 Bean:Write的 属性filter应为false，即：filter="false"
	 * @param textStr 待转换的源串
	 * @return 对应的HTML原码 
	 * @author Allen Zhang
	 */
	public static String createAreaHTML(String textStr) {
		StringBuffer selectBuffer = new StringBuffer();
		int charCount = 0;
		if (textStr != null) {
			charCount = textStr.length ();
		}
		if (charCount > 0) {
			for (int charNum = 0; charNum < charCount; charNum++) {
				char character = textStr.charAt(charNum);
				switch (character) {
					/**
					 * ASCII space (&#x0020;) 
					 * ASCII tab (&#x0009;) 
					 * ASCII form feed (&#x000C;) 
					 * Zero-width space (&#x200B;)
					 */ 
					case '\u0020': {
						selectBuffer.append("&nbsp;");
						break;
					}
					case '\u0009': {
						selectBuffer.append("&nbsp;&nbsp;&nbsp;&nbsp;");
						break;
					}
					case '\u000C':  {
						break;
					}
					case '\u200B':  {
						selectBuffer.append("&nbsp;");
						break;
					}
					case '\u2028': {
						break;
					}
					case '\u2029': {
						break;
					}
					case '\r': {
						break;
					}
					case '\n': {
						if (selectBuffer.length() > 4) {
							String endStr = selectBuffer.toString().substring(selectBuffer.length() - 4, selectBuffer.length());
							if (!"<BR>".equals(endStr)) {
								selectBuffer.append("<BR>");
							}
						}
						break;
					}
					default:  {
						selectBuffer.append(character);
						break;
					}
				}
			}
		}
		else {
			selectBuffer.append("");
		}
		return selectBuffer.toString();
	}
	
	
	/**
	 * 生成段落数组
	 * @param textStr 待转换的源串
	 * @return 段落数组
	 * @author Allen Zhang
	 */
	public static String[] createAreaArray(String textStr) {
		String[] htmlArray = createAreaHTML(textStr).split("<BR>");
		for (int i = 0 ; i < htmlArray.length ; i++) {
			htmlArray[i] = htmlArray[i].trim();
		}
		return htmlArray;
	}
	
	/**
	 * 生成WORD回车符的字符串
	 * @param textStr 待转换的源串
	 * @return 去掉回车符的字符串  
	 * @author Allen Zhang
	 */
	public static String createAreaTEXTForWord(String textStr) {
		StringBuffer selectBuffer = new StringBuffer();
		int charCount = 0;
		if (textStr != null) {
			charCount = textStr.length ();
		}
		if (charCount > 0) {
			for (int charNum = 0; charNum < charCount; charNum++) {
				char character = textStr.charAt(charNum);
				switch (character) {
					/**
					 * ASCII space (&#x0020;) 
					 * ASCII tab (&#x0009;) 
					 * ASCII form feed (&#x000C;) 
					 * Zero-width space (&#x200B;)
					 */ 
					case '\u0020': {
						selectBuffer.append("");
						break;
					}
					case '\u0009': {
						selectBuffer.append("");
						break;
					}
					case '\u000C':  {
						break;
					}
					case '\u200B':  {
						selectBuffer.append("");
						break;
					}
					case '\u2028': {
						selectBuffer.append("");
						break;
					}
					case '\u2029': {
						selectBuffer.append("");
						break;
					}
					case '\r': {
						selectBuffer.append("");
						break;
					}
					case '\n': {
						selectBuffer.append("\\n");
						break;
					}
					default:  {
						selectBuffer.append(character);
						break;
					}
				}
			}
		}
		else {
			selectBuffer.append("");
		}
		return selectBuffer.toString();
	}

	public static String formatStr(String textStr) {
		StringBuffer selectBuffer = new StringBuffer();
		int charCount = 0;
		if (textStr != null) {
			charCount = textStr.length ();
		}
		if (charCount > 0) {
			for (int charNum = 0; charNum < charCount; charNum++) {
				char character = textStr.charAt(charNum);
				switch (character) {
					/**
					 * ASCII space (&#x0020;)
					 * ASCII tab (&#x0009;)
					 * ASCII form feed (&#x000C;)
					 * Zero-width space (&#x200B;)
					 */
					case '\u0020': {
						selectBuffer.append("");
						break;
					}
					case '\u0009': {
						selectBuffer.append("");
						break;
					}
					case '\u000C':  {
						break;
					}
					case '\u200B':  {
						selectBuffer.append("");
						break;
					}
					case '\u2028': {
						selectBuffer.append("");
						break;
					}
					case '\u2029': {
						selectBuffer.append("");
						break;
					}
					case '\r': {
						selectBuffer.append("");
						break;
					}
					case '\n': {
						selectBuffer.append("");
						break;
					}
					default:  {
						selectBuffer.append(character);
						break;
					}
				}
			}
		}
		else {
			selectBuffer.append("");
		}
		return selectBuffer.toString();
	}
	
	/**
	 * 切字符串，把源字符串切为指定长度的字符串
	 * @param sourceStr 源字符串
	 * @param strLength 指定长度
	 * @return 切后字符串
	 * @author zengpzh
	 */
	public static String sliceString(String sourceStr, int strLength){
		if (sourceStr != null) {
			if (strLength > 0 && sourceStr.length() > strLength) {
				return sourceStr.substring(0, strLength) + "...";
			}
			else {
				return sourceStr;
			}
		}
		else {
			return "";
		}
	}	
	
	/**
	 * 判断是否为允许注册的名称
	 * @param matchString 待测试字符串
	 * @param fieldName 字段名称
	 * @throws com.chinawiserv.deepone.manager.core.exception.MsgException 不为允许注册的名称抛出此异常
	 * @author zengpzh
	 */
	public static void allowRegisterName(String matchString, String fieldName) throws MsgException {
		if (!Validate.isAllowRegisterName(matchString)) {
			throw new MsgException(fieldName + "&nbsp;&nbsp;必须为汉字、数字、字母、下划线或其组合，请重试！");
		}
	}

	/**
	 * 判断是否为数字
	 * @param matchString 待测试字符串
	 * @param fieldName 字段名称
	 * @throws com.chinawiserv.deepone.manager.core.exception.MsgException 不为允许注册的名称抛出此异常
	 * @author zengpzh
	 */
	public static void allowRegisterNumber(String matchString, String fieldName) throws MsgException {
		if (!Validate.isNumeric(matchString)) {
			throw new MsgException(fieldName + "&nbsp;&nbsp;必须为数字，请重试！");
		}
	}	
	
	/**
	 * 纠正domainName（显示）
	 * @param website 待纠正的domainName
	 * @return 待纠正后的domainName
	 * @author zengpzh
	 */
	public static String correctDomainNameForShow(String website) {
		String domainName = "";
		if (website != null && !"".equals(website.trim())) {
			website = website.toLowerCase();
			if (website.indexOf("http")>-1 || website.indexOf("www")>-1) {
				if (website.startsWith("www")) {
					website = "http://" + website;
					domainName = "<a href='"+website+"' target='_bank'>"+website+"</a>";
				}
				else if (website.startsWith("http")) {
					domainName = "<a href='"+website+"' target='_bank'>"+website+"</a>";
				}
				else if (website.startsWith("<a")) {
					domainName = website;
				}
				else {
					website = "http://www." + website;
					domainName = "<a href='"+website+"' target='_bank'>"+website+"</a>";
				}
			}
			else {
				domainName = website;
			}
		}
		else {
			domainName = website;
		}		
		return domainName;
	}	
	
	/**
	 * 纠正domainName（储存）
	 * @param website 待纠正的domainName
	 * @return 待纠正后的domainName
	 * @author zengpzh
	 */
	public static String correctDomainName(String website) {
		String domainName = "";
		if (website != null && !"".equals(website.trim())) {
			website = website.toLowerCase();
			if (website.indexOf("http")>-1 || website.indexOf("www")>-1) {
				if (website.startsWith("www")) {
					website = "http://" + website;
					domainName = website;
				}
				else if (website.startsWith("http")) {
					domainName = website;
				}
				else {
					website = "http://www." + website;
					domainName = website;
				}
			}
			else {
				domainName = website;
			}
		}
		else {
			domainName = website;
		}		
		return domainName;
	}	
	
	/**
	 * 从Map中取得指定键的值
	 * @param map
	 * @param key 指定键
	 * @return 从Map中取得指定键的值
	 * @throws Exception
	 * @author zengpzh
	 */
	public static Object getObjFromMap(Map<String, Object> map, String key) throws Exception {
		if (map != null && key != null && !"".equals(key.trim())) {
			return map.get(key.toUpperCase());
		}
		else {
			return null;
		}
	}
	
	/**
	 * 从Map中取得指定键的值
	 * @param map
	 * @param key 指定键
	 * @return 从Map中取得指定键的值
	 * @throws Exception
	 * @author zengpzh
	 */
	public static String getStringFromMap(Map<String, Object> map, String key) throws Exception {
		Object obj = getObjFromMap(map, key);
		if (obj != null) {
			return String.valueOf(obj);
		}
		else {
			return ""; 
		}
	}
	
	/**
	 * 从Map中取得指定键的值
	 * @param map
	 * @param key 指定键
	 * @return 从Map中取得指定键的值
	 * @throws Exception
	 * @author zengpzh
	 */
	public static int getNumberFromMap(Map<String, Object> map, String key) throws Exception {
		Object obj = getObjFromMap(map, key);
		if (obj != null) {
			try {
				return ((BigDecimal)obj).intValue();
			} catch (Exception e) {
				try {
					return ((Integer)obj).intValue();
				} catch (Exception e2) {
					return 0;
				}
			}
		}
		else {
			return 0; 
		}
	}
	
	/**
	 * 从Map中取得指定键的值
	 * @param map
	 * @param key 指定键
	 * @return 从Map中取得指定键的值
	 * @throws Exception
	 * @author zengpzh
	 */
	public static double getDoubleFromMap(Map<String, Object> map, String key) throws Exception {
		Object obj = getObjFromMap(map, key);
		if (obj != null) {
			try {
				return ((BigDecimal)obj).doubleValue();
			} catch (Exception e) {
				try {
					return ((Integer)obj).doubleValue();
				} catch (Exception e2) {
					return 0.0;
				}
			}
		}
		else {
			return 0.0; 
		}
	}	
	
	/**
	 * 将Raw对象转成String
	 * @param obj
	 * @return
	 * @throws Exception
	 * @author zengpzh
	 */
	public static String rawObjToString(Object obj) throws Exception {
		String result = "";
		if (obj != null) {
			try {
				byte[] bytes = (byte[])obj;
				for (int i = 0 ;i <bytes.length; i++ ) {
					String aChar =  Integer.toHexString(bytes[i]& 0xFF);
					while (aChar.length() < 2) {
						aChar = "0" + aChar;
					}
					result += aChar;
				}
				} catch (Exception e) {
			}
		}
		return (result != null ? result.toUpperCase() : "");
	}	
	
	/**
	 * 获取在线帮助文件
	 * @param realPath
	 * @return
	 * @throws Exception
	 * @author zengpzh
	 */
	public static File[] getHelpOnlineFiles(String realPath) {
		try {
			File file = new File(realPath);
			return (file != null ? file.listFiles(Tools.getFileRegexFilter("ssk\\d{6}\\.htm")) : new File[0]);
		} catch (Exception e) {
			return new File[0];
		}
	}
	
	/**
	 * 获取在线帮助文件名
	 * @param realPath
	 * @return
	 * @throws Exception
	 * @author zengpzh
	 */
	public static ArrayList<String> getHelpOnlineFileNames(String realPath) {
		ArrayList<String> fileNames = new ArrayList<String>(0);
		try {
			File[] files = Tools.getHelpOnlineFiles(realPath);
			if (files != null) {
				for (File afile : files) {
					fileNames.add(afile.getName());
				}
			}
		} catch (Exception e) {
		}
		return fileNames;
	}
	
	/**
	 * 生成 FilenameFilter
	 * @param regex
	 * @return
	 * @author zengpzh
	 */
    private static FilenameFilter getFileRegexFilter(String regex) throws Exception {
        final String regex_ = regex;
        return new FilenameFilter() {
            public boolean accept(File file, String name) {
                boolean ret = name.matches(regex_); 
                return ret;
            }
        };
    }	
    
    /**
     * 生成序列号 1888->00001888
     * @param len 生成后序列号的总长度
     * @param number 当前整数序列号 如1888
     * @return
     * @author zengpzh
     */
    public static String getNumberString(Integer len,String number){
		int length=len-number.length();
		for(int i=0;i<length;i++){
		    number="0"+number;
		}
		return number;
    } 
    
    /**
     * 生成100-100000000的随机字符
     * @return
     * @author zengpzh
     */
    public static String getRandomData(){
		String number="1";
		int num=Integer.valueOf(Tools.random(1)).intValue();
		if(num>8){
		    num=8;
		}
		for(int i=0;i<num;i++){
		    number=number+"0";
		}
		return number;
    }    
    
	/**
	 * 从Map中取得指定键的值
	 * @param map
	 * @param key 指定键
	 * @return 从Map中取得指定键的值
	 * @throws Exception
	 * @author zengpzh
	 */
	public static String getRawAsStringFromMap(Map<String, Object> map, String key) throws Exception {
		return rawObjToString(getObjFromMap(map, key));
	}
	
	/**
	 * 从属性文件里载入属性
	 * @param propertyFileName 属性文件名（不需要扩展名，即：不包含 .properties 的文件） 
	 * @return ResourceBundle
	 * @throws Exception
	 * @author zengpzh
	 */
	public static ResourceBundle loadPropertiesFromPropertyFile(String propertyFileName) throws Exception {
		return ResourceBundle.getBundle(propertyFileName, Locale.CHINA);
	}
	
	/**
	 * 打印空行
	 */
	public static void println() {
		//System.out.println("");
	}

	public static String toMB(String value) {
		String result;
		if (value != null && !"".equals(value.trim())) {
			Double lv = Double.valueOf(value);
			if (lv > 1024) {
				lv = lv / 1024;
				if (lv > 1024) {
					lv = lv / 1024;
					result = String.format("%.2f", lv) + "(MB)";
				}
				else {
					result = String.format("%.2f", lv) + "(KB)";
				}
			}
			else {
				result = value + "(Byte)";
			}
		}
		else {
			result = value + "(Byte)";
		}
		return result;
	}

}