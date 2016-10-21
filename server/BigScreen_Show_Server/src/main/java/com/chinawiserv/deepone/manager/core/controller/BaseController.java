package com.chinawiserv.deepone.manager.core.controller;

import com.chinawiserv.deepone.manager.core.util.BrowseTool;
import com.chinawiserv.deepone.manager.core.util.ConvertUtil;
import com.chinawiserv.deepone.manager.core.util.StringUtil;
import com.chinawiserv.deepone.manager.filter.Filter1_Jwt;
import com.chinawiserv.deepone.manager.model.common.Authorization;
import com.chinawiserv.deepone.manager.model.common.JwtInfo;
import com.chinawiserv.deepone.manager.servlet.ValidateCodeCreator;
import com.chinawiserv.deepone.manager.sysexecutor.LogCommand;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.StringUtils;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * 控制器基类
 *
 * @author hadoop
 */
public class BaseController implements Serializable {

    private static final long serialVersionUID = -6380802243887186460L;

    /**
     * 日志记录变量
     */
    private String logCommand;

    /**
     * 取出并清除日志
     */
    public String getAndClearLog() {
        String logCommand = this.logCommand;
        this.logCommand = null;
        return logCommand;
    }

    /**
     * 写日志
     */
    protected void writeLog(String logCommand) {
        this.logCommand = logCommand;
    }

    /**
     * 写日志
     */
    protected void writeLog(String accountId, String loginName, String realName, String logCommand, HttpServletRequest request) {
        if (StringUtils.isNotBlank(accountId) && StringUtils.isNotBlank(loginName) && StringUtils.isNotBlank(realName)
                && StringUtils.isNotBlank(logCommand) && request != null) {
            String browserDetail = request.getHeader("User-Agent");
            String browser = BrowseTool.checkBrowse(browserDetail);
            LogCommand.pack_User_Access_Log(accountId, loginName, realName, getIpAddr(request), browser, browserDetail, logCommand);
        }
    }

    protected HttpSession getSession(HttpServletRequest request) {
        if (request != null) {
            return request.getSession();
        } else {
            return null;
        }
    }

    /**
     * 从Session中获取对象
     *
     * @param key 键
     * @return 获取到的对象
     * @author Allen Zhang
     */
    protected Object getObjFromSession(String key, HttpServletRequest request) {
        Object obj = null;
        if (request != null && key != null) {
            HttpSession session = request.getSession();
            if (session != null) {
                obj = session.getAttribute(key);
            }
        }
        return obj;
    }

    /**
     * 从回话中删除对象
     *
     * @param key
     * @param request
     */
    protected void removeObjFromSession(String key, HttpServletRequest request) {
        if (request != null && key != null) {
            HttpSession session = request.getSession();
            if (session != null) {
                session.removeAttribute(key);
            }
        }
    }

    /**
     * 保存对象到 Session 里
     *
     * @param key     键
     * @param obj     待保存对象
     * @param request
     */
    protected void putToSession(String key, Object obj, HttpServletRequest request) {
        if (request != null) {
            HttpSession session = request.getSession();
            if (session != null) {
                session.setAttribute(key, obj);
            }
        }
    }

    /**
     * 中文搜索，关键字编码转换（只换get方式提交的数据） 当搜索关键字可能包含中文时，请使用本方法转换，否则可能出现乱码
     *
     * @param searchKeyWord 搜索关键字
     * @return 转换后的搜索关键字（正常文字）
     * @author Allen Zhang
     */
    protected String convertToUTF8(String searchKeyWord, HttpServletRequest request) {
        if (searchKeyWord != null && request != null) {
            if ("get".equalsIgnoreCase(request.getMethod())) {
                return ConvertUtil.toUTF8(searchKeyWord).trim();
            } else {
                return searchKeyWord.trim();
            }
        } else {
            return "";
        }
    }

    /**
     * 中文搜索，关键字编码转换（只换post方式提交的数据） 当搜索关键字可能包含中文时，请使用本方法转换，否则可能出现乱码
     *
     * @param searchKeyWord 搜索关键字
     * @return 转换后的搜索关键字（正常文字）
     * @author Allen Zhang
     */
    public String convertToUTF8ForPost(String searchKeyWord, HttpServletRequest request) {
        if (searchKeyWord != null && request != null) {
            if ("post".equalsIgnoreCase(request.getMethod())) {
                return ConvertUtil.toUTF8(searchKeyWord).trim();
            } else {
                return searchKeyWord.trim();
            }
        } else {
            return "";
        }
    }

    /**
     * 验证 提交数据到数据库时输的验证码是否正确
     *
     * @param validateCode 页面显示的验证码
     * @return 验证码正确 返回 true,否则返回 false
     */
    public boolean validateValidateCode(String validateCode, HttpServletRequest request) {
        Date validateCodeExpDateSaved = (Date) this.getObjFromSession(ValidateCodeCreator.VALIDATECODE_EXP_DATE, request);
        String validateCodeSaved = (String) this.getObjFromSession(ValidateCodeCreator.VALIDATECODE, request);
        this.removeObjFromSession(ValidateCodeCreator.VALIDATECODE_EXP_DATE, request);
        this.removeObjFromSession(ValidateCodeCreator.VALIDATECODE, request);
        if (validateCodeExpDateSaved != null && validateCodeSaved != null) {
            return validateCodeExpDateSaved.after(new Date()) && validateCodeSaved.equalsIgnoreCase(validateCode);
        } else {
            return false;
        }
    }

