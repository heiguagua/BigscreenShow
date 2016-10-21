package com.chinawiserv.deepone.manager.service;

import java.util.List;
import java.util.Map;

/**
 * 业务接口：ServerMonitorService，请在处理动作里直接调用业务接口，而不是它的实现类。
 * 
 * @author StartFramework
 */
public interface ServerMonitorService {


    /**
     * 查询各个服务器的CPU、内存、磁盘的监控数据
     * @return
     * @throws Exception
     * @author wuty
     *
     */
    List<Map<String, Object>> findServerMonitorList() throws Exception;

    /**
     * 查询虚拟机的个数
     * @return
     * @throws Exception
     * @author wuty
     *
     */
    int findVirtualMachineNum() throws Exception;

}
