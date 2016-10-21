package com.chinawiserv.deepone.manager.filter;

import com.chinawiserv.core.common.util.PropertiesReader;
import com.chinawiserv.deepone.manager.core.controller.BaseController;
import com.chinawiserv.deepone.manager.core.exception.MsgException;
import com.chinawiserv.deepone.manager.model.common.Authorization;
import com.chinawiserv.deepone.manager.model.common.JwtInfo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Jwt过滤器,实现认证拦截
 * Created by zengpzh on 2016/6/17.
 */
@WebFilter(urlPatterns = "/*")
public class Filter1_Jwt implements Filter {

    //jwt配置文件路径
    private static final String JWT_CONFIG = "jwt.properties";

    //jwt配置文件对象
    private static final Properties jwtProperties;

    //token过期时间key
    private static final String TOKEN_INVALID_MINUTE_KEY = "token.invalid.minute";

    //默认token过期时间
    private static final String DEFAULT_TOKEN_INVALID_MINUTE = "10";

    //token过期时间
    private static final int TOKEN_INVALID_MINUTE;

    //token过期检查间隔时间key
    private static final String TOKENS_CHECK_INTERVAL_MINUTE_KEY = "tokens.check.interval.minute";

    //默认token过期检查间隔时间
    private static final String DEFAULT_TOKENS_CHECK_INTERVAL_MINUTE = "10";

    //token过期检查间隔时间
    private static final int TOKENS_CHECK_INTERVAL_MINUTE;

    //操作间隔时间限制key
    private static final String HANDLE_INTERVAL_LIMIT_TIME_KEY = "handle.interval.limit.time";

    //默认操作间隔时间限制
    private static final String DEFAULT_HANDLE_INTERVAL_LIMIT_TIME = "500";

    //操作间隔时间限制
    private static final long HANDLE_INTERVAL_LIMIT_TIME;

    //token开始检查延迟时间
    private static final int TOKENS_CACHE_CHECK_DELAY_MILL_SEC = 10 * 1000;

    /**
     * 初始化参数
     */
    static {
        jwtProperties = PropertiesReader.getPropertiesByName(JWT_CONFIG);
        TOKEN_INVALID_MINUTE = Integer.parseInt(jwtProperties.getProperty(TOKEN_INVALID_MINUTE_KEY, DEFAULT_TOKEN_INVALID_MINUTE));
        TOKENS_CHECK_INTERVAL_MINUTE = Integer.parseInt(jwtProperties.getProperty(TOKENS_CHECK_INTERVAL_MINUTE_KEY, DEFAULT_TOKENS_CHECK_INTERVAL_MINUTE));
        HANDLE_INTERVAL_LIMIT_TIME = Long.parseLong(jwtProperties.getProperty(HANDLE_INTERVAL_LIMIT_TIME_KEY, DEFAULT_HANDLE_INTERVAL_LIMIT_TIME));
    }

    //认证token加密密钥
    public static final String AUTH_TOKEN_SECURITY_KEY = "wokzhen";

    //已认证token缓存
    private static final Map<String, JwtInfo> authedTokensCache = new ConcurrentHashMap<String, JwtInfo>();

    //已认证token缓存读写锁
    private static final ReadWriteLock authedTokensCacheLock = new ReentrantReadWriteLock();

    //计时器
    private final Timer timer = new Timer();

    /**
     * 过滤器初始化
     * @param filterConfig 过滤器配置项
     * @throws ServletException 初始化异常
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        //计算token缓存检查时间
        final long tokenCacheCheckTime = TOKENS_CHECK_INTERVAL_MINUTE * 60 * 1000;
        //启动计时器
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                //如果缓存不为空，准备检查
                if(!authedTokensCache.isEmpty()) {
                    //取出缓存中所有的已认证token
                    Set<String> keys = authedTokensCache.keySet();
                    //遍历
                    for (String registerKey : keys) {
                        //如果token过期,清除token
                        if (isExpired(registerKey)) {
                            unRegister(registerKey);
                        }
                    }
                }
            }
        }, TOKENS_CACHE_CHECK_DELAY_MILL_SEC, tokenCacheCheckTime);
    }

    /**
     * 过滤器
     * @param servletRequest http请求
     * @param servletResponse http响应
     * @param filterChain 过滤器链
     * @throws java.io.IOException IO异常
     * @throws ServletException servlet异常
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        final HttpServletRequest request = (HttpServletRequest) servletRequest;
        //取出请求头中的认证信息
        final String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            final Authorization authorization = new Authorization();
            boolean authorizationState = true;
            String authorizationMsg = null;
            Claims claims = null;
            boolean needLogin = true;
            //找到认证token
            final String authedToken = authHeader.substring(7);// The Part after "Bearer"
            try {
                String registerKey;
                try {
                    //解密token,检查token正确性
                    claims = getClaims(authedToken);
                    //获取注册key
                    registerKey = getRegisterKey(claims);
                } catch (Exception e) {
                    throw new Exception("无效的Token！");
                }
                //检查签证地址是否为当前客户端
                if (isClientMatch(request, claims.getIssuer())) {
                    //检查是否注册过
                    if (isExists(registerKey, authedToken)) {
                        //检查是否过期
                        if (isExpired(registerKey)) {
                            unRegister(registerKey);
                            throw new Exception("token已过期，请重新登录！");
                        } else {
                            //没有过期，重新设置过期时间
                            register(registerKey, new JwtInfo(authedToken, claims.get("loginName", String.class), claims.getIssuer(), request));
                            authorization.setRegisterKey(registerKey);
                            authorization.setAuthedToken(authedToken);
                        }
                    } else throw new Exception("无效的Token！");
                }else throw new Exception("不匹配的Token！");
            } catch (MsgException e) {
                authorizationMsg = e.getMessage();
                authorizationState = false;
                needLogin = false;
            } catch (Exception e1) {
                authorizationMsg = e1.getMessage();
                authorizationState = false;
                needLogin = true;
            }
            //生成认证对象并放入http请求中
            authorization.setState(authorizationState);
            authorization.setMsg(authorizationMsg);
            authorization.setClaims(claims);
            authorization.setNeedLogin(needLogin);
            request.setAttribute("Authorization", authorization);
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    /**
     * 过滤器销毁
     */
    @Override
    public void destroy() {
        authedTokensCache.clear();
        timer.cancel();
    }

