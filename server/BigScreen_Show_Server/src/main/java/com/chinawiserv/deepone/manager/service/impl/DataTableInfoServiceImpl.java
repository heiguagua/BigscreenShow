package com.chinawiserv.deepone.manager.service.impl;

import com.chinawiserv.deepone.manager.repository.DataTableInfoRepository;
import com.chinawiserv.deepone.manager.service.DataTableInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 业务对象：DataTableInfoServiceImpl，此对象应实现对应业务接口DataTableInfoService的所有方法。
 * 
 * @author StartFramework
 */
@Service
public class DataTableInfoServiceImpl implements DataTableInfoService {

	@Autowired
	private DataTableInfoRepository dao;
	

    @Override
    public List<Map<String, Object>> findAllTableInfoList() throws Exception {
        return dao.getAllTableInfoList();
    }

    @Override
    public List<Map<String, Object>> findDepTableNum() throws Exception {
        return dao.getDepTableNum();
    }

    @Override
    public List<Map<String, Object>> findDepColumnNum() throws Exception {
        return dao.getDepColumnNum();
    }
}
