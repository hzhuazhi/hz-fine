package com.hz.fine.master.core.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hz.fine.master.core.model.region.RegionModel;
import org.apache.commons.lang.StringUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * @author df
 * @Description:根据IP获取省份的外部接口
 * @create 2018-10-25 15:44
 **/
public class AddressUtils {

//    /**
//     * 淘宝接口 根据IP定位省份城市
//     *
//     * @param content
//     *            请求的参数 格式为：name=xxx&pwd=xxx
//     * @param encodingString
//     *            服务器端请求编码。如GBK,UTF-8等
//     * @return
//     * @throws UnsupportedEncodingException
//     */
//    public RegionModel getAddresses(String content, String encodingString) throws UnsupportedEncodingException {
//        RegionModel vo = new RegionModel();
//        vo.setIp(content);
//        // 这里调用pconline的接口
//        String urlStr = "http://ip.taobao.com/service/getIpInfo.php";
//        // 从http://whois.pconline.com.cn取得IP所在的省市区信息
//        String returnStr = this.getResult(urlStr, "ip="+content, encodingString);
//        if (returnStr != null) {
//            // 处理返回的省市区信息
////			System.out.println(returnStr);
//            String[] temp = returnStr.split(",");
//            if (temp.length < 3) {
//                return null;// 无效IP，局域网测试
//            }
//            String region = (temp[5].split(":"))[1].replaceAll("\"", "");
//            region = decodeUnicode(region);// 省份
//
//            String country = "";
//            String area = "";
//            // String region = "";
//            String city = "";
//            String county = "";
//            String isp = "";
//            Map<String,String> result = new HashMap<String, String>();
//            for (int i = 0; i < temp.length; i++) {
//                switch (i) {
//                    case 1:
//                        country = (temp[i].split(":"))[2].replaceAll("\"", "");
//                        country = decodeUnicode(country);// 国家
//                        result.put("country", country);
//                        break;
//                    case 3:
//                        area = (temp[i].split(":"))[1].replaceAll("\"", "");
//                        area = decodeUnicode(area);// 地区
//                        result.put("area", area);
//                        break;
//                    case 5:
//                        region = (temp[i].split(":"))[1].replaceAll("\"", "");
//                        region = decodeUnicode(region);// 省份
//                        region=region.replaceAll("省", "");
//                        vo.setProvince(region);
//                        result.put("region", region);
//                        break;
//                    case 7:
//                        city = (temp[i].split(":"))[1].replaceAll("\"", "");
//                        city = decodeUnicode(city);// 市区
//                        city=city.replaceAll("市", "");
//                        vo.setCity(city);
//                        result.put("city", city);
//                        break;
//                    case 9:
//                        county = (temp[i].split(":"))[1].replaceAll("\"", "");
//                        county = decodeUnicode(county);// 地区
//                        result.put("county", county);
//                        break;
//                    case 11:
//                        isp = (temp[i].split(":"))[1].replaceAll("\"", "");
//                        isp = decodeUnicode(isp); // ISP公司
//                        result.put("isp", isp);
//                        break;
//                }
//            }
//            return vo;
//        }
//        return null;
//    }

    /**
     * 淘宝接口 根据IP定位省份城市
     *
     * @param content
     *            请求的参数 格式为：name=xxx&pwd=xxx
     * @param encodingString
     *            服务器端请求编码。如GBK,UTF-8等
     * @return
     * @throws UnsupportedEncodingException
     */
    public RegionModel getAddresses(String content, String encodingString) throws UnsupportedEncodingException {
        RegionModel vo = new RegionModel();
        vo.setIp(content);
        // 这里调用pconline的接口
        String urlStr = "http://ip.taobao.com/service/getIpInfo.php";
        // 从http://whois.pconline.com.cn取得IP所在的省市区信息
        String returnStr = this.getResult(urlStr, "ip="+content, encodingString);
        if (returnStr != null) {
            JSONObject obj = JSON.parseObject(returnStr);
            String code = obj.getString("code");
            if (code.equals("0")){
                JSONObject data = obj.getJSONObject("data");
                String country_id = data.getString("country_id");
                String province = data.getString("region");
                String city = data.getString("city");
                if (country_id.equals("CN")){
                    if (!StringUtils.isBlank(province)){
                        vo.setProvince(province);
                    }
                    if (!StringUtils.isBlank(city)){
                        vo.setCity(city);
                    }
                }else {
                    String country = data.getString("country");
                    if (!StringUtils.isBlank(country)){
                        vo.setProvince(country);
                    }
                }
            }
            return vo;
        }
        return null;
    }

