package com.chinawiserv.deepone.manager.core.dao;

import com.chinawiserv.deepone.manager.core.util.BeanUtil;
import com.chinawiserv.deepone.manager.core.util.DateTime;
import com.chinawiserv.deepone.manager.core.util.StringUtil;
import com.chinawiserv.deepone.manager.core.util.Tools;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.util.*;

/**
 * 工具模板
 * <pre>
 * 本类主要提供一些共用的访问方法，主要包含：
 *   1、日期、时间处理工具
 *   2、从Map中取得指定键的值
 * </pre>
 * @author zengpzh
 * @version 0.1
 */
public class ToolTemplate implements Serializable {
	
	private static final long serialVersionUID = 2985727482684794800L;
	
	private static final Log _log = LogFactory.getLog(ToolTemplate.class);
	
	/**
	 * 装载Bean
	 * 从Map里获取与Bean属性名称相同的Key键对应的值填充到Bean对应的属性
	 * @param instance 被装载的Bean
	 * @param map 数据来源的Map
	 * @return 载入成功的属性数量
	 * @author zengpzh
	 */
	public int populateBeanFormMap(Object instance, Map<String, Object> map) {
		return BeanUtil.populateBeanFormMap(instance, map);
	}	

	/**
	 * Bean属性拷贝
	 * Bean间存在名称不相同的属性，则PropertyUtils不对这些属性进行处理，需要程序员手动处理。
	 * BeanUtils发现名称相同但类型不同的属性，PropertyUtils进行类型转换（在支持的数据类型范围内进行转换）。
	 * @param dest 目标Bean
	 * @param orig 原始Bean
	 * @throws Exception
	 * @author zengpzh
	 */
	public void copyProperties(Object dest, Object orig) throws Exception {
		BeanUtil.copyProperties(dest, orig);
	}
	
	/**
	 * 根据指定属性名称从指定例实中获取该的属性值
	 * @param instance 实例
	 * @param propertyName 属性名称
	 * @return 属性值
	 * @throws Exception
	 */
	public Object getProperty(Object instance, String propertyName) throws Exception {
		return BeanUtil.getProperty(instance, propertyName);
	}

	/**
	 * 获取一个Bean的所有其属性值不为空(null)的属性，以Map的键、值形式存到Map中，属性值保持原来的类型
	 * @param instance 一个Bean
	 * @return 所有其属性值不为空(null)的属性
	 * @author zengpzh
	 */
	public Map<?, ?> getPropertiesWithoutNullValueProperty(Object instance) {
		return BeanUtil.getPropertiesWithoutNullValueProperty(instance);
	}

	/**
	 * 获取一个Bean的所有其属性值不为空(null)的属性名称
	 * @param instance 一个Bean
	 * @return 所有其属性值不为空(null)的属性名称
	 * @author zengpzh
	 */
	public List<String> getPropertyNamesWithoutNullValueProperty(Object instance) {
		return BeanUtil.getPropertyNamesWithoutNullValueProperty(instance);
	}
	
	/**
	 * 从Map中取得指定键的值
	 * @param map
	 * @param key 指定键
	 * @return 从Map中取得指定键的值
	 * @throws Exception
	 * @author zengpzh
	 */
	public Object getObjFromMap(Map<String, Object> map, String key) throws Exception {
		return Tools.getObjFromMap(map, key);
	}
	
	/**
	 * 从Map中取得指定键的值
	 * @param map
	 * @param key 指定键
	 * @return 从Map中取得指定键的值
	 * @throws Exception
	 * @author zengpzh
	 */
	public String getStringFromMap(Map<String, Object> map, String key) throws Exception {
		return Tools.getStringFromMap(map, key);
	}
	
	public String getStringFromMap_new(Map<String, Object> map, String key) throws Exception {
		return Tools.getStringFromMap(map, key).replace("'", "");
	}
	
	/**
	 * 从Map中取得指定键的值
	 * @param map
	 * @param key 指定键
	 * @return 从Map中取得指定键的值
	 * @throws Exception
	 * @author zengpzh
	 */
	public int getNumberFromMap(Map<String, Object> map, String key) throws Exception {
		return Tools.getNumberFromMap(map, key);
	}
	
	/**
	 * 从Map中取得指定键的值
	 * @param map
	 * @param key 指定键
	 * @return 从Map中取得指定键的值
	 * @throws Exception
	 * @author zengpzh
	 */
	public double getDoubleFromMap(Map<String, Object> map, String key) throws Exception {
		return Tools.getDoubleFromMap(map, key);
	}
	
