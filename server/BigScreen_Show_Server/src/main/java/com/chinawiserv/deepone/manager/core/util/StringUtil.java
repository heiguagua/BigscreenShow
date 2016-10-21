package com.chinawiserv.deepone.manager.core.util;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * String工具类
 * @author zengpzh
 * @version 0.1
 */

public class StringUtil {
	
	/**
	 * 获取字符串字节数
	 * @param aString
	 * @return 字符串字节数
	 * @author zengpzh
	 */
	public static int getByteAmountFromString(String aString) {
		int byteLength = 0;
		if (aString != null && aString.length() > 0) {
			byte[] _bytes = aString.getBytes();
			byteLength = _bytes.length;
		}
		return byteLength;
	}
	
    /**
     * 格式化 请求参数
     * @param parameter
     * @return
     * @throws Exception
     * @author zengpzh
     */
    public static String formatParameter(String parameter) {
    	//清除空白字符，清除掉参数的首尾的空白字符，如果仅参数全由空白字符 (whitespace)组成则返回null
    	parameter = StringUtils.trimToNull(parameter);
    	// "" 或 "\n \n\t" 或 null
   		if (StringUtils.isBlank(parameter)) {
   			parameter = "";
   		}
   		else if ("null".equalsIgnoreCase(parameter) || "\"null\"".equalsIgnoreCase(parameter)  || "'null'".equalsIgnoreCase(parameter)) {
   			parameter = "";
   		}
   		else if ("undefined".equalsIgnoreCase(parameter) || "\"undefined\"".equalsIgnoreCase(parameter)  || "'undefined'".equalsIgnoreCase(parameter)) {
   			parameter = "";
   		}
    	return parameter;
    }
	
	/**
	 * Ajax 生成分页链接字符串
	 * @param recordTotal 总记录数
	 * @param recordTotalAPage 每页记录数
	 * @param pageIndex 第几页
	 * @param doSearchFunctionName 执行搜索的JS函数名称
	 * @return 分页链接字符串
	 * @author zengpzh
	 */
	public static String pageLinkString(int recordTotal, int recordTotalAPage, int pageIndex,
			                     String doSearchFunctionName) {
		String space= "&nbsp;&nbsp;";
		if (doSearchFunctionName == null) {
			doSearchFunctionName = "framework_doSearch";
		}
		else if ("".equals(doSearchFunctionName.trim())) {
			doSearchFunctionName = "framework_doSearch";
		}
		if (recordTotal > 0 && recordTotalAPage > 0 && pageIndex > 0) {
			int pages = recordTotal / recordTotalAPage;
			if ((recordTotal % recordTotalAPage) > 0) {
				pages++;
			}
			int prior = pageIndex - 1;
			if (prior <= 0) {
				prior = 1;
			}

			int next = pageIndex + 1;
			if (next >= pages) {
				next = pages;
			}
			if (pages > 1) {
				String reStr = createBoundForCutPage(pageIndex, 10, pages);
				int lowerLimit = Integer.valueOf(reStr.substring(0, reStr.indexOf("_")));
				int upperLimit = Integer.valueOf(reStr.substring(reStr.indexOf("_") + 1, reStr.length()));
				String pageHTML = "";
				for (int i = lowerLimit; i <= upperLimit ; i ++) {
					if (pageIndex != i) {
						pageHTML += "<a href='javascript:"+doSearchFunctionName+"("+i+");'>["+i+"]</a>" + "&nbsp;";
					}
					else {
						pageHTML += i + "&nbsp;&nbsp;";
					}
				}
				
				StringBuffer pageStr = new StringBuffer();
				pageStr.append("第&nbsp;"+pageIndex+"&nbsp;&nbsp;页&nbsp;/&nbsp;&nbsp;共&nbsp;"+pages+"&nbsp;&nbsp;页&nbsp;/&nbsp;&nbsp;共&nbsp;"+recordTotal+"&nbsp;&nbsp;条记录");
				pageStr.append(space);
				
				if (pageIndex > 1) {
					pageStr.append("<a href='javascript:void(0);' onclick='javascript:"+doSearchFunctionName+"(1);'>首页</a>");
				}
				else {
					pageStr.append("首页");
				}
				pageStr.append(space);
				
				
				if (pageIndex > 1) {
					pageStr.append("<a href='javascript:void(0);' onclick='javascript:"+doSearchFunctionName+"("+prior+");'>前页</a>");
				}
				else {
					pageStr.append("前页");
				}
				pageStr.append(space);
				
				if (!"".equals(pageHTML)) {
					pageStr.append(pageHTML);
					pageStr.append(space);
				}
				
				if (pageIndex < pages) {
					pageStr.append("<a href='javascript:void(0);' onclick='javascript:"+doSearchFunctionName+"("+next+");'>后页</a>");
				}
				else {
					pageStr.append("后页");
				}
				pageStr.append(space);
				
				if (pageIndex < pages) {
					pageStr.append("<a href='javascript:void(0);' onclick='javascript:"+doSearchFunctionName+"("+pages+");'>末页</a>");
				}
				else {
					pageStr.append("末页");
				}
				
				pageStr.append(space);
				
				String gotoPage = "跳转到，第 <input type='text' id='pageIndexForcreatePage' name='pageIndexForcreatePage' style='width:30px;height:14px;text-align:center;font-size:12px;' value='"+next+"' onkeypress='javascript:JSUtil.numberAllowed(event);' maxlength='10' /> 页 " +
						          " <input type='button' value='GO' class='gobutton' onclick='javascript:"+doSearchFunctionName+"(document.getElementById(\"pageIndexForcreatePage\").value);'/> ";
				pageStr.append(gotoPage);
				
				return pageStr.toString();
			}
			else {
				return "第 1 页 /"+space+"共 1 页 /"+space+"共 "+recordTotal+" 条记录";
			}
		}
		else {
			return "第 1 页 /"+space+"共 1 页 /"+space+"共 "+recordTotal+" 条记录";
		}
	}
	
