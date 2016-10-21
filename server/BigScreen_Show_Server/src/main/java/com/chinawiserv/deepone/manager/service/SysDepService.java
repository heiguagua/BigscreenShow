package com.chinawiserv.deepone.manager.service;

import com.chinawiserv.deepone.manager.model.common.GridInfo;

import java.util.List;
import java.util.Map;

/**
 * 业务接口：SysDepService，请在处理动作里直接调用业务接口，而不是它的实现类。
 * 
 * @author StartFramework
 */
public interface SysDepService{


    /**
     * 查询部门表信息所有数据
     * @return
     * @throws Exception
     * @author wuty
     *
     */
    List<Map<String, Object>> findSysDepList() throws Exception;

}
