package com.chinawiserv.deepone.manager.model.enums;

/**
 * 角色权限是否标识为快捷方式
 * 
 * @author zengpzh
 * 
 * Nov 27, 2009 11:32:42 AM
 */
public enum RolePopedomShortcutEnum {
    DATA_0("0", "普通"), DATA_1("1", "快捷方式");

    private String dbValue;
    private String chValue;

    private RolePopedomShortcutEnum(String dbValue, String chValue) {
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
