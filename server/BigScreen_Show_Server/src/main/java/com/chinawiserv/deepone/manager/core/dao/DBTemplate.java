package com.chinawiserv.deepone.manager.core.dao;

import com.chinawiserv.deepone.manager.core.exception.MsgException;
import com.chinawiserv.deepone.manager.core.exception.NoSQLException;
import com.chinawiserv.deepone.manager.core.exception.NoSelectedColumns;
import com.chinawiserv.deepone.manager.core.util.Tools;
import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.KeyHolder;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;

/**
 * 数据库访问模板
 * 
 * <pre>
 * 本类主要提供一些共用的访问方法，主要包含：
 *   1、（不）带参数查询 多行多列，使用SQL语句查询
 *   2、（不）带参数查询 单整数数据，使用SQL语句查询
 *   3、（不）带参数查询 单字符串数据，使用SQL语句查询
 *   4、（不）带参数查询 单对象数据，使用SQL语句查询
 *   5、带参数 更新（添加、删除、编辑），使用SQL语句查询
 *   6、零 SQL 操作数据库
 *   7、包装Excel数据对象（createExcelObject）
 * </pre>
 * @author zengpzh
 * @version 0.1
 */
public class DBTemplate extends JdbcDaoSupport implements Serializable {

	private static final long serialVersionUID = 2626542718216634946L;

	private static final Logger log = LoggerFactory.getLogger(DBTemplate.class);

	/**
	 * 事务隔离级别命令
	 */
	private String transactionIsolation = null;
	private String transactionIsolationTemp = null;
	private boolean transactionIsolationIsRun = false;

	/**
	 * 工具模板
	 */
	private ToolTemplate toolTemplate = new ToolTemplate();

	/**
	 * 当前操作的实际表名
	 */
	private String realTableNameForInsertActor;

	/**
	 * 是否允许打印信息到控制台 是否允许显示开发调试功能 true 显示，否则不显示
	 */
	private boolean allowPrintInfoOnConsole = true;

	/**
	 * 是否允许打印当前线程信息
	 */
	private boolean allowPrintCurrentThread = false;

	/**
	 * 是否允许打印事务级别提示信息
	 */
	private boolean allowPrintTransactionHint = false;

	/**
	 * 初始化模板
	 *
	 * @param realTableNameForInsertActor
	 */
	public DBTemplate(String realTableNameForInsertActor) {
		this.realTableNameForInsertActor = realTableNameForInsertActor;
	}

	/**
	 * 取得 工具访问模板
	 *
	 * @return 工具访问模板
	 * @author zengpzh
	 */
	protected ToolTemplate getToolTemplate() {
		return toolTemplate;
	}

	/**
	 * 获取原始 SimpleJdbcTemplate
	 * 
	 * @return SimpleJdbcTemplate
	 * @author zengpzh
	 */
	protected JdbcTemplate getJdbc() {
		return this.getJdbcTemplate();
	}
	
	/**
	 * 以 YYYYMMDDHHMMSS 加 毫秒形式返回当前时间
	 * @return 当前时间
	 * @author zengpzh
	 */
	private String getMillisecond() {
		return this.getToolTemplate().getCurrentDate_YYYYMMDDHHMMSS_millisecond();
	}

	/**
	 * 执行隔离级别事物命令
	 * @author zengpzh
	 *
	 */
	private void runTransactionIsolation() {
		if (transactionIsolation != null && !"".equals(transactionIsolation.trim())) {
			if (allowPrintTransactionHint && allowPrintInfoOnConsole) {
				log.info("激活开始（隔离级别事物） 开始时间：" + getMillisecond());
			}
			this.getJdbcTemplate().execute(transactionIsolation);
			if (allowPrintTransactionHint) {
				this.printInfoOnConsole(transactionIsolation);
			}
			if (allowPrintTransactionHint && allowPrintInfoOnConsole) {
				log.info("激活完成（隔离级别事物） 结束时间：" + getMillisecond());
				Tools.println();
			}
		}
	}
	
	/**
	 * 设置隔离级别事物命令
	 * @author zengpzh
	 *
	 */
	public void setTransactionIsolation(String value) {
		transactionIsolation = value;
	}
	
	/**
	 * 挂事务隔离级别，但不清楚，在调 transactionIsolationReset 之后，又可以继续执行事务
	 * @author zengpzh
	 *
	 */
	public void transactionIsolationPause() {
		if (!transactionIsolationIsRun) {
			transactionIsolationTemp = transactionIsolation;
			transactionIsolation = null;
			transactionIsolationIsRun = true;
		}
	}

	/**
	 * 恢复事务隔离级别执行
	 * @author zengpzh
	 *
	 */
	public void transactionIsolationReset() {
		if (transactionIsolationIsRun) {
			transactionIsolation = transactionIsolationTemp;
			transactionIsolationTemp = null;
			transactionIsolationIsRun = false;
		}
	}
	