	/**
	 * 获取上下文基础地址
	 * @return 上下文基础地址
	 * @author zengpzh
	 */
	public String getBasePath(HttpServletRequest request) throws Exception {
		return StringUtil.getBasePath(request);
	}
	
	/**
	 * 切字符串，把源字符串切为指定长度的字符串。
	 * @param sourceStr 源字符串
	 * @param strLength 指定长度
	 * @return 切后字符串
	 * @author zengpzh
	 */
	protected String sliceString(String sourceStr, int strLength) throws Exception {
		return StringUtil.sliceString(sourceStr, strLength);
	}
	
	/**
	 * 转化时间成指定格式的字符串
	 * @param currentDate 待转化给定日期时间
	 * @param datetimeFormat 为日期、时间指定的格式
	 * @return 按指定格式转后的日期、时间字符串
	 * @author Allen Zhang
	 */
	public String convertDateTime(Date currentDate, String datetimeFormat) {
		return DateTime.convertDateTime(currentDate, datetimeFormat);
	}
	
	/**
	 * 将指定格式的字符串日期时间转化为Date类型的日期时间
	 * @param datetimeStr 指定格式的日期日期字符串
	 * @param datetimeFormat 为日期、时间指定的格式
	 * @return 转换成的Date类型的日期时间
	 * @author Allen Zhang
	 */
	public Date parseDateTime(String datetimeStr, String datetimeFormat) {  
		return DateTime.parseDateTime(datetimeStr, datetimeFormat);
	}  	
	
    /**
     * 日期 加 年，月，天，时，分，秒 数
     * @param currentDate 当前时间
     * @param AddType 添加类型  年，月，天，时，分，秒
     * @param dateTimeCount 数量
     * @return 加后的日期
     * @author Allen Zhang
     */
    public Date addDateTime(Date currentDate, int AddType, int dateTimeCount) {
    	return DateTime.addDateTime(currentDate, AddType, dateTimeCount);
    }

	/**
	 * 获取当前时间
	 * 格式：yyyy-MM-dd
	 * @return 当前时间的字符串形式
	 * @author Allen Zhang
	 */
	public String getCurrentDate_YYYYMMDD() {
		return DateTime.getCurrentDate_YYYYMMDD();
	}

	/**
	 * 获取当前时间
	 * 格式：yyyy-MM-dd HH:mm
	 * @return 当前时间的字符串形式
	 * @author Allen Zhang
	 */
	public String getCurrentDate_YYYYMMDDHHMM() {
		return DateTime.getCurrentDate_YYYYMMDDHHMM();
	}
	
	/**
	 * 获取当前时间
	 * 格式：yyyy-MM-dd HH:mm:ss
	 * @return 当前时间的字符串形式
	 * @author Allen Zhang
	 */
	public String getCurrentDate_YYYYMMDDHHMMSS() {
		return DateTime.getCurrentDate_YYYYMMDDHHMMSS();
	}

	/**
	 * 获取当前时间
	 * 格式：yyyy-MM-dd HH:mm:ss 包含毫秒
	 * @return 当前时间的字符串形式
	 * @author Allen Zhang
	 */
	public String getCurrentDate_YYYYMMDDHHMMSS_millisecond() {
		return DateTime.getCurrentDate_YYYYMMDDHHMMSS_millisecond();
	}

	
	/**
	 * 获取当前时间
	 * 格式：yyyyMMddHHmmss
	 * @return 当前时间的字符串形式
	 * @author Allen Zhang
	 */
	public String getCurrentDate_YYYYMMDDHHMMSSWithOutSeparator() {
		return DateTime.getCurrentDate_YYYYMMDDHHMMSSWithOutSeparator();
	}

	/**
	 * 获取当前时间
	 * 格式：yyyy年MM月dd日
	 * @return 当前时间的字符串形式
	 * @author Allen Zhang
	 */
	public String getCurrentDate_YYYYMMDD_CN() {
		return DateTime.getCurrentDate_YYYYMMDD_CN();
	}

	/**
	 * 获取当前时间
	 * 格式：yyyy年MM月dd日HH时mm分
	 * @return 当前时间的字符串形式
	 * @author Allen Zhang
	 */
	public String getCurrentDate_YYYYMMDDHHMM_CN() {
		return DateTime.getCurrentDate_YYYYMMDDHHMM_CN();
	}
	
