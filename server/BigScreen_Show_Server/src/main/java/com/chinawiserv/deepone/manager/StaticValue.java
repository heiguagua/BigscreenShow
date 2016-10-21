package com.chinawiserv.deepone.manager;

import com.chinawiserv.deepone.manager.model.common.OptionBean;

import java.util.ArrayList;

/**
 * 静态储存区
 * 
 */
public class StaticValue {

	public static final String SQL_SPE = "\n";
	public static final String HTML_SPE = "<br>";
	public static final String SQL_FILE_NAME = "import.sql";
	
	/**
	 * 获取列表头
	 * @return
	 * @author zengpzh
	 */
	public static ArrayList<OptionBean> getBaseHeader() {
		ArrayList<OptionBean> listHeader = new ArrayList<OptionBean>();
		listHeader.add(new OptionBean("", "-请选择-"));
		return listHeader;
	}

	/**
	 * 获取列表头
	 * @return
	 * @author zengpzh
	 */
	public static ArrayList<OptionBean> getBaseHeaderForSearch() {
		ArrayList<OptionBean> listHeader = new ArrayList<OptionBean>();
		listHeader.add(new OptionBean("", ""));
		return listHeader;
	}

	/**
	 * 自定义SELECT头信息
	 * @param customHeader
	 * @return
	 * @author zengpzh
	 */
	public static ArrayList<OptionBean> getCustomHeaderList(String customHeader) {
		ArrayList<OptionBean> listHeader = new ArrayList<OptionBean>();
		listHeader.add(new OptionBean("", customHeader));
		return listHeader;
	}

	/**
	 * 用户状态枚举
	 * @return
	 * @author zengpzh
	 */
	public static final ArrayList<OptionBean> getAccountStatus() {
		ArrayList<OptionBean> result = getBaseHeader();
		result.add(new OptionBean(com.chinawiserv.deepone.manager.model.enums.AccountStatus.DATA_0.getDbValue(), com.chinawiserv.deepone.manager.model.enums.AccountStatus.DATA_0.getChValue()));
		result.add(new OptionBean(com.chinawiserv.deepone.manager.model.enums.AccountStatus.DATA_1.getDbValue(), com.chinawiserv.deepone.manager.model.enums.AccountStatus.DATA_1.getChValue()));
		return result;
	}

	/**
	 * 获取菜单级别
	 * @return
	 * @author zengpzh
	 */
	public static final ArrayList<OptionBean> getMenuLevelList() {
		ArrayList<OptionBean> result = getBaseHeader();
		result.add(new OptionBean(com.chinawiserv.deepone.manager.model.enums.MenuLevelEnum.DATA_0.getDbValue(), com.chinawiserv.deepone.manager.model.enums.MenuLevelEnum.DATA_0.getChValue()));
		result.add(new OptionBean(com.chinawiserv.deepone.manager.model.enums.MenuLevelEnum.DATA_1.getDbValue(), com.chinawiserv.deepone.manager.model.enums.MenuLevelEnum.DATA_1.getChValue()));
		result.add(new OptionBean(com.chinawiserv.deepone.manager.model.enums.MenuLevelEnum.DATA_2.getDbValue(), com.chinawiserv.deepone.manager.model.enums.MenuLevelEnum.DATA_2.getChValue()));
		return result;
	}
}