	/**
	 * 带参数查询 多行多列，使用SQL语句查询
	 * @param selectSql 查询SQL语句
	 * @param params 参数数组，用于传递参数值，如果不必传递参数，则此参数可赋予null值
	 * @return 返回结果为对象列表<br />
	 *         对象列表的每个元素为一个Map的实现<br />
	 *         该Map的实现的key为数据库字段名称（区分大小写的数据库字段名称）<br />
	 *         Map的实现的value为对应值
	 * @author zengpzh
	 */
	public List<Map<String, Object>> findForList(String selectSql, List<Object> params) throws Exception {
		if (allowPrintInfoOnConsole) {
			log.info("开始执行（SQL 多行多列） 开始时间：" + getMillisecond());
		}
		List<Map<String, Object>> result = this.findForListWithOutLog(selectSql, params);
		if (allowPrintInfoOnConsole) {
			log.info("执行完成（SQL 多行多列） 结束时间：" + getMillisecond() + " 返回记录数：" + result.size());
			Tools.println();
		}
		return result;
	}

	/**
	 * 带参数查询 多行多列，使用SQL语句查询
	 * @param selectSql 查询SQL语句
	 * @param params 参数数组，用于传递参数值，如果不必传递参数，则此参数可赋予null值
	 * @return 返回结果为对象列表<br />
	 *         对象列表的每个元素为一个Map的实现<br />
	 *         该Map的实现的key为数据库字段名称（区分大小写的数据库字段名称）<br />
	 *         Map的实现的value为对应值
	 * @author zengpzh
	 */
	public List<Map<String, Object>> findForListWithOutLog(String selectSql, List<Object> params) throws Exception {
		this.testStringIsNullOrBlank(selectSql);
		this.runTransactionIsolation();
		this.printInfoOnConsole(selectSql, params);
		if (params != null) {
			return this.getJdbc().queryForList(selectSql, params.toArray());
		} else {
			return this.getJdbc().queryForList(selectSql);
		}
	}

	/**
	 * 不带参数查询 多行多列，使用SQL语句查询
	 * @param selectSql 查询SQL语句
	 * @return 返回结果为对象列表<br />
	 *         对象列表的每个元素为一个Map的实现<br />
	 *         该Map的实现的key为数据库字段名称（区分大小写的数据库字段名称）<br />
	 *         Map的实现的value为对应值
	 * @author zengpzh
	 */
	public List<Map<String, Object>> findForList(String selectSql) throws Exception {
		return this.findForList(selectSql, null);
	}

	/**
	 * 带参数查询 单整型数据，使用SQL语句查询
	 * @param selectSql  查询SQL语句，该SQl语句的结果必须一行一列，并且为整数的数据
	 * @param params  参数数组，用于传递参数值，如果不必传递参数，则此参数可赋予null值
	 * @return 返回结果为一个整数数据
	 * @author zengpzh
	 */
	public int findForInteger(String selectSql, List<Object> params) throws Exception {
		if (allowPrintInfoOnConsole) {
			log.info("开始执行（SQL 单整型数据） 开始时间：" + getMillisecond());
		}
		int result = this.findForIntegerWithOutLog(selectSql, params);
		if (allowPrintInfoOnConsole) {
			log.info("执行完成（SQL 单整型数据） 结束时间：" + getMillisecond() + " 返回值：" + result);
			Tools.println();
		}
		return result;
	}

	/**
	 * 带参数查询 单整型数据，使用SQL语句查询
	 * @param selectSql 查询SQL语句，该SQl语句的结果必须一行一列，并且为整数的数据
	 * @param params 参数数组，用于传递参数值，如果不必传递参数，则此参数可赋予null值
	 * @return 返回结果为一个整数数据
	 * @author zengpzh
	 */
	public int findForIntegerWithOutLog(String selectSql, List<Object> params) throws Exception {
		this.testStringIsNullOrBlank(selectSql);
		this.runTransactionIsolation();
		this.printInfoOnConsole(selectSql, params);
		try {
			Number number = null;
			if (params != null) {
				number = this.getJdbc().queryForObject(selectSql, params.toArray(), Integer.class);
			} else {
				number = this.getJdbc().queryForObject(selectSql, Integer.class);
			}
			return (number != null ? number.intValue() : 0);
		} catch (Exception e) {
			return 0;
		}
	}

	/**
	 * 不带参数查询 单整数数据，使用SQL语句查询
	 * @param selectSql  查询SQL语句，该SQl语句的结果必须一行一列，并且为整数的数据
	 * @return 返回结果为一个整数数据
	 * @author zengpzh
	 */
	public int findForInteger(String selectSql) throws Exception {
		return this.findForInteger(selectSql, null);
	}

	/**
	 * 带参数查询 单Double数据，使用SQL语句查询
	 * @param selectSql 查询SQL语句，该SQl语句的结果必须一行一列，并且为Double的数据
	 * @param params 参数数组，用于传递参数值，如果不必传递参数，则此参数可赋予null值
	 * @return 返回结果为一个Double数据
	 * @author zengpzh
	 */
	public double findForDouble(String selectSql, List<Object> params) throws Exception {
		double result = 0.0;
		if (allowPrintInfoOnConsole) {
			log.info("开始执行（SQL 单浮点数据） 开始时间：" + getMillisecond());
		}
		this.testStringIsNullOrBlank(selectSql);
		this.printInfoOnConsole(selectSql, params);
		try {
			Object obj = this.findForObject(selectSql, params);
			if (obj != null) {
				if (obj instanceof BigDecimal) {
					BigDecimal bigDecimal = (BigDecimal) obj;
					result = bigDecimal.doubleValue();
				} else {
					throw new Exception("非 BigDecimal 类型，目前此方法只对有效！");
				}
			}
		} catch (Exception e) {
		}
		if (allowPrintInfoOnConsole) {
			log.info("执行完成（SQL 单浮点数据） 结束时间：" + getMillisecond() + " 返回值：" + result);
			Tools.println();
		}
		return result;
	}

