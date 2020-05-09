package com.hz.fine.master.core.common.utils;

import org.apache.commons.lang.StringUtils;
import sun.misc.BASE64Decoder;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>公共方法类</p>
 * <p>提供字符串处理的实用方法集</p>
 */
public class StringUtil {
	public StringUtil() {
	}

	public static final String escapeForIntro(String string) {
		String str = string;
		str = replace(str, "\r\n", "<br>");
		str = replace(str, "\n", "<br>");
		str = replace(str, "'", "\\'");
		return replace(str, "\r", "");

	}

	/**
	 * 得到非空的字符串，若字符串对象为null，则返回""。
	 * @param objValue Object待转换的原字符串对象
	 * @return String 转换后的字符串
	 * */
	public static String getNotNullStr(Object objValue) {
		return (objValue == null ? "" : objValue.toString());
	}

	/**
	 * 得到非空的字符串，若字符串为null，则返回""。
	 * @param strValue String待转换的原字符串
	 * @return String 转换后的字符串
	 * */
	public static String getNotNullStr(String strValue) {
		return (strValue == null ? "" : strValue.trim());
	}
	/**
	 * 用"0"补足一个字符串到指定长度
	 * @param str -  源字符串
	 * @param size - 补足后应达到的长度
	 * @return - 补零后的结果
	 */
	public static String fillZero(String str, int size) {
		String result;
		if (str.length() < size) {
			char[] s = new char[size - str.length()];
			for (int i = 0; i < (size - str.length()); i++) {
				s[i] = '0';
			}
			result = new String(s) + str;
		} else {
			result = str;
		}
		return result;
	}

	/**
	 * 根据字符串（文件类型或者多行输入类型）获取字符串数组
	 * @param str1 String 输入字符串
	 * @return String[] 返回结果
	 */
	public static String[] getStrArryByString(String str1) {
		if (str1.indexOf("\t") > 0) {
			for (int i = 0; i < str1.length(); i++) {
				if (str1.substring(i, i + 1).equals("\t")) {
					str1 = str1.substring(0, i) + " " + str1.substring(i + 1, str1.length());
				}
			}
		}
		StringTokenizer stringTokenizer = new StringTokenizer(str1, "\r\n");
		String[] strId = new String[stringTokenizer.countTokens()];
		int i = 0;
		while (stringTokenizer.hasMoreTokens()) {
			strId[i] = stringTokenizer.nextToken();
			i++;
		}
		return strId;
	}

	/**
	 * 判断一个字符串是否为 NUll 或为空
	 * @param inStr inStr
	 * @return boolean
	 */
	public static boolean isValid(String inStr) {
		if (inStr == null) {
			return false;
		} else if (inStr.equals("")) {
			return false;
		} else if (inStr.equals("null")) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 判断一个字符串是否不为 NUll 或为空
	 * @param inStr inStr
	 * @return boolean
	 */
	public static boolean checkNotNull(String str) {
		boolean flag = false;

		if (str != null && str.trim().length() != 0)
			flag = true;
		return flag;
	}

	/**
	 * 获得指定长度的空格
	 * @param spaceNum spaceNum
	 * @return String
	 */
	public static String getStrSpace(int spaceNum) {
		return getStrWithSameElement(spaceNum, " ");
	}

	/**
	 * 获得指定长度的字符串
	 * @param num int
	 * @param element String
	 * @return String
	 */
	public static String getStrWithSameElement(int num, String element) {
		if (num <= 0) {
			return "";
		}

		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < num; i++) {
			sb.append(element);
		}
		return sb.toString();
	}
	/**
	 * 获得指定长度的字符串
	 * @param num int
	 * @param element String
	 * @return String
	 */
	public static String getStrWithSameElement( String element,int num) { 
		if(num != 0 && element!=null && element.length()>num){ 
			return element.substring(0, num);
		}else{
			return ""; 
		} 
	}
	/**
	 * 从右或左加空格
	 * @param strIn String
	 * @param totalLength int
	 * @param isRightAlign boolean
	 * @return String
	 */
	public static String getFillString(String strIn, int totalLength, boolean isRightAlign) {
		int spaceLength = 0;
		String spaces = null;
		String strOut = null;

		if (strIn == null) {
			strIn = "";
		}

		spaceLength = totalLength - strIn.length();

		if (spaceLength < 0) {
			spaceLength = 0;
		}
		spaces = StringUtil.getStrSpace(spaceLength);

		if (isRightAlign) {
			strOut = spaces + strIn;
		} else {
			strOut = strIn + spaces;

		}
		return strOut;
	}