    /**
     * 获取已注册的jwt信息
     * @registerKey 注册key
     * @return jwt信息
     */
    public static JwtInfo getRegisteredJwtInfo(String registerKey){
        JwtInfo jwtInfo = null;
        authedTokensCacheLock.readLock().lock();
        if(StringUtils.isNotBlank(registerKey)) jwtInfo = authedTokensCache.get(registerKey);
        authedTokensCacheLock.readLock().unlock();
        return jwtInfo;
    }

    /**
     * 注册jwt
     * @param registerKey 注册key
     * @param jwtInfo jwt信息
     * @throws Exception 异常
     */
    public static void register(String registerKey, JwtInfo jwtInfo) throws Exception{
        if (registerKey != null && jwtInfo != null && registerKey.equals(jwtInfo.toString())) {
            //设置过期时间
            Calendar now = Calendar.getInstance();
            now.add(Calendar.MINUTE, TOKEN_INVALID_MINUTE);
            Date newExpDate = now.getTime();
            JwtInfo oldJwtInfo = getRegisteredJwtInfo(registerKey);
            String newLatestRequest = jwtInfo.getLatestRequest();
            //这次请求与上次请求相同时需要检查访问频率
            if(oldJwtInfo != null) {
                String oldLatestRequest = oldJwtInfo.getLatestRequest();
                //判断上次请求与这次请求是否相同
                if(oldLatestRequest != null && oldLatestRequest.equals(newLatestRequest)) {
                    Date oldExpDate = oldJwtInfo.getExpDate();
                    if (oldExpDate != null && newExpDate.getTime() - oldExpDate.getTime() <= HANDLE_INTERVAL_LIMIT_TIME) {
                        throw new MsgException("操作失败，你的访问频率太高！");
                    }
                }
            }
            jwtInfo.setLatestRequest(newLatestRequest);
            jwtInfo.setExpDate(newExpDate);
            //注册token
            authedTokensCacheLock.writeLock().lock();
            authedTokensCache.put(registerKey, jwtInfo);
            authedTokensCacheLock.writeLock().unlock();
        }else throw new Exception("操作失败，jwt信息不正确！");
    }

    /**
     * 获取注册key
     * @param claims token内容体
     * @return 注册key
     * @throws MsgException
     */
    public static String getRegisterKey(Map<String, Object> claims) throws Exception {
        if(claims == null) throw new Exception("不完整的token内容体！");
        String loginName = (String) claims.get("loginName");
        String issuer = (String) claims.get(Claims.ISSUER);
        if(StringUtils.isBlank(loginName) || StringUtils.isBlank(issuer)) throw new Exception("不完整的token内容体！");
        return loginName + " - " + issuer;
    }

    /**
     * 注销jwt
     * @param registerKey 注册key
     */
    public static void unRegister(String registerKey) {
        if (registerKey != null) {
            authedTokensCacheLock.writeLock().lock();
            authedTokensCache.remove(registerKey);
            authedTokensCacheLock.writeLock().unlock();
        }
    }

    /**
     * 检查jwt是否过期
     * @param registerKey 注册key
     * @return true:过期  false:未过期
     */
    public static boolean isExpired(String registerKey) {
        boolean result = registerKey == null;
        authedTokensCacheLock.readLock().lock();
        if(!result){
            //取出token携带的jwt信息
            JwtInfo jwtInfo = authedTokensCache.get(registerKey);
            //检查jwt信息中的过期时间
            result |= jwtInfo != null && jwtInfo.getExpDate() != null && jwtInfo.getExpDate().before(new Date());
        }
        authedTokensCacheLock.readLock().unlock();
        return result;
    }

    /**
     * 是否已注册
     * @param registerKey 注册key
     * @return true:已注册  false:未注册
     */
    public static boolean isExists(String registerKey, String authedToken) {
        authedTokensCacheLock.readLock().lock();
        boolean result = false;
        if(StringUtils.isNotBlank(registerKey) && StringUtils.isNotBlank(authedToken) && authedTokensCache.containsKey(registerKey)) {
            JwtInfo jwtInfo = authedTokensCache.get(registerKey);
            result |= jwtInfo != null && authedToken.equals(jwtInfo.getToken());
        }
        authedTokensCacheLock.readLock().unlock();
        return result;
    }

    /**
     * 是否签证地址是否与客户端匹配
     * @param request request请求
     * @param issuer 检查的签证地址
     * @return true: 匹配  false: 不匹配
     */
    private static boolean isClientMatch(HttpServletRequest request, String issuer){
        return BaseController.getIpAddr(request).equals(issuer);
    }

    /**
     * 解密认证token的内容体
     * @param authedToken 被解密的token
     * @return 解密后的内容体
     */
    private static Claims getClaims(String authedToken) {
        return Jwts.parser().setSigningKey(AUTH_TOKEN_SECURITY_KEY).parseClaimsJws(authedToken).getBody();
    }

}