	/**
	 * 不带参数查询 单Double数据，使用SQL语句查询
	 * @param selectSql 查询SQL语句，该SQl语句的结果必须一行一列，并且为Double的数据
	 * @return 返回结果为一个Double数据
	 * @author zengpzh
	 */
	public double findForDouble(String selectSql) throws Exception {
		return this.findForDouble(selectSql, null);
	}

	/**
	 * 带参数查询 单字符串数据，使用SQL语句查询
	 * @param selectSql 查询SQL语句，该SQl语句的结果必须一行一列，并且字符串的数据
	 * @param params 参数数组，用于传递参数值，如果不必传递参数，则此参数可赋予null值
	 * @return 返回结果为一个字符串数据
	 * @author zengpzh
	 */
	public String findForString(String selectSql, List<Object> params) throws Exception {
		String result = "";
		if (allowPrintInfoOnConsole) {
			log.info("开始执行（SQL 单字符串数据） 开始时间：" + getMillisecond());
		}
		this.testStringIsNullOrBlank(selectSql);
		this.runTransactionIsolation();
		this.printInfoOnConsole(selectSql, params);
		try {
			if (params != null) {
				result = String.valueOf(this.getJdbc().queryForObject(selectSql, String.class, params.toArray()));
			} else {
				result = String.valueOf(this.getJdbc().queryForObject(selectSql, String.class));
			}
		} catch (Exception e) {
		}
		if (allowPrintInfoOnConsole) {
			log.info("执行完成（SQL 单字符串数据） 结束时间：" + getMillisecond()+ " 返回值：" + result);
			Tools.println();
		}
		return result;
	}

	/**
	 * 不带参数查询 单字符串数据，使用SQL语句查询
	 * @param selectSql 查询SQL语句，该SQl语句的结果必须一行一列，并且字符串的数据
	 * @return 返回结果为一个字符串数据
	 * @author zengpzh
	 */
	public String findForString(String selectSql) throws Exception {
		return this.findForString(selectSql, null);
	}

	/**
	 * 带参数查询 单对象数据，使用SQL语句查询
	 * @param selectSql 查询SQL语句，该SQl语句的结果必须一行一列，并且单对象的数据
	 * @param params 参数数组，用于传递参数值，如果不必传递参数，则此参数可赋予null值
	 * @return 返回结果为一个对象数据
	 * @author zengpzh
	 */
	public Object findForObject(String selectSql, List<Object> params) throws Exception {
		Object result = null;
		if (allowPrintInfoOnConsole) {
			log.info("开始执行（SQL 单对象数据） 开始时间：" + getMillisecond());
		}
		this.testStringIsNullOrBlank(selectSql);
		this.runTransactionIsolation();
		this.printInfoOnConsole(selectSql, params);
		try {
			if (params != null) {
				result = this.getJdbc().queryForObject(selectSql, Object.class, params.toArray());
			} else {
				result = this.getJdbc().queryForObject(selectSql, Object.class);
			}
		} catch (Exception e) {
		}
		if (allowPrintInfoOnConsole) {
			log.info("执行完成（SQL 单对象数据） 结束时间：" + getMillisecond() + " 返回值：" + result);
			Tools.println();
		}
		return result;
	}

	/**
	 * 不带参数查询 单对象数据，使用SQL语句查询
	 * @param selectSql 查询SQL语句，该SQl语句的结果必须一行一列，并且单对象的数据
	 * @return 返回结果为一个对象数据
	 * @author zengpzh
	 */
	public Object findForObject(String selectSql) throws Exception {
		return this.findForObject(selectSql, null);
	}

	/**
	 * 带参数 更新（添加、删除、编辑），使用SQL语句查询
	 * @param insertOrUpdateOrDeleteSql  添加、删除、编辑SQL语句
	 * @param params 参数数组，用于传递参数值，如果不必传递参数，则此参数可赋予null值
	 * @return 被更新的数量
	 * @author zengpzh
	 */
	public int updateWithParams(String insertOrUpdateOrDeleteSql, List<Object> params) throws Exception {
		int result = 0;
		String _temp = "添加、删除、编辑";
		if (insertOrUpdateOrDeleteSql != null) {
			String _temp_string_  = insertOrUpdateOrDeleteSql.trim().substring(0, 6);
			if ("insert".equalsIgnoreCase(_temp_string_)) {
				_temp = "添加";
			}
			else if ("update".equals(_temp_string_)) {
				 _temp = "编辑";
			}
			else if ("delete".equalsIgnoreCase(_temp_string_)) {
				 _temp = "删除";
			}
		}
		if (allowPrintInfoOnConsole) {
			log.info("开始执行（SQL "+_temp+"） 开始时间：" + getMillisecond());
		}
		this.testStringIsNullOrBlank(insertOrUpdateOrDeleteSql);
		this.printInfoOnConsole(insertOrUpdateOrDeleteSql, params);
		if (params != null && !params.isEmpty()) {
			result = this.getJdbc().update(insertOrUpdateOrDeleteSql, params.toArray());
		} else {
			result = this.getJdbc().update(insertOrUpdateOrDeleteSql);
		}
		if (allowPrintInfoOnConsole) {
			log.info("执行完成（SQL "+_temp+"） 结束时间：" + getMillisecond()+ " 影响记录数：" + result);
			Tools.println();
		}
		return result;
	}
	public int updateWithParams(String insertOrUpdateOrDeleteSql) throws Exception {
		return this.updateWithParams(insertOrUpdateOrDeleteSql, null);
	}

