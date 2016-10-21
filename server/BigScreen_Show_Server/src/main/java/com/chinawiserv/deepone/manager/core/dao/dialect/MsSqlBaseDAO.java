package com.chinawiserv.deepone.manager.core.dao.dialect;

import com.chinawiserv.deepone.manager.core.dao.BaseDAO;
import com.chinawiserv.deepone.manager.core.dao.DBTemplate;
import com.chinawiserv.deepone.manager.core.util.Tools;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * MS SQl 2005 数据库访问对象层基类
 * <pre>
 * 本类提供一些共用的分页访问方法，主要包含：
 *   1、生成分页链接字符串（Ajax）
 *   2、生成分页链接字符串
 * </pre>
 * @author zengpzh
 * @version 0.1
 */
public class MsSqlBaseDAO extends BaseDAO {
	
	private static final Log log = LogFactory.getLog(MsSqlBaseDAO.class);
	
	protected void runAfterReadyDataSource() {
		/**
		 * 使用隔离级别 前提，快照隔离级别需要在数据库中一次性地激活。激活之后，每个连接可以在需要的时候使用它: 
		 * 使用语句：ALTER DATABASE databaseName SET ALLOW_SNAPSHOT_ISOLATION ON;
		 */
		this.getDbTemplate().setTransactionIsolation("SET TRANSACTION ISOLATION LEVEL SNAPSHOT;");
	}
	
	/**
	 * 分页查询方法（SQL）
	 * @param selectSql 查询SQL语句
	 * @param params 参数
	 * @param pageObj 分页对象
	 * @return 分页数据
	 * @throws Exception
	 * @author zengpzh
	 */
	/*protected List<Map<String, Object>> findPage(String selectSql, ArrayList<Object> params, PageObj pageObj) throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("开始执行（SQL分页查询） 开始时间："+this.getTT().getCurrentDate_YYYYMMDDHHMMSS_millisecond());
		}
		selectSql = this.getTT().handleBlank(selectSql);
        //取出 from 前的内容
        int indexOfFrom = this.getFirstIndexOfFromClause(selectSql);
        if (indexOfFrom < 0) {
        	throw new NoFromClauseException();
        }
        String selectClause = selectSql.substring(0, indexOfFrom);
		
		//取出 order by 语句
		int lastIndexOfOrderByClause = this.getLastIndexOfOrderByClause(selectSql);
        if (lastIndexOfOrderByClause < 0) {
        	throw new NoOrderByClauseException();
        }
		String orderByClause = selectSql.substring(lastIndexOfOrderByClause, selectSql.length());
		
        //取出 from 及 group by 语句后的内容
        String formAndWhereAndGroupByClause = selectSql.substring(indexOfFrom, lastIndexOfOrderByClause);

		pageObj.setTotal(this.findRecordsAmountAutoHandle("", selectClause, formAndWhereAndGroupByClause, params));
		List<Map<String, Object>> result = this.getDbTemplate().findForListWithOutLog(wrapSql(selectClause, orderByClause, formAndWhereAndGroupByClause, pageObj), params); 
		if (log.isDebugEnabled()) {
			log.debug("执行完成（SQL分页查询） 结束时间："+this.getTT().getCurrentDate_YYYYMMDDHHMMSS_millisecond()+" 返回记录数："+result.size()+"，总记录数："+pageObj.getTotal());
		}
		return result;
	}*/
	