	/**
	 * 生成分页链接区间生成优化算法
	 * 使用例子：
	 *   String reStr = createBoundForCutPage(180, 50, 1000);
	 *   int lowerLimit = Integer.valueOf(reStr.substring(0, reStr.indexOf("_")));
	 *   int upperLimit = Integer.valueOf(reStr.substring(reStr.indexOf("_") + 1, reStr.length()));
	 * 
	 *   结果
	 *   lowerLimit = 155
	 *   upperLimit = 205
	 *   
	 * @param currentPage 当前第几页
	 * @param bound 区间里最大显示数目
	 * @param totalPage 总共页数
	 * @return 优化后显示区间
	 * @author zengpzh
	 */
	private static String createBoundForCutPage(int currentPage, int bound, int totalPage) {
		int lowerLimit = 0;
		int upperLimit = 0;
		int gap = 0;
		if (currentPage > 0 && bound > 0 && 
			totalPage > 0 && totalPage >= currentPage) {
			int boundHalf = bound / 2 + 1;
			if (currentPage < boundHalf) {
				lowerLimit = 1;
				if (totalPage > bound) {
					upperLimit = bound; 
				}
				else {
					upperLimit = totalPage;
				}
			}
			else if (currentPage > (totalPage - boundHalf)) {
				lowerLimit = totalPage - bound;
				if (lowerLimit <= 0) {
					lowerLimit = 1;
				}
				upperLimit = totalPage;
			}
			else {
				lowerLimit = currentPage - boundHalf;
				if (lowerLimit <= 0) {
					lowerLimit = 1;
				}
				upperLimit = currentPage + boundHalf;
				if (upperLimit >= totalPage) {
					upperLimit = totalPage;
				}
				gap = upperLimit - lowerLimit;
				if (gap > bound) {
					if (lowerLimit == 1) {
						upperLimit = upperLimit - (gap - bound); 
					}
					else if (upperLimit == totalPage) {
						lowerLimit = lowerLimit + (gap - bound);
					}
					else {
						lowerLimit = lowerLimit + ((gap - bound) / 2);
						upperLimit = upperLimit - ((gap - bound) / 2);
					}
				}
				else if (gap < bound) {
					if (lowerLimit == 1) {
						upperLimit = upperLimit + (gap - bound); 
					}
					else if (upperLimit == totalPage) {
						lowerLimit = lowerLimit - (gap - bound);
					}
					else {
						lowerLimit = lowerLimit - ((gap - bound) / 2);
						upperLimit = upperLimit + ((gap - bound) / 2);
					}
				}
			}
			while (upperLimit - lowerLimit >= bound) {
				lowerLimit++;
			}
		}
		return String.valueOf(lowerLimit) + "_" + String.valueOf(upperLimit);
	}
	
	/**
	 * 处理字符串中的空格
	 * @param aString
	 * @return
	 * @author zengpzh
	 */
	public static String handleBlank(String aString) {
		if (aString != null && !"".equals(aString.trim())) {
			while (aString.indexOf("  ") > -1) {
				aString = aString.replace("  ", " ").replace("	", " ");
			}
			return aString.trim();
		}
		else {
			return "";
		}
	}
	
	/**
	 * 切字符串，把源字符串切为指定长度的字符串。
	 * @param sourceStr 源字符串
	 * @param strLength 指定长度
	 * @return 切后字符串
	 * @author zengpzh
	 */
	public static String sliceString(String sourceStr, int strLength) {
		if (sourceStr != null) {
			if (strLength > 0 && sourceStr.length() > strLength) {
				return sourceStr.substring(0, strLength) + "...";
			}
			else {
				return sourceStr;
			}
		}
		else {
			return "";
		}
	}	

	/**
	 * 获取上下文基础地址
	 * @return 上下文基础地址
	 * @author zengpzh
	 */
	public static String getBasePath(HttpServletRequest request) {
		if (request != null) {
			StringBuffer buffer = new StringBuffer(); 
			buffer.append(request.getScheme()).append("://").append(request.getServerName()).append(":").append(request.getServerPort()).append(request.getContextPath()).append("/");
			return buffer.toString();
		}
		else {
			return "";
		}
	}
	
	/**
	 * 格式化字节数量
	 * @param numBytes
	 * @return 格式化后的字节数量
	 * @author zengpzh
	 */
	public static String toByteFormat(float numBytes) {
		String[] labels = {"bytes", "KB", "MB", "GB"};
		int labelIndex = 0;
		while (labelIndex < labels.length - 1 && numBytes > 1024) {
			numBytes /= 1024;
			labelIndex++;
		}
		return (Math.round(numBytes * 10) / 10f) + labels[labelIndex];
	}

}