	/**
	 * 执行SQL语句，一条或多条SQL语句，如果是多条，每条间加分号（;）
	 * 
	 * @param sqls
	 * @throws Exception
	 * @author zengpzh
	 */
	public void executeSql(String sqls) throws Exception {
		if (sqls != null && !"".equals(sqls) && !"null".equals(sqls)) {
			if (allowPrintInfoOnConsole) {
				log.info("开始执行（执行SQL语句，可批量） 开始时间：" + getMillisecond());
			}
			this.getJdbcTemplate().execute(sqls);
			if (allowPrintInfoOnConsole) {
				log.info(sqls);
			}
			if (allowPrintInfoOnConsole) {
				log.info("执行完成（执行SQL语句，可批量） 结束时间：" + getMillisecond());
				Tools.println();
			}
		}
	}

	/**
	 * 保存数据到数据库 特别提示：此方法只处理值对象里其不为空（null）的字段
	 * 即：如果值对象里某个字段的值为（null），那么它不参与处理，生成插入的SQL语句时也不涉及该字段。
	 * @param obj  该DAO对应的表对应的POJO对象
	 * @throws Exception
	 */
	public void saveData(Object obj) throws Exception {
		this.saveDataByInsertActor(obj, this.getRealTableNameForInsertActor(), null);
	}

	/**
	 * 保存数据到数据库，并返回指定的自动生成字段所生成的值 特别提示：此方法只处理值对象里其不为空（null）的字段
	 * 即：如果值对象里某个字段的值为（null），那么它不参与处理，生成插入的SQL语句时也不涉及该字段。
	 * @param obj  该DAO对应的表对应的POJO对象
	 * @param keyColumn 指定的自动生成的字段
	 * @return 指定的自动生成键所生成的值
	 * @throws Exception
	 */
	public String saveData(Object obj, String keyColumn) throws Exception {
		return this.saveDataByInsertActor(obj, this.getRealTableNameForInsertActor(), keyColumn);
	}

	/**
	 * 保存数据到数据库（批量处理） 特别提示：此方法只处理值对象里其不为空（null）的字段
	 * 即：如果值对象里某个字段的值为（null），那么它不参与处理，生成插入的SQL语句时也不涉及该字段。
	 * @param objs 该DAO对应的表对应的POJO对象组
	 * @throws Exception
	 */
	public void saveData(List<?> objs) throws Exception {
		this.saveDataByInsertActor(objs, this.getRealTableNameForInsertActor(),null);
	}

	/**
	 * 保存数据到数据库，（批量处理），并指定某个字段为数据库自动生成字段 特别提示：此方法只处理值对象里其不为空（null）的字段
	 * 即：如果值对象里某个字段的值为（null），那么它不参与处理，生成插入的SQL语句时也不涉及该字段。
	 * @param objs 该DAO对应的表对应的POJO对象组
	 * @param keyColumn 指定的自动生成的字段
	 * @throws Exception
	 */
	public void saveData(List<?> objs, String keyColumn) throws Exception {
		this.saveDataByInsertActor(objs, this.getRealTableNameForInsertActor(), keyColumn);
	}

	/**
	 * 批量更新
	 * @param updateSql 更新SQL语句
	 * @param params  参数
	 * @throws Exception
	 * @author zengpzh
	 */
	public int[] batchUpdate(String updateSql, List<Object[]> params) throws Exception {
		if (allowPrintInfoOnConsole) {
			log.info("批量更新-开始时间：" + getMillisecond());
		}
		int[] ints = this.getJdbc().batchUpdate(updateSql, params);
		if (allowPrintInfoOnConsole) {
			log.info("批量更新-结束时间：" + getMillisecond()+ " 影响记录数：" + ints.length);
			Tools.println();
		}
		return ints;
	}

