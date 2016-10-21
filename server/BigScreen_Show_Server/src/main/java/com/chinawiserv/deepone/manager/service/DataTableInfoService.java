package com.chinawiserv.deepone.manager.service;


import java.util.List;
import java.util.Map;

/**
 * 业务接口：DataTableInfoService，请在处理动作里直接调用业务接口，而不是它的实现类。
 * 
 * @author StartFramework
 */
public interface DataTableInfoService{


    /**
     * 查询采集表信息所有数据
     * @return
     * @throws Exception
     * @author wuty
     *
     */
    List<Map<String, Object>> findAllTableInfoList() throws Exception;

    /**
     * 查询各个部门采集的表的数量
     * @return
     * @throws Exception
     * @author wuty
     *
     */
    List<Map<String, Object>> findDepTableNum() throws Exception;

    /**
     * 查询各个部门采集的字段的数量
     * @return
     * @throws Exception
     * @author wuty
     *
     */
    List<Map<String, Object>> findDepColumnNum() throws Exception;
}
