package com.chinawiserv.deepone.manager.core.util;

import java.text.SimpleDateFormat;

/**
 * 字符串验证器
 * <pre>
 * 本类提供字符串验证器的静态方法，主要方法有
 * 1、日期验证，匹配格式如: YYYY-MM-DD isValidatedDatetime_YYYYMMDD()
 * 2、日期验证，匹配格式如 : YYYY-MM-DD HH:MM isValidatedDatetime_YYYYMMDDHHMM()
 * 3、日期验证，匹配格式如 : YYYY-MM-DD HH:MM:SS isValidatedDatetime_YYYYMMDDHHMMSS()
 * 4、验证数字（[0-9]），恰好 numBit_n 次 isNumByBitExact()
 * 5、验证数字（[0-9]），至少 numBit_n 次，但是不超过 numBit_m 次 isNumByBitFromMinToMax()
 * 6、验证字母或数字（[a-zA-Z_0-9]），恰好 numBit_n 次 isCharNumByBitExact()
 * 7、验证字母或数字（[a-zA-Z_0-9]），至少 numBit_n 次，但是不超过 numBit_m 次 isCharNumByBitFromMinToMax()
 * 8、验证金额，小数点前：至少 numBit_n 次，但是不超过 numBit_m 次 ; 小数点后：两位、一位、没有小数位 isPrice()
 * 9、验证 E mail 格式 isEmail()
 * 10、电话号码：至少 n 次，但是不超过 m 次 isTel()
 * 11、判断一个字符串是否为全字母 isLetter()
 * 12、判断一个字符串是否为全数字 isNumeric()
 * 13、判断一个字符串是否为全数字或字母 isLetterOrNumeric()
 * 14、判断一个字符串是否为全汉字 isAllChineseCharacters()
 * 15、判断是否为允许注册的名称 isAllowRegisterName()
 * 16、验证是否是合格的中国邮政编码 isPostalcode()
 * 17、验证是否是合格的身份证号码   isIDcard()
 * 18、验证是否是合格的移动电话号码 checkMobile()
 * 19、验证是否是合格的固定电话号码 checkTel()
 * 20、验证是否是合格的价格   checkPrice()
 * </pre>
 * @author zengpzh
 * @version 0.1
 */
public class Validate {
	
	/**
	 * 日期验证，匹配格式如: YYYY-MM-DD
	 * @param datetime 日期字符串
	 * @return 合符上述格式，返回 true ，否则返回false
	 * @author Allen Zhang 
	 */
	public static boolean isValidatedDatetime_YYYYMMDD(String datetime) {
		if (datetime != null) {
			boolean validatedDatetime = datetime.matches("[123]\\d{3}-[01]\\d-[0123]\\d");
			if (validatedDatetime) {
				try {
			    	SimpleDateFormat formatter  =  new  SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			    	formatter.setLenient(false);
			        formatter.parse(datetime + " 00:00:00");
			        return true;
				} catch (Exception e) {
					return false;
				}
			}
			else {
				return false;					
			}
		}
		else {
			return false;
		}
	}
	
	/**
	 * 日期验证，匹配格式如: YYYY-MM-DD（可YYYY-M-D 之类的）
	 * @param datetime 日期字符串
	 * @return 合符上述格式，返回 true ，否则返回false
	 * @author Allen Zhang 
	 */
	public static boolean isValidatedDatetime_YYYYMMDD_FREE(String datetime) {
		if (datetime != null) {
			boolean validatedDatetime = datetime.matches("[123]\\d{3}-[01]{0,1}\\d-[0123]{0,1}\\d");
			if (validatedDatetime) {
				try {
			    	SimpleDateFormat formatter  =  new  SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			    	formatter.setLenient(false);
			        formatter.parse(datetime + " 00:00:00");
			        return true;
				} catch (Exception e) {
					return false;
				}
			}
			else {
				return false;					
			}
		}
		else {
			return false;
		}
	}
	
	
	/**
	 * 日期验证，匹配格式如 : YYYY-MM-DD HH:MM
	 * @param datetime 日期字符串
	 * @return 合符上述格式，返回 true ，否则返回false
	 * @author Allen Zhang 
	 */
	public static boolean isValidatedDatetime_YYYYMMDDHHMM(String datetime) {
		if (datetime != null) {
			boolean validatedDatetime = datetime.matches("[123]\\d{3}-[01]\\d-[0123]\\d\\s[012]\\d:[012345]\\d");
			if (validatedDatetime) {
				try {
			    	SimpleDateFormat formatter  =  new  SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			    	formatter.setLenient(false);
			        formatter.parse(datetime + ":00");
			        return true;
				} catch (Exception e) {
					return false;
				}
			}
			else {
				return false;					
			}
		}
		else {
			return false;
		}
	}