	/**
	 * 更新数据，使用POJO对象加条件字段，注：这里设置条件时，只采用了 = 符号，如果为非 = 符号的请使用其他办法实现更新
	 * 特别提示：此方法只处理值对象里其不为空（null）的字段
	 * 即：如果值对象里某个字段的值为（null），那么它不参与处理，生成插入的SQL语句时也不涉及该字段。
	 * @param obj  值对象
	 * @param conditionFieldNames 条件字段（可多个字段）
	 * @throws Exception
	 */
	public void updateData(Object obj, String... conditionFieldNames) throws Exception {
		if (obj != null && conditionFieldNames != null) {
			if (conditionFieldNames.length > 0) {
				ArrayList<String> fieldArray = new ArrayList<String>(conditionFieldNames.length);
				for (String field : conditionFieldNames) {
					if (field != null) {
						fieldArray.add(field.toLowerCase());
					} else {
						throw new MsgException("温馨提示，更新条件字段不能为空(null)！");
					}
				}
				fieldArray.trimToSize();
				String sql = " update " + realTableNameForInsertActor + " set ";
				Object[] params;
				Map<?, ?> properties = this.getToolTemplate().getPropertiesWithoutNullValueProperty(obj);
				if (properties != null && !properties.isEmpty()) {
					params = new Object[properties.size()];
					int paramIndex = 0;
					Set<?> keySet = properties.keySet();
					Iterator<?> it = keySet.iterator();
					if (it != null) {
						while (it.hasNext()) {
							String propertyName = String.valueOf(it.next());
							if (!fieldArray.contains(propertyName.toLowerCase())) {
								sql += " " + propertyName + " = ?, ";
								params[paramIndex++] = properties.get(propertyName);
							}
						}
					}
					sql = sql.trim();
					if (sql.endsWith(",")) {
						sql = sql.substring(0, sql.length() - 1);
					}
					if (fieldArray.size() > 0) {
						sql += " where ";
						if (it != null) {
							it = keySet.iterator();
							while (it.hasNext()) {
								String propertyName = String.valueOf(it.next());
								if (fieldArray.contains(propertyName.toLowerCase())) {
									sql += " " + propertyName + " = ? and ";
									params[paramIndex++] = properties.get(propertyName);
								}
							}
						}
						sql = sql.trim();
						if (sql.endsWith("and")) {
							sql = sql.substring(0, sql.length() - 3);
						}
					}
					this.updateWithParams(sql, this.toArray(params));
				}
			} else {
				throw new MsgException("Update数据时，必须提供至少一个条件！请检查updateData(Object obj, String... conditionFieldNames)是否没有提供conditionFieldNames...");
			}
		}
	}
	
	/**
	 * 将 Object[]转化为ArrayList<Object>形式
	 * @param params 参数
	 * @return 处理结果
	 * @throws Exception
	 * @author zengpzh
	 */
	private List<Object> toArray(Object[] params) throws Exception {
		if (params != null) {
			List<Object> p = new ArrayList<Object>(params.length);
			for (Object o : params) {
				p.add(o);
			}
			return p;
		}
		else {
			return null;
		}
	}

	/**
	 * 保存数据到数据库（自动生成SQL语句，无需开发者编写SQL语句）
	 * @param obj  当前DAO对应的表对应的POJO对象
	 * @param keyColumn 指定的自动生成的键
	 * @return 指定的自动生成键所生成的值
	 * @throws Exception
	 * @author zengpzh
	 */
	private String saveDataByInsertActor(final Object obj, final String tableName, final String keyColumn) throws Exception {
		if (allowPrintInfoOnConsole) {
			log.info("开始执行（添加，对象） 开始时间：" + getMillisecond());
		}
		boolean requiredReturn = false;
		String returnValue = "";
		if (keyColumn != null && !"".equals(keyColumn.trim())) {
			requiredReturn = true;
		}
		SimpleJdbcInsert insertActor = this.newInsertActor(tableName);
		insertActor.setColumnNames(this.getToolTemplate().getPropertyNamesWithoutNullValueProperty(obj));
		if (requiredReturn) {
			insertActor = insertActor.usingGeneratedKeyColumns(keyColumn);
			KeyHolder keyHolder = insertActor.executeAndReturnKeyHolder(this.objToParameter(obj));
			if (keyHolder != null) {
				List<?> list = keyHolder.getKeyList();
				if (list != null && list.size() > 0) {
					Map<?, ?> map = (Map<?, ?>) list.get(0);
					returnValue = String.valueOf(map.get(keyColumn));
				}
			}
		} else {
			insertActor.execute(this.objToParameter(obj));
			returnValue = "";
		}
		this.printInfoOnConsole(insertActor.getInsertString(), obj);
		if (allowPrintInfoOnConsole) {
			log.info("执行完成（添加，对象） 结束时间："+ getMillisecond());
			Tools.println();
		}
		return returnValue;
	}

	/**
	 * 保存数据到数据库，（批量处理）（自动生成SQL语句，无需开发者编写SQL语句）
	 * @param objs 当前DAO对应的表对应的POJO对象组
	 * @param keyColumn 指定的自动生成的字段
	 * @throws Exception
	 * @author zengpzh
	 */
	private void saveDataByInsertActor(final List<?> objs, final String tableName, final String keyColumn) throws Exception {
		SimpleJdbcInsert insertActor = this.newInsertActor(tableName);
		if (objs != null && objs.size() > 0) {
			insertActor.setColumnNames(this.getToolTemplate().getPropertyNamesWithoutNullValueProperty(objs.get(0)));
		}
		if (keyColumn != null && !"".equals(keyColumn.trim())) {
			insertActor = insertActor.usingGeneratedKeyColumns(keyColumn);
		}
		insertActor.executeBatch(this.objsToParameters(objs));
		this.printInfoOnConsole(insertActor.getInsertString());
	}

	/**
	 * 转换Object对象成SimpleJdbcInsert执行的参数对象
	 * @param obj Object对象
	 * @return SimpleJdbcInsert执行的参数对象
	 * @author zengpzh
	 */
	private SqlParameterSource objToParameter(Object obj) {
		if (obj != null) {
			SqlParameterSource parameter = new BeanPropertySqlParameterSource(obj);
			return parameter;
		} else {
			return null;
		}
	}

	/**
	 * 转换Object对象数组成SimpleJdbcInsert执行的参数对象数组
	 * @param objs Object对象集
	 * @return SimpleJdbcInsert执行的参数对象数组
	 * @author zengpzh
	 */
	private SqlParameterSource[] objsToParameters(List<?> objs) {
		if (objs != null && objs.size() > 0) {
			SqlParameterSource[] parameters = new SqlParameterSource[objs.size()];
			for (int i = 0; i < objs.size(); i++) {
				parameters[i] = new BeanPropertySqlParameterSource(objs.get(i));
			}
			return parameters;
		} else {
			return null;
		}
	}

