package com.chinawiserv.deepone.manager.core.dao.dialect;

import com.chinawiserv.deepone.manager.core.dao.BaseDAO;
import com.chinawiserv.deepone.manager.model.common.GridInfo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;
import java.util.Map;

public class MySqlBaseDAO  extends BaseDAO {
	
	private static final Log log = LogFactory.getLog(MsSqlBaseDAO.class);
	
	protected void runAfterReadyDataSource() {
		this.getDbTemplate().setTransactionIsolation(null);
	}	
	
	/**
	 * 分页查询方法（SQL）
	 * @param selectSql 查询SQL语句
	 * @param params 参数
	 * @param gridInfo 分页对象
	 * @return 分页数据
	 * @throws Exception
	 * @author zengpzh
	 */
	protected List<Map<String, Object>> findPage(String selectSql, List<Object> params, GridInfo gridInfo) throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("开始执行（SQL分页查询） 开始时间："+this.getTT().getCurrentDate_YYYYMMDDHHMMSS_millisecond());
		}
		gridInfo.setTotal(this.findRecordsAmountAutoHandle(selectSql, params));
		List<Map<String, Object>> result = this.getDbTemplate().findForListWithOutLog(this.wrapSqlGrid(selectSql, gridInfo), params);
		if (log.isDebugEnabled()) {
			log.debug("执行完成（SQL分页查询） 结束时间："+this.getTT().getCurrentDate_YYYYMMDDHHMMSS_millisecond()+" 返回记录数："+result.size()+"，总记录数："+gridInfo.getTotal());
		}
		return result;
	}
	
	/**
	 * 自动统计记录数量
	 * @param selectSql 查询SQL语句
	 * @param params 参数
	 * @return 记录数量
	 * @throws Exception
	 * @author zengpzh
	 */
	private int findRecordsAmountAutoHandle(String selectSql, List<Object> params) throws Exception {
		return this.getDbTemplate().findForIntegerWithOutLog(" select count(1) as recordsAmount from ( " + selectSql +" ) resultSetAlias ", params);
	}

	/**
	 * 为Oracle数据库 计算分页信息
	 * @return
	 * @throws Exception
	 * @author zengpzh
	 */
	private String wrapSqlGrid(String selectSql, GridInfo gridInfo) throws Exception {
		return selectSql + " limit "+gridInfo.getPageSize()*(gridInfo.getCurrent() -1)+", " + gridInfo.getPageSize();
	}

}
