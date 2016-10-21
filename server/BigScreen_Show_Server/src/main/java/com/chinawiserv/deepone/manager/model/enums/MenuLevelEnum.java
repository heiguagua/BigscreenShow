package com.chinawiserv.deepone.manager.model.enums;

/**
 * 枚举示例 1.当添加到List做键值时： list.add(new OptionBean(EnumExample.DATA_1.getDbValue()
 * , EnumExample.DATA_1.getChValue())) ;
 * 
 * 2.通过数据库里的值读出中文数据时： EnumExample.valueOf(EnumTools.getName(整型参数)).getChValue()
 * 
 * @author zengpzh
 * 
 *         Nov 19, 2009 3:26:13 PM
 */
public enum MenuLevelEnum {
	DATA_0("0", "一级菜单"), DATA_1("1", "二级菜单"), DATA_2("2", "三级菜单");

	private String dbValue;
	private String chValue;

	private MenuLevelEnum(String dbValue, String chValue) {
		this.dbValue = dbValue;
		this.chValue = chValue;
	}

	public String getDbValue() {
		return dbValue;
	}

	public void setDbValue(String dbValue) {
		this.dbValue = dbValue;
	}

	public String getChValue() {
		return chValue;
	}

	public void setChValue(String chValue) {
		this.chValue = chValue;
	}

}