	/**
	 * 转换Map对象成SimpleJdbcCall执行的参数对象
	 * @param inParams
	 * @param outParams
	 * @return SimpleJdbcCall执行的参数对象
	 * @author zengpzh
	 */
	private SqlParameterSource mapToParameter(Map<String, Object> inParams, String... outParams) {
		HashMap<String, Object> params = new HashMap<String, Object>();
		if (inParams != null) {
			params.putAll(inParams);
		}
		if (outParams != null && outParams.length > 0) {
			for (String outParam : outParams) {
				params.put(outParam, null);
			}
		}
		if (params.size() > 0) {
			return new MapSqlParameterSource(params);
		} else {
			return null;
		}
	}

	/**
	 * 转换Map对象成SimpleJdbcCall执行的参数对象
	 * @param inParams
	 * @param outParams
	 * @return SimpleJdbcCall执行的参数对象
	 * @author zengpzh
	 */
	private SqlParameterSource mapToParameterInPackage(Map<String, Object> inParams, Map<String, Object> outParams) {
		HashMap<String, Object> params = new HashMap<String, Object>();
		if (inParams != null && !inParams.isEmpty()) {
			params.putAll(inParams);
		}
		if (outParams != null && !outParams.isEmpty()) {
			params.putAll(outParams);
		}
		if (params.size() > 0) {
			return new MapSqlParameterSource(params);
		} else {
			return null;
		}
	}
	
	/**
	 * 调用储存过程，返回过程输出参数的值 返回值是一个Map，这个Map里包含了所有输出参数及值，key是输出参数名称，value是输出参数值
	 * @param procedureName 过程名
	 * @param inParams 过程输入参数
	 * @param outParams 过程输出参数
	 * @return 过程返回一个Map，这个Map包含所有在存储过程中指定的参数名称作为key。
	 * @throws Exception
	 * @author zengpzh
	 */
	public Map<String, Object> callProcedureForOutParameters(String procedureName, Map<String, Object> inParams, String... outParams) throws Exception {
		SimpleJdbcCall callActor = this.newCallActor(procedureName);
		SqlParameterSource params = this.mapToParameter(inParams, outParams);
		if (params != null) {
			return callActor.execute(params);
		} else {
			return callActor.execute();
		}
	}

	/**
	 * 调用储存过程，返回过程输出参数的值 返回值是一个Map，这个Map里包含了所有输出参数及值，key是输出参数名称，value是输出参数值
	 * @param packageAndProcedureName 包名及过程名
	 * @param inParams 过程输入参数
	 * @param outParams 过程输出参数
	 * @return 过程返回一个Map，这个Map包含所有在存储过程中指定的参数名称作为key。
	 * @throws Exception
	 * @author zengpzh
	 */
	public Map<String, Object> callProcedureInPackageForOutParameters(String packageAndProcedureName, Map<String, Object> inParams, String... outParams) throws Exception {
		int position = packageAndProcedureName.indexOf(".");
		String packageName = "";
		String procedureName = "";
		if (position > 0) {
			packageName = packageAndProcedureName.substring(0, position);
			procedureName = packageAndProcedureName.substring(position + 1);
		}
		else {
			procedureName = packageAndProcedureName;
		}
		SimpleJdbcCall callActor = this.newCallActor(procedureName);
		if (packageName != null && !"".equals(packageName)) {
			callActor.withCatalogName(packageName);
		}
		SqlParameterSource params = this.mapToParameter(inParams, outParams);
		if (params != null) {
			return callActor.execute(params);
		} else {
			return callActor.execute();
		}
	}
	
	/**
	 * 调用储存过程，返回过程的结果集 返回值是一个过程的结果集，这个过程的结果集包含了查询的结果集
	 * @param procedureName 过程名
	 * @param inParams 过程输入参数
	 * @return 返回值是一个过程的结果集，这个过程的结果集包含了查询的结果集
	 * @throws Exception
	 * @author zengpzh
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> callProcedureForResultSet(String procedureName, Map<String, Object> inParams) throws Exception {
		SimpleJdbcCall callActor = this.newCallActor(procedureName);
		callActor.returningResultSet("callProcedureResultSet",new CallerRowMapper());
		SqlParameterSource params = this.mapToParameter(inParams);
		Map<String, Object> result = null;
		if (params != null) {
			result = callActor.execute(params);
		} else {
			result = callActor.execute();
		}
		if (result != null) {
			return (List<Map<String, Object>>) result.get("callProcedureResultSet");
		} else {
			return new ArrayList<Map<String, Object>>(0);
		}
	}



	/**
	 * 调用储存过程，返回过程的输出参数的值及结果集
	 * 输出参数的值：返回值是一个Map，这个Map里包含了所有输出参数及值，key是输出参数名称，value是输出参数值
	 * 结果集：是一个过程的结果集，这个过程的结果集包含了查询的结果集
	 * 
	 * @param procedureName 过程名
	 * @param inParams 过程输入参数
	 * @return 返回值是一个过程的结果集，这个过程的结果集包含了查询的结果集
	 * @throws Exception
	 * @author zengpzh
	 */
	@SuppressWarnings("unchecked")
	public ProcedureResult callProcedureForOutParametersAndResultSet(String procedureName, Map<String, Object> inParams,String... outParams) throws Exception {
		ProcedureResult procedureResult = new ProcedureResult();
		SimpleJdbcCall callActor = this.newCallActor(procedureName);
		callActor.returningResultSet("callProcedureResultSet",new CallerRowMapper());
		SqlParameterSource params = this.mapToParameter(inParams, outParams);
		Map<String, Object> result = null;
		if (params != null) {
			result = callActor.execute(params);
		} else {
			result = callActor.execute();
		}
		if (result != null) {
			procedureResult.setResultSet((List<Map<String, Object>>) result.get("callProcedureResultSet"));
			HashMap<String, Object> _outParameterResult = null;
			if (outParams != null && outParams.length > 0) {
				_outParameterResult = new HashMap<String, Object>(outParams.length);
				for (String outParam : outParams) {
					_outParameterResult.put(outParam, result.get(outParam));
				}
			} else {
				_outParameterResult = new HashMap<String, Object>(0);
			}
			procedureResult.setOutParameterResult(_outParameterResult);
		} else {
			procedureResult.setOutParameterResult(new HashMap<String, Object>(0));
			procedureResult.setResultSet(new ArrayList<Map<String, Object>>(0));
		}
		return procedureResult;
	}

