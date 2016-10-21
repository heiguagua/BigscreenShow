package com.chinawiserv.deepone.manager.core.util;

import org.apache.commons.beanutils.PropertyUtils;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.*;

/**
 * Bean工具类
 * @author zengpzh
 * @version 0.1
 */
public class BeanUtil {
	
	/**
	 * 装载Bean
	 * 从Map里获取与Bean属性名称相同的Key键对应的值填充到Bean对应的属性
	 * @param instance 被装载的Bean
	 * @param map 数据来源的Map
	 * @return 载入成功的属性数量
	 * @author zengpzh
	 */
	public static int populateBeanFormMap(Object instance, Map<String, Object> map) {
		int successTotal = 0;
		List<String> propertyNames = getPropertyNames(instance);
		if (propertyNames != null) {
			for (String propertyName : propertyNames) {
				try {
					Object obj = Tools.getObjFromMap(map, propertyName);
					if (obj instanceof BigDecimal) {
						PropertyUtils.setProperty(instance, propertyName, Double.valueOf(String.valueOf(obj)));
					}
					else {
						PropertyUtils.setProperty(instance, propertyName, obj);
					}
					successTotal++;
				} catch (Exception e) {
				}
			}
		}
		return successTotal;
	}	

	/**
	 * 获取一个Bean的所有属性，以Map的键、值形式存到Map中，属性值保持原来的类型
	 * @param instance 一个Bean
	 * @return 所有属性
	 * @author zengpzh
	 */
	public static Map<?, ?> getProperties(Object instance) {
		if (instance != null) {
			try {
				Map<?, ?> map = PropertyUtils.describe(instance);
				if (map != null) {
					map.remove("class");
				}
				return map;
			} catch (Exception e) {
				return null;
			}
		}
		else {
			return null;
		}
	}
	
	/**
	 * 获取一个Bean的所有属性名称
	 * @param instance 一个Bean
	 * @return 所有属性名称
	 * @author zengpzh
	 */
	public static List<String> getPropertyNames(Object instance) {
		ArrayList<String> propertyNames = new ArrayList<String>();
		if (instance != null) {
			Map<?, ?> map = getProperties(instance);
			if (map != null) {
				Set<?> keySet = map.keySet();
				Iterator<?> it = keySet.iterator();
				if (it != null) {
					while (it.hasNext()) {
						String propertyName = String.valueOf(it.next()); 
						if (propertyName != null && !"".equals(propertyName.trim())) {
							propertyNames.add(propertyName);
						}
					}
				}
			}
		}
		propertyNames.trimToSize();
		return propertyNames;
	}

	/**
	 * 获取一个Bean的所有其属性值不为空(null)的属性名称
	 * @param instance 一个Bean
	 * @return 所有其属性值不为空(null)的属性名称
	 * @author zengpzh
	 */
	public static List<String> getPropertyNamesWithoutNullValueProperty(Object instance) {
		ArrayList<String> propertyNames = new ArrayList<String>();
		if (instance != null) {
			Map<?, ?> map = getProperties(instance);
			if (map != null) {
				Set<?> keySet = map.keySet();
				Iterator<?> it = keySet.iterator();
				if (it != null) {
					while (it.hasNext()) {
							String propertyName = String.valueOf(it.next());
							if (propertyName != null && !"".equals(propertyName.trim()) && propertyName.indexOf("_SaveDB") < 0) {
								try {
									Object propertyValue = map.get(propertyName);
									if (propertyValue != null && !"null".equalsIgnoreCase(String.valueOf(propertyValue))) {
										propertyNames.add(propertyName);
									}
								} catch (Exception e) {
								}
							}
					}
				}
			}
		}
		propertyNames.trimToSize();
		return propertyNames;
	}
	
