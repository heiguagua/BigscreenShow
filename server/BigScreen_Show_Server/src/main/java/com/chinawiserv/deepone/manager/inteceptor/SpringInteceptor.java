package com.chinawiserv.deepone.manager.inteceptor;

import com.chinawiserv.deepone.manager.core.controller.BaseController;
import com.chinawiserv.deepone.manager.core.util.BrowseTool;
import com.chinawiserv.deepone.manager.core.util.StringUtil;
import com.chinawiserv.deepone.manager.model.common.Authorization;
import com.chinawiserv.deepone.manager.model.common.Response;
import com.chinawiserv.deepone.manager.model.common.ResponseHead;
import com.chinawiserv.deepone.manager.sysexecutor.LogCommand;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * 拦截器
 */
public class SpringInteceptor implements HandlerInterceptor {

    private static final Log log = LogFactory.getLog(SpringInteceptor.class);

    /**
     * 无需登录，可以访问
     */
    private List<String> methodUrls1;

    /**
     * 需要登录，无需授权，可以访问
     */
    private List<String> methodUrls2;

    /**
     * 拦截器初始化
     *
     * @param methodUrls1 无需登录可以访问的url集
     * @param methodUrls2 需要登录无需授权可以访问的url集
     */
    public SpringInteceptor(List<String> methodUrls1, List<String> methodUrls2) {
        /**
         * 无需登录，可以访问
         */
        if (methodUrls1 != null && methodUrls1.size() > 0) {
            this.methodUrls1 = new ArrayList<String>(methodUrls1.size());
            for (String methodUrls : methodUrls1) {
                if (StringUtils.isNotBlank(methodUrls)) {
                    this.methodUrls1.add(methodUrls.toLowerCase());
                }
            }
        } else {
            this.methodUrls1 = new ArrayList<String>(0);
        }

        /**
         * 需要登录，无需授权，可以访问
         */
        if (methodUrls2 != null && methodUrls2.size() > 0) {
            this.methodUrls2 = new ArrayList<String>(methodUrls2.size());
            for (String methodUrls : methodUrls2) {
                if (StringUtils.isNotBlank(methodUrls)) {
                    this.methodUrls2.add(methodUrls.toLowerCase());
                }
            }
        } else {
            this.methodUrls2 = new ArrayList<String>(0);
        }
    }

