package com.chinawiserv.deepone.manager.controller.data;

import com.chinawiserv.deepone.manager.core.controller.BaseController;
import com.chinawiserv.deepone.manager.model.common.Response;
import com.chinawiserv.deepone.manager.model.common.ResponseHead;
import com.chinawiserv.deepone.manager.service.DataTableInfoService;
import com.chinawiserv.deepone.manager.service.ServerMonitorService;
import com.chinawiserv.deepone.manager.service.SysDepService;
import net.sf.json.JSONArray;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 各个服务器监控数据汇集情况
 * @author wuty
 */
@Controller
@RequestMapping("/data/serverMonitor")
public class ServerMonitorController extends BaseController {

	private static final Log _log = LogFactory.getLog(ServerMonitorController.class);

	@Autowired
	private ServerMonitorService serverMonitorService;


    /**
     * 查询各个服务器的CPU、内存、磁盘的监控明细数据
     * @author wuty
     */
    @RequestMapping("/detail")
    @ResponseBody
    public Response getDepTableNumInfo(HttpServletRequest request) {
        Response response = new Response();
        ResponseHead responseHead = response.getHead();
        try {
            List<Map<String, Object>> resultList = serverMonitorService.findServerMonitorList();
            response.setBody(resultList);
        } catch (Exception e) {
            _log.debug(e.getMessage());
            responseHead.setStatus(ResponseHead.Status.SERVER_EXCEPTION.getCode());
            responseHead.setMessage(e.getMessage());
        }
        return response;
    }

    /**
     * 查询虚拟机设备的个数
     * @author wuty
     */
    @RequestMapping("/number")
    @ResponseBody
    public Response getVirtualMachineNum(HttpServletRequest request) {
        Response response = new Response();
        ResponseHead responseHead = response.getHead();
        List<Integer> resultList = new ArrayList<>();
        try {
            int machineNum = serverMonitorService.findVirtualMachineNum();
            resultList.add(machineNum);
            response.setBody(resultList);
        } catch (Exception e) {
            _log.debug(e.getMessage());
            responseHead.setStatus(ResponseHead.Status.SERVER_EXCEPTION.getCode());
            responseHead.setMessage(e.getMessage());
        }
        return response;
    }

}
