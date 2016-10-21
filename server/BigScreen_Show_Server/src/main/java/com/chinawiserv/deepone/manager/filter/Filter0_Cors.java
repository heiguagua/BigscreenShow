package com.chinawiserv.deepone.manager.filter;

import com.chinawiserv.core.common.util.PropertiesReader;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import javax.servlet.annotation.WebFilter;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * 跨域过滤器
 * Created by zengpzh on 2016/6/17.
 */
@WebFilter(urlPatterns = "/*")
public final class Filter0_Cors extends CorsFilter {

    //跨域配置文件地址
    private static final String  CORS_CONFIG = "cors.properties";

    //跨域映射地址key
    private static final String  MAPPING_URL_KEY = "Mapping.Url";

    //默认映射地址
    private static final String  DEFAULT_MAPPING_URL = "/**";

    //默认允许跨域访问服务器
    private static final String  DEFAULT_ORIGIN = "*";

    //分隔符
    private static final String  SPLIT = ",";

    public Filter0_Cors() {
        super(getCorsConfigurationSource());
    }

    /**
     * 设置跨域配置
     * @return 跨域配置源
     */
    private static CorsConfigurationSource getCorsConfigurationSource(){
        Properties properties = PropertiesReader.getPropertiesByName(CORS_CONFIG);
        final UrlBasedCorsConfigurationSource urlConfig = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(getList(properties, HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN));
        config.setAllowedHeaders(getList(properties, HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS));
        config.setAllowedMethods(getList(properties, HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS));
        config.setExposedHeaders(getList(properties, HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS));
        config.setAllowCredentials(getBoolean(properties, HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS));
        config.setMaxAge(getLong(properties, HttpHeaders.ACCESS_CONTROL_MAX_AGE));

        urlConfig.registerCorsConfiguration(properties.getProperty(MAPPING_URL_KEY, DEFAULT_MAPPING_URL), config);
        return urlConfig;
    }

    /**
     * 获取指定key的值集
     * @param properties 配置文件
     * @param key  指定key
     * @return 值集合
     */
    private static List<String> getList(Properties properties, String key){
        return getList(properties, key, SPLIT);
    }

    /**
     * 获取指定key的值集
     * @param properties 配置文件
     * @param key 指定key
     * @param split 分隔符
     * @return 值集合
     */
    private static List<String> getList(Properties properties, String key, String split){
        if(properties != null && key != null){
            if(split == null) split = SPLIT;
            return Arrays.asList(properties.getProperty(key, DEFAULT_ORIGIN).split(split));
        }else return Arrays.asList(CrossOrigin.DEFAULT_ORIGINS);
    }

    /**
     * 获取布尔值
     * @param properties 配置文件
     * @param key 指定key
     * @return 布尔值
     */
    private static Boolean getBoolean(Properties properties, String key){
        if(properties != null && key != null){
            try{
                return Boolean.parseBoolean(properties.getProperty(key, "" + CrossOrigin.DEFAULT_ALLOW_CREDENTIALS));
            }catch (Exception e){}
        }
        return CrossOrigin.DEFAULT_ALLOW_CREDENTIALS;
    }

    /**
     * 获取Long值
     * @param properties 配置文件
     * @param key 指定key
     * @return Long值
     */
    private static Long getLong(Properties properties, String key){
        if(properties != null && key != null){
            try{
                return Long.parseLong(properties.getProperty(key, "" + CrossOrigin.DEFAULT_MAX_AGE));
            }catch (Exception e){}
        }
        return CrossOrigin.DEFAULT_MAX_AGE;
    }

}