    /**
     * 方法执行前的预处理
     *
     * @param request  http请求
     * @param response response响应
     * @param handler  操作对象
     * @return 是否拦截
     * @throws Exception
     */
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        boolean allowinvoke = false;
        boolean needLogin = true;
        String message = null;
        if (request != null) {
            String url = request.getServletPath();//注：这里不能用getRequestURI(), 因为该方法会包含ContextPath一起取出来
            while (StringUtils.isNotBlank(url) && url.startsWith("/")) {
                url = url.substring(1);
            }
            String method = request.getMethod();
            if(StringUtils.isNotBlank(method)){
                method = method.toLowerCase();
            }
            url = url.toLowerCase();
            method = method.toLowerCase();
            String methodUrl = method + ":" + url;
            /**
             * 无需登录，可以访问
             */
            if (hasAuthority(this.methodUrls1, methodUrl)) {
                allowinvoke = true;
            } else {
                /**
                 * 需要登录，无需授权，可以访问
                 */
                try {
                    //取出request中的认证信息
                    Authorization authorization = (Authorization) request.getAttribute("Authorization");
                    if (authorization != null) {
                        //检查认证状态
                        boolean authorizationState = authorization.getState();
                        if (authorizationState) {
                            //检查是否无需授权
                            if (hasAuthority(this.methodUrls2, methodUrl)) {
                                allowinvoke = true;
                            } else {
                                //取出认证内容体
                                Claims claims = authorization.getClaims();
                                if (claims != null) {
                                    //取出账号id
                                    String accountId = claims.get("accountId", String.class);
                                    //检查当前账号id是否具有该url权限
                                    if (StringUtils.isNotBlank(accountId)) {
                                        this.formatParameters(request);
                                        if (log.isInfoEnabled()) {
                                            log.info("当前资源：" + url);
                                        }
                                        allowinvoke = true;
                                    } else {
                                        message = "被禁止资源，Url：" + url;
                                        if (handler instanceof HandlerMethod) {
                                            HandlerMethod hm = (HandlerMethod) handler;
                                            Object bean = hm.getBean();
                                            if (bean != null) {
                                                message += "，Action：" + bean.getClass();
                                            }
                                        }
                                        if (log.isInfoEnabled()) {
                                            log.info(message);
                                        }
                                    }
                                    authorization.setNeedLogin(false);
                                }
                            }
                        } else {
                            message = authorization.getMsg();
                        }
                        needLogin = authorization.getNeedLogin();
                    }
                }  catch (Exception e){
                    Response res = new Response(ResponseHead.Status.SERVER_EXCEPTION, e.getMessage());
                    response.getWriter().write(res.toJsonString());
                    return false;
                }
            }
        }
        request.setAttribute("_allowinvoke_", allowinvoke);
        if (!allowinvoke) {
            if(needLogin) this.turnToLoginPage(request, response, handler);
            else if(message != null){
                Response res = new Response(ResponseHead.Status.WARNING_INFO, message);
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(res.toJsonString());
            }
        }
        return allowinvoke;
    }

    /**
     * 方法内容执行完后的处理
     *
     * @param request      request请求
     * @param response     response响应
     * @param handler      操作对象
     * @param modelAndView 返回的视图
     * @throws Exception
     */
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        try {
            if (handler instanceof HandlerMethod) {
                HandlerMethod hm = (HandlerMethod) handler;
                Object bean = hm.getBean();
                if (bean instanceof BaseController) {
                    writeLog((BaseController) bean, request);
                }
            }
        } catch (Exception e) {
            if (log.isErrorEnabled()) {
                log.error(e.getMessage());
            }
        }
    }

    /**
     * 方法完成后的处理
     *
     * @param request  request请求
     * @param response response响应
     * @param handler  操作对象
     * @param e        异常
     * @throws Exception
     */
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception e) throws Exception {
        boolean allowinvoke = request.getAttribute("_allowinvoke_") == null ? false : (Boolean) request.getAttribute("_allowinvoke_");
        if (!allowinvoke) {
            this.turnToLoginPage(request, response, handler);
        }
    }

    /**
     * 写日志
     * @param request request请求
     */
    public void writeLog(BaseController controller, HttpServletRequest request) {
        if(controller != null){
            Object obj = controller.getAndClearLog();
            String logCommand = "";
            if (obj != null) {
                logCommand = String.valueOf(obj);
            }
            if (!"".equals(logCommand.trim()) && !"null".equalsIgnoreCase(logCommand.toLowerCase())) {
                String browserDetail = request.getHeader("User-Agent");
                String browser = BrowseTool.checkBrowse(browserDetail);
                if (request != null) {
                    Authorization authorization = (Authorization) request.getAttribute("Authorization");
                    if (authorization != null) {
                        boolean authorizationState = authorization.getState();
                        if (authorizationState) {
                            Claims claims = authorization.getClaims();
                            if (claims != null) {
                                LogCommand.pack_User_Access_Log(claims.get("accountId", String.class), claims.get("loginName", String.class), claims.get("realName", String.class), BaseController.getIpAddr(request), browser, browserDetail, logCommand);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 跳转至登录页
     *
     * @param request
     * @param response
     */
    private void turnToLoginPage(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (request != null) {
            try {
                RequestDispatcher rd = request.getRequestDispatcher("/needLogin");
                rd.forward(request, response);
            } catch (Exception e) {
                if (log.isErrorEnabled()) {
                    log.error(e.getMessage());
                }
            }
        }
    }

    /**
     * 获取上下文基础地址
     *
     * @return 上下文基础地址
     * @author zengpzh
     */
    public String getBasePath(HttpServletRequest request) {
        return StringUtil.getBasePath(request);
    }

    /**
     * 格式化 请求参数
     *
     * @param request
     * @return
     * @throws Exception
     * @author zengpzh
     */
    public Map<String, String[]> formatParameters(HttpServletRequest request) throws Exception {
        Map<String, String[]> map = null;
        if (request != null) {
            Map<?, ?> parameters = request.getParameterMap();
            if (parameters != null) {
                map = new HashMap<String, String[]>();
                Set<?> set = parameters.keySet();
                if (set != null && !set.isEmpty()) {
                    Iterator<?> it = set.iterator();
                    while (it.hasNext()) {
                        String key = String.valueOf(it.next());
                        String[] values = (String[]) parameters.get(key);
                        if (values != null) {
                            for (int i = 0; i < values.length; i++) {
                                if (values[i] instanceof String) {
                                    values[i] = StringUtil.formatParameter(values[i]);
                                }
                            }
                        }
                        map.put(key, values);
                    }
                }
            }
        }
        return map;
    }

    /**
     * 验证url是否匹配urls集中的权限
     *
     * @param methodUrls 权限url集
     * @param methodUrl  待验证的url
     * @return 是否具备权限
     */
    private boolean hasAuthority(List<String> methodUrls, String methodUrl) {
        if (methodUrls != null && !methodUrls.isEmpty() && StringUtils.isNotBlank(methodUrl)) {
            //如果权限method url集合中包含当前method url,则直接通过
            if (methodUrls.contains(methodUrl)) return true;
            //分隔当前method url
            String[] methodUrlArray = methodUrl.split(":");
            //分隔后必须包含method和url
            if(methodUrlArray.length == 2) {
                String method = methodUrlArray[0];
                String url = methodUrlArray[1];
                if(StringUtils.isNotBlank(method) && StringUtils.isNotBlank(url)) {
                    //遍历权限method url
                    for (String tempMethodUrl : methodUrls) {
                        //分隔权限method url
                        String[] tempMethodUrlArray = tempMethodUrl.split(":");
                        String tempMethod;
                        String tempUrl;
                        if (tempMethodUrlArray.length == 2) {
                            tempMethod = tempMethodUrlArray[0];
                            tempUrl = tempMethodUrlArray[1];
                        } else if (tempMethodUrlArray.length == 1) {
                            tempMethod = "*";
                            tempUrl = tempMethodUrlArray[0];
                        } else continue;
                        if (StringUtils.isNotBlank(tempMethod) && StringUtils.isNotBlank(tempUrl)) {
                            //如果权限的method是*或与当前url的method相等
                            if (tempMethod.equals("*") || tempMethod.equals(method)){
                                //且权限的url是**或与当前url相等，则通过
                                if(tempUrl.equals("**") || tempUrl.equals(url)) return true;
                                else if (tempUrl.contains("**")) {//否则判断权限的url中是否包含**，如果是，则除去**部分，判断当前url是否匹配
                                    int index = tempUrl.indexOf("**");
                                    //匹配则通过
                                    if (url.indexOf(tempUrl.substring(0, index)) >= 0 && url.indexOf(tempUrl.substring(index + 2, tempUrl.length())) >= 0) return true;
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }
}