	/**
	 * 获取当前时间
	 * 格式：HH:mm:ss
	 * @return 当前时间的字符串形式
	 * @author Allen Zhang
	 */
	public String getCurrentDate_HHMMSS() {
		return DateTime.getCurrentDate_HHMMSS();
	}
	
	/**
	 * 获取当前时间
	 * 格式：yyyy年MM月dd日HH时mm分ss秒
	 * @return 当前时间的字符串形式
	 * @author Allen Zhang
	 */
	public String getCurrentDate_YYYYMMDDHHMMSS_CN() {
		return DateTime.getCurrentDate_YYYYMMDDHHMMSS_CN();
	}

	/**
	 * 转换时间
	 * 格式：yyyy-MM-dd
	 * @param currentDate 等转换日期
	 * @return 当前时间的字符串形式
	 * @author Allen Zhang
	 */
	public String convertDateTime_YYYYMMDD(Date currentDate) {
		return DateTime.convertDateTime_YYYYMMDD(currentDate);
	}
	
	/**
	 * 转换时间
	 * 格式：yyyy-MM-dd
	 * @param currentDate 等转换日期
	 * @return 当前时间的字符串形式
	 * @author Allen Zhang
	 */
	public String convertDateTime_MMDD(Date currentDate) {
		return DateTime.convertDateTime_MMDD(currentDate);
	}
	/**
	 * 转换时间
	 * 格式：yyyy-MM-dd HH:mm
	 * @param currentDate 等转换日期
	 * @return 当前时间的字符串形式
	 * @author Allen Zhang
	 */
	public String convertDateTime_YYYYMMDDHHMM(Date currentDate) {
		return DateTime.convertDateTime_YYYYMMDDHHMM(currentDate);
	}
	
	/**
	 * 转换时间
	 * 格式：yyyy-MM-dd HH:mm:ss
	 * @param currentDate 等转换日期
	 * @return 当前时间的字符串形式
	 * @author Allen Zhang
	 */
	public String convertDateTime_YYYYMMDDHHMMSS(Date currentDate) {
		return DateTime.convertDateTime_YYYYMMDDHHMMSS(currentDate);
	}
	
	/**
	 * 转换时间
	 * 格式：dd日HH:mm
	 * @param currentDate 等转换日期
	 * @return 当前时间的字符串形式
	 * @author Allen Zhang
	 */
	public String convertDateTime_DDHHMM(Date currentDate) {
		return DateTime.convertDateTime_DDHHMM(currentDate);
	}

	/**
	 * 转换时间
	 * 格式：HH:mm:ss
	 * @param currentDate 等转换日期
	 * @return 当前时间的字符串形式
	 * @author Allen Zhang
	 */
	public String convertDateTime_HHMMSS(Date currentDate) {
		return DateTime.convertDateTime_HHMMSS(currentDate);
	}

	/**
	 * 转换时间
	 * 格式：HH:mm
	 * @param currentDate 等转换日期
	 * @return 当前时间的字符串形式
	 * @author Allen Zhang
	 */
	public String convertDateTime_HHMM(Date currentDate) {
		return DateTime.convertDateTime_HHMM(currentDate);
	}

	/**
	 * 转换时间
	 * 格式：yyyy年MM月dd日
	 * @param currentDate 等转换日期
	 * @return 当前时间的字符串形式
	 * @author Allen Zhang
	 */
	public String convertDateTime_YYYYMMDD_CN(Date currentDate) {
		return DateTime.convertDateTime_YYYYMMDD_CN(currentDate);
	}
	
	/**
	 * 转换时间
	 * 格式：yyyy年MM月dd日HH时mm分
	 * @param currentDate 等转换日期
	 * @return 当前时间的字符串形式
	 * @author Allen Zhang
	 */
	public String convertDateTime_YYYYMMDDHHMM_CN(Date currentDate) {
		return DateTime.convertDateTime_YYYYMMDDHHMM_CN(currentDate);
	}
	
	/**
	 * 转换时间
	 * 格式：yyyy年MM月dd日HH时mm分ss秒
	 * @param currentDate 等转换日期
	 * @return 当前时间的字符串形式
	 * @author Allen Zhang
	 */
	public String convertDateTime_YYYYMMDDHHMMSS_CN(Date currentDate) {
		return DateTime.convertDateTime_YYYYMMDDHHMMSS_CN(currentDate);
	}
	
