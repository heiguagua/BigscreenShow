package com.chinawiserv.deepone.manager.service.impl;

import com.chinawiserv.deepone.manager.model.common.GridInfo;
import com.chinawiserv.deepone.manager.repository.DataTableInfoRepository;
import com.chinawiserv.deepone.manager.repository.SysDepRepository;
import com.chinawiserv.deepone.manager.service.DataTableInfoService;
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
public class SysDepServiceImpl implements SysDepService {

	@Autowired
	private SysDepRepository dao;
	

    @Override
    public List<Map<String, Object>> findSysDepList() throws Exception {
        return dao.getSysDepList();
    }
}
