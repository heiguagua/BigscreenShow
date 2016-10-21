package com.chinawiserv.deepone.manager.controller.data;

import com.chinawiserv.deepone.manager.core.controller.BaseController;
import com.chinawiserv.deepone.manager.model.common.Response;
import com.chinawiserv.deepone.manager.model.common.ResponseHead;
import com.chinawiserv.deepone.manager.service.DataTableInfoService;
import com.chinawiserv.deepone.manager.service.SysDepService;
import net.sf.json.JSONArray;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 各部门数据汇集情况
 * @author wuty
 */
@Controller
@RequestMapping("/data/depDataInfo")
public class DepDataInfoController extends BaseController {

	private static final Log _log = LogFactory.getLog(DepDataInfoController.class);

	@Autowired
	private DataTableInfoService dataTableInfoService;
    @Autowired
    private SysDepService sysDepService;

	/**
	 * 部门数量、表数量、列数量的统计信息
	 * @author wuty
	 */
	@RequestMapping("/quantity")
	@ResponseBody
	public Response getQuantityInfo(HttpServletRequest request) {
		Response response = new Response();
		ResponseHead responseHead = response.getHead();
        List<Map<String,Integer>> resultList = new ArrayList<>();
        Map<String,Integer> resultMap = new HashMap<>();
		try {
            int tableNum = 0;
            int columnNum = 0;
            int depNum = 0;
			List<Map<String, Object>> dataTableList = dataTableInfoService.findAllTableInfoList();
            tableNum = dataTableList.size();
            for(Map<String,Object> dataTableInfo : dataTableList){
                String columns = dataTableInfo.get("TABLE_DETAIL").toString();
                JSONArray columnArray = JSONArray.fromObject(columns);
                columnNum+=columnArray.size();
            }
            List<Map<String, Object>> sysDepList = sysDepService.findSysDepList();
            if(sysDepList!= null && !sysDepList.isEmpty()){
                depNum = sysDepList.size();
            }
            resultMap.put("tableNum",tableNum);
            resultMap.put("columnNum",columnNum);
            resultMap.put("depNum",depNum);
            resultList.add(resultMap);
			response.setBody(resultList);
//			this.writeLog("查询用户（以列表形式展示）");
		} catch (Exception e) {
			_log.debug(e.getMessage());
			responseHead.setStatus(ResponseHead.Status.SERVER_EXCEPTION.getCode());
			responseHead.setMessage(e.getMessage());
		}
		return response;
	}

    /**
     * 各个部门的表数量详情信息
     * @author wuty
     */
    @RequestMapping("/table")
    @ResponseBody
    public Response getDepTableNumInfo(HttpServletRequest request) {
        Response response = new Response();
        ResponseHead responseHead = response.getHead();
        try {
            List<Map<String, Object>> resultList = dataTableInfoService.findDepTableNum();
            response.setBody(resultList);
//			this.writeLog("查询用户（以列表形式展示）");
        } catch (Exception e) {
            _log.debug(e.getMessage());
            responseHead.setStatus(ResponseHead.Status.SERVER_EXCEPTION.getCode());
            responseHead.setMessage(e.getMessage());
        }
        return response;
    }

    /**
     * 各个部门的字段数量详情信息
     * @author wuty
     */
    @RequestMapping("/column")
    @ResponseBody
    public Response getDepColumnNumInfo(HttpServletRequest request) {
        Response response = new Response();
        ResponseHead responseHead = response.getHead();
        List<Map<String,String>> resultList = new ArrayList<>();
        Map<String,Integer> resultMap = new HashMap<>();
        try {
            List<Map<String, Object>> depColumnList = dataTableInfoService.findDepColumnNum();
            for(Map<String,Object> dataColumnInfo : depColumnList){
                String depName = dataColumnInfo.get("depName").toString();
                String columnDetail = dataColumnInfo.get("tableDetail").toString();
                int singleColumnNum = JSONArray.fromObject(columnDetail).size();
                if(depName!=null && !"".equals(depName)){
                    if(resultMap.containsKey(depName)){
                        int columnNum = resultMap.get(depName);
                        resultMap.put(depName,columnNum+singleColumnNum);
                    }else{
                        resultMap.put(depName,singleColumnNum);
                    }
                }
            }
            Set<String> keys = resultMap.keySet();
            for(String key : keys){
                Map<String,String> columnsMap = new HashMap<>();
                columnsMap.put("depName",key);
                columnsMap.put("columnNum",String.valueOf(resultMap.get(key)));
                resultList.add(columnsMap);
            }
            response.setBody(resultList);
//			this.writeLog("查询用户（以列表形式展示）");
        } catch (Exception e) {
            _log.debug(e.getMessage());
            responseHead.setStatus(ResponseHead.Status.SERVER_EXCEPTION.getCode());
            responseHead.setMessage(e.getMessage());
        }
        return response;
    }
}
