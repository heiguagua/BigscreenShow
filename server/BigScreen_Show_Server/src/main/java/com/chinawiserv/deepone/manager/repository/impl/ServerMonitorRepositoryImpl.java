package com.chinawiserv.deepone.manager.repository.impl;

import com.chinawiserv.deepone.manager.core.dao.dialect.DialectBaseDAO;
import com.chinawiserv.deepone.manager.repository.ServerMonitorRepository;
import com.chinawiserv.deepone.manager.repository.SysDepRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 数据访问对象：ServerMonitorRepositoryImpl，请将访问数据库的直接方法写到此对象里。
 * 
 * 温馨提示：
 * 1、如果您准备访问数据库，请使用：this.getDbTemplate()...
 *     A、DbTemplate里的void saveData(...)方法让您无需写SQL语句即能把数据存入到数据库，并包含批量处理！
 *     B、DbTemplate里的void saveDataAndReturnKey(...)方法让您无需写SQL语句即能把数据存入到数据库，并返回数据库自动产生的键值！
 *     C、DbTemplate里的findForXXX(...)方法让您随心所欲的查询您需要的内容！
 *     D、DbTemplate里的void updateData(...)方法让您对象化方式修改数据库的内容！
 *     E、DbTemplate里的int updateWithParams(...)方法让您随心所欲的修改数据库的内容！
 * 2、如果您准备使用某工具，请使用：this.getTT()...
 *     A、ToolTemplate里的dt工具提供了丰富的日期处理方法！
 *     B、ToolTemplate里的int loadPropertiesToBeanFormMap(...)方法能方便的将Map里的值拷贝到POJO对象中！
 *     C、ToolTemplate里的getXXXFromMap(Map<String, Object> map, String key)方法能方便的从Map里获取指定键的值 ！
 * 3、如果DbTemplate里所封装的功能不能达到您要求，请使用this.getJdbc()...
 * 
 * @author StartFramework
 */

@Repository
public class ServerMonitorRepositoryImpl extends DialectBaseDAO implements ServerMonitorRepository {


    @Override
    public List<Map<String, Object>> getServerMonitorList() throws Exception {
        String selectSql = " SELECT a.nodeName,a.cpuRate,b.memRate,c.diskRate FROM " +
                " (SELECT s.nodeName,ROUND(ROUND(1-cp.idle,2)*100) AS cpuRate FROM " +
                " (SELECT DISTINCT(nodename) AS nodeName FROM cpu) s" +
                " LEFT JOIN cpu cp ON s.nodeName=cp.nodename AND cp.ctime=(SELECT MAX(ctime) FROM cpu)" +
                " GROUP BY s.nodeName) a " +
                " LEFT JOIN (SELECT s.nodeName,ROUND(ROUND(m.used/m.total,4)*100,2) AS memRate FROM " +
                " (SELECT DISTINCT(nodename) AS nodeName FROM cpu) s" +
                " LEFT JOIN mem m ON s.nodeName=m.nodename AND m.ctime=(SELECT MAX(ctime) FROM mem) AND m.total !=0 AND m.used !=0" +
                " GROUP BY s.nodeName) b ON a.nodeName=b.nodeName" +
                " LEFT JOIN (SELECT s.nodeName,ROUND(ROUND(d.used/(d.avail+d.used),4)*100,2) AS diskRate FROM " +
                " (SELECT DISTINCT(nodename) AS nodeName FROM cpu) s" +
                " LEFT JOIN disk d ON s.nodeName=d.nodename AND d.ctime=(SELECT MAX(ctime) FROM disk)" +
                " GROUP BY s.nodeName) c ON a.nodeName=c.nodeName";
        return getDbTemplate().findForList(selectSql);
    }

    @Override
    public int getVirtualMachineNum() throws Exception {
        String selectSql = " SELECT COUNT(DISTINCT(nodename)) AS num FROM cpu";
        return getDbTemplate().findForInteger(selectSql);
    }


}
