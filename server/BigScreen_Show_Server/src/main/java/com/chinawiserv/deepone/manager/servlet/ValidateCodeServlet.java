package com.chinawiserv.deepone.manager.servlet;

import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Date;

/**
 * 随机图片验证码调用的Servlet
 * <pre>
 * 在页面上只需要直接调用该Servlet即可
 * 页面上使用方法：
 * <img id="myValidateImage" src="<%=basePath%>validatecodeServlet"/>
 * 	配置格式：
 * 	<servlet>
 * 	    <servlet-name>ValidateCode</servlet-name>
 * 	    <servlet-class>com.chinawiserv.dcm.servlet.ValidateCodeServlet</servlet-class>
 * 	</servlet>
 * 	<servlet-mapping>
 * 	    <servlet-name>ValidateCode</servlet-name>
 * 	    <url-pattern>/validatecode</url-pattern>
 * 	</servlet-mapping>
 * </pre>
 * @author zengpzh
 * @version 0.1
 */
public class ValidateCodeServlet extends HttpServlet {    

	private static final long serialVersionUID = -5008620428872558929L;

    private static final Log _log = LogFactory.getLog(ValidateCodeServlet.class);

	/**
	 * 处理 get 请求
	 * @param request http 请求
	 * @param response http 响应
	 * @throws javax.servlet.ServletException
	 * @throws java.io.IOException
	 * @author zengpzh
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
                throws ServletException, IOException {    
		/**
		 * 页面属性设置
		 */
        response.setContentType("image/jpeg");    
        response.setHeader("Pragma", "No-cache");    
        response.setHeader("Cache-Control", "no-cache");    
        response.setDateHeader("Expires", 0);
        
        try {    
        	/**
        	 * 生成图象与验证码
        	 */
            ValidateCodeCreator validateCodeCreator = new ValidateCodeCreator();    
            BufferedImage img = validateCodeCreator.creatImageAndCode();

            request.getSession().setAttribute(ValidateCodeCreator.VALIDATECODE_EXP_DATE, DateUtils.addMinutes(new Date(), 1));
            request.getSession().setAttribute(ValidateCodeCreator.VALIDATECODE, validateCodeCreator.getValidateCode());
            _log.debug("验证码：" + request.getSession().getAttribute(ValidateCodeCreator.VALIDATECODE));
            ImageIO.write(img, "JPEG", response.getOutputStream());    
            response.getOutputStream().flush();    
            response.getOutputStream().close();    
            
        } catch (Exception e) {    
        }
    }    
}