	/**
	 * 分页查询方法（SQL），主要为级联查询使用（前面带WITH的语句）
	 * @param withClause 级联查询WITH的语句 with c (...)
	 * @param selectSql 查询SQL语句
	 * @param params 参数
	 * @param pageObj 分页对象
	 * @return 分页数据
	 * @throws Exception
	 * @author zengpzh
	 * @modify zengpzh
	 */
	/*protected List<Map<String, Object>> findPageForWith(String withClause, String selectSql, ArrayList<Object> params, PageObj pageObj) throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("开始执行（SQL分页查询，包含With子句） 开始时间："+this.getTT().getCurrentDate_YYYYMMDDHHMMSS_millisecond());
		}
		selectSql = this.getTT().handleBlank(selectSql);
        //取出 from 前的内容
        int indexOfFrom = this.getFirstIndexOfFromClause(selectSql);
        if (indexOfFrom < 0) {
        	throw new NoFromClauseException();
        }
        String selectClause = selectSql.substring(0, indexOfFrom);
		
		//取出 order by 语句
		int lastIndexOfOrderByClause = this.getLastIndexOfOrderByClause(selectSql);
        if (lastIndexOfOrderByClause < 0) {
        	throw new NoOrderByClauseException();
        }
		String orderByClause = selectSql.substring(lastIndexOfOrderByClause, selectSql.length());
		
        //取出 from 及 group by 语句后的内容
        String formAndWhereAndGroupByClause = selectSql.substring(indexOfFrom, lastIndexOfOrderByClause);

		pageObj.setTotal(this.findRecordsAmountAutoHandle(withClause, selectClause, formAndWhereAndGroupByClause, params));
		List<Map<String, Object>> result = this.getDbTemplate().findForListWithOutLog(withClause+wrapSql(selectClause, orderByClause, formAndWhereAndGroupByClause, pageObj), params);
		if (log.isDebugEnabled()) {
			log.debug("执行完成（SQL分页查询，包含With子句） 结束时间："+this.getTT().getCurrentDate_YYYYMMDDHHMMSS_millisecond()+" 返回记录数："+result.size()+"，总记录数："+pageObj.getTotal());
		}
		return result;
	}*/
	
	/**
	 * 分页查询方法（储存过程）（返回：分页数据）
	 * 警告：如果使用此方法调储存过程，其储存过程必须满足以下条件：
	 * 1、储存过程必须包含名为 startIndex 类型为 int 的输入参数，以接收当前页码
	 * 2、储存过程必须包含名为 endIndex 类型为 int 的输入参数，以接收当前页的记录数量
	 * 3、储存过程必须包含名为 recordsTotal 类型为 int 的输出参数，以返回满足条件的记录数量
	 * 在MS SQL 2005 以上版本中，过程分页SQL语句可参考：
	 * select resultSetAlias.* from ( select * , row_number() over (order by fieldName) as _pro_page_row_num_ from tableName) resultSetAlias where _pro_page_row_num_ between startIndex and recordsTotal ;
	 * @param procedureName 储存过程名称 
	 * @param inParams 储存过程输入参数
	 * @param pageObj 分页对象
	 * @return 分页数据
	 * @throws Exception
	 * @author zengpzh
	 */
	/*protected List<Map<String, Object>> findPageByProcedure(String procedureName, HashMap<String, Object> inParams, PageObj pageObj) throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("开始执行（储存过程："+procedureName+"） 开始时间："+this.getTT().getCurrentDate_YYYYMMDDHHMMSS_millisecond());
		}
		if (inParams == null) {
			inParams = new HashMap<String, Object>(2);
		}
		inParams.put("startIndex", (pageObj.getPageCount()*(pageObj.getPageIndex()-1)+1));
		inParams.put("endIndex", pageObj.getPageCount()*(pageObj.getPageIndex()));
		ProcedureResult procedureResult = this.getDbTemplate().callProcedureForOutParametersAndResultSet(procedureName, inParams, "recordsTotal");
		Map<String, Object> outParameterResult = procedureResult.getOutParameterResult();
		try {
			pageObj.setTotal(Integer.valueOf(String.valueOf(outParameterResult.get("recordsTotal"))));
			if (log.isDebugEnabled()) {
				log.debug("储存过程参数："+inParams.toString());
			}
		} catch (Exception e) {
			pageObj.setTotal(0);
		}
		if (log.isDebugEnabled()) {
			log.debug("执行完成（储存过程："+procedureName+"） 结束时间："+this.getTT().getCurrentDate_YYYYMMDDHHMMSS_millisecond()+" 返回记录数："+procedureResult.getResultSet().size()+"，总记录数："+pageObj.getTotal());
		}
		return procedureResult.getResultSet();
	}*/
	
