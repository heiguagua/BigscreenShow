package com.chinawiserv.deepone.manager.repository;


import java.util.List;
import java.util.Map;

/**
 * 数据访问接口：ServerMonitorRepository，请将访问数据库的直接方法写到此接口实现类里。
 * 
 * 温馨提示： 1、如果您准备访问数据库，请使用：this.getDbTemplate()... A、DbTemplate里的void
 * saveData(...)方法让您无需写SQL语句即能把数据存入到数据库，并包含批量处理！ B、DbTemplate里的void
 * saveDataAndReturnKey(...)方法让您无需写SQL语句即能把数据存入到数据库，并返回数据库自动产生的键值！
 * C、DbTemplate里的findForXXX(...)方法让您随心所欲的查询您需要的内容！ D、DbTemplate里的void
 * updateData(...)方法让您对象化方式修改数据库的内容！ E、DbTemplate里的int
 * updateWithParams(...)方法让您随心所欲的修改数据库的内容！ 2、如果您准备使用某工具，请使用：this.getTT()...
 * A、ToolTemplate里的dt工具提供了丰富的日期处理方法！ B、ToolTemplate里的int
 * loadPropertiesToBeanFormMap(...)方法能方便的将Map里的值拷贝到POJO对象中！
 * C、ToolTemplate里的getXXXFromMap(Map<String, Object> map, String
 * key)方法能方便的从Map里获取指定键的值 ！ 3、如果DbTemplate里所封装的功能不能达到您要求，请使用this.getJdbc()...
 * 
 * 
 * @author StartFramework
 */
public interface ServerMonitorRepository {


    /**
     * 查询各个服务器监控信息
     * @return
     * @throws Exception
     */
    public List<Map<String,Object>> getServerMonitorList() throws Exception;

    /**
     * 查询各个服务器监控信息
     * @return
     * @throws Exception
     */
    public int getVirtualMachineNum() throws Exception;


}