    /**
     * 获取请求 当前 URL
     *
     * @return 当前请求 URL
     * @author zengpzh
     */
    public String getRequestURL(HttpServletRequest request) {
        if (request != null) {
            return request.getRequestURL().toString();
        } else {
            return "";
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
     * @param parameter
     * @return
     * @throws Exception
     * @author zengpzh
     */
    public String formatParameter(String parameter) {
        return StringUtil.formatParameter(parameter);
    }

    protected void writeDialogIdToResponse(HttpServletRequest request, Model model) {
        if (request != null && model != null) {
            Map<String, String[]> parameters = request.getParameterMap();
            if (parameters != null && !parameters.isEmpty()) {
                String[] pid = (String[]) parameters.get("_mochaui_dialog_id");
                if (pid != null && pid.length > 0) {
                    model.addAttribute("_mochaui_dialog_id", pid[0]);
                }
                String[] wid = (String[]) parameters.get("_self_dialog_id");
                if (wid != null && wid.length > 0) {
                    model.addAttribute("_self_dialog_id", wid[0]);
                }
            }
        }
    }

    /**
     * 注册jwt信息并返回认证token
     *
     * @param request
     * @param claims
     * @return
     */
    protected String register(HttpServletRequest request, Claims claims) throws Exception {
        if (claims != null && !claims.isEmpty()) {
            String issuer = getIpAddr(request);
            claims.setIssuer(issuer);
            claims.setIssuedAt(new Date());
            String registerKey = Filter1_Jwt.getRegisterKey(claims);
            if (StringUtils.isNotBlank(registerKey)) {
                JwtInfo jwtInfo = Filter1_Jwt.getRegisteredJwtInfo(registerKey);
                if (jwtInfo == null) {
                    JwtBuilder jwtBuilder = Jwts.builder();
                    jwtBuilder.setClaims(claims);
                    String authedToken = jwtBuilder.signWith(SignatureAlgorithm.HS512, Filter1_Jwt.AUTH_TOKEN_SECURITY_KEY).compact();
                    jwtInfo = new JwtInfo(authedToken, (String) claims.get("loginName"), issuer, request);
                } else jwtInfo.setLatestRequest(request);
                Filter1_Jwt.register(registerKey, jwtInfo);
                return jwtInfo.getToken();
            }
        }
        return null;
    }

    /**
     * 刷新新jwt信息并返回认证token
     * @param request
     * @param claims
     * @return
     * @throws Exception
     */
    protected String refreshRegister(HttpServletRequest request, Claims claims)throws Exception {
        if (claims != null && !claims.isEmpty()) {
            String issuer = getIpAddr(request);
            claims.setIssuer(issuer);
            claims.setIssuedAt(new Date());
            String registerKey = Filter1_Jwt.getRegisterKey(claims);
            if (StringUtils.isNotBlank(registerKey)) {
                JwtBuilder jwtBuilder = Jwts.builder();
                jwtBuilder.setClaims(claims);
                String authedToken = jwtBuilder.signWith(SignatureAlgorithm.HS512, Filter1_Jwt.AUTH_TOKEN_SECURITY_KEY).compact();
                JwtInfo jwtInfo = new JwtInfo(authedToken, (String) claims.get("loginName"), issuer, request);
                Filter1_Jwt.unRegister(registerKey);
                Filter1_Jwt.register(registerKey, jwtInfo);
                return jwtInfo.getToken();
            }
        }
        return null;
    }

    /**
     * 获取客户端ip地址
     *
     * @param httpServletRequest 请求
     * @return ip地址
     */
    public static String getIpAddr(HttpServletRequest httpServletRequest) {
        String ip = httpServletRequest.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = httpServletRequest.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = httpServletRequest.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = httpServletRequest.getRemoteAddr();
        }
        return ip == null ? null : ip.split(",")[0];
    }

    protected Claims getClaims(HttpServletRequest request){
        Authorization authorization = (Authorization) request.getAttribute("Authorization");
        if(authorization != null){
            return authorization.getClaims() == null ? null : authorization.getClaims();
        }
        return null;
    }

    /**
     * 获取用户id
     * @param request request请求
     * @return 用户id
     */
    protected String getAccountId(HttpServletRequest request){
        Claims claims = getClaims(request);
        if(claims != null){
            return claims.get("accountId", String.class);
        }
        return null;
    }

    /**
     * 获取用户登录名
     * @param request
     * @return
     */
    protected String getLoginName(HttpServletRequest request){
        Claims claims = getClaims(request);
        if(claims != null){
            return claims.get("loginName", String.class);
        }
        return null;
    }

    /**
     * 删除jwt注册信息
     * @param request
     */
    protected void unLogin(HttpServletRequest request){
        Authorization authorization = (Authorization) request.getAttribute("Authorization");
        if(authorization != null){
            boolean authorizationState = authorization.getState();
            if(authorizationState){
                String registerKey = authorization.getRegisterKey();
                String authedToken = authorization.getAuthedToken();
                if(StringUtils.isNotBlank(registerKey) && StringUtils.isNotBlank(authedToken) && Filter1_Jwt.isExists(registerKey, authedToken)) {
                    Filter1_Jwt.unRegister(registerKey);
                }
            }
        }
    }

}