	/**
	 * 以String类型返回错误抛出的堆栈信息
	 * @param t Throwable
	 * @return String
	 */
	public static String getStackTrace(Throwable t) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);

		t.printStackTrace(pw);
		return sw.toString();
	}

	/**
	 * 转换字符串第一个字符为大写
	 * @param str String
	 * @return String
	 */
	public static String getStrByUpperFirstChar(String str) {
		try {
			return str.substring(0, 1).toUpperCase() + str.substring(1);
		} catch (Exception e) {
			return "";
		}

	}

	/**
	 * 转换字符串第一个字符为小写
	 * @param str String
	 * @return String
	 */
	public static String getStrByLowerFirstChar(String str) {
		try {
			return str.substring(0, 1).toLowerCase() + str.substring(1);
		} catch (Exception e) {
			return "";
		}

	}

	/**
	 * 通过字符串转换成相应的整型，并返回。
	 * @param strValue String 待转换的字符串
	 * @return int 转换完成的整型
	 * */
	public static int getStrToInt(String strValue) {
		if (null == strValue) {
			return 0;
		}
		int iValue = 0;
		try {
			iValue = new Integer(strValue.trim()).intValue();
		} catch (Exception ex) {
			iValue = 0;
		}
		return iValue;
	}

	/**
	 * 通过字符串转换成相应的DOUBLE，并返回。
	 * @param strValue String 待转换的字符串
	 * @return double 转换完成的DOUBLE
	 * */
	public static double getStrToDouble(String strValue) {
		if (null == strValue) {
			return 0;
		}
		double dValue = 0;
		try {
			dValue = Double.parseDouble(strValue.trim());
		} catch (Exception ex) {
			dValue = 0;
		}
		return dValue;
	}

	/**
	 * 通过字符串转换成相应的短整型，并返回。
	 * @param strValue String 待转换的字符串
	 * @return short 转换完成的短整型
	 * */
	public static short getStrToShort(String strValue) {
		if (null == strValue) {
			return 0;
		}
		short iValue = 0;
		try {
			iValue = new Short(strValue.trim()).shortValue();
		} catch (Exception ex) {
			iValue = 0;
		}
		return iValue;
	}

	/**
	 * 通过字符串转换成相应的长整型，并返回。
	 * @param strValue String 待转换的字符串
	 * @return long 转换完成的长整型
	 * */
	public static long getStrToLong(String strValue) {
		if (null == strValue) {
			return 0;
		}
		long lValue = 0;
		try {
			lValue = new Long(strValue.trim()).longValue();
		} catch (Exception ex) {
			lValue = 0;
		}
		return lValue;
	}

	public static String toLengthForEn(String str, int length) {
		if (null != str) {
			if (str.length() <= length) {
				return str;
			} else {
				str = str.substring(0, length - 2);
				str = str + "..";
				return str;
			}
		} else {
			return "";
		}
	}

	/**
	  * 降字符串转换成给定长度的字符串，如超出的话截断，并在最后以两个点结尾
	  * @param str String
	  * @param length int
	  * @return String
	  */
	public static String toLengthForIntroduce(String str, int length) {
		str = delTag(str);

		byte[] strByte = str.getBytes();
		int byteLength = strByte.length;
		char[] charArray;
		StringBuffer buff = new StringBuffer();
		if (byteLength > (length * 2)) {
			charArray = str.toCharArray();
			int resultLength = 0;
			for (int i = 0; i < charArray.length; i++) {
				resultLength += String.valueOf(charArray[i]).getBytes().length;
				if (resultLength > (length * 2)) {
					break;
				}
				buff.append(charArray[i]);

			}
			buff.append("..");
			str = buff.toString();
		}

		//		str = replace(str, "'", "\\'");
		str = replace(str, "\"", "\\\"");
		str = replace(str, "，", ",");
		return str;

	}

	public static String delTag(String str) {
		str = str + "<>";
		StringBuffer buff = new StringBuffer();
		int start = 0;
		int end = 0;

		while (str.length() > 0) {
			start = str.indexOf("<");
			end = str.indexOf(">");
			if (start > 0) {
				buff.append(str.substring(0, start));
			}
			if (end > 0 && end <= str.length()) {
				str = str.substring(end + 1, str.length());
			} else {
				str = "";
			}

		}
		String result = buff.toString();

		while (result.startsWith(" ")) {

			result = result.substring(result.indexOf(" ") + 1, result.length());

		}
		return result;

	}

	public static final String replace(String line, String oldString, String newString) {
		if (line == null) {
			return null;
		}
		int i = 0;
		if ((i = line.indexOf(oldString, i)) >= 0) {
			char[] line2 = line.toCharArray();
			char[] newString2 = newString.toCharArray();
			int oLength = oldString.length();
			StringBuffer buf = new StringBuffer(line2.length);
			buf.append(line2, 0, i).append(newString2);
			i += oLength;
			int j = i;
			while ((i = line.indexOf(oldString, i)) > 0) {
				buf.append(line2, j, i - j).append(newString2);
				i += oLength;
				j = i;
			}
			buf.append(line2, j, line2.length - j);
			return buf.toString();
		}
		return line;

	}

	//	Replace
	public static String Replace(String source, String oldString, String newString) {
		if (source == null) {
			return null;
		}
		StringBuffer output = new StringBuffer();
		int lengOfsource = source.length();
		int lengOfold = oldString.length();
		int posStart = 0;
		int pos;
		while ((pos = source.indexOf(oldString, posStart)) >= 0) {
			output.append(source.substring(posStart, pos));
			output.append(newString);
			posStart = pos + lengOfold;
		}
		if (posStart < lengOfsource) {
			output.append(source.substring(posStart));
		}
		return output.toString();
	}

	//此函数前台使用中，请勿随便修改，不然会造成显示混乱(以前修改版本在下方注释中)
	public static String toHtml(String s) {
		s = Replace(s, "<", "&lt;");
		s = Replace(s, ">", "&gt;");
		s = Replace(s, "\t", "    ");
		s = Replace(s, "\r\n", "\n");
		s = Replace(s, "\n", "<br>");
		//s = Replace(s, " ", "&nbsp;");
		s = Replace(s, "'", "&#39;");
		s = Replace(s, "\"", "&quot;");
		s = Replace(s, "\\", "&#92;");
		s = Replace(s, "%", "％");
		//	s = Replace(s, "&", "&amp;");
		return s;
	}

	//	逆
	public static String unHtml(String s) {

		//s = Replace(s, "&lt;", "<");
		//s = Replace(s, "&gt;", ">");
		//		s = Replace(s, "    ", "\t");
		//		s = Replace(s, "\n", "\r\n");
		s = Replace(s, "<br>", "\n");
		//		s = Replace(s, "&nbsp;", " ");
		//		s = Replace(s, "&amp;", "&");
		//		s = Replace(s, "&#39;", "'");
		//		s = Replace(s, "&#92;", "\\");
		//		s = Replace(s, "％", "%");
		return s;
	}

	//	此函数后台使用中，请勿随便修改，不然会造成显示混乱(以前修改版本在下方注释中)
	public static String toHtmlBack(String s) {
		//显示
		s = Replace(s, "&", "&amp;");
		s = Replace(s, "\\", "&#92;");
		s = Replace(s, "\"", "&quot;");
		s = Replace(s, "<", "&lt;");
		s = Replace(s, ">", "&gt;");
		s = Replace(s, "\t", "    ");
		s = Replace(s, "\r\n", "\n");
		//		s = Replace(s, "\n", "<br>");
		//		s = Replace(s, " ", "&nbsp;");
		//		s = Replace(s, "'", "&#39;");
		//		s = Replace(s, "%", "%");

		return s;
	}

	//	逆
	public static String unHtmlBack(String s) {
		s = Replace(s, "&lt;", "<");
		s = Replace(s, "&gt;", ">");
		s = Replace(s, "    ", "\t");
		s = Replace(s, "\n", "\r\n");
		s = Replace(s, "<br>", "\n");
		s = Replace(s, "&nbsp;", " ");
		s = Replace(s, "&amp;", "&");
		s = Replace(s, "&#39;", "'");
		s = Replace(s, "&#92;", "\\");
		s = Replace(s, "％", "%");
		return s;
	}

	//判断是否含有中文，如果含有中文返回ture
	public static boolean containsChinese(String str) throws UnsupportedEncodingException {

		if (str.length() < (str.getBytes()).length)
			return true;

		return false;

	}

	public static boolean isEmpty(String str) {
		if (str == null)
			return true;
		return "".equals(str.trim());
	}

	public static String[] split(String str1, String str2) {
		return StringUtils.split(str1, str2);
	}

	/**
	 *
	 * <br>
	 * <b>功能：</b>将字符串转成list<br>
	 * <b>日期：</b> Oct 28, 2013 <br>
	 * @param exp 分割符 如","
	 * @param value
	 * @return
	 */
	public static List<String> StringToList(String value, String exp) {
		List<String> resultList = new ArrayList<String>();
		String[] vals = split(value, exp);
		for (String val : vals) {
			resultList.add(val);
		}
		return resultList;
	}

	/**
	 *
	 * <br>
	 * <b>功能：</b>数字转换成字符串<br>
	 * <b>日期：</b> Jul 30, 2013 <br>
	 * @param arrs
	 * @return
	 */
	public static String arrayToString(String[] arrs) {
		StringBuffer result = new StringBuffer("");
		if (arrs != null && arrs.length > 0) {
			for (int i = 0; i < arrs.length; i++) {

				if (!result.toString().equals("")) {
					result.append(",");
				}
				if (arrs[i] != null && !"".equals(arrs[i].trim())) {
					result.append(arrs[i]);
				}
			}
		}
		return result.toString();
	}

	/**
	 *
	 * <br>
	 * <b>功能：</b>数字转换成字符串<br>
	 * <b>日期：</b> Jul 30, 2013 <br>
	 * @param arrs
	 * @return
	 */
	public static String arrayToString(Object[] arrs) {
		StringBuffer result = new StringBuffer("");
		if (arrs != null && arrs.length > 0) {
			for (int i = 0; i < arrs.length; i++) {

				if (!result.toString().equals("")) {
					result.append(",");
				}
				if (arrs[i] != null && !"".equals(arrs[i].toString().trim())) {
					result.append(arrs[i]);
				}
			}
		}
		return result.toString();
	}

	public static String left(String str, int length) {
		return StringUtils.left(str, length);
	}

	/**
	 *
	 * <br>
	 * <b>功能：</b>替换回车<br>
	 * <b>日期：</b> Oct 26, 2013 <br>
	 * @param str
	 * @return
	 */
	public static String replaceHuiche(String str) {
		String after = str.replaceAll("\r\n", "");
		return after;
	}
	/**
	 *
	 * <br>
	 * <b>功能：</b>替换"-"," "<br>
	 * <b>日期：</b> Oct 26, 2013 <br>
	 * @param str
	 * @return
	 */
	public static String replaceZiFu(String str) {
		String after = str.replaceAll("-", "").replaceAll(" ", "").replaceAll(":", "");
		return after;
	}
	/**
	 * 根据输入的长度截取字符串，单个单词超过输入长度的强制加<BR>换行
	 * @param str 输入的字符串
	 * @param len 输入的长度
	 * @return 处理过后的字符串
	 */
	public static String truncateStr(String str, int len) {
		if (str != null && !("").equalsIgnoreCase(str)) {

			String strs[] = str.split(" ");
			StringBuffer buff = new StringBuffer();
			if (strs.length > 0) {
				for (int i = 0; i < strs.length; i++) {
					StringBuffer temp = new StringBuffer();
					while (strs[i].length() > len) {
						temp.append(strs[i].substring(0, len) + "<BR>");
						strs[i] = strs[i].substring(len);
					}
					temp.append(strs[i]);
					buff.append(temp.toString() + " ");
				}

			}
			return buff.toString();
		} else {
			return "";
		}
	}

	public static String truncateStr2(String str, int len) {
		if (str != null && !("").equalsIgnoreCase(str) && len != 0) {
			String strs[] = str.split(" ");

			StringBuffer buff = new StringBuffer();
			for (int i = 0; i < strs.length; i++) {
				StringBuffer temp = new StringBuffer();
				String tempstr = "";
				while (strs[i].length() > len) {
					tempstr = strs[i].substring(0, len);
					tempstr = tempstr.replaceAll(" ", "&nbsp; ");
					tempstr = tempstr.replaceAll("<", "&lt; ");
					tempstr = tempstr.replaceAll("\n", "<br> ").replaceAll("\"", "&quot; ").replaceAll("'", "&#39; ");
					tempstr = tempstr + "<br>";
					temp.append(tempstr);

					strs[i] = strs[i].substring(len);
				}
				tempstr = strs[i];
				tempstr = tempstr.replaceAll(" ", "&nbsp; ");
				tempstr = tempstr.replaceAll("<", "&lt; ");
				tempstr = tempstr.replaceAll("\n", "<br> ").replaceAll("\"", "&quot; ").replaceAll("'", "&#39; ");

				temp.append(tempstr);
				buff.append(temp.toString() + " ");
			}

			if (buff.length() > 0)
				return buff.toString().substring(0, buff.length() - 1);
			else
				return str;
		} else {
			return "";
		}
	}

	/**
	 * 编码转换，从unicode转换为GBK
	 * @param str 输入字符串
	 * @return str编码转换后的字符串
	 * @throws UnsupportedEncodingException
	 */
	public static String unicodeToGB(String l_S_Source) throws UnsupportedEncodingException {
		String l_S_Desc = "";
		if (l_S_Source != null && !l_S_Source.trim().equals("")) {
			byte l_b_Proc[] = l_S_Source.getBytes("GBK");
			l_S_Desc = new String(l_b_Proc, "ISO8859_1");
		}
		return l_S_Desc;
	}

	/**
	 * 编码转换，从GBK转换为unicode
	 * @param l_S_Source 输入字符串
	 * @return str 编码转换后的字符串
	 * @throws UnsupportedEncodingException
	 */
	public static String GBToUnicode(String l_S_Source) throws UnsupportedEncodingException {
		String l_S_Desc = "";
		if (l_S_Source != null && !l_S_Source.trim().equals("")) {
			byte l_b_Proc[] = l_S_Source.getBytes("ISO8859_1");
			l_S_Desc = new String(l_b_Proc, "GBK");
		}
		return l_S_Desc;
	}

	/**
	 * Escapes a <code>String</code> according the JavaScript string literal
	 * escaping rules. The resulting string will not be quoted.
	 *
	 * <p>It escapes both <tt>'</tt> and <tt>"</tt>.
	 * In additional it escapes <tt>></tt> as <tt>\></tt> (to avoid
	 * <tt>&lt;/script></tt>). Furthermore, all characters under UCS code point
	 * 0x20, that has no dedicated escape sequence in JavaScript language, will
	 * be replaced with hexadecimal escape (<tt>\x<i>XX</i></tt>).
	 */
	public static String javaScriptStringEnc(String s) {
		int ln = s.length();
		for (int i = 0; i < ln; i++) {
			char c = s.charAt(i);
			if (c == '"' || c == '\'' || c == '\\' || c == '>' || c < 0x20) {
				StringBuffer b = new StringBuffer(ln + 4);
				b.append(s.substring(0, i));
				while (true) {
					if (c == '"') {
						b.append("\\\"");
					} else if (c == '\'') {
						b.append("\\'");
					} else if (c == '\\') {
						b.append("\\\\");
					} else if (c == '>') {
						b.append("\\>");
					} else if (c < 0x20) {
						if (c == '\n') {
							b.append("\\n");
						} else if (c == '\r') {
							b.append("\\r");
						} else if (c == '\f') {
							b.append("\\f");
						} else if (c == '\b') {
							b.append("\\b");
						} else if (c == '\t') {
							b.append("\\t");
						} else {
							b.append("\\x");
							int x = c / 0x10;
							b.append((char) (x < 0xA ? x + '0' : x - 0xA + 'A'));
							x = c & 0xF;
							b.append((char) (x < 0xA ? x + '0' : x - 0xA + 'A'));
						}
					} else {
						b.append(c);
					}
					i++;
					if (i >= ln) {
						return b.toString();
					}
					c = s.charAt(i);
				}
			} // if has to be escaped
		} // for each characters
		return s;
	}

	private static StringUtil instance = null;

	public static synchronized StringUtil getInstance() {
		if (instance == null) {
			instance = new StringUtil();
		}
		return instance;
	}

	/**
	 * 将多个连续空格替换为一个,"a  b"-->"a b"
	 * @param src
	 * @return
	 * @author WilliamLau
	 * @desc
	 */
	public static String trimContinuousSpace(String src) {
		return src.replaceAll("\\s+", " ");
	}

	public static String replace(String src, String target, String rWith, int maxCount) {
		return StringUtils.replace(src, target, rWith, maxCount);
	}

	public static boolean equals(String str1, String str2) {
		return StringUtils.equals(str1, str2);
	}

	public static boolean isAlphanumeric(String str) {
		return StringUtils.isAlphanumeric(str);
	}

	public static boolean isNumeric(String str) {
		return StringUtils.isNumeric(str);
	}

	public static String[] stripAll(String[] strs) {
		return StringUtils.stripAll(strs);
	}

	public static String toFloatNumber(String num) {
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(2);
		nf.setMinimumFractionDigits(2);
		return nf.format(Double.parseDouble(num));
	}

	public static String toFloatNumber(Double num, int accuracy) {
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(accuracy);
		nf.setMinimumFractionDigits(accuracy);
		return nf.format(num);
	}

	public static String inputStreamToString(InputStream is) {

		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String line = null;
		try {

			while ((line = reader.readLine()) != null) {

				sb.append(line + "/n");
			}
		} catch (IOException e) {

			e.printStackTrace();
		}
		return sb.toString();
	}

	public static String wcsUnescape(String str) {
		str = str.replace("#lt;", "<");
		str = str.replace("#gt;", ">");
		str = str.replace("#quot;", "\"");
		str = str.replace("#amp;amp;", "&");
		str = str.replace("#amp;", "&");
		str = str.replace("#039;", "'");
		return str;
	}

	/**
	 * 
	 * <br>
	 * <b>功能：</b>返回string型的字节数<br>
	 * <b>日期：</b> Sep 2, 2013 <br>
	 * @param str
	 * @return
	 */
	public static int getByteLength(String str) {
		if (str == null) {
			return 0;
		}
		return str.getBytes().length;
	}

	/**
	 * 
	 * <br>
	 * <b>功能：</b>详细的功能描述<br>
	 * <b>日期：</b> Sep 2, 2013 <br>
	 * @param str 字符
	 * @param limitLen 长度
	 * @return
	 */
	public static String getByteStr(String str, int limitLen) {
		StringBuffer sb = new StringBuffer();
		char[] chars = getNotNullStr(str).toCharArray();
		int len = 0;
		for (char c : chars) {
			len += getByteLength(String.valueOf(c));
			if (len <= limitLen) {
				sb.append(c);
			}
		}
		return sb.toString();

	}

	/**
	*@param  content 内容
	*@param  length 指定长度。 超出这个长度就截取字符串。
	*@param  padding 超出长度后，尾加上字符，如"..."，可以为空
	*@return 返回结果 如果内容没有超出指定的长度。则返回原字符串，超出长度后则截取到指定的长度的内容
	*/
	public static String subStr(String content, Integer length, String padding) throws UnsupportedEncodingException {
		String str = "";
		int paddSize = StringUtils.isBlank(padding) ? 0 : padding.length();
		//如果内容为空，或者小于指定的长度。则返回原内容
		if (StringUtils.isBlank(content) || content.length() <= length) {
			return content;
		}
		str = content.substring(0, length - paddSize);
		if (StringUtils.isNotBlank(padding)) {
			str += padding;
		}
		return str;
	}

	/**
	 * 获取UUID随机码
	 * @return
	 */
	public static String generationUUID() {
		UUID uuid = UUID.randomUUID();
		return uuid.toString().substring(0, 7);
	}
	
	/***
     * 电话号码格式化 获取11位
     * @param pn
     * @return
     */
    public static String getRegularUserPn(String pn){
 	   String phonenumber  =  "";
 	   if(pn!=null && pn.length()==11){
 		  phonenumber = pn;
 	   }else{
 		   if(pn!=null && pn.length()>11){
 			  String rpn=pn.substring(pn.length()-11, pn.length());
 	 		  phonenumber=rpn;
 		   } 
 	   }
 	   return phonenumber;
    }
    
    public static boolean regexIP(String ip){
    	boolean boo = false;
    	if(ip!=null && !ip.equals("")){
    		String regex = "(2[5][0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})\\.(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})\\.(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})\\.(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})";
        	boo = ip.matches(regex);
    	} 
    	return boo;
    }
    