	/**
	 * 调用储存过程，返回过程的输出参数的值及结果集
	 * 输出参数的值：返回值是一个Map，这个Map里包含了所有输出参数及值，key是输出参数名称，value是输出参数值
	 * 结果集：是一个过程的结果集，这个过程的结果集包含了查询的结果集
	 * 
	 * @param packageName 包名
	 * @param procedureName 过程名
	 * @param inParams 过程输入参数
	 * @return 返回值是一个过程的结果集，这个过程的结果集包含了查询的结果集
	 * @throws Exception
	 * @author zengpzh
	 */
	@SuppressWarnings("unchecked")
	public ProcedureResult callProcedureInPackageForOutParametersAndResultSet(String procedureName, String packageName, Map<String, Object> inParams, Map<String, Object> outParams) throws Exception {
		ProcedureResult procedureResult = new ProcedureResult();
		SimpleJdbcCall callActor = this.newCallActor(procedureName);
		if (packageName != null && !"".equals(packageName)) {
			callActor.withCatalogName(packageName);
		}
		callActor.returningResultSet("callProcedureResultSet", new CallerRowMapper());
		SqlParameterSource params = this.mapToParameterInPackage(inParams, outParams);
		Map<String, Object> result = null;
		if (params != null) {
			result = callActor.execute(params);
		} else {
			result = callActor.execute();
		}
		if (result != null) {
			Set<?> set = result.keySet();
			if (set != null && !set.isEmpty()) {
				Iterator<?> it = set.iterator();
				HashMap<String, Object> _outParameterResult = new HashMap<String, Object>(outParams.size());
				while (it.hasNext()) {
					String key = String.valueOf(it.next());
					Object object = result.get(key);
					if (object instanceof ArrayList) {
						procedureResult.setResultSet((List<Map<String, Object>>)object);
					}
					else {
						_outParameterResult.put(key, object);
					}
				}
				if (procedureResult.getResultSet() == null) {
					procedureResult.setResultSet(new ArrayList<Map<String, Object>>(0));
				}
				procedureResult.setOutParameterResult(_outParameterResult);
			}
			else {
				procedureResult.setOutParameterResult(new HashMap<String, Object>(0));
				procedureResult.setResultSet(new ArrayList<Map<String, Object>>(0));
			}
		} else {
			procedureResult.setOutParameterResult(new HashMap<String, Object>(0));
			procedureResult.setResultSet(new ArrayList<Map<String, Object>>(0));
		}
		return procedureResult;
	}
	
	/**
	 * 储存过程结果集包装类
	 * 
	 * @author zengpzh
	 * @version 0.1 (2009.12.16)
	 * @modify zengpzh (2009.12.16)
	 */
	private class CallerRowMapper implements RowMapper<Object> {
		public Object mapRow(ResultSet resultSet, int rowNum) throws SQLException {
			Map<String, Object> result = null;
			ResultSetMetaData rsmd = resultSet.getMetaData();
			int columnCount = rsmd.getColumnCount();
			if (columnCount <= 0) {
				throw new NoSelectedColumns();
			} else {
				result = new HashMap<String, Object>(columnCount);
				for (int i = 1; i <= columnCount; i++) {
					result.put(rsmd.getColumnName(i).toUpperCase(), resultSet.getObject(i));
				}
			}
			return result;
		}
	}

	/**
	 * 创建自动保存数据对象
	 * @author zengpzh
	 * @return 自动保存数据对象
	 */
	protected SimpleJdbcInsert newInsertActor(String tableName) {
		return new SimpleJdbcInsert(this.getDataSource()).withTableName(tableName);
	}

	/**
	 * 创建过程调用对象
	 * @return 过程调用对象
	 * @author zengpzh
	 */
	protected SimpleJdbcCall newCallActor(String procedureName) {this.runTransactionIsolation();
		return new SimpleJdbcCall(this.getDataSource()).withProcedureName(procedureName);
	}