	/**
	 * 解析时间
	 * 格式：yyyy-MM-dd
	 * @param currentDate 等解析日期
	 * @return 当前时间的字符串形式
	 * @author Allen Zhang
	 */
	public Date parseDateTime_YYYYMMDD(String currentDate) {
		return DateTime.parseDateTime_YYYYMMDD(currentDate);
	}
	
	/**
	 * 解析时间
	 * 格式：yyyy-MM-dd HH:mm
	 * @param currentDate 等解析日期
	 * @return 当前时间的字符串形式
	 * @author Allen Zhang
	 */
	public Date parseDateTime_YYYYMMDDHHMM(String currentDate) {
		return DateTime.parseDateTime_YYYYMMDDHHMM(currentDate);
	}
	
	/**
	 * 解析时间
	 * 格式：yyyy-MM-dd HH:mm:ss
	 * @param currentDate 等解析日期
	 * @return 当前时间的字符串形式
	 * @author Allen Zhang
	 */
	public Date parseDateTime_YYYYMMDDHHMMSS(String currentDate) {
		return DateTime.parseDateTime_YYYYMMDDHHMMSS(currentDate);
	}

	/**
	 * 解析时间
	 * 格式：yyyy年MM月dd
	 * @param currentDate 等解析日期
	 * @return 当前时间的字符串形式
	 * @author Allen Zhang
	 */
	public Date parseDateTime_YYYYMMDD_CN(String currentDate) {
		return DateTime.parseDateTime_YYYYMMDD_CN(currentDate);
	}
	
	/**
	 * 解析时间
	 * 格式：yyyy年MM月dd日HH时mm分
	 * @param currentDate 等解析日期
	 * @return 当前时间的字符串形式
	 * @author Allen Zhang
	 */
	public Date parseDateTime_YYYYMMDDHHMM_CN(String currentDate) {
		return DateTime.parseDateTime_YYYYMMDDHHMM_CN(currentDate);
	}
	
	/**
	 * 解析时间
	 * 格式：yyyy年MM月dd日HH时mm分ss秒
	 * @param currentDate 等解析日期
	 * @return 当前时间的字符串形式
	 * @author Allen Zhang
	 */
	public Date parseDateTime_YYYYMMDDHHMMSS_CN(String currentDate) {
		return DateTime.parseDateTime_YYYYMMDDHHMMSS_CN(currentDate);
	}

    /**
     * 获取当前日期的第二天
     * @param day
     * @return
     * @author zengpzh
     */
    public String getTomorrow_YYYYMMDD(String day){
		return DateTime.getTomorrow_YYYYMMDD(day);
    }
	
	/**
     * 日期 加 年 数
     * @param currentDate 当前时间
     * @param years 年数量
     * @return 加后的日期
     * @author Allen Zhang
     */
    public Date addYears(Date currentDate, int years) {
    	return DateTime.addYears(currentDate, years);
    }
    
    /**
     * 日期 加 月 数
     * @param currentDate 当前时间
     * @param months 月数量
     * @return 加后的日期
     * @author Allen Zhang
     */
    public Date addMonths(Date currentDate, int months) {
    	return DateTime.addMonths(currentDate, months);
    }
    
    /**
     * 日期 加 天 数
     * @param currentDate 当前时间
     * @param days 日数量
     * @return 加后的日期
     * @author Allen Zhang
     */
    public Date addDays(Date currentDate, int days) {
    	return DateTime.addDays(currentDate, days);
    }

    /**
     * 日期 加 小时 数
     * @param currentDate 当前时间
     * @param hours 小时数量
     * @return 加后的日期
     * @author Allen Zhang
     */
    public Date addHours(Date currentDate, int hours) {
    	return DateTime.addHours(currentDate, hours);
    }

    /**
     * 日期 加 分 数
     * @param currentDate 当前时间
     * @param minutes 分钟数量
     * @return 加后的日期
     * @author Allen Zhang
     */
    public Date addMinutes(Date currentDate, int minutes) {
    	return DateTime.addMinutes(currentDate, minutes);
    }

    /**
     * 日期 加 秒 数
     * @param currentDate 当前时间
     * @param seconds 秒数量
     * @return 加后的日期
     * @author Allen Zhang
     */
    public Date addSeconds(Date currentDate, int seconds) {
    	return DateTime.addSeconds(currentDate, seconds);
    }

   /**
    * 取 星期几
    * @param currentDate 当前日期
    * @return 星期几
    * @author Allen Zhang
    */
   public String getWeekByDate(Date currentDate) {
	   return DateTime.getWeekByDate(currentDate);
   }
   