//    public static String getGidRandom() throws Exception{
//  	   String num=DateUtil.getNowPlusTimeMill();
//  	   String randomNum = System.currentTimeMillis()+"";
//  	   return num+randomNum;
//     }

	public static String getGidRandom() throws Exception{
		String num = DateUtil.getNowPlusTimeMill();
		int a[] = new int[10];
		String randomNum = "";
		for(int i=0;i<a.length;i++ ) {
			a[i] = (int)(10*(Math.random()));
			randomNum += a[i];
		}
		return num+randomNum;
	}
    
    public static boolean isNullValue(String value) {
		return value == null || value.trim().equals("");
	}

	public static final String leftAppendStr(String input, int num, char chr) {
		int size = num - input.length();
		char[] c = new char[size];
		for (int i = 0; i < size; i++) {
			c[i] = chr;
		}
		return String.valueOf(c).concat(input);
	}
 
	public static final String leftAppendStr(String input, int num) {
		return leftAppendStr(input, num, '0');
	}

	 
	public static String filterUnNumber(String str) {
		if (StringUtil.isNullValue(str))
			return "";
		String regEx = "[^0-9]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		return m.replaceAll("").trim();
	}

	 
	public static boolean isDigit(String str) {
		String regEx = "(\\d+?)";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		return m.matches();
	}
	
	public static boolean isMobileNumber(String number){
		if(StringUtil.isNullValue(number))
			return false;
		if(number.length() > 11)
			number = number.substring(number.length() - 11);
		Pattern p = Pattern.compile("^((13[0-9])|(14[0-9])|(15[0-9])|(18[0-9]))\\d{8}$");
		Matcher m = p.matcher(number);
		return m.matches();
	}


	public static String findFromString(String str, String regEx, String groupIndex) {
		return findFromString(str, regEx, groupIndex, false);
	}

	public static String findIgnoreCaseFromString(String str, String regEx) {
		return findFromString(str, regEx, "1", true);
	}

	public static String findIgnoreCaseFromString(String str, String regEx, String groupIndex) {
		return findFromString(str, regEx, groupIndex, true);
	}

	public static String findFromString(String str, String regEx, String groupIndex, boolean ignoreCase) {
		if (StringUtil.isNullValue(str))
			return null;
		if (StringUtil.isNullValue(regEx))
			return null;
		Pattern p = null;
		if (ignoreCase)
			p = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
		else
			p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		m = p.matcher(str);
		if (m.find()) {
			return m.group(Integer.parseInt(groupIndex));
		} else
			return null;
	}
	
	public static final String getParameterValue(String argsList, String key){
		if(StringUtil.isNullValue(argsList))
			return "";
		if(StringUtil.isNullValue(key))
			return "";
		
		String reg = String.format("%s=(.*?)", new Object[]{key});
		String value = StringUtil.findFromString(argsList, reg.concat("&"), "1");
		if(value != null)
			return value;
		value = StringUtil.findFromString(argsList, "&".concat(reg).concat("&"), "1");
		if(value != null)
			return value;
		value = StringUtil.findFromString(argsList, "&".concat(reg).concat("$"), "1");
		return value == null ? "" : value;
	}
	
	
	public static final String escapeXml(String str){
		if(StringUtil.isNullValue(str))
			return "";
		str = str.replaceAll("&", "&amp;");
		str = str.replaceAll("<", "&lt;");
		str = str.replaceAll(">", "&gt;");
		str = str.replaceAll("'", "&apos;");
		str = str.replaceAll("\"", "&quot;");
		return str;
	}
	
	public static final String unescapeXml(String str){
		if(StringUtil.isNullValue(str))
			return "";
		str = str.replaceAll("&amp;", "&");
		str = str.replaceAll("&lt;", "<");
		str = str.replaceAll("&gt;", ">");
		str = str.replaceAll("&apos;", "'");
		str = str.replaceAll("&quot;", "\"");
		return str;
	}
	
	public final static boolean isValidMobileNumber(String number){
		if(StringUtil.isNullValue(number))
			return false;
		if(number.endsWith("00000000") || number.endsWith("13800138000"))
			return false;
		return StringUtil.isMobileNumber(number);
	}
	
	public final static String filterNullValue(String str){
		return str == null ? "" : str.trim();
	}
	
	/**
	 * url编码
	 * @param url
	 * @return
	 */
	public final static String urlEncode(String url) {
		if (isNullValue(url)) {
			return "";
		}

		try {
			return URLEncoder.encode(url, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return "";
		}
	}
	
	public static String getXMLValue(String srcXML, String element) {
		String begElement = "<" + element + ">";
		String endElement = "</" + element + ">";
		int begPos = srcXML.indexOf(begElement);
		int endPos = srcXML.indexOf(endElement);
		if (begPos != -1 && endPos != -1) {
			begPos += begElement.length();
			return srcXML.substring(begPos, endPos);
		} else {
			return "";
		}
	}
	
	private static String hexString = "0123456789ABCDEF";

	/*
	 * 将字符串编码成16进制数字,适用于所有字符（包括中文）
	 */
	public static String encode(String str) {
		// 根据默认编码获取字节数组
		byte[] bytes = str.getBytes();
		StringBuilder sb = new StringBuilder(bytes.length * 2);
		// 将字节数组中每个字节拆解成2位16进制整数
		for (int i = 0; i < bytes.length; i++) {
			sb.append(hexString.charAt((bytes[i] & 0xf0) >> 4));
			sb.append(hexString.charAt((bytes[i] & 0x0f) >> 0));
		}
		return sb.toString();
	}

	/*
	 * 将16进制数字解码成字符串,适用于所有字符（包括中文）
	 */
	public static String decode(String bytes) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream(bytes.length() / 2);
		// 将每2位16进制整数组装成一个字节
		for (int i = 0; i < bytes.length(); i += 2)
			baos.write((hexString.indexOf(bytes.charAt(i)) << 4 | hexString.indexOf(bytes.charAt(i + 1))));
		return new String(baos.toByteArray());
	}

	/**
	 * base64加密
	 * 超过76字符不会换行的加密方法
	 * @param str
	 * @return
	 */
	public final static String mergeCodeBase64(String str) throws UnsupportedEncodingException {
		org.apache.commons.codec.binary.Base64 bas=new org.apache.commons.codec.binary.Base64();
		byte[] resByte=bas.encode(str.getBytes("UTF-8"));
		String res=new String(resByte);
		return res;
	}


	/**
	 * base64解密
	 * @param str
	 * @return
	 */
	public final static String decoderBase64(String str) throws IOException {
		BASE64Decoder decoder = new BASE64Decoder();
		byte[] bytes = decoder.decodeBuffer(str);
		String res=new String(bytes);
		return res;
	}

	/**
	 * 获取用户的ip地址
	 * @param request
	 * @return
	 */
	public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip != null && ip.trim().length() > 0) {
			String[] ips = ip.trim().split(",");
			int size = ips.length;
			if (size > 0) {
				ip = ips[0].trim();
			}
		}
		if (isInvalidIp(ip)) {
			ip = request.getHeader("X-Real-IP");
		}
		if (isInvalidIp(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (isInvalidIp(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (isInvalidIp(ip)) {
			ip = request.getHeader("Cdn-Src-Ip");
		}
		if (isInvalidIp(ip)) {
			ip = request.getRemoteAddr();
		}
		if (ip != null && ip.startsWith("0:0:0:0")) {
			ip = "127.0.0.1";
		}
		return ip;
	}
	private final static String UNKNOWN = "unknown";
	private static boolean isInvalidIp(String ip){
		return ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip);
	}

//	/**
//	 * @Description: TODO(获取IP地址)
//	 * @author df
//	 * @param request
//	 * @create 16:34 2018/11/2
//	 **/
//	public static String getIpAddress(HttpServletRequest request){
//		String ip = request.getHeader("X-Forwarded-For");
//		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//			ip = request.getHeader("Proxy-Client-IP");
//		}
//		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//			ip = request.getHeader("WL-Proxy-Client-IP");
//		}
//		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//			ip = request.getHeader("HTTP_CLIENT_IP");
//		}
//		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
//		}
//		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//			ip = request.getRemoteAddr();
//		}
//		return ip;
//	}


	/**
	 * @Description: TODO(获取IP地址)
	 * @author df
	 * @param request
	 * @create 16:34 2018/11/2
	 **/
	public static String getIpAddress(HttpServletRequest request){
		String Xip = request.getHeader("X-Real-IP");
		String XFor = request.getHeader("X-Forwarded-For");
		if(StringUtils.isNotEmpty(XFor) && !"unKnown".equalsIgnoreCase(XFor)){
			//多次反向代理后会有多个ip值，第一个ip才是真实ip
			int index = XFor.indexOf(",");
			if(index != -1){
				return XFor.substring(0,index);
			}else{
				return XFor;
			}
		}
		XFor = Xip;
		if(StringUtils.isNotEmpty(XFor) && !"unKnown".equalsIgnoreCase(XFor)){
			return XFor;
		}
		if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
			XFor = request.getHeader("Proxy-Client-IP");
		}
		if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
			XFor = request.getHeader("WL-Proxy-Client-IP");
		}
		if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
			XFor = request.getHeader("HTTP_CLIENT_IP");
		}
		if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
			XFor = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
			XFor = request.getRemoteAddr();
		}
		return XFor;
	}

	public static String getPostBodyData(HttpServletRequest request){
		String body = "";
		StringBuilder stringBuilder = new StringBuilder();
		BufferedReader bufferedReader = null;
		InputStream inputStream = null;
		try {
			inputStream = request.getInputStream();
			if (inputStream != null) {
				bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
				char[] charBuffer = new char[128];
				int bytesRead = -1;
				while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
					stringBuilder.append(charBuffer, 0, bytesRead);
				}
			} else {
				stringBuilder.append("");
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		body = stringBuilder.toString();
		return body;
	}

	/**
	 * @Description: 随机生成6位随机数（字母+数字）
	 * @return String
	 * @author yoko
	 * @date 2019/11/12 22:58
	 */
	public static String randomCode(){
		String randomcode = "";
		// 用字符数组的方式随机
		String model = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		char[] m = model.toCharArray();
		for (int j = 0; j < 6; j++) {
			char c = m[(int) (Math.random() * 36)];
			// 保证六位随机数之间没有重复的
			if (randomcode.contains(String.valueOf(c))) {
				j--;
				continue;
			}
			randomcode = randomcode + c;
		}
		return randomcode;
	}

	/**
	 * @Description: 计算涨幅
	 * <p>返回的数据是百分比的</p>
	 * @param t - 今日价格
	* @param y - 昨日价格
	 * @return java.lang.String
	 * @author yoko
	 * @date 2019/11/22 21:20
	 */
	public static String getGrowthRate(String t, String y){
		String z = "100";
		BigDecimal resDoble;
		BigDecimal tt = new BigDecimal(t);
		BigDecimal yy = new BigDecimal(y);
		BigDecimal zz = new BigDecimal(z);
		BigDecimal jf_res = tt.subtract(yy);
		resDoble = jf_res.divide(yy, 4, BigDecimal.ROUND_HALF_UP).multiply(zz);
		DecimalFormat sb = new DecimalFormat("###.##");
		String data = sb.format(resDoble);
		return data;
	}

    /**
     * @Description: 计算两数相乘
     * @param x
    *@param y
     * @return java.lang.String
     * @author yoko
     * @date 2019/11/28 22:24
     */
	public static String getMultiply(String x, String y){
        BigDecimal resDoble;
        BigDecimal xx = new BigDecimal(x);
        BigDecimal yy = new BigDecimal(y);
        resDoble = xx.multiply(yy);
        DecimalFormat sb = new DecimalFormat("###.##");
        String str = sb.format(resDoble);
        return str;
    }

    /**
     * @Description: 计算两数相加
     * @param x
    *@param y
     * @return java.lang.String
     * @author yoko
     * @date 2019/11/28 22:24
     */
    public static String getBigDecimalAdd(String x, String y){
        BigDecimal resDoble;
        BigDecimal xx = new BigDecimal(x);
        BigDecimal yy = new BigDecimal(y);
        resDoble = xx.add(yy);
        DecimalFormat sb = new DecimalFormat("###.##");
        String str = sb.format(resDoble);
        return str;
    }

	/**
	 * @Description: 计算两数相减
	 * <p>值相减，如果不为负数，则返回true；反之则返回fasle</p>
	 * @param x
	*@param y
	 * @return java.lang.String
	 * @author yoko
	 * @date 2019/11/28 22:24
	 */
	public static boolean getBigDecimalSubtract(String x, String y){
		BigDecimal resDoble;
		BigDecimal xx = new BigDecimal(x);
		BigDecimal yy = new BigDecimal(y);
		resDoble = xx.subtract(yy);
		if (resDoble.signum() < 0){
			return false;
		}else {
			return true;
		}
	}

	/**
	 * @Description: 计算两数相除
	 * @param x
	*@param y
	 * @return java.lang.String
	 * @author yoko
	 * @date 2019/11/28 22:24
	 */
	public static String getBigDecimalDivide(String x, String y){
		BigDecimal resDoble;
		BigDecimal xx = new BigDecimal(x);
		BigDecimal yy = new BigDecimal(y);
		resDoble = xx.divide(yy);
		DecimalFormat sb = new DecimalFormat("###.##");
		String str = sb.format(resDoble);
		return str;
	}



	/**
	 * @Description: 计算两数相加
	 * @param x
	*@param y
	 * @return java.lang.String
	 * @author yoko
	 * @date 2019/11/28 22:24
	 */
	public static BigDecimal getBigDecimalAdd(BigDecimal x, BigDecimal y){
		BigDecimal resDoble;
		resDoble = x.add(y);
		DecimalFormat sb = new DecimalFormat("###.##");
		String str = sb.format(resDoble);
		BigDecimal res = new BigDecimal(str);
		return res;
	}


	/**
	 * @Description: 计算两数相减
	 * <p>值相减，如果不为负数，则返回true；反之则返回fasle</p>
	 * @param x
	*@param y
	 * @return java.lang.String
	 * @author yoko
	 * @date 2019/11/28 22:24
	 */
	public static BigDecimal getBigDecimalSubtract(BigDecimal x, BigDecimal y){
		BigDecimal resDoble;
		resDoble = x.subtract(y);
		DecimalFormat sb = new DecimalFormat("###.##");
		String str = sb.format(resDoble);
		BigDecimal res = new BigDecimal(str);
		return res;
	}

	/**
	 * @Description: 计算两数相乘
	 * @param x
	*@param y
	 * @return java.lang.String
	 * @author yoko
	 * @date 2019/11/28 22:24
	 */
	public static BigDecimal getMultiply(BigDecimal x, BigDecimal y){
		BigDecimal resDoble;
		resDoble = x.multiply(y);
		DecimalFormat sb = new DecimalFormat("###.##");
		String str = sb.format(resDoble);
		BigDecimal res = new BigDecimal(str);
		return res;
	}


	/**
	 * @Description: 计算两数相除
	 * @param x
	*@param y
	 * @return java.lang.String
	 * @author yoko
	 * @date 2019/11/28 22:24
	 */
	public static BigDecimal getBigDecimalDivide(BigDecimal x, BigDecimal y){
		BigDecimal resDoble;
		resDoble = x.divide(y,  2, BigDecimal.ROUND_HALF_UP);
//		DecimalFormat sb = new DecimalFormat("###.##");
//		String str = sb.format(resDoble);
//		BigDecimal res = new BigDecimal(str);
		return resDoble;
	}

	public static void main(String [] args){
		BigDecimal x = new BigDecimal(5.243);
		BigDecimal y = new BigDecimal(2.244);
		BigDecimal add = getBigDecimalAdd(x, y);// 相加
		BigDecimal subtract = getBigDecimalSubtract(x, y);// 减
		BigDecimal multiply = getMultiply(x, y);// 乘
		BigDecimal divide = getBigDecimalDivide(x, y);// 除
		System.out.println("add:" + add);
		System.out.println("subtract:" + subtract);
		System.out.println("multiply:" + multiply);
		System.out.println("divide:" + divide);
	}

}
