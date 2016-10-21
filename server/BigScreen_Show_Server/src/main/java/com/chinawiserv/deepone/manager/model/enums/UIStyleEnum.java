package com.chinawiserv.deepone.manager.model.enums;

import com.chinawiserv.deepone.manager.StaticValue;
import com.chinawiserv.deepone.manager.model.common.OptionBean;

import java.util.ArrayList;

/**
 * UI风格
 * 
 * @author zengpzh
 */
public enum UIStyleEnum {

	DATA_0("0", "TAB风格", "success_tab"),
	
	DATA_1("1", "MAC风格", "success_mac");

	private String id;

	private String label;
	
	private String path;

	private UIStyleEnum(String id, String label, String path) {
		this.id = id;
		this.label = label;
		this.path = path;
	}

	/**
	 * 包装 成 列表
	 * @return
	 * @author zengpzh
	 */
	public static final ArrayList<OptionBean> pack() {
		ArrayList<OptionBean> result = StaticValue.getBaseHeader();
		result.add(new OptionBean(DATA_0.getId(), DATA_0.getLabel()));
		result.add(new OptionBean(DATA_1.getId(), DATA_1.getLabel()));
		return result;
	}		
	
	/**
	 * 翻译 枚举 信息 （for label）
	 * @param id 枚举的 ID
	 * @return
	 * @throws Exception
	 * @author zengpzh
	 */
	public static String translate(String id) throws Exception {
		if (id != null) {
			String label = "";
			UIStyleEnum objs[] = UIStyleEnum.values();
			for (int i = 0; i < objs.length; i++) {
				UIStyleEnum obj = objs[i];
				if (id.equals(obj.getId())) {
					label = obj.getLabel();
					break;
				}
			}
			return label;
		}
		else {
			return "";
		}
	}

	/**
	 * 翻译 枚举 信息（for path）
	 * @param id 枚举的 ID
	 * @return
	 * @throws Exception
	 * @author zengpzh
	 */
	public static String translateForPath(String id) throws Exception {
		if (id != null) {
			String path = "";
			UIStyleEnum objs[] = UIStyleEnum.values();
			for (int i = 0; i < objs.length; i++) {
				UIStyleEnum obj = objs[i];
				if (id.equals(obj.getId())) {
					path = obj.getPath();
					break;
				}
			}
			return path;
		}
		else {
			return "";
		}
	}	
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
}