	/**
	 * 分页查询方法（储存过程）（返回：分页数据、过程输出参数）
	 * 警告：如果使用此方法调储存过程，其储存过程必须满足以下条件：
	 * 1、储存过程必须包含名为 startIndex 类型为 int 的输入参数，以接收当前页码
	 * 2、储存过程必须包含名为 endIndex 类型为 int 的输入参数，以接收当前页的记录数量
	 * 3、储存过程必须包含名为 recordsTotal 类型为 int 的输出参数，以返回满足条件的记录数量
	 * 在MS SQL 2005 以上版本中，过程分页SQL语句可参考：
	 * select resultSetAlias.* from ( select * , row_number() over (order by fieldName) as _pro_page_row_num_ from tableName) resultSetAlias where _pro_page_row_num_ between startIndex and recordsTotal ;
	 * @param procedureName 储存过程名称 
	 * @param inParams 储存过程输入参数
	 * @param pageObj 分页对象
	 * @return 分页数据、过程输出参数
	 * @throws Exception
	 * @author zengpzh
	 */
	/*protected ProcedureResult findPageByProcedureWithOutParameter(String procedureName, HashMap<String, Object> inParams, PageObj pageObj, String... outParams) throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("开始执行（储存过程："+procedureName+"，包含输出参数） 开始时间："+this.getTT().getCurrentDate_YYYYMMDDHHMMSS_millisecond());
		}
		if (inParams == null) {
			inParams = new HashMap<String, Object>(2);
		}
		inParams.put("startIndex", (pageObj.getPageCount()*(pageObj.getPageIndex()-1)+1));
		inParams.put("endIndex", pageObj.getPageCount()*(pageObj.getPageIndex()));
		String[] outParamsWithRecordsTotal = null;
		if (outParams != null) {
			int size = outParams.length;
			outParamsWithRecordsTotal = new String[size+1];
			for(int i=0; i<size; i++) {
				outParamsWithRecordsTotal[i] = outParams[i];
			}
			outParamsWithRecordsTotal[size] = "recordsTotal";
		}
		else {
			outParamsWithRecordsTotal = new String[]{"recordsTotal"};
		}
		ProcedureResult procedureResult = this.getDbTemplate().callProcedureForOutParametersAndResultSet(procedureName, inParams, outParamsWithRecordsTotal);
		Map<String, Object> outParameterResult = procedureResult.getOutParameterResult();
		try {
			pageObj.setTotal(Integer.valueOf(String.valueOf(outParameterResult.get("recordsTotal"))));
			if (log.isDebugEnabled()) {
				log.debug("储存过程参数："+inParams.toString());
			}
		} catch (Exception e) {
			pageObj.setTotal(0);
		}
		if (log.isDebugEnabled()) {
			log.debug("执行完成（储存过程："+procedureName+"，包含输出参数） 结束时间："+this.getTT().getCurrentDate_YYYYMMDDHHMMSS_millisecond()+" 返回记录数："+procedureResult.getResultSet().size()+"，总记录数："+pageObj.getTotal());
		}
		return procedureResult;
	}	*/
	
	/**
	 * 分页查询方法（SQL，For UI）
	 * @param selectSql 查询SQL语句
	 * @param params 参数
	 * @param pageObj 分页对象
	 * @return 分页数据
	 * @throws Exception
	 * @author zengpzh
	 */
	/*protected List<Map<String, Object>> findPageForUI(String selectSql, ArrayList<Object> params, UIPageObj pageObj) throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("开始执行（SQL分页查询，For UI） 开始时间："+this.getTT().getCurrentDate_YYYYMMDDHHMMSS_millisecond());
		}
		selectSql = this.getTT().handleBlank(selectSql);
        //取出 from 前的内容
        int indexOfFrom = this.getFirstIndexOfFromClause(selectSql);
        if (indexOfFrom < 0) {
        	throw new NoFromClauseException();
        }
        String selectClause = selectSql.substring(0, indexOfFrom);
		
		//取出 order by 语句
		int lastIndexOfOrderByClause = this.getLastIndexOfOrderByClause(selectSql);
        if (lastIndexOfOrderByClause < 0) {
        	throw new NoOrderByClauseException();
        }
		String orderByClause = selectSql.substring(lastIndexOfOrderByClause, selectSql.length());
		
        //取出 from 及 group by 语句后的内容
        String formAndWhereAndGroupByClause = selectSql.substring(indexOfFrom, lastIndexOfOrderByClause);

		pageObj.setTotal(this.findRecordsAmountAutoHandle("", selectClause, formAndWhereAndGroupByClause, params));
		pageObj.setPageStr(StringUtil.pageLinkString(pageObj.getTotal(), pageObj));
		List<Map<String, Object>> result = this.getDbTemplate().findForListWithOutLog(wrapSql(selectClause, orderByClause, formAndWhereAndGroupByClause, pageObj), params); 
		if (log.isDebugEnabled()) {
			log.debug("执行完成（SQL分页查询，For UI） 结束时间："+this.getTT().getCurrentDate_YYYYMMDDHHMMSS_millisecond()+" 返回记录数："+result.size()+"，总记录数："+pageObj.getTotal());
		}
		return result;
	}*/

