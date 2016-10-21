package com.chinawiserv.deepone.manager.core.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 浏览器工具
 * @author zengpzh
 *
 */
public class BrowseTool {

	private final static String IE9 = "MSIE 9.0";

	private final static String IE8 = "MSIE 8.0";

	private final static String IE7 = "MSIE 7.0";

	private final static String IE6 = "MSIE 6.0";

	private final static String MAXTHON = "Maxthon";

	private final static String QQ = "QQBrowser";

	private final static String GREEN = "GreenBrowser";

	private final static String SE360 = "360SE";

	private final static String FIREFOX = "Firefox";

	private final static String OPERA = "Opera";

	private final static String CHROME = "Chrome";

	private final static String SAFARI = "Safari";

	private final static String OTHER = "other";

	public static String checkBrowse(String userAgent) {
		if (regex(OPERA, userAgent)) {
			return OPERA;
		}
		else if (regex(CHROME, userAgent)){
			return CHROME;
		}
		else if (regex(FIREFOX, userAgent)){
			return FIREFOX;
		}
		else if (regex(SAFARI, userAgent)){
			return SAFARI;
		}
		else if (regex(SE360, userAgent)){
			return SE360;
		}
		else if (regex(GREEN, userAgent)){
			return GREEN;
		}
		else if (regex(QQ, userAgent)){
			return QQ;
		}
		else if (regex(MAXTHON, userAgent)){
			return MAXTHON;
		}
		else if (regex(IE9, userAgent)){
			return IE9;
		}
		else if (regex(IE8, userAgent)){
			return IE8;
		}
		else if (regex(IE7, userAgent)){
			return IE7;
		}
		else if (regex(IE6, userAgent)){
			return IE6;
		}
		else {
			return OTHER;
		}
	}

	private static boolean regex(String regex, String str) {
		Pattern p = Pattern.compile(regex, Pattern.MULTILINE);
		Matcher m = p.matcher(str);
		return m.find();
	}
}
