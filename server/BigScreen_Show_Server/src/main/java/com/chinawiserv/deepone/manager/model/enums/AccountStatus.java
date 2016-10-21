package com.chinawiserv.deepone.manager.model.enums;

/**
 * 用户状态枚举
 * 
 * @author zengpzh
 * 
 * Dec 2, 2009 2:13:46 PM
 */
public enum AccountStatus {
	
	DATA_0("0", "闲置"),
	
	DATA_1("1", "激活");

	private String dbValue;

	private String chValue;

	private AccountStatus(String dbValue, String chValue) {
		this.dbValue = dbValue;
		this.chValue = chValue;
	}

	public String getDbValue() {
		return dbValue;
	}

	public String getChValue() {
		return chValue;
	}
}