	/**
	 * 日期验证，匹配格式如 : YYYY-MM-DD HH:MM（可YYYY-M-D H:M之类的）
	 * @param datetime 日期字符串
	 * @return 合符上述格式，返回 true ，否则返回false
	 * @author Allen Zhang 
	 */
	public static boolean isValidatedDatetime_YYYYMMDDHHMM_FREE(String datetime) {
		if (datetime != null) {
			boolean validatedDatetime = datetime.matches("[123]\\d{3}-[01]{0,1}\\d-[0123]{0,1}\\d\\s[012]{0,1}\\d:[012345]{0,1}\\d");
			if (validatedDatetime) {
				try {
			    	SimpleDateFormat formatter  =  new  SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			    	formatter.setLenient(false);
			        formatter.parse(datetime + ":00");
			        return true;
				} catch (Exception e) {
					return false;
				}
			}
			else {
				return false;					
			}
		}
		else {
			return false;
		}
	}
	
	/**
	 * 日期验证，匹配格式如 : YYYY-MM-DD HH:MM:SS
	 * @param datetime 日期字符串
	 * @return 合符上述格式，返回 true ，否则返回false
	 * @author Allen Zhang 
	 */
	public static boolean isValidatedDatetime_YYYYMMDDHHMMSS(String datetime) {
		if (datetime != null) {
			boolean validatedDatetime = datetime.matches("[123]\\d{3}-[01]\\d-[0123]\\d\\s[012]\\d:[012345]\\d:[012345]\\d");
			if (validatedDatetime) {
				try {
			    	SimpleDateFormat formatter  =  new  SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			    	formatter.setLenient(false);
			        formatter.parse(datetime);
			        return true;
				} catch (Exception e) {
					return false;
				}
			}
			else {
				return false;					
			}
		}
		else {
			return false;
		}
	}

	/**
	 * 日期验证，匹配格式如 : YYYY-MM-DD HH:MM:SS（可YYYY-M-D H:M:S之类的）
	 * @param datetime 日期字符串
	 * @return 合符上述格式，返回 true ，否则返回false
	 * @author Allen Zhang 
	 */
	public static boolean isValidatedDatetime_YYYYMMDDHHMMSS_FREE(String datetime) {
		if (datetime != null) {
			boolean validatedDatetime = datetime.matches("[123]\\d{3}-[01]{0,1}\\d-[0123]{0,1}\\d\\s[012]{0,1}\\d:[012345]{0,1}\\d:[012345]{0,1}\\d");
			if (validatedDatetime) {
				try {
			    	SimpleDateFormat formatter  =  new  SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			    	formatter.setLenient(false);
			        formatter.parse(datetime);
			        return true;
				} catch (Exception e) {
					return false;
				}
			}
			else {
				return false;					
			}
		}
		else {
			return false;
		}
	}
	
	public static boolean isValidatedDatetime_YYYYMMDDHHMMSS_small(String datetime) {
		if (datetime != null) {
			boolean validatedDatetime = datetime.matches("[123]\\d{3}-[01]\\d-[0123]\\d\\s[012]\\d:[012345]\\d:[012345]\\d");
			if (validatedDatetime) {
				try {
			    	SimpleDateFormat formatter  =  new  SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
			    	formatter.setLenient(false);
			        formatter.parse(datetime);
			        return true;
				} catch (Exception e) {
					return false;
				}
			}
			else {
				return false;					
			}
		}
		else {
			return false;
		}
	}
	
