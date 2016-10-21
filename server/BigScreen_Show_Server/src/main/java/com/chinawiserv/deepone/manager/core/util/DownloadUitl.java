package com.chinawiserv.deepone.manager.core.util;

import com.chinawiserv.deepone.manager.core.exception.MsgException;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;

/**
 * 下载相关的模块工具类
 * <pre>
 * 本类提供静态的下载相关方法，主要方法有：
 * 1、保存为文件 exportToFile()
 * 2、将数据从输入流里拷贝到输出流里，拷贝完后可释放输入流决定于isFree的值 copyDataToOsFromIsAndClearIs()
 * 3、设置下载文件时的 response 的 Header setCustomHeader()
 * 4、映射 设置下载文件头的消息 mapContentType();
 * 5、从 InputStream 类型数据里 获取 byte[]类型数据 toByteArrayFromInputStream()
 * 6、将 ByteArrayOutputStream 转换成 InputStream toInputStream()
 * </pre>
 * @author zengpzh
 * @version 0.1
 */
public class DownloadUitl {
	
	public static int BUFFER_SIZE = 1024;
	
	/**
	 * 下载path文件
	 * @param path
	 * @throws Exception
	 * @author zf
	 */
	public static void downloadData(String path, HttpServletResponse response)throws Exception{
		BufferedInputStream in = null;
		BufferedOutputStream out = null;
		InputStream fin = null;
		OutputStream fout = null;
		try{
			File downloadFile = new File(path);
			fin = new FileInputStream(downloadFile);
			fout = response.getOutputStream();
			if(fout != null || !"".equals(fout)){
				in = new BufferedInputStream(fin);
				out = new BufferedOutputStream(fout);
				//这个就是弹出下载对话框的关键代码
				response.setHeader("Content-disposition",
		                "attachment;filename=" +
		                URLEncoder.encode(path, "utf-8"));
				byte[] buffer = new byte[BUFFER_SIZE];//创建字节大小
				//循环含义：当输入流读取字节大于0时，就一直输出字节-------也就是下载
				while(in.read(buffer) > 0){
					out.write(buffer);
				}
				response.flushBuffer();
			}
		}
		catch(Exception e){
			throw new MsgException("下载中途遇到小问题，但不影响您正常下载！");
		}finally{
			
			out.flush();
			fout.flush();
			in.close();
			out.close();
			fin.close();
			fout.close();
		}
	}
	
	/**
	 * 保存为文件
	 * @param fileSource byte[] 输入信息
	 * @param fileNameWithExt 带扩展名的文件名
	 * @param response 响应
	 * @return 错误信息，如果为""表示没有错误
	 * @author Allen Zhang
	 */
	public static String exportToFile(byte[] fileSource, 
			                          String fileNameWithExt, 
			                          HttpServletResponse response) throws Exception {
		String errorMsg = "";
		if (fileNameWithExt != null && (!"".equals(fileNameWithExt))) {
		    try  {
		    	setCustomHeader(fileNameWithExt, response);  
		    	InputStream is = new ByteArrayInputStream(fileSource);  
		    	copyDataToOsFromIsAndClearIs(is, response.getOutputStream(), true);
		    }
		    catch (IOException e)  {  
		    	errorMsg = e.getMessage();
		    	throw e;
		    } 		
		}
		return errorMsg;
	}

	/**
	 * 保存为文件
	 * @param fileSource ByteArrayOutputStream 输入信息
	 * @param fileNameWithExt 带扩展名的文件名
	 * @param response 响应
	 * @return 错误信息，如果为""表示没有错误
	 * @author Allen Zhang
	 */
	public static String exportToFile(ByteArrayOutputStream fileSource, 
			                          String fileNameWithExt, 
			                          HttpServletResponse response) throws Exception {
		String errorMsg = "";
		if (fileNameWithExt != null && (!"".equals(fileNameWithExt))) {
		    try  {
		    	setCustomHeader(fileNameWithExt, response);  
		    	copyDataToOsFromIsAndClearIs(toInputStream(fileSource), response.getOutputStream(), true);
		    }
		    catch (IOException e)  {  
		    	errorMsg = e.getMessage();
		    	throw e;
		    } 		
		}
		return errorMsg;
	}
	
	/**
	 * 保存为文件
	 * @param fileSource InputStream 流
	 * @param fileNameWithExt 带扩展名的文件名
	 * @param response 响应
	 * @return 错误信息，如果为""表示没有错误
	 * @author Allen Zhang
	 */
	public synchronized static String exportToFile(InputStream fileSource, 
												   String fileNameWithExt,
												   HttpServletResponse response) throws Exception {
		String errorMsg = "";
		try{
			setCustomHeader(fileNameWithExt, response);  
			copyDataToOsFromIsAndClearIs(fileSource, response.getOutputStream(), true);
		} catch(IOException e) {
			errorMsg = e.getMessage();
			throw e;
		}
		catch(Exception e) {
			errorMsg = e.getMessage();
			throw e;
		}		
		return errorMsg;
	}		