	/**
	 * 获取一个Bean的所有其属性值不为空(null)的属性，以Map的键、值形式存到Map中，属性值保持原来的类型
	 * @param instance 一个Bean
	 * @return 所有其属性值不为空(null)的属性
	 * @author zengpzh
	 */
	public static Map<?, ?> getPropertiesWithoutNullValueProperty(Object instance) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if (instance != null) {
			Map<?, ?> map = getProperties(instance);
			if (map != null) {
				Set<?> keySet = map.keySet();
				Iterator<?> it = keySet.iterator();
				if (it != null) {
					while (it.hasNext()) {
						String propertyName = String.valueOf(it.next());
						if (propertyName != null && !"".equals(propertyName.trim()) && propertyName.indexOf("_SaveDB") < 0) {
							String propertyName_SaveDB = propertyName + "_SaveDB";
							try {
								Object propertyValue = map.get(propertyName);
								Object propertyValue_SaveDB = map.get(propertyName_SaveDB);
								if (propertyValue_SaveDB != null) {
									if ("true".equalsIgnoreCase(propertyValue_SaveDB.toString())) {
										if (propertyValue != null && !"null".equalsIgnoreCase(String.valueOf(propertyValue))) {
											resultMap.put(propertyName, propertyValue);
										}
									}
								}
								else {
									if (propertyValue != null && !"null".equalsIgnoreCase(String.valueOf(propertyValue))) {
										resultMap.put(propertyName, propertyValue);
									}
								}
							} catch (Exception e) {
							}
						}
					}
				}
			}
		}
		return resultMap;
	}
	
	/**
	 * 根据指定属性名称从指定例实中获取该的属性值
	 * @param instance 实例
	 * @param propertyName 属性名称
	 * @return 属性值
	 * @throws Exception
	 */
	public static Object getProperty(Object instance, String propertyName) throws Exception {
		return PropertyUtils.getProperty(instance, propertyName);
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
	public static void copyProperties(Object dest, Object orig) throws Exception {
		final String dateFormat = "_DateFormat";
		List<String> origPropertyNames = getPropertyNamesWithoutNullValueProperty(orig);
		List<String> destPropertyNames = getPropertyNames(dest);
		if (origPropertyNames != null && destPropertyNames != null) {
			Map<String, String> propertiesType = getPropertiesType(dest);
			for (String propertyName : origPropertyNames) {
				if (destPropertyNames.contains(propertyName)) {
					try {
						String type = propertiesType.get(propertyName);
						if ("java.util.Date".equalsIgnoreCase(type)) {
							String datetimeFormat = String.valueOf(PropertyUtils.getProperty(orig, propertyName+dateFormat));
							if (datetimeFormat == null || "".equals(datetimeFormat.trim())) {
								datetimeFormat = "yyyy-MM-dd HH:mm:ss";
							}
							else {
								datetimeFormat = datetimeFormat.replace("Y", "y").
								                                replace("D", "d").
								                                replace("h", "H").
								                                replace("S", "s").
								                                replace("-mm", "-MM").
								                                replace("mm-", "MM-").
								                                replace(":MM", ":mm").
								                                replace("MM:", "mm:")
								                                ;
							}
							Date date = DateTime.parseDateTime(String.valueOf(PropertyUtils.getProperty(orig, propertyName)), datetimeFormat);
							PropertyUtils.setProperty(dest, propertyName, date);
						}
						else if ("java.lang.Double".equalsIgnoreCase(type) || "double".equalsIgnoreCase(type)) {
							PropertyUtils.setProperty(dest, propertyName, Double.valueOf(String.valueOf(PropertyUtils.getProperty(orig, propertyName))));
						}
						else if ("java.lang.Float".equalsIgnoreCase(type) || "float".equalsIgnoreCase(type)) {
							PropertyUtils.setProperty(dest, propertyName, Float.valueOf(String.valueOf(PropertyUtils.getProperty(orig, propertyName))));
						}
						else if ("java.lang.Long".equalsIgnoreCase(type) || "long".equalsIgnoreCase(type) ) {
							PropertyUtils.setProperty(dest, propertyName, Long.valueOf(String.valueOf(PropertyUtils.getProperty(orig, propertyName))));
						}
						else if ("java.lang.String".equalsIgnoreCase(type) || "string".equalsIgnoreCase(type) ) {
							PropertyUtils.setProperty(dest, propertyName, String.valueOf(PropertyUtils.getProperty(orig, propertyName)));
						}
						else if ("java.lang.Integer".equalsIgnoreCase(type) || "int".equalsIgnoreCase(type)) {
							PropertyUtils.setProperty(dest, propertyName, Integer.valueOf(String.valueOf(PropertyUtils.getProperty(orig, propertyName))));
						}
						else if ("java.lang.Short".equalsIgnoreCase(type) || "short".equalsIgnoreCase(type)) {
							PropertyUtils.setProperty(dest, propertyName, Short.valueOf(String.valueOf(PropertyUtils.getProperty(orig, propertyName))));
						}
					} catch (Exception e) {
					}
				}
			}
		}
	}
	
	/**
	 * 取得指定例实的属性名及对应类型
	 * @param instance 指定例实
	 * @return 指定例实的属性名及对应类型
	 * @author zengpzh
	 */
	private static Map<String, String> getPropertiesType(Object instance) {
		Map<String, String> map = new HashMap<String, String>();
		if (instance != null) {
			Field[] Fields = instance.getClass().getDeclaredFields();
			if (Fields != null) {
				for (Field field : Fields) {
					try {
						map.put(field.getName(), field.getType().getName());
					} catch (Exception e) {
					}
				}
			}
		}
		return map;
	}
	
}
