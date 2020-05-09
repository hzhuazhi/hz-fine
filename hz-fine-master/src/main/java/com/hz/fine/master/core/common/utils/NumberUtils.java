package com.hz.fine.master.core.common.utils;

import org.apache.commons.lang.StringUtils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Number工具类
 * @author wenqi.huang on 16/4/8.
 */
public class NumberUtils {

    /**
     * 把字符串转换为int,如转换失败,则返回指定的默认值
     * @param str
     * @param defaultValue
     * @return
     */
    public static int parseInt(String str, int defaultValue) {
        if (StringUtils.isEmpty(str)) {
            return defaultValue;
        }
        int value;
        try {
            value = Integer.parseInt(str.trim());
        } catch (Exception e) {
            value = defaultValue;
        }
        return value;
    }

    /**
     * 把字符串转换为float,如转换失败,则返回指定的默认值
     * @param str
     * @param defaultValue
     * @return
     */
    public static float parseFloat(String str, float defaultValue) {
        if (StringUtils.isEmpty(str)) {
            return defaultValue;
        }
        float value;
        try {
            value = Float.parseFloat(str.trim());
        } catch (Exception e) {
            value = defaultValue;
        }
        return value;
    }

    /**
     * 把字符串转换为long,如转换失败,则返回指定的默认值
     * @param str
     * @param defaultValue
     * @return
     */
    public static long parseLong(String str, long defaultValue) {
        if (StringUtils.isEmpty(str)) {
            return defaultValue;
        }
        long value;
        try {
            value = Long.parseLong(str.trim());
        } catch (Exception e) {
            value = defaultValue;
        }
        return value;
    }

    /**
     * 把字符串转换为double,如转换失败,则返回指定的默认值
     * @param str
     * @param defaultValue
     * @return
     */
    public static double parseDouble(String str, double defaultValue) {
        if (StringUtils.isEmpty(str)) {
            return defaultValue;
        }
        double value;
        try {
            value = Double.parseDouble(str.trim());
        } catch (Exception e) {
            value = defaultValue;
        }
        return value;
    }

    /**
     * 把两个Long相加返回结果,null视为0
     * @param val1
     * @param val2
     * @return
     */
    public static Long add(Long val1, Long val2) {
        Long tmpval1 = val1 != null ? val1 : 0;
        Long tmpval2 = val2 != null ? val2 : 0;
        return tmpval1.longValue() + tmpval2.longValue();
    }

    /**
     * 把两个Integer相加返回结果,null视为0
     * @param val1
     * @param val2
     * @return
     */
    public static Integer add(Integer val1, Integer val2) {
        Integer tmpval1 = val1 != null ? val1 : 0;
        Integer tmpval2 = val2 != null ? val2 : 0;
        return tmpval1.intValue() + tmpval2.intValue();
    }

    /**
     * 把两个Double相加返回结果,null视为0
     * @param val1
     * @param val2
     * @return
     */
    public static Double add(Double val1, Double val2) {
        Double tmpval1 = val1 != null ? val1 : 0D;
        Double tmpval2 = val2 != null ? val2 : 0D;
        return tmpval1.doubleValue() + tmpval2.doubleValue();
    }

    /**
     * 把double转换为字符串,保留小数点后几位。
     * @param f
     * @param fractionDigits 指定需要保留小数点后几位。
     * @return
     */
    public static String numberFormat(double f, int fractionDigits) {
        NumberFormat nf1 = NumberFormat.getInstance(Locale.CHINA);
        nf1.setMaximumFractionDigits(fractionDigits);
        nf1.setGroupingUsed(false);//指定不使用逗号分隔符（每隔三位）
        return nf1.format(f);
    }

    /**
     * 把float转换为字符串,保留小数点后几位。
     * @param n
     * @param fractionDigits 指定需要保留小数点后几位。
     * @return
     */
    public static String numberFormat(float n, int fractionDigits) {
        NumberFormat nf = NumberFormat.getInstance();
        nf.setGroupingUsed(false);//指定不使用逗号分隔符（每隔三位）
        nf.setMaximumFractionDigits(fractionDigits);
        return nf.format(n);
    }

    /**
     * 把double转换为百分比字符串,比如0.121314会转换为12.13%,最多保留小数点后两位
     * @param d
     * @return
     */
    public static String percentFormat(double d) {
        DecimalFormat nf = (DecimalFormat) NumberFormat.getPercentInstance();
        nf.applyPattern("##.##%");
        return nf.format(d);
    }

    /**
     * 判断某个字符串是否是数字（允许包含0或1个小数点）
     * @param str
     * @return
     */
    public static boolean isNumeric(String str){
        if(StringUtils.isBlank(str)){
            return false;
        }
        Pattern pattern = Pattern.compile("[0-9]*\\.?[0-9]+");
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;
    }

}
