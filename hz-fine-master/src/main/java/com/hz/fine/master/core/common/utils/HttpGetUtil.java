package com.hz.fine.master.core.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author df
 * @Description:get请求的公共类
 * @create 2019-05-27 19:35
 **/
public class HttpGetUtil {
    private final static Logger log = LoggerFactory.getLogger(HttpGetUtil.class);

    /**
     * @Description: TODO(get请求url地址，返回固定字段的值)
     * @author df
     * @param url-要请求的url
     * @param field-想要的字段，用于返回使用
     * @create 19:38 2019/5/27
     **/
    public static String sendGetUrl(String url, String field){
        String str = "";
        try{
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url);
            HttpResponse httpResponse = httpClient.execute(httpGet);
            HttpEntity entity = httpResponse.getEntity();
            if (httpResponse.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                return null;
            }
            String data = EntityUtils.toString(entity);
            Object obj = JSON.parseObject(data);
            str = (String) ((JSONObject) obj).get(field);
            return str;
        }catch (Exception e){
            log.error(String.format("HttpGetUtil.getUrl is error!,  parame : url = %s, and field = %s", url, field));
            e.printStackTrace();
            return null;
        }
    }


    /**
     * @Description: TODO(get请求url地址，返回json值)
     * @author df
     * @param url-要请求的url
     * @create 19:38 2019/5/27
     **/
    public static String sendGetUrl(String url){
        String str = "";
        try{
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url);
            HttpResponse httpResponse = httpClient.execute(httpGet);
            HttpEntity entity = httpResponse.getEntity();
            if (httpResponse.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                return null;
            }
            str = EntityUtils.toString(entity);
            return str;
        }catch (Exception e){
            log.error(String.format("HttpGetUtil.getUrl is error!,  parame : url = %s", url));
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        String url = "http://114.55.219.150:9999/wjyidongyueduziji/smsorder.wckj?productId=3&price=2000&imsi=1&mobile=15967171415&channelId=1344&exData=x";
        String field = "resultCode";
        String str = sendGetUrl(url, field);
        System.out.println(str);
        String str1 = sendGetUrl(url);
        System.out.println("str1:" + str1);
    }



    public static String sendPost(String url, String param) throws IOException {
        HttpPost request = new HttpPost(url);
//        request.setEntity(
//                new StringEntity(param,"ISO-8859-1")
//        );
        return send(url,param);
    }


    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url   发送请求的 URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    public static String send(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        StringBuilder result = new StringBuilder();
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setRequestProperty("accept-language", "en-US,en;q=0.5");
            // 发送POST请求,必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setConnectTimeout(3000);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();

            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) out.close();
                if (in != null) in.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result.toString();
    }

}