    /**
     *  新浪接口 根据IP定位省份城市
     *
     * @param content
     *            请求的参数 格式为：name=xxx&pwd=xxx
     * @param encodingString
     *            服务器端请求编码。如GBK,UTF-8等
     * @param type-类型：1表示中国国内，2表示国外
     * @return
     * @throws UnsupportedEncodingException
     */
    public RegionModel sina_getAddresses(String content, String encodingString, int type)
            throws UnsupportedEncodingException {
        RegionModel vo = new RegionModel();
        vo.setIp(content);
        // 这里调用pconline的接口
        String urlStr = "http://int.dpool.sina.com.cn/iplookup/iplookup.php?format=js&ip="+content;
        // 从http://whois.pconline.com.cn取得IP所在的省市区信息
        String returnStr = this.getResult(urlStr, "ip="+content, encodingString);
        if (returnStr != null) {
            // 处理返回的省市区信息
            String[] temp = returnStr.split(",");
            if (temp.length < 3) {
                return null;// 无效IP，局域网测试
            }

            String country = "";
            String region = "";
            String city = "";
            Map<String,String> result = new HashMap<String, String>();
            for (int i = 0; i < temp.length; i++) {
                String[] arrays = temp[i].replaceAll("\"", "").split(":");
                if(arrays == null || arrays.length !=2) continue;
                if("province".equals(arrays[0])) {
                    region = decodeUnicode(arrays[1]);// 省份
                    region=region.replaceAll("省", "").replace("市", "");
                    vo.setProvince(region);
                    result.put("region", region);
                }
                if("city".equals(arrays[0])){
                    city = decodeUnicode(arrays[1]);// 市区
                    city=city.replaceAll("市", "");
                    vo.setCity(city);
                    result.put("region", region);
                }
                if("country".equals(arrays[0])){
                    country = decodeUnicode(arrays[1]);// 市区
                    country = decodeUnicode(country);// 国家
                    result.put("country", country);
                }
            }
            if(StringUtils.isBlank(country)){
                return null;
            }
            if(type == 1){
                //中国的
            }else{
                //国外的：值只需要获取国家就好了
                vo.setProvince(country);
            }
            return vo;
        }
        return null;
    }