	/**
	 * 转 数组 为 字符串
	 * @param params
	 * @return
	 * @author zengpzh
	 */
	private String buildRunSql(String sql, List<Object> params) {
		String runSql = sql;
		if (sql != null && params != null) {
			int paramCount = params.size();
			for (int i = 0; i < paramCount; i++) {
				Object obj = params.get(i);
				int index = runSql.indexOf("?");
				if(index > -1) {
					if (obj instanceof String) {
						String param = String.valueOf(obj);
						if (param != null && !"".equals(param.trim())) {
							if (!param.startsWith("'")) {
								param = "'" + param;
							}
							if (!param.endsWith("'")) {
								param = param + "'";
							}
						} else {
							param = "''";
						}
						runSql = runSql.substring(0, index) + param
								+ runSql.substring(index + 1, runSql.length());
					} else if (obj instanceof Integer || obj instanceof Double || obj instanceof Float) {
						runSql = runSql.substring(0, index) + obj + runSql.substring(index + 1, runSql.length());
					} else {
						runSql = runSql.substring(0, index) + obj + runSql.substring(index + 1, runSql.length());
					}
				}
			}
		}
		if (runSql != null) {
			while (runSql.indexOf("  ") > -1 || runSql.indexOf("	") > -1) {
				runSql = runSql.replace("  ", " ").replace("	", " ");
			}
		}
		return runSql.trim() + " ;";
	}

	/**
	 * 转 对象 为 字符串
	 * @param sql
	 * @param obj
	 * @return
	 * @author zengpzh
	 */
	private String buildRunSql(String sql, Object obj) {
		int _1 = sql.indexOf("(");
		int _2 = sql.indexOf(")");
		String fields = sql.substring(_1 + 1, _2);
		String[] fieldArray = fields.split(",");
		String runSql = sql.substring(0, _2).toLowerCase() + ") values (";
		for (String field : fieldArray) {
			try {
				runSql += "'" + PropertyUtils.getProperty(obj, field.trim())+ "', ";
			} catch (Exception e) {
				runSql += "'', ";
			}
		}
		runSql = runSql.trim();
		runSql = runSql.substring(0, runSql.length() - 1);
		runSql += ")";
		return runSql.trim() + " ;";
	}

	/**
	 * 测试一字符串是否为空或空白，如果为空或空白则抛出异常 NoSQLException
	 * 
	 * @param aString
	 * @throws Exception
	 * @author zengpzh
	 */
	public void testStringIsNullOrBlank(String aString) throws NoSQLException {
		if (aString == null) {
			throw new NoSQLException();
		} else if ("".equals(aString.trim())) {
			throw new NoSQLException();
		}
	}

	/**
	 * 控制台打印信息
	 * @param sql  打印的信息
	 * @author zengpzh
	 */
	public void printInfoOnConsole(String sql) {
		this.printInfoOnConsole(sql, null);
	}

	/**
	 * 控制台打印信息
	 * @param sql 打印的信息
	 * @param params 参数
	 * @author zengpzh
	 */
	public void printInfoOnConsole(String sql, List<Object> params) {
		if (allowPrintInfoOnConsole) {
			 if (allowPrintCurrentThread && allowPrintInfoOnConsole) {
				 log.info("CurrentThread[Id:"+Thread.currentThread().getId()+"，Name:"+Thread.currentThread().getName()+"]");
			 }
			String threadInfo = "";
			if (params != null) {
				if (allowPrintInfoOnConsole) {
					log.info(threadInfo + "" + this.buildRunSql(sql, params));
				}
			} else {
				if (allowPrintInfoOnConsole) {
					log.info(threadInfo + "" + sql);
				}
			}
		}
	}

	/**
	 * 控制台打印信息
	 * @param sql 打印的信息
	 * @param obj 参数
	 * @author zengpzh
	 */
	public void printInfoOnConsole(String sql, Object obj) {
		 if (allowPrintCurrentThread && allowPrintInfoOnConsole) {
			 log.info("CurrentThread[Id:"+Thread.currentThread().getId()+"，Name:"+Thread.currentThread().getName()+"]");
		 }
		String threadInfo = "";
		if (obj != null) {
			if (allowPrintInfoOnConsole) {
				log.info(threadInfo + "" + this.buildRunSql(sql, obj));
			}
		} else {
			if (allowPrintInfoOnConsole) {
				log.info(threadInfo + "" + sql);
			}
		}
	}

	public boolean getAllowPrintInfoOnConsole() {
		return allowPrintInfoOnConsole;
	}

	/**
	 * 是否允许打印信息到控制台，是否允许显示开发调试功能
	 * 
	 * @param allowPrintInfoOnConsole  true 显示（调试），否则不显示（正常运行）
	 */
	public void setAllowPrintInfoOnConsole(boolean allowPrintInfoOnConsole) {
		this.allowPrintInfoOnConsole = allowPrintInfoOnConsole;
	}

	/**
	 * 获取当前操作的实际表名
	 * 
	 * @return 当前操作的实际表名
	 * @author zengpzh
	 */
	private String getRealTableNameForInsertActor() {
		return this.realTableNameForInsertActor;
	}

	public void setAllowPrintCurrentThread(boolean allowPrintCurrentThread) {
		this.allowPrintCurrentThread = allowPrintCurrentThread;
	}

	public void setAllowPrintTransactionHint(boolean allowPrintTransactionHint) {
		this.allowPrintTransactionHint = allowPrintTransactionHint;
	}
}