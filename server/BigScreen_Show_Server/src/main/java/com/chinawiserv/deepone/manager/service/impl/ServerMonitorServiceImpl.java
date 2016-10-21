package com.chinawiserv.deepone.manager.service.impl;

import com.chinawiserv.deepone.manager.repository.ServerMonitorRepository;
import com.chinawiserv.deepone.manager.repository.SysDepRepository;
import com.chinawiserv.deepone.manager.service.ServerMonitorService;
import com.chinawiserv.deepone.manager.service.SysDepService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 业务对象：SysDepServiceImpl，此对象应实现对应业务接口DataTableInfoService的所有方法。
 * 
 * @author StartFramework
 */
@Service
public class ServerMonitorServiceImpl implements ServerMonitorService {

	@Autowired
	private ServerMonitorRepository dao;
	

    @Override
    public List<Map<String, Object>> findServerMonitorList() throws Exception {
        return dao.getServerMonitorList();
    }

    @Override
    public int findVirtualMachineNum() throws Exception {
        return dao.getVirtualMachineNum();
    }
}