    /**
     * 获取两个 Date 相差的 天数
     * @param dateBeg 开始日期
     * @param dateEnd 结束日期
     * @return 两个 Date 相差的 天数
     * @author Allen Zhang
     */
    public long getDays(Date dateBeg, Date dateEnd) {
    	return DateTime.getDays(dateBeg, dateEnd);
    }

    /**
     * 获取两个 Date 相差的 分钟数
     * @param dateBeg 开始日期
     * @param dateEnd 结束日期
     * @return 两个 Date 相差的 分钟数
     * @author Allen Zhang
     */
    public long getMinutes(Date dateBeg, Date dateEnd) {
    	return DateTime.getMinutes(dateBeg, dateEnd);
    }

    /**
     * 获取两个 Date 相差的 分钟数
     * @param dateBeg 开始日期
     * @param dateEnd 结束日期
     * @return 两个 Date 相差的 分钟数
     * @author Allen Zhang
     */
    public long getMinutes_No_abs(Date dateBeg, Date dateEnd) {
    	return DateTime.getMinutes_No_abs(dateBeg, dateEnd);
    }
    
	/**
	 * 生成 bit 位 随即数
	 * @return  bit 位 随即数
	 * @author Allen Zhang
	 */
	public String random(int bit) {
		return Tools.random(bit);
	}
	
	/**
	 * 处理字符串中的空格
	 * @param aString
	 * @return
	 * @author zengpzh
	 */
	public String handleBlank(String aString) {
		return StringUtil.handleBlank(aString);
	}

	/**
	 * 生成主建值
	 * @return
	 * @throws Exception
	 * @author zengpzh
	 */
	public String createGuid() throws Exception {
		return UUID.randomUUID().toString();
	}
	
	/**
	 * 把字符串写入文本中
	 * 
	 * @param fileName 生成的文件绝对路径
	 * @param content 文件要保存的内容
	 * @param encode 文件编码
	 * @return
	 */
	public boolean writeStringToFile(String fileName, String content,String encode) {
	    File file = new File(fileName);
	    try {
		if (file.isFile()&&file.canRead()) {
		    file.deleteOnExit();
		    file = new File(file.getAbsolutePath());
		}
		OutputStreamWriter os = null;
		if (StringUtils.isBlank(encode)) {
		    encode="utf-8";
		}
		os = new OutputStreamWriter(new FileOutputStream(file), encode);
		os.write(content);
		os.close();
	    } 
	    catch (Exception e) {
	    	if (_log.isDebugEnabled()) {
	    		_log.debug("ToolTemplate.writeStringToFile():"+e.getMessage());
	    	}
	    	return false;
	    }
		return true;
	}
	
	public String getRawAsStringFromMap(Map<String, Object> map, String key) throws Exception {
		return Tools.getRawAsStringFromMap(map, key);
	}
	
	public List<Object> newList(Object... params) throws Exception {
		ArrayList<Object> list;
		if (params != null && params.length > 0) {
			list = new ArrayList<Object>(params.length);
			for (Object param : params) {
				if (param != null) {
					list.add(param);
				}
			}
			list.trimToSize();
		}
		else {
			list = new ArrayList<Object>();
		}
		return list;
	}
	
	/**
	 * 求时间差
	 * @param beginDate 开始时间
	 * @param endDate 结束时间
	 * @return 时间差（X天X小时X分）
	 */
	public String dateDiff(Date beginDate, Date endDate) {
		return DateTime.dateDiff(beginDate, endDate);
	}

	/**
	 * 以指定字符串包装以指定分隔符分隔的数组字符串的各个元素
	 * @param stringArray 数组字符串
	 * @param split 分隔符
	 * @param wrap 包装符
	 * @return 各元素被包装后的数组字符串
	 */
	public String wrapStringArray(String stringArray, String split, String wrap){
		if(StringUtils.isNotBlank(stringArray)){
			if(StringUtils.isBlank(split)) split = ",";
			if(StringUtils.isBlank(wrap)) wrap = "'";
			String[] tempstringArray = stringArray.split(split);
			StringBuffer wrapstringArray = new StringBuffer();
			for(String tempRemoveGuid : tempstringArray){
				if(StringUtils.isNotBlank(tempRemoveGuid)){
					wrapstringArray.append(split);
					wrapstringArray.append(wrap);
					wrapstringArray.append(tempRemoveGuid);
					wrapstringArray.append(wrap);
				}
			}
			return wrapstringArray.toString().replaceFirst(split, "");
		}
		return stringArray;
	}
}