	/**
	 * 批量执行SQL（通过过程），特别提示，无事物控制
	 * 
	 * 过程中PSYS_BatchRunSQLCommnd 的SQL代码
		create PROCEDURE [dbo].[PSYS_BatchRunSQLCommnd]
		    @sqls VARCHAR (8000),
		    @StrSeprate VARCHAR (12),
			@result VARCHAR (8000) out
		AS
			BEGIN
			SET  NOCOUNT ON;
			SELECT @result = '';
			IF (@sqls IS NOT NULL)
			BEGIN
				DECLARE @i INT;
				DECLARE @sql   NVARCHAR (4000)
				SET @sqls = rtrim (ltrim (@sqls));
				SET @i = charindex (@StrSeprate, @sqls);
				WHILE @i >= 1
				BEGIN
					SELECT @sql = left (@sqls, @i - 1);
		
					BEGIN TRY
						EXEC sp_executesql @sql;
					END TRY
					BEGIN CATCH
						SELECT @result = @result + @sql + ';';
					END CATCH
		
					SET @sqls = substring (@sqls, @i + 1, len (@sqls) - @i);
					SET @i = charindex (@StrSeprate, @sqls);
		
					IF (@i <=0 AND @sqls != '')
					BEGIN
						SELECT @sql = @sqls;
						BEGIN TRY
							EXEC sp_executesql @sql;
						END TRY
						BEGIN CATCH
							SELECT @result = @result + @sql + ';';
						END CATCH
		            END
				END
			END
		END
	 * 
	 * 
	 * @param sqls
	 * @param seprate 分隔符
	 * @return 错误的SQl
	 * @throws Exception
	 * @author zengpzh
	 */
	protected String batchRunSQLCommnd(ArrayList<String> sqls, String seprate) throws Exception {
		int limit_byte = 8000;
		StringBuffer errorSqls = new StringBuffer();
		ArrayList<String> _sqls = new ArrayList<String>();
		if (sqls != null && !sqls.isEmpty()) {
			StringBuffer waitSqls = new StringBuffer();
			int _w_byte = 0;
			for (String sql : sqls) {
				int _byte = Tools.getBytesFromAString(sql);
				if (_byte <= limit_byte) {
					_w_byte += _byte;
					if ((limit_byte - _w_byte) >= _byte) {
						waitSqls.append(sql);
					}
					else {
						_sqls.add(waitSqls.toString());
						waitSqls = new StringBuffer();
						_w_byte = 0;
						waitSqls.append(sql);
					}
				}
				else {
					errorSqls.append(sql);
				}
			}
			_sqls.add(waitSqls.toString());
		}
		if(_sqls != null && !_sqls.isEmpty()){
			for (String _sql : _sqls) {
				HashMap<String, Object> inParams = new HashMap<String, Object>(2);
				inParams.put("sqls", _sql);
				inParams.put("StrSeprate", seprate);
				if (log.isDebugEnabled()) {
					log.debug("开始执行（无事务控制批量执行） 开始时间："+this.getTT().getCurrentDate_YYYYMMDDHHMMSS_millisecond());
					log.debug(_sql);
				}
				Map<String, Object> map = this.getDbTemplate().callProcedureForOutParameters("PSYS_BatchRunSQLCommnd", inParams, "result");
				String result = String.valueOf(map.get("result"));
				errorSqls.append(result);
				if (log.isDebugEnabled()) {
					if (result != null && !"".equals(result)) {
						log.debug("未执行成功的SQL："+result);
					}
					log.debug("执行结束（无事务控制批量执行） 开始时间："+this.getTT().getCurrentDate_YYYYMMDDHHMMSS_millisecond());
				}
			}
		}
		return errorSqls.toString();
	}
	