	/**
	 * 验证数字（[0-9]），恰好 numBit_n 次
	 * @param numStr 待验证数字
	 * @param numBit_n 出现的次数
	 * @return 合符上述格式，返回 true ，否则返回false
	 * @author Allen Zhang 
	 */
	public static boolean isNumByBitExact(String numStr, int numBit_n) {
		if (numStr != null) {
			if (!"".equals(numStr) && numBit_n > 0) {
				return numStr.matches("\\d{"+numBit_n+"}"); 
			}
			else {
				return false;
			}
		}
		else {
			return false;
		}
	}

	/**
	 * 验证数字（[0-9]），至少 numBit_n 次，但是不超过 numBit_m 次
	 * @param numStr 待验证数字
	 * @param numBit_n 出现的最少numBit_n次数
	 * @param numBit_m 出现的最多numBit_n次数
	 * @return 合符上述格式，返回 true ，否则返回false
	 * @author Allen Zhang
	 */
	public static boolean isNumByBitFromMinToMax(String numStr, int numBit_n, int numBit_m) {
		if (numStr != null) {
			if (numBit_m >= numBit_n && numBit_n > 0 && numBit_m > 0) {
				if (!"".equals(numStr)) {
					return numStr.matches("\\d{"+numBit_n+","+numBit_m+"}"); 
				}
				else {
					return false;
				}
			}
			else {
				return false;
			}
		}
		else {
			return false;
		}
	}

	/**
	 * 验证字母或数字（[a-zA-Z_0-9]），恰好 numBit_n 次
	 * @param numStr 待验证数字
	 * @param numBit_n 出现的次数
	 * @return 合符上述格式，返回 true ，否则返回false
	 * @author Allen Zhang 
	 */
	public static boolean isCharNumByBitExact(String numStr, int numBit_n) {
		if (numStr != null) {
			if (!"".equals(numStr) && numBit_n > 0) {
				return numStr.matches("\\w{"+numBit_n+"}"); 
			}
			else {
				return false;
			}
		}
		else {
			return false;
		}
	}

	/**
	 * 验证字母或数字（[a-zA-Z_0-9]），至少 numBit_n 次，但是不超过 numBit_m 次
	 * @param numStr 待验证字母或数字
	 * @param numBit_n 出现的最少numBit_n次数
	 * @param numBit_m 出现的最多numBit_n次数
	 * @return 合符上述格式，返回 true ，否则返回false
	 * @author Allen Zhang 
	 */
	public static boolean isCharNumByBitFromMinToMax(String numStr, int numBit_n, int numBit_m) {
		if (numStr != null) {
			if (numBit_m >= numBit_n && numBit_n > 0 && numBit_m > 0) {
				if (!"".equals(numStr)) {
					return numStr.matches("\\w{"+numBit_n+","+numBit_m+"}"); 
				}
				else {
					return false;
				}
			}
			else {
				return false;
			}
		}
		else {
			return false;
		}
	}

	/**
	 * 验证金额，小数点前：至少 numBit_n 次，但是不超过 numBit_m 次 ; 小数点后：两位、一位、没有小数位
	 * @param numStr 待验证金额
	 * @param numBit_n 出现的最少numBit_n次数
	 * @param numBit_m 出现的最多numBit_n次数
	 * @return 合符上述格式，返回 true ，否则返回false
	 * @author Allen Zhang 
	 */
	public static boolean isPrice(String numStr, int numBit_n, int numBit_m) {
		if (numStr != null) {
			if (!"".equals(numStr) && numBit_n >= 1 && numBit_m > numBit_n) {
				return numStr.matches("\\d{"+numBit_n+","+numBit_m+"}\\.\\d\\d") ||
				       numStr.matches("\\d{"+numBit_n+","+numBit_m+"}\\.\\d") ||
				       numStr.matches("\\d{"+numBit_n+","+numBit_m+"}");
			}
			else {
				return false;
			}
		}
		else {
			return false;
		}
	}
	
	/**
	 * 验证 E mail 格式
	 * @param email Email地址 
	 * @return 如果为正确格式的Email地址的字符串，返回true，否则返回 false
	 */
	public static boolean isEmail(String email) {
		if (email != null) {
			if (!"".equals(email)) {
				return email.matches("\\b(^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@([A-Za-z0-9-])+(\\.[A-Za-z0-9-]+)*((\\.[A-Za-z0-9]{2,})|(\\.[A-Za-z0-9]{2,}\\.[A-Za-z0-9]{2,}))$)\\b");
			}
			else {
				return false;
			}
		}
		else {
			return false;
		}
	}	
	
