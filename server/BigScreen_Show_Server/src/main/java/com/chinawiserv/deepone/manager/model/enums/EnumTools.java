package com.chinawiserv.deepone.manager.model.enums;

import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;

/**
 * 枚举工具类
 * @author zengpzh
 */
public class EnumTools {

    /**
     * 求组合名称,如枚举名称为DATA_XX_XX的数据类型
     * 
     * @param obj
     *                枚举参数数组
     * @param data
     *                数据参数
     * @return
     * @throws Exception
     * @author zengpzh
     */
    public static String getName(Object[] obj, String data) throws Exception {
	String name = "";
	boolean forSign = false;
	for (int j = 0; j < obj.length; j++) {
	    Class<?> classData = Class.forName(obj[j].getClass()
		    .getCanonicalName());
	    Field field[] = classData.getDeclaredFields();
	    for (int i = 0; i < field.length; i++) {
		String fieldName = field[i].getName();
		if (fieldName.startsWith("DATA_")
			&& StringUtils.equals(fieldName.substring(fieldName
				.length() - 2, fieldName.length()), data)) {
		    name = field[i].get(obj[j]).toString();
		    break;
		}
	    }
	    if (forSign) {
		break;
	    }
	}
	return name;
    }

    /**
     * 一般名称,如枚举名称为DATA_XX的数据类型
     * 
     * @param data
     *                数据参数
     * @return
     * @author zengpzh
     */
    public static String getName(String data) {
	return "DATA_" + data;
    }

    /**
     * 获取下划线类型名称_xxxx
     * 
     * @param data
     * @author zengpzh
     */
    public static String getNameWith_(String data) {
	return "_" + data;
    }

    public static String getDBName(String data) {
	if (data == null || "".equals(data)) {
	    data = "null";
	}
	return "DB_" + data;
    }

}
