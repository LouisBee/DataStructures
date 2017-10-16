package com.louis.bee.utils.number;

import java.util.regex.Pattern;

public class BigNumber {

	private static final Pattern PATTERN_NUMERIC = Pattern.compile("[0-9]*");
	private static final String STR_INVAID_PARAM = "invaid param";
	
	public static String add(String strA, String strB) {
		if (!isNumeric(strA) || !isNumeric(strB)) {
			return STR_INVAID_PARAM;
		}
		
		strA = removeZeroPrefix(strA);
		strB = removeZeroPrefix(strB);
		
		StringBuilder sb = new StringBuilder();
		int step = 0; // 进位的值，0或1
		for (int i = 0; i < strA.length() || i < strB.length(); i++) {
			int a = i < strA.length() ? numericCharToValue(strA.charAt(strA.length() - 1 - i)) : 0; // 超出范围时，用0代替
			int b = i < strB.length() ? numericCharToValue(strB.charAt(strB.length() - 1 - i)) : 0;
			int res = a + b + step;
			sb.append(valueToNumericChar(res % 10));
			step = res / 10;
		}
		
		if (step > 0) {
			sb.append(valueToNumericChar(step));
		}
		
		return sb.reverse().toString();
	}
	
	public static String subtract(String strA, String strB) {
		if (!isNumeric(strA) || !isNumeric(strB)) {
			return STR_INVAID_PARAM;
		}
		
		strA = removeZeroPrefix(strA);
		strB = removeZeroPrefix(strB);
		
		if (strA.equals(strB)) {
			return String.valueOf(0);
		}
		
		boolean isBigger = true; // 数A是否大于数B，A小于B时，值为负
		if (!isBigger(strA, strB)) {
			isBigger = false;
			String tmp = strA;
			strA = strB;
			strB = tmp;
		}
		
		StringBuilder sb = new StringBuilder();
		int step = 0;
		for (int i = 0; i < strA.length(); i++) {
			int a = numericCharToValue(strA.charAt(strA.length() - 1 - i));
			int b = i < strB.length() ? numericCharToValue(strB.charAt(strB.length() - 1 - i)) : 0; // 超出范围时，用0代替
			int res = a - step - b;
			sb.append(Math.abs((res + 10) % 10));
			step = res < 0 ? 1 : 0;
		}
		
		// 去掉前导0
		for (int i = sb.length() - 1; i > 0; i--) {
			if (sb.charAt(i) != '0') {
				break;
			}
			sb.deleteCharAt(i);
		}
		
		if (!isBigger) {
			sb.append('-');
		}
		
		return sb.reverse().toString();
	}
	
	public static String multiply(String strA, String strB) {
		if (!isNumeric(strA) || !isNumeric(strB)) {
			return STR_INVAID_PARAM;
		}
		
		strA = removeZeroPrefix(strA);
		strB = removeZeroPrefix(strB);
		
		if (isBigger(strB, strA)) {
			String tmp = strA;
			strA = strB;
			strB = tmp;
		}
		
		String res = "0";
		for (int i = 0; i < strB.length(); i++) {
			int b = numericCharToValue(strB.charAt(i));
			String c = multiplyByNumeric(strA, b);
			
			res += '0'; // 上一次的结果乘10
			res = add(res, c);
		}
		
		return res;
	}
	
	/**
	 * 计算大数与一个非副整数(小于10)的积
	 * @param strA
	 * @param b
	 * @return
	 */
	public static String multiplyByNumeric(String strA, int b) {
		if (!isNumeric(strA)) {
			return STR_INVAID_PARAM;
		} else if (b < 0 || b > 9) {
			return STR_INVAID_PARAM;
		}
		
		if (b == 0) {
			return "0";
		} else if (b == 1) {
			return strA;
		}
		
		StringBuilder sb = new StringBuilder();
		int step = 0;
		for (int i = 0; i < strA.length(); i++) {
			int a = numericCharToValue(strA.charAt(strA.length() - 1 - i));
			int res = a * b +step;
			sb.append(res % 10);
			step = res / 10;
		}
		
		if (step != 0) {
			sb.append(valueToNumericChar(step));
		}
		
		return sb.reverse().toString();
	}
	
	public static boolean isNumeric(String str) {
		if (str == null || str.isEmpty()) {
			return false;
		}
		
		return PATTERN_NUMERIC.matcher(str).matches();
	}
	
	/**
	 * 去掉前导‘0’，如果src的字符全是‘0’，则返回‘0’
	 * @param src
	 * @return
	 */
	public static String removeZeroPrefix(String src) {
		if (src == null || src.isEmpty()) {
			return src;
		}
		
		// 寻找第一个非0下标
		int idxNoZero = 0;
		for (; idxNoZero < src.length(); idxNoZero++) {
			if (src.charAt(idxNoZero) != '0') {
				break;
			}
		}
		
		return idxNoZero < src.length() ? src.substring(idxNoZero) : "0";
	}
	
	public static int numericCharToValue(char ch) {
		return ch - '0';
	}
	
	public static char valueToNumericChar(int i) {
		return (char) (i + '0');
	}
	
	/**
	 * 
	 * @param strA
	 * @param strB
	 * @return strA对应的数是否大于strB对应的数
	 */
	public static boolean isBigger(String strA, String strB) {
		if (strA.length() > strB.length()) {
			return true;
		}
		
		if (strA.length() < strB.length()) {
			return false;
		}
		
		for (int i = 0; i < strA.length(); i++) {
			if (strA.charAt(i) > strB.charAt(i)) {
				return true;
			} else if (strA.charAt(i) < strB.charAt(i)) {
				return false;
			}
		}
		return false;
	}
}