	/**
	 * 自动统计记录数量
	 * @param formAndWhereAndGroupByClause 查询SQL语句
	 * @param params 参数
	 * @return 记录数量
	 * @throws Exception
	 * @author zengpzh
	 */
	private int findRecordsAmountAutoHandle(String withClause, String selectClause, String formAndWhereAndGroupByClause, ArrayList<Object> params) throws Exception {
		return this.getDbTemplate().findForIntegerWithOutLog(withClause + " select count(1) as recordsAmount from ( " + selectClause + " " +formAndWhereAndGroupByClause + " ) as resultSetAlias ", params);
	}


//	/**
//	 * 包装分页SQL语句
//	 * @param selectClause
//	 * @param orderByClause
//	 * @param formAndWhereAndGroupByClause
//	 * @param pageObj 分页对象
//	 * @return 包装后的分页SQL语句
//	 * @throws Exception
//	 * @author zengpzh
//	 */
//	private String wrapSql(String selectClause, String orderByClause, String formAndWhereAndGroupByClause, PageObj pageObj) throws Exception {
//        final String randomFieldName = "_sql_page_row_num_";
//        //生成最后的查询语句
//        StringBuffer sql = new StringBuffer();
//        sql.append("select resultSetAlias.* from ( ");
//        sql.append(selectClause);
//        sql.append(", row_number() over (");
//        sql.append(orderByClause);
//        sql.append(") as ");
//        sql.append(randomFieldName);
//        sql.append(" ");
//        sql.append(formAndWhereAndGroupByClause);
//        sql.append(" ) resultSetAlias ");
//        sql.append(" where ");
//        sql.append(randomFieldName);
//        sql.append(" >= ");
//        sql.append((pageObj.getPageCount()*(pageObj.getPageIndex()-1)+1));
//        sql.append(" and ");
//        sql.append(randomFieldName);
//        sql.append(" <= ");
//        sql.append(pageObj.getPageCount()*(pageObj.getPageIndex()));
//        return sql.toString();
//	}

	@Override
	protected DBTemplate getDbTemplate() {
		return super.getDbTemplate();
	}

	/**
	 * 查询order by子句的位置
	 * @param sql
	 * @return
	 * @author zengpzh
	 */
	private int getFirstIndexOfFromClause(final String sql) {
		if (sql != null && !"".equals(sql.trim())) {
			int index = -1;
			String _sql = sql.toLowerCase();
			int fromAmount = 0;
			int indexTemp = 0;
			while (indexTemp >= 0) {
				indexTemp = _sql.indexOf("from", indexTemp);
				if (indexTemp >= 0) {
					fromAmount ++;
					String selectClause = _sql.substring(0, indexTemp);
					int selectAmount = calculateAmount(selectClause);
					if ((selectAmount - fromAmount) == 0 ) {
						index = indexTemp;
						break;
					}
					else {
						index = indexTemp;
					}
					indexTemp +=4; 
				}
				else {
					break;
				}
			}
			return index;
		} 
		else {
			return -1;
		}
	}

	/**
	 * 计算 select 出现次数
	 * @param aString
	 * @return select 出现次数
	 * @author zengpzh
	 */
	private int calculateAmount(String aString) {
		int amount = 0;
		int indexTemp = 0;
		while (indexTemp >= 0) {
			indexTemp = aString.indexOf("select", indexTemp);
			if (indexTemp >=0) {
				amount ++;
				indexTemp +=6; 
			}
			else {
				break;
			}
		}
		return amount;
	}
	
	/**
	 * 查询order by 子句的位置
	 * 
	 * @param sql
	 * @return
	 * @author zengpzh
	 */
	private int getLastIndexOfOrderByClause(final String sql) {
		if (sql != null && !"".equals(sql.trim())) {
			return sql.toLowerCase().lastIndexOf("order by");
		} else {
			return -1;
		}
	}
}