    /**
     *  太平洋接口 根据IP定位省份城市
     *
     * @param content
     *            请求的参数 格式为：name=xxx&pwd=xxx
     * @param encodingString
     *            服务器端请求编码。如GBK,UTF-8等
     * @param type-类型：1表示中国国内，2表示国外
     * @return
     * @throws UnsupportedEncodingException
     */
    public RegionModel taipingyang_getAddresses(String content, String encodingString, int type)
            throws UnsupportedEncodingException {
        RegionModel vo = new RegionModel();
        vo.setIp(content);
        // 这里调用pconline的接口
        String urlStr = "http://whois.pconline.com.cn/ipJson.jsp?";
        // 从http://whois.pconline.com.cn取得IP所在的省市区信息
        String returnStr = this.getResult(urlStr, "ip="+content, encodingString);
        if (returnStr != null) {
            // 处理返回的省市区信息
            String[] temp = returnStr.split(",");
            String region = "";
            String city = "";
            String cityOperator = "";
            String country = "";
            for (int i = 0; i < temp.length; i++) {
                String[] arrays = temp[i].replaceAll("\"", "").split(":");
                if(arrays == null || arrays.length !=2) continue;
                if("pro".equals(arrays[0])) {
                    region = decodeUnicode(arrays[1]);// 省份
                    region=region.replaceAll("省", "").replace("市", "");
                    vo.setProvince(region);
                }
                if("city".equals(arrays[0])){
                    city = decodeUnicode(arrays[1]);// 市区
                    city=city.replaceAll("市", "");
                    vo.setCity(city);
                }
                if("addr".equals(arrays[0])){
                    if (type == 1){
                        cityOperator = decodeUnicode(arrays[1]);// 城市+运营商
                        if (StringUtils.isNotEmpty(cityOperator)){
                            String []operator = cityOperator.split(" ");
                            if (operator.length ==2){
                                vo.setIsp(operator[1]);
                            }
                            //不属于中国
                            if (StringUtils.isBlank(region) && !StringUtils.isBlank(operator[1])){
                                vo.setProvince(operator[1]);
                                region = operator[1];
                            }

                        }
                    }else if(type == 2){
                        country = decodeUnicode(arrays[1]);// 国家
                        if(type ==2){
//                            byte [] strBe = country.getBytes("UTF-8");
//                            String str = new String(strBe,"UTF-8");
                            vo.setProvince(country.trim());
                        }
                    }


                }
            }
//            if(!StringUtils.isNotEmpty(region)) return null;
            if(type ==1){
                if(StringUtils.isBlank(region)){
                    return null;
                }
            }else if(type == 2){
                if(StringUtils.isBlank(country)){
                    return null;
                }
            }
            return vo;
        }
        return null;
    }
    /**
     * @param urlStr
     *            请求的地址
     * @param content
     *            请求的参数 格式为：name=xxx&pwd=xxx
     * @param encoding
     *            服务器端请求编码。如GBK,UTF-8等
     * @return
     */
    private String getResult(String urlStr, String content, String encoding) {
        URL url = null;
        HttpURLConnection connection = null;
        try {
            url = new URL(urlStr);
            connection = (HttpURLConnection) url.openConnection();// 新建连接实例
            connection.setConnectTimeout(8000);// 设置连接超时时间，单位毫秒
            connection.setReadTimeout(8000);// 设置读取数据超时时间，单位毫秒
            connection.setDoOutput(true);// 是否打开输出流 true|false
            connection.setDoInput(true);// 是否打开输入流true|false
            connection.setRequestMethod("POST");// 提交方法POST|GET
            connection.setUseCaches(false);// 是否缓存true|false
            connection.connect();// 打开连接端口
            DataOutputStream out = new DataOutputStream(
                    connection.getOutputStream());// 打开输出流往对端服务器写数据
            out.writeBytes(content);// 写数据,也就是提交你的表单 name=xxx&pwd=xxx
            out.flush();// 刷新
            out.close();// 关闭输出流

            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    connection.getInputStream(), encoding));// 往对端写完数据对端服务器返回数据
            // ,以BufferedReader流来读取
            StringBuffer buffer = new StringBuffer();
            String line = "";
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            reader.close();
            return buffer.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();// 关闭连接
            }
        }
        return null;
    }

    /**
     * unicode 转换成 中文
     *
     * @author fanhui 2007-3-15
     * @param theString
     * @return
     */
    public static String decodeUnicode(String theString) {
        char aChar;
        int len = theString.length();
        StringBuffer outBuffer = new StringBuffer(len);
        for (int x = 0; x < len;) {
            aChar = theString.charAt(x++);
            if (aChar == '\\') {
                aChar = theString.charAt(x++);
                if (aChar == 'u') {
                    int value = 0;
                    for (int i = 0; i < 4; i++) {
                        aChar = theString.charAt(x++);
                        switch (aChar) {
                            case '0':
                            case '1':
                            case '2':
                            case '3':
                            case '4':
                            case '5':
                            case '6':
                            case '7':
                            case '8':
                            case '9':
                                value = (value << 4) + aChar - '0';
                                break;
                            case 'a':
                            case 'b':
                            case 'c':
                            case 'd':
                            case 'e':
                            case 'f':
                                value = (value << 4) + 10 + aChar - 'a';
                                break;
                            case 'A':
                            case 'B':
                            case 'C':
                            case 'D':
                            case 'E':
                            case 'F':
                                value = (value << 4) + 10 + aChar - 'A';
                                break;
                            default:
                                throw new IllegalArgumentException(
                                        "Malformed      encoding.");
                        }
                    }
                    outBuffer.append((char) value);
                } else {
                    if (aChar == 't') {
                        aChar = '\t';
                    } else if (aChar == 'r') {
                        aChar = '\r';
                    } else if (aChar == 'n') {
                        aChar = '\n';
                    } else if (aChar == 'f') {
                        aChar = '\f';
                    }
                    outBuffer.append(aChar);
                }
            } else {
                outBuffer.append(aChar);
            }
        }
        return outBuffer.toString();
    }

    /**
     * @Description: TODO(把IP转换成long类型的)
     * @author df
     * @param ip
     * @create 16:15 2017/11/6
     **/
    public static long getIpValue(String ip){
        long ipvalue=0;
        if(!StringUtils.isNotEmpty(ip) || ip.indexOf(":") > -1){
            return ipvalue;
        }
        String[] ipSegment = ip.split("\\.");
        // 计算ip对应的值
        ipvalue = Long.parseLong(ipSegment[0]) * 16777216;
        ipvalue += Long.parseLong(ipSegment[1]) * 65536;
        ipvalue += Long.parseLong(ipSegment[2]) * 256;
        ipvalue += Long.parseLong(ipSegment[3]);
        return ipvalue;
    }

    public static void main(String[] args) {
        String ip = "223.75.100.0";
        long num =getIpValue(ip);
        System.out.println("num:"+num);
    }

   /* // 测试
    public static void main(String[] args) {
//		System.out.println(decodeUnicode("\u5e7f\u5dde"));

        AddressUtils addressUtils = new AddressUtils();
        String ip = "120.199.8.123";
        RegionModel address = new RegionModel();
        RegionModel address2 = new RegionModel();
        RegionModel address3 = new RegionModel();
        try {
            address2 = addressUtils.sina_getAddresses(ip, "utf-8");
            address3 = addressUtils.taipingyang_getAddresses(ip, "gbk");
            address = addressUtils.getAddresses(ip, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.println(address.getIp()+" "+address.getProvince()+" "+address.getCity());
        System.out.println(address2.getIp()+" "+address2.getProvince()+" "+address2.getCity());
        System.out.println(address3.getIp()+" "+address3.getProvince()+" "+address2.getCity());
    }*/
}
