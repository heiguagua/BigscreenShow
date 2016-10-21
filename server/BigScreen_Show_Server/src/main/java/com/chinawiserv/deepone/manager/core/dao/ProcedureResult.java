package com.chinawiserv.deepone.manager.core.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 储存过程结果集类
 * @author zengpzh
 * @version 0.1
 */
public class ProcedureResult implements Serializable{

	private static final long serialVersionUID = 1768570428641176979L;

	/**
	 * 一个Map，这个Map里包含了所有输出参数及值，key是输出参数名称，value是输出参数值
	 */
	private Map<String, Object> outParameterResult;
	
	/**
	 * 一个过程的结果集，这个过程的结果集包含了查询的结果集
	 */
	private List<Map<String, Object>> resultSet;

	public Map<String, Object> getOutParameterResult() {
		return outParameterResult;
	}

	public void setOutParameterResult(Map<String, Object> outParameterResult) {
		this.outParameterResult = outParameterResult;
	}

	public List<Map<String, Object>> getResultSet() {
		return resultSet;
	}

	public void setResultSet(List<Map<String, Object>> resultSet) {
		this.resultSet = resultSet;
	}
	
}