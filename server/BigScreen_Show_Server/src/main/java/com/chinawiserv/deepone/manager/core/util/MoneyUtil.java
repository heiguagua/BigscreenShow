package com.chinawiserv.deepone.manager.core.util;

/**
 * 人民币转大写工具类
 * <pre>
 * 本类提供将双精度类型数据转化为人民币的静态方法，主要方法有
 * 1、将双精度类型数据转化为人民币 numberToRMBStr()
 * </pre>
 * @author zengpzh
 * @version 0.1
 */
public class MoneyUtil {

	/**
	 * 将双精度类型数据转化为人民币
	 * @param waitConvertNumber 双精度类型待数据
	 * @param requirePrefix 需要加上前缀 ￥，为1时，需要加，否则不加
	 * @return 转化后的人民币
	 * @author Allen Zhang
	 */
	public static String numberToRMBStr(double waitConvertNumber, String... requirePrefix) {
		String signStr = "" ;
		String tailStr = "" ;
		long fraction = 0, integer = 0;
		int jiao = 0, fen = 0;
		if( waitConvertNumber< 0 ) {
			waitConvertNumber = -waitConvertNumber;
			signStr = "负";
		}
		if(waitConvertNumber > 99999999999999.999 || waitConvertNumber <-99999999999999.999 ) {
			return "数值位数过大!";
		}
		/**
		 *  四舍五入到分
		 */  
		long temp = Math.round(waitConvertNumber*100); 
		integer = temp/100;
		fraction = temp%100;
		jiao = (int)fraction/10;
		fen = (int)fraction%10;
		if( jiao==0 && fen==0 ) {
			tailStr = "整";
		}
		else {
			tailStr = HanDigiStr[jiao];
			if( jiao!=0 ) {
				tailStr += "角";
			}
			/**
			 * 零元后不写零几分s
			 */
			if( integer==0 && jiao==0 ) {
				tailStr = "";
			}
			if( fen!=0 ) {
				tailStr += HanDigiStr[fen] + "分";
			}
		}
		
		/**
		 * 判断是否需要加 
		 */
		boolean requirePrefix_bol = false;
		if (requirePrefix != null) {
			if (requirePrefix.length > 0 && "1".equals(requirePrefix[0])) {
				requirePrefix_bol = true;
			}
		}
		if (requirePrefix_bol) {
			return "￥"+signStr+positiveIntegerToHanStr(String.valueOf(integer) )+"元"+tailStr;
		}
		else {
			return signStr+positiveIntegerToHanStr(String.valueOf(integer) )+"元"+tailStr;
		}
	}
 
	private static String positiveIntegerToHanStr(String numberStr) {
		String rMBStr = "";
		if (numberStr == null || "".equals(numberStr)) {
			return rMBStr;
		}
		boolean lastzero = false;
		/**
		 * 亿、万进位前有数值标记
		 */
		boolean hasvalue= false;

		int numberLen = 0, tmp_Char = 0;
		numberLen = numberStr.length();
		if(numberLen > 15) {
			return "数值过大!";
		}
		for(int i = numberLen - 1; i>=0; i--) {
			if( numberStr.charAt(numberLen-i-1)==' ' ) {
				continue;      
			}
			tmp_Char = numberStr.charAt(numberLen-i-1) - '0';
			if( tmp_Char<0 || tmp_Char>9 ) {
				return "输入含非数字字符!";
			}
			if( tmp_Char!=0 ) {
				/**
				 * 若干零后若跟非零值，只显示一个零
				 */
				if( lastzero ) rMBStr += HanDigiStr[0];
				/**
				 * 除了亿万前的零不带到后面，
				 * 如十进位前有零也不发壹音用，
				 * 十进位处于第一位不发壹音
				 */
				if( !( tmp_Char==1 && (i%4)==1 && i==numberLen-1 )) {
					rMBStr += HanDigiStr[tmp_Char];
				}
				/**
				 * 非零值后加进位，个位为空
				 */
				rMBStr += HanDiviStr[i];
				/**
				 *  置万进位前有值标记
				 */
				hasvalue = true;
			} 
			else {
				/**
				 * 亿万之间必须有非零值方显示万
				 */
				if( (i%8)==0 || ((i%8)==4 && hasvalue) ) {
					/**
					 * “亿”或“万”
					 */
					rMBStr += HanDiviStr[i];
				}
			}
			/**
			 * 万进位前有值标记逢亿复位
			 */
			if( i%8==0 ) hasvalue = false ;
			lastzero = (tmp_Char==0) && (i%4!=0);    
		}
		/**
		 *  输入空字符或"0"，返回"零"
		 */
		if( rMBStr.length()==0 ) {
			return HanDigiStr[0]; 
		}
		return rMBStr;
	}
	
	private static String HanDigiStr[] = new String[]{"零","壹","贰","叁","肆","伍","陆","柒","捌","玖"};

	private static String HanDiviStr[] = new String[]{"","拾","佰","仟","万","拾","佰","仟","亿",
                                                     "拾","佰","仟","万","拾","佰","仟","亿",
                                                     "拾","佰","仟","万","拾","佰","仟" };
}