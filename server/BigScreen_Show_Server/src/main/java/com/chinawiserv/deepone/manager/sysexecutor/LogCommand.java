package com.chinawiserv.deepone.manager.sysexecutor;

import com.chinawiserv.deepone.manager.core.util.DateTime;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.Serializable;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;


/**
 * commands容器
 * @author zengpzh
 */
public class LogCommand implements Serializable {

	private static final long serialVersionUID = 337321753259461934L;

	private static final Log log = LogFactory.getLog(LogCommand.class);

	/**
	 * commands容器
	 */
	private static ConcurrentLinkedQueue<String> commands = new ConcurrentLinkedQueue<String>();

	/**
	 * 每次处理最大条数
	 */
	public static int _max = 32;

	public static int _total = 12000;
	
	public static String _showLogs = "0";
	
	public static String insertSql = " insert into TSYS_SYSTEM_ACCESS_LOG(guid, accountId, loginName, realName,  accessIP, browser, browserDetail, command, accessDateTime) values ";

	/**
	 * 取出 命令
	 * @return 命令
	 * @author zengpzh
	 */
	protected synchronized static String poll() {
		if (commands != null) {
			StringBuffer sql = new StringBuffer(_max * 3000);
			Object obj = commands.poll();
			int _num = 0;
			while (obj != null && !"null".equals(obj)) {
				sql.append(obj + ",");
				obj = null;
				_num++;
				if (_num >= _max) {
					break;
				}
				obj = commands.poll();
			}
			if ("1".equals(_showLogs) && log.isDebugEnabled()) {
				log.debug("从CommandsPool.commands中取出记录数：" + _num + "，剩余总记录数："+ commands.size() + " - " + DateTime.getCurrentDate_YYYYMMDDHHMMSS_millisecond());
			}
			System.gc();
			String sqlLast = sql.toString();
			if (sqlLast.endsWith(",")) {
				sqlLast = sqlLast.substring(0, sqlLast.length() - 1);
			}
 			return sqlLast;
		} else {
			return "";
		}
	}

	/**
	 * 包装访问日志
	 * @param accountId
	 * @param accessIP
	 * @param command
	 * @author zengpzh
	 */
	public synchronized static void pack_User_Access_Log(String accountId, String loginName, String realName, String accessIP, String browser, String browserDetail, String command) {
		if (command != null && !"".equals(command.trim())) {
			command = command.replace("'", "''");
		}
		String guid = UUID.randomUUID().toString();
		LogCommand.saveToDatabase("('"+ guid+ "', '"+ accountId+ "', '"+ loginName+ "', '"+ realName+ "', '"+ accessIP+ "', '"+ browser+ "', '"+ browserDetail + "', '"+command+"', '"+DateTime.getCurrentDate_YYYYMMDDHHMMSS()+"')");
	}
	
	/**
	 * 写入 命令
	 * @return 命令
	 * @author zengpzh
	 */
	private synchronized static void saveToDatabase(String command) {
		try {
			if (commands != null) {
				commands.add(command);
				if (commands.size() > _total) {
					commands.poll();
				}
			}
			if ("1".equals(_showLogs)) {
				log.debug("一条SQL命令存入CommandsPool.commands，剩余总记录数："+ commands.size() + " - "+ DateTime.getCurrentDate_YYYYMMDDHHMMSS_millisecond() + " - command：" + command);
			}
		} catch (Exception e) {
			if (log.isErrorEnabled()) {
				log.error("LogExecutor.DataHandlerRunnable.dataHandler();错误信息："+ e.getMessage());
			}
		}
	}
}