	/**
	 * 将数据从输入流里拷贝到输出流里，拷贝完后可释放输入流决定于isFree的值
	 * @param is 输入流
 	 * @param os 输出流
 	 * @param isFree 为true时，拷贝完后释放输入流,否则不释放
	 * @throws Exception
	 * @author Allen Zhang
	 */
	public synchronized static void copyDataToOsFromIsAndClearIs(InputStream is, 
			                                                     OutputStream os, 
			                                                     boolean isFree) throws IOException {
		if (is != null && os != null) {
			try {
				byte[] buffer = new byte[1024];    
				int tmpLen = is.read(buffer);    
				while(tmpLen > 0) {
					os.write(buffer);
					tmpLen = is.read(buffer); 
				}
				if (isFree) {
					is.close();
				}
			} catch (IOException e) {
				throw e;
			}
			catch (Exception e) {
			}
		}
	}
	
	/**
	 * 设置下载文件时的 response 的 Header 
	 * @param fileNameWithExt 带扩展名的文件名
	 * @param response Http 响应
	 * @author Allen Zhang
	 */
	public static void setCustomHeader(String fileNameWithExt, HttpServletResponse response) throws Exception {
		try {
		    StringBuffer sb = new StringBuffer(50);  
		    sb.append("attachment; filename=");  
		    sb.append(fileNameWithExt);  
	    	response.setContentType(mapContentType(fileNameWithExt));
	    	response.setHeader("Content-Disposition", new String(sb.toString().getBytes(),"ISO8859-1"));  
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 映射 设置下载文件头的消息 
	 * @param fileName 文件名称
	 * @return 映射信息
	 * @author Allen Zhang
	 */
	public static String mapContentType(String fileName) {
		String fileNameTmp = "";
		if (fileName != null) {
			fileNameTmp = fileName.toLowerCase();
		}
		String ret = "application/x-msdownload;charset=GB2312";
		if (fileNameTmp.endsWith("txt")) {
			ret = "text/plain;charset=GB2312";
		}
		else if (fileNameTmp.endsWith("gif")) {
			ret = "image/gif;charset=GB2312";
		}
		else if (fileNameTmp.endsWith("jpg")) {
			ret = "image/jpeg;charset=GB2312";
		}
		else if (fileNameTmp.endsWith("jpeg")) {
			ret = "image/jpeg;charset=GB2312";
		}
		else if (fileNameTmp.endsWith("jpe")) {
			ret = "image/jpeg;charset=GB2312";
		}
		else if (fileNameTmp.endsWith("zip")) {
			ret = "application/zip;charset=GB2312";
		}
		if (fileNameTmp.endsWith("rar")) {
			ret = "application/rar;charset=GB2312";
		}
		else if (fileNameTmp.endsWith("doc")) {
			ret = "application/msword";
		}
		else if (fileNameTmp.endsWith("ppt")) {
			ret = "application/vnd.ms-powerpoint;charset=GB2312";
		}
		else if (fileNameTmp.endsWith("xls")) {
			ret = "application/vnd.ms-excel;charset=GB2312";
		}
		else if (fileNameTmp.endsWith("html")) {
			ret = "text/html;charset=GB2312";
		}
		else if (fileNameTmp.endsWith("htm")) {
			ret = "text/html;charset=GB2312";
		}
		else if (fileNameTmp.endsWith("tif")) {
			ret = "image/tiff;charset=GB2312";
		}
		else if (fileNameTmp.endsWith("tiff")) {
			ret = "image/tiff;charset=GB2312";
		}
		else if (fileNameTmp.endsWith("pdf")) {
			ret = "application/pdf;charset=GB2312";
		}
		else if (fileNameTmp.endsWith("xml")) {
			ret =  "application/xml;charset=GB2312";
		}
		return ret;
	}	 
	
	/**
	 * 从 InputStream 类型数据里 获取 byte[]类型数据
	 * @param is InputStream 类型数
	 * @return 从 InputStream 类型数据里 获取的 byte[]类型数据
	 * @throws java.io.IOException
	 * @author Allen Zhang
	 */
	public static byte[] toByteArrayFromInputStream(InputStream is) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if (is != null) {
            byte buffer[] = new byte[BUFFER_SIZE];
            int byteRead;
            while ((byteRead = is.read(buffer)) > 0) {
                baos.write(buffer, 0, byteRead);
            }
        }
        return baos.toByteArray();
	}

	/**
	 * 将 ByteArrayOutputStream 转换成 InputStream
	 * @param baos ByteArrayOutputStream对象数据
	 * @return 转换成 的 InputStream
	 * @author Allen Zhang
	 */
	protected static InputStream toInputStream(ByteArrayOutputStream baos) {
        return new ByteArrayInputStream(baos.toByteArray());
    }		
}