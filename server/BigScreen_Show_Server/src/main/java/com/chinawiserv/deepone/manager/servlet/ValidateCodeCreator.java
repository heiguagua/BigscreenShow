package com.chinawiserv.deepone.manager.servlet;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * 随机图片验证码生成器
 * <pre>
 * 本类提供随机图片验证码生成器，调用ValidateCodeCreator()方法
 * 将生成随机图片与验证码
 * 随机图片：将被输出到response.getOutputStream()
 * 验证码:将被保存到 Session 里默认名称为：validateCode
 * </pre>
 * @author zengpzh
 * @version 0.1
 */
public class ValidateCodeCreator {

    /**
     * 验证码 validateCode 信息相关的 Key
     */
    public static final String VALIDATECODE = "validateCode";

    /**
     * 验证码 validateCode 过期时间 Key
     */
    public static final String VALIDATECODE_EXP_DATE = "validateCodeExpDate";
   
	/**
	 * 随机生成的验证码
	 */
    private String validateCode;
    
    private static final String randomSrc = "01234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"; 
	
	/**
	 * 验证码的位数
	 */
	private final int validateCodeLen = 4;
	
	/**
	 * 内存图象的 width
	 */
	private final int bufferedImageWidth = 96;
	
	/**
	 * 内存图象的 height
	 */
	private final int bufferedImageHeight = 37;

	/**
	 * 产生验证码与验证码图象
	 * @return 验证码图象
	 * @author Allen Zhang
	 */
    public BufferedImage creatImageAndCode() {
    	/**
    	 * 初始化验证码
    	 */
    	validateCode = "";
        
        /**
         * 在内存中创建图象
         */
        BufferedImage validateImage = new BufferedImage(bufferedImageWidth, 
        		                                        bufferedImageHeight, 
        		                                        BufferedImage.TYPE_INT_RGB);
        
        /**
         * 获取图形上下文
         */
        Graphics myGraphic = validateImage.getGraphics();
        
        /**
         * 生成随机类
         */
        Random random = new Random();
        
        /**
         * 设定背景色
         */
        myGraphic.setColor(new Color(255, 255, 255));  
        
        myGraphic.fillRect(0, 0, bufferedImageWidth, bufferedImageHeight);
        
        /**
         * 设定字体
         */
        myGraphic.setFont(new Font("Times New Roman", Font.ITALIC, 20));
        
        /**
         * 随机产生干扰线
         */
        myGraphic.setColor(getRandColor(190, 220)); 
        
        for (int i = 0; i < 37; i++) {
            int x = random.nextInt(bufferedImageWidth);    
            int y = random.nextInt(bufferedImageHeight);    
            int xl = random.nextInt(bufferedImageWidth);    
            int yl = random.nextInt(bufferedImageHeight);    
            myGraphic.drawLine(x, y, x + xl, y + yl);    
        }
        
        /**
         * 取随机产生的认证码
         */
        for (int i = 0; i < validateCodeLen; i++) {
        	int index = random.nextInt(randomSrc.length());
            String rand = String.valueOf(randomSrc.charAt(index));    
            validateCode += rand;
            /**
             * 将认证码显示到图象中
             */
            myGraphic.setColor(getRandColor(0, 190));    
            myGraphic.drawString(rand, 23 * i + 9, 25);
        }
        
        /**
         * 图象生效
         */
        myGraphic.dispose();
        
        return validateImage;    
    }

    /**
     * 给定范围获得随机颜色
     * @param fc
     * @param bc
     * @return 随机颜色
     */
    private Color getRandColor(int fc, int bc) {
        Random random = new Random();    
        if (fc > 255) {
            fc = 255;    
        }
        if (bc > 255) {
            bc = 255;    
        }
        int r = fc + random.nextInt(bc - fc);    
        int g = fc + random.nextInt(bc - fc);    
        int b = fc + random.nextInt(bc - fc);    
        return new Color(r, g, b);    
    }

	public String getValidateCode() {
		return validateCode;
	}
}