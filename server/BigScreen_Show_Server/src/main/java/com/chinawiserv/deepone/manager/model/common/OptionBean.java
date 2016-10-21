package com.chinawiserv.deepone.manager.model.common;

import java.io.Serializable;

/**
 * 为 Spring mvc 的 <s:select> 标签生成下拉列表的数据Bean
 * <pre> 
 * 在 Spring mvc 中使用实例：
 * <s:select path="modelAttributeName" items="${modelAttribute}" itemLabel="itemLabel" itemValue="itemValue"></s:select>
 * 其中：exampleId 被定义为 String
 *      exampleList 被定义为 ArrayList<OptionBean>
 * </pre> 
 * @author zengpzh
 * @version 0.1
 */
public class OptionBean implements Serializable {
	
	private static final long serialVersionUID = -7381403261462602850L;

	/**
	 * 代码
	 */
	public String itemValue;
	
	/**
	 * 显示文字
	 */
	public String itemLabel;
	
	public OptionBean() {
	}
	
	public OptionBean(String itemValue, String itemLabel) {
		this.itemValue = itemValue;
		this.itemLabel = itemLabel;
	}
	
	public String getItemValue() {
		return itemValue;
	}

	public void setItemValue(String itemValue) {
		this.itemValue = itemValue;
	}

	public String getItemLabel() {
		return itemLabel;
	}

	public void setItemLabel(String itemLabel) {
		this.itemLabel = itemLabel;
	}

}