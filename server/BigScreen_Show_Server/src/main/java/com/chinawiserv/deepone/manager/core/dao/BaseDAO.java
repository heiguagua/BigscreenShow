package com.chinawiserv.deepone.manager.core.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * 数据库访问对象顶层基类
 * <pre>
 * 本类主要提供一些共用的访问方法，主要包含：
 *   1、（不）带参数查询 多行多列，使用SQL语句查询
 *   2、（不）带参数查询 单整数数据，使用SQL语句查询
 *   3、（不）带参数查询 单字符串数据，使用SQL语句查询
 *   4、（不）带参数查询 单对象数据，使用SQL语句查询
 *   5、带参数 更新（添加、删除、编辑），使用SQL语句查询
 *   6、包装Excel数据对象（createExcelObject）
 *   7、从Map中取得指定键的值
 * </pre>
 * @author zengpzh
 * @version 0.1
 */
public class BaseDAO {
	
	/**
	 * 数据库访问模板
	 */
	private DBTemplate dbTemplate;

	/**
	 * 取得 数据库访问模板
	 * @return 数据库访问模板
	 * @author zengpzh
	 */
	protected DBTemplate getDbTemplate() {
		return dbTemplate;
	}

	/**
	 * 取得 工具访问模板
	 * @return 工具访问模板
	 * @author zengpzh
	 */
	public ToolTemplate getTT() {
		return this.dbTemplate.getToolTemplate();
	}
	
	/**
	 * 获取原始 SimpleJdbcTemplate
	 * @return SimpleJdbcTemplate
	 * @author zengpzh
	 */
	protected JdbcTemplate getJdbc() {
		return this.dbTemplate.getJdbcTemplate();
	}

	/**
	 * 是否允许打印信息到控制台，是否允许显示开发调试功能
	 * @param allowPrintInfoOnConsole true 显示（调试），否则不显示（正常运行）
	 */
	protected void setAllowPrintInfoOnConsole(boolean allowPrintInfoOnConsole) {
		this.dbTemplate.setAllowPrintInfoOnConsole(allowPrintInfoOnConsole);
	}
	
	/**
	 * 获取当前操作的实际表名
	 * @return 当前操作的实际表名
	 * @author zengpzh
	 */
	protected String getRealTableNameForInsertActor() {
		return "";
	}

	/**
	 * 为模板设置数据库源
	 * @param dataSource
	 */
	@Autowired
	@Qualifier("dataSource")
	public void setDataSource(DataSource dataSource) {
		this.dbTemplate = new DBTemplate(this.getRealTableNameForInsertActor());
		this.dbTemplate.setDataSource(dataSource);
		this.runAfterReadyDataSource();
	}
	
	/**
	 * 数据源准备好之后，执行
	 * @author zengpzh
	 *
	 */
	protected void runAfterReadyDataSource() {
	}
}