	/**
	 * 电话号码：至少 n 次，但是不超过 m 次
	 * @param telStr
	 * @return
	 * @author zengpzh
	 */
	public static boolean isTel(String telStr) {
		if (telStr != null) {
			if (!"".equals(telStr)) {
				return telStr.matches("\\d{5,32}"); 
			}
			else {
				return false;
			}
		}
		else {
			return false;
		}
	}
	
	/**
	 * 判断一个字符串是否为全字母
	 * @param matchString 待匹配字符串
	 * @return 如果为全字母 返回true；否则返回false
	 * @author zengpzh
	 */
	public static boolean isLetter(String matchString){
		if (matchString != null) {
			return matchString.matches("[a-zA-Z]*");
		}
		else {
			return false;
		}
	}  	
	
	/**
	 * 判断一个字符串是否为全数字
	 * @param matchString 待匹配字符串
	 * @return 如果为全数字 返回true；否则返回false
	 * @author zengpzh
	 */
	public static boolean isNumeric(String matchString){
		if (matchString != null) {
			return matchString.matches("[0-9]*");
		}
		else {
			return false;
		}
	}  

	/**
	 * 判断一个字符串是否为全数字,可以包括小数点
	 * @param matchString 待匹配字符串
	 * @return 如果为全数字 返回true；否则返回false
	 * @author zengpzh
	 */
	public static boolean isNumericWithPoint(String matchString){
		if (matchString != null) {
			return matchString.matches("[0-9.]*");
		}
		else {
			return false;
		}
	}  
	
	/**
	 * 判断一个字符串是否为全数字或字母
	 * @param matchString 待匹配字符串
	 * @return 如果为全数字或字母 返回true；否则返回false
	 * @author zengpzh
	 */
	public static boolean isLetterOrNumeric(String matchString){
		if (matchString != null) {
			return matchString.matches("\\w*");
		}
		else {
			return false;
		}
	}  
	
	/**
	 * 判断一个字符串是否为全汉字
	 * @param matchString 待匹配字符串
	 * @return 如果为全汉字 返回true；否则返回false
	 * 注：\u4e00-\u9fa5 汉字的unicode码范围
	 * @author zengpzh
	 */
	public static boolean isAllChineseCharacters(String matchString) {
		if (matchString != null) {
			return matchString.matches("[\u4e00-\u9fa5]*");
		}
		else {
			return false;
		}
	}
	
	/**
	 * 判断是否为允许注册的名称
	 * @param matchString
	 * @return 为允许注册的名称 返回true, 否则返回 false
	 * @author zengpzh
	 */
	public static boolean isAllowRegisterName(String matchString) {
		if (matchString != null && !"".equals(matchString.trim())) {
			boolean result = true;
			for (int i = 0 ; i < matchString.length() ; i ++) {
				String tempString = String.valueOf(matchString.charAt(i));
				result = isAllChineseCharacters(tempString) || isLetterOrNumeric(tempString);
				if (!result) {
					break;
				}
			}
			return result;
		}
		else {
			return false;
		}
	}
	
		/**
	 * 判断一个字符串是否为中国邮政编码（6位数字）
	 * @param matchString 待匹配字符串
	 * @return 如果是 返回true；否则返回false
	 * @author zh
	 */
	public static boolean isPostalcode(String matchString){
		if (matchString != null) {
			return matchString.matches("^\\d{6}$");
		}
		else {
			return false;
		}
	}  
	
	/**
	 * 判断一个字符串是否为合格的身份证号码 
	 * 15位：^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$
	 * 18位：^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([\\d|x|X]{1})$
	 * @param matchString 待匹配字符串
	 * @return 如果是 返回true；否则返回false
	 * @author zh
	 */
	public static boolean isIDcard(String matchString){
		if (matchString != null) {
			return matchString.matches("(^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$)|" +
					"(^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([\\d|x|X]{1})$)");
		}
		else {
			return false;
		}
	}  
	
	/**
	 * 判断一个字符串是否为移动电话
	 * @param matchString 待匹配字符串
	 * @return 如果为移动电话 返回true；否则返回false
	 * @author wyz
	 */
	public static boolean checkMobile(String matchString){
		if (matchString != null) {
			return matchString.matches("^(?:13\\d|15\\d|18\\d)-?\\d{5}(\\d{3}|\\*{3})$");
		}
		else {
			return false;
		}
	} 
	
	/**
	 * 判断一个字符串是否为固定电话
	 * @param matchString 待匹配字符串
	 * @return 如果为固定电话 返回true；否则返回false
	 * @author wyz
	 */
	public static boolean checkTel(String matchString){
		if (matchString != null) {
			return matchString.matches("^(([0\\+]\\d{2,3}[-_]?)?(0\\d{2,3})[-_]?)?(\\d{7,8})([-_]?(\\d{3,}))?$");
		}
		else {
			return false;
		}
	} 
	
	/**
	 * 判断一个字符串是否为价格
	 * @param matchString 待匹配字符串
	 * @return 如果为价格 返回true；否则返回false
	 * @author wyz
	 */
	public static boolean checkPriceBuy(String matchString){
		if (matchString != null) {
			//return matchString.matches("^\\d{1,10}$|^\\d{1,10}\\.\\d{1,2}\\w?$");
			boolean flag = matchString.matches("^\\d{1,10}$|^\\d{1,10}\\.\\d{1,2}\\w?$");
			if (flag) {
				if (Float.parseFloat(matchString) <= 50) {
					return true;
				}else {
					return false;
				}
			}else {
				return false;
			}
		}
		else {
			return false;
		}
	} 
	public static boolean checkPriceSale(String matchString){
		if (matchString != null) {
			//return matchString.matches("^\\d{1,10}$|^\\d{1,10}\\.\\d{1,2}\\w?$");
			boolean flag = matchString.matches("^\\d{1,10}$|^\\d{1,10}\\.\\d{1,2}\\w?$");
			if (flag) {
				if (Float.parseFloat(matchString) <= 100) {
					return true;
				}else {
					return false;
				}
			}else {
				return false;
			}
		}
		else {
			return false;
		}
	} 
	
	
	/**
	 * 判断一个字符串是否为价格，不限制长度
	 * @param matchString 待匹配字符串
	 * @return 如果为价格 返回true；否则返回false
	 * @author yuxl
	 */
	public static boolean IsPrice(String matchString){
		if (matchString != null) {
			//return matchString.matches("^\\d{1,10}$|^\\d{1,10}\\.\\d{1,2}\\w?$");
			boolean flag = matchString.matches("^\\d{1,10}$|^\\d{1,10}\\.\\d{1,2}\\w?$");
			if (flag) {	
					return true;
				
			}else {
				return false;
			}
		}
		else {
			return false;
		}
	} 
	
	
	/**
	 * 判断一个字符串是否为全数字
	 * @param matchString 待匹配字符串
	 * @return 如果为全数字 返回true；否则返回false
	 * @author zengpzh
	 */
	public static boolean isInt(String matchString){
		if (matchString != null) {
			return matchString.matches("[0-9]*");
		}
		else {
			return false;
		}
	}
	
	/**
	 * 判断一个字符串是否是固定电话或移动电话
	 * @param matchString
	 * @return boolean
	 * @author zh
	 */
	public static boolean isTelOrMobile(String matchString){
		if (matchString != null) {
			return (checkTel(matchString) || checkMobile(matchString));
		}
		else {
			return false;
		}
	}
	
	/**
	 * 判断一个字符串是否为证件编号，编号必须为数字、字母、"-"或其组合且第一位只能为数字或字母，结尾也为数字或字母
	 * @param matchString 待匹配字符串
	 * @return 如果为证件编号 返回true；否则返回false
	 * @author ZXB
	 */
	public static boolean checkCard(String matchString){
		if (matchString != null) {
			return matchString.matches("^[a-zA-Z0-9]|[a-zA-Z0-9-]{1,30}[a-zA-Z0-9]$");
		}
		else {
			return false;
		}
	} 
}