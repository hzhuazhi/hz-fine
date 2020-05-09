package com.hz.fine.master.core.common.utils;

import com.alibaba.fastjson.JSON;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.Map.Entry;

/**
 * @ClassName: HttpSendUtils
 * @Description:Http协议
 * @author df
 * @date 2014-9-23 上午10:26:41
 */
public class HttpSendUtils {

	private static final int TIME_OUT = 10;
	
	private static final int READ_TIME_OUT = 20;
	
	//返回状态码
	public static boolean sendGetCode(String httpUrl, String odno, Map<String, String> parameter , Map<String, String> headerMap) {
		return sendGetCode(httpUrl, "UTF-8", odno, parameter, headerMap);
	}
	
	public static boolean sendGetCode(String httpUrl, String encoding, String odno, Map<String, String> parameter , Map<String, String> headerMap){
		if (httpUrl == null) {
			return false;
		}
		String urlStr = null;
		StringBuilder sb = null;
		if(parameter != null){
			sb = new StringBuilder();
			Iterator<Entry<String, String>> iterator = parameter.entrySet().iterator();
			while (iterator.hasNext()) {
				if (sb.length() > 0) {
					sb.append('&');
				}
				Entry<String, String> entry = iterator.next();
				String key = entry.getKey();
				String value;
				try {
					value = URLEncoder.encode(entry.getValue(), encoding);
				} catch (UnsupportedEncodingException e) {
					value = "";
				}
				sb.append(key).append('=').append(value);
			}
		}
		
		if(sb != null){
			if (httpUrl.lastIndexOf('?') != -1) {
				urlStr = httpUrl + '&' + sb.toString();
			} else {
				urlStr = httpUrl + '?' + sb.toString();
			}
		}else{
			urlStr = httpUrl;
		}
		HttpURLConnection httpCon = null;
		int responseCode = 0;
		try {
			URL url = new URL(urlStr);
			httpCon = (HttpURLConnection) url.openConnection();
			httpCon.setDoOutput(true);
			httpCon.setDoInput(true);
			httpCon.setRequestMethod("GET");
			httpCon.setConnectTimeout(TIME_OUT * 1000);
			httpCon.setReadTimeout(READ_TIME_OUT * 1000);
			if(headerMap != null ){//
				Iterator<Entry<String, String>> headerIterator = headerMap.entrySet().iterator();
				while (headerIterator.hasNext()) {
					Entry<String, String> entry = headerIterator.next();
					//System.out.println(entry.getKey()+"--" + entry.getValue());
					//httpCon.setRequestProperty("Authorization", "Basic ZG9vcmNhY0Bkb29ybW9iaS5jb206ZG9vcjEyMw==");
					httpCon.setRequestProperty(entry.getKey(), entry.getValue());
				}
			}
			InputStream in = httpCon.getInputStream();
			byte[] readByte = new byte[1024];
			// 读取返回的内容
			int readCount = in.read(readByte, 0, 1024);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			while (readCount != -1) {
				baos.write(readByte, 0, readCount);
				readCount = in.read(readByte, 0, 1024);
			}
			String responseBody =null;
			responseBody = new String(baos.toByteArray(), encoding);
			baos.close();
			responseCode = httpCon.getResponseCode();
		} catch (Exception e) {
			System.out.println(String.format("-->Exception:%s", httpUrl));
			e.printStackTrace();
		} finally {
			if (httpCon != null)
				httpCon.disconnect();
		}
		
		return HttpURLConnection.HTTP_OK == responseCode;
	}
	
	
	
	//返回状态码
	public static boolean sentPostCode(String httpUrl, String postBody, String encoding, Map<String, String> headerMap) {
		HttpURLConnection httpCon = null;
		URL url = null;
		try {
			url = new URL(httpUrl);
			httpCon = (HttpURLConnection) url.openConnection();
		} catch (Exception e1) {
			return false;
		}
		if (httpCon == null) {
			return false;
		}
		httpCon.setDoOutput(true);
		httpCon.setConnectTimeout(TIME_OUT * 1000);
		httpCon.setReadTimeout(READ_TIME_OUT * 1000);
		httpCon.setDoOutput(true);
		httpCon.setUseCaches(false);
		try {
			httpCon.setRequestMethod("POST");
		} catch (ProtocolException e1) {
			return false;
		}
		if (headerMap != null) {
			Iterator<Entry<String, String>> iterator = headerMap.entrySet().iterator();
			while (iterator.hasNext()) {
				Entry<String, String> entry = iterator.next();
				httpCon.addRequestProperty(entry.getKey(), entry.getValue());
			}
		}
		OutputStream output;
		try {
			output = httpCon.getOutputStream();
			output.write(postBody.getBytes(encoding));
		} catch (IOException e1) {
			return false;
		}
		
		try {
			output.flush();
			output.close();
		} catch (IOException e1) {
			return false;
		}
		try {
			int responseCode = httpCon.getResponseCode();
			return (HttpURLConnection.HTTP_OK == responseCode);
		} catch (IOException e) {
			e.printStackTrace();
		}   
		return false;
	}
	
	/**
	 * 通过HTTP GET 发送参数
	 * 
	 * @param httpUrl
	 * @param parameter
	 * @param headerMap
	 */
	public static String sendGet(String httpUrl, Map<String, String> parameter , Map<String, String> headerMap) {
		return sendGet(httpUrl, "UTF-8", parameter, headerMap);
	}
	
	public static String sendGet(String httpUrl,String encoding, Map<String, String> parameter , Map<String, String> headerMap){
		if (httpUrl == null) {
			System.out.println("");
			return null;
		}
		String urlStr = null;
		StringBuilder sb = null;
		if(parameter != null){
			sb = new StringBuilder();
			Iterator<Entry<String, String>> iterator = parameter.entrySet().iterator();
			while (iterator.hasNext()) {
				if (sb.length() > 0) {
					sb.append('&');
				}
				Entry<String, String> entry = iterator.next();
				String key = entry.getKey();
				String value;
				try {
					value = URLEncoder.encode(entry.getValue(), encoding);
				} catch (UnsupportedEncodingException e) {
					value = "";
				}
				sb.append(key).append('=').append(value);
			}
		}
		
		if(sb != null){
			if (httpUrl.lastIndexOf('?') != -1) {
				urlStr = httpUrl + '&' + sb.toString();
			} else {
				urlStr = httpUrl + '?' + sb.toString();
			}
		}else{
			urlStr = httpUrl;
		}
		HttpURLConnection httpCon = null;
		String responseBody = null;
		try {
			URL url = new URL(urlStr);
			httpCon = (HttpURLConnection) url.openConnection();
			httpCon.setDoOutput(true);
			httpCon.setDoInput(true);
			httpCon.setRequestMethod("GET");
			httpCon.setConnectTimeout(TIME_OUT * 1000);
			httpCon.setReadTimeout(READ_TIME_OUT * 1000);
			if(headerMap != null ){//
				Iterator<Entry<String, String>> headerIterator = headerMap.entrySet().iterator();
				while (headerIterator.hasNext()) {
					Entry<String, String> entry = headerIterator.next();
					//System.out.println(entry.getKey()+"--" + entry.getValue());
					//httpCon.setRequestProperty("Authorization", "Basic ZG9vcmNhY0Bkb29ybW9iaS5jb206ZG9vcjEyMw==");
					httpCon.setRequestProperty(entry.getKey(), entry.getValue());
				}
			}
			
			// 开始读取返回的内容
			InputStream in = httpCon.getInputStream();
			byte[] readByte = new byte[1024];
			// 读取返回的内容
			int readCount = in.read(readByte, 0, 1024);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			while (readCount != -1) {
				baos.write(readByte, 0, readCount);
				readCount = in.read(readByte, 0, 1024);
			}
			responseBody = new String(baos.toByteArray(), encoding);
			baos.close();
		} catch (Exception e) {
			System.out.println(String.format("-->Exception:%s", httpUrl));
			e.printStackTrace();
		} finally {
			if (httpCon != null)
				httpCon.disconnect();
		}
		return responseBody;
	}
	
	
	/**
	 * 通过HTTP GET 发送参数
	 * 
	 * @param httpUrl
	 * @param timeout
	 * @param parameter
	 * @param headerMap
	 */
	public static String sendGet(String httpUrl, int timeout, Map<String, String> parameter , Map<String, String> headerMap) {
		if (httpUrl == null) {
			System.out.println("");
			return null;
		}
		String urlStr = null;
		StringBuilder sb = null;
		if(parameter != null){
			sb = new StringBuilder();
			Iterator<Entry<String, String>> iterator = parameter.entrySet().iterator();
			while (iterator.hasNext()) {
				if (sb.length() > 0) {
					sb.append('&');
				}
				Entry<String, String> entry = iterator.next();
				String key = entry.getKey();
				String value;
				try {
					value = URLEncoder.encode(entry.getValue(), "UTF-8");
				} catch (UnsupportedEncodingException e) {
					value = "";
				}
				sb.append(key).append('=').append(value);
			}
		}
		
		if(sb != null){
			if (httpUrl.lastIndexOf('?') != -1) {
				urlStr = httpUrl + '&' + sb.toString();
			} else {
				urlStr = httpUrl + '?' + sb.toString();
			}
		}else{
			urlStr = httpUrl;
		}
		HttpURLConnection httpCon = null;
		String responseBody = null;
		try {
			URL url = new URL(urlStr);
			httpCon = (HttpURLConnection) url.openConnection();
			httpCon.setDoOutput(true);
			httpCon.setDoInput(true);
			httpCon.setRequestMethod("GET");
			httpCon.setConnectTimeout(timeout * 1000);
			httpCon.setReadTimeout(READ_TIME_OUT * 1000);
			if(headerMap != null ){//
				Iterator<Entry<String, String>> headerIterator = headerMap.entrySet().iterator();
				while (headerIterator.hasNext()) {
					Entry<String, String> entry = headerIterator.next();
					//System.out.println(entry.getKey()+"--" + entry.getValue());
					//httpCon.setRequestProperty("Authorization", "Basic ZG9vcmNhY0Bkb29ybW9iaS5jb206ZG9vcjEyMw==");
					httpCon.setRequestProperty(entry.getKey(), entry.getValue());
				}
			}
			
			// 开始读取返回的内容
			InputStream in = httpCon.getInputStream();
			byte[] readByte = new byte[1024];
			// 读取返回的内容
			int readCount = in.read(readByte, 0, 1024);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			while (readCount != -1) {
				baos.write(readByte, 0, readCount);
				readCount = in.read(readByte, 0, 1024);
			}
			responseBody = new String(baos.toByteArray(), "UTF-8");
			baos.close();
		} catch (Exception e) {
			System.out.println(String.format("-->Exception:%s", httpUrl));
			e.printStackTrace();
		} finally {
			if (httpCon != null)
				httpCon.disconnect();
		}
		return responseBody;
	}
	
	/**
	 * 使用HTTP POST 发送文本
	 * 
	 * @param httpUrl
	 *            发送的地址
	 * @param postBody
	 *            发送的内容
	 * @return 返回HTTP SERVER的处理结果,如果返回null,发送失败
	 */
	public static String sentPost(String httpUrl, String postBody) {
		return sentPost(httpUrl, postBody, "UTF-8", null);
	}

	/**
	 * 使用HTTP POST 发送文本
	 * 
	 * @param httpUrl
	 *            发送的地址
	 * @param postBody
	 *            发送的内容
	 * @return 返回HTTP SERVER的处理结果,如果返回null,发送失败
	 */
	public static String sentPost(String httpUrl, String postBody, String encoding) {
		return sentPost(httpUrl, postBody, encoding, null);
	}

	/**
	 * 使用HTTP POST 发送文本
	 * @param httpUrl   目的地址
	 * @param postBody  post的包体
	 * @param headerMap 增加的Http头信息
	 * @return
	 */
	public static String sentPost(String httpUrl, String postBody, Map<String, String> headerMap) {
		return sentPost(httpUrl, postBody, "UTF-8", headerMap);
	}

	/**
	 * 使用HTTP POST 发送文本
	 * 
	 * @param httpUrl
	 *            发送的地址
	 * @param postBody
	 *            发送的内容
	 * @param encoding
	 *            发送的内容的编码
	 * @param headerMap 增加的Http头信息          
	 * @return 返回HTTP SERVER的处理结果,如果返回null,发送失败
	 */
	public static String sentPost(String httpUrl, String postBody, String encoding, Map<String, String> headerMap) {
		HttpURLConnection httpCon = null;
		String responseBody = null;
		URL url = null;
		try {
			url = new URL(httpUrl);
		} catch (MalformedURLException e1) {
			return null;
		}
		try {
			httpCon = (HttpURLConnection) url.openConnection();
		} catch (IOException e1) {
			return null;
		}
		if (httpCon == null) {
			return null;
		}
		httpCon.setDoOutput(true);
		httpCon.setConnectTimeout(TIME_OUT * 1000);
		httpCon.setReadTimeout(READ_TIME_OUT * 1000);
		httpCon.setDoOutput(true);
		httpCon.setUseCaches(false);
		try {
			httpCon.setRequestMethod("POST");
		} catch (ProtocolException e1) {
			return null;
		}
		if (headerMap != null) {
			Iterator<Entry<String, String>> iterator = headerMap.entrySet().iterator();
			while (iterator.hasNext()) {
				Entry<String, String> entry = iterator.next();
				httpCon.addRequestProperty(entry.getKey(), entry.getValue());
			}
		}
		OutputStream output;
		try {
			output = httpCon.getOutputStream();
		} catch (IOException e1) {
			return null;
		}
		try {
			output.write(postBody.getBytes(encoding));
		} catch (UnsupportedEncodingException e1) {
			return null;
		} catch (IOException e1) {
			return null;
		}
		try {
			output.flush();
			output.close();
		} catch (IOException e1) {
			return null;
		}


		// 开始读取返回的内容
		InputStream in;
		try {
			in = httpCon.getInputStream();
		} catch (IOException e1) {
			return null;
		}
		/**
		 * 这个方法可以在读写操作前先得知数据流里有多少个字节可以读取。
		 * 需要注意的是，如果这个方法用在从本地文件读取数据时，一般不会遇到问题，
		 * 但如果是用于网络操作，就经常会遇到一些麻烦。
		 * 比如，Socket通讯时，对方明明发来了1000个字节，但是自己的程序调用available()方法却只得到900，或者100，甚至是0，
		 * 感觉有点莫名其妙，怎么也找不到原因。
		 * 其实，这是因为网络通讯往往是间断性的，一串字节往往分几批进行发送。
		 * 本地程序调用available()方法有时得到0，这可能是对方还没有响应，也可能是对方已经响应了，但是数据还没有送达本地。
		 * 对方发送了1000个字节给你，也许分成3批到达，这你就要调用3次available()方法才能将数据总数全部得到。
		 * 
		 * 经常出现size为0的情况，导致下面readCount为0使之死循环(while (readCount != -1) {xxxx})，出现死机问题
		 */
		int size = 0;
		try {
			size = in.available();
		} catch (IOException e1) {
			return null;
		}
		if (size == 0) {
			size = 1024;
		}
		byte[] readByte = new byte[size];
		// 读取返回的内容
		int readCount = -1;
		try {
			readCount = in.read(readByte, 0, size);
		} catch (IOException e1) {
			return null;
		}
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		while (readCount != -1) {
			baos.write(readByte, 0, readCount);
			try {
				readCount = in.read(readByte, 0, size);
			} catch (IOException e) {
				return null;
			}
		}
		try {
			responseBody = new String(baos.toByteArray(), encoding);
		} catch (UnsupportedEncodingException e) {
			return null;
		} finally {
			if (httpCon != null) {
				httpCon.disconnect();
			}
			if (baos != null) {
				try {
					baos.close();
				} catch (IOException e) {
				}
			}
		}
		
		return responseBody;
	}
	
	public static String httpsentPost(String httpUrl, Map<String,String> postBody, String encoding, Map<String, String> headerMap) {
		HttpClient httpClient = null;
		String responseString = "";
		try {			
			String sellerKey = postBody.get("seller_key");
			String imsi = postBody.get("imsi");
			String appName = postBody.get("app_name");
			String fee = postBody.get("fee");
			String outTradeNo = postBody.get("out_trade_no");
			String sign = postBody.get("sign");
			
			httpClient = new DefaultHttpClient();
			HttpPost post = new HttpPost(httpUrl);
			
			List <NameValuePair> nvps = new ArrayList <NameValuePair>();
			nvps.add(new BasicNameValuePair("seller_key",sellerKey));
			nvps.add(new BasicNameValuePair("imsi",imsi));
			nvps.add(new BasicNameValuePair("app_name",appName));
			nvps.add(new BasicNameValuePair("fee",fee));
			nvps.add(new BasicNameValuePair("out_trade_no", outTradeNo));
			nvps.add(new BasicNameValuePair("sign",sign));
			post.setEntity(new UrlEncodedFormEntity(nvps,encoding));
			
			HttpResponse response = httpClient.execute(post);
			if (response.getStatusLine().getStatusCode() == 200) {
				responseString = EntityUtils.toString(response.getEntity());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (httpClient != null) {
				httpClient.getConnectionManager().shutdown();
			}
		}
		return responseString;
	}


	/**
	 * 使用HTTP POST 发送文本-----sp要求HTTP头中Content-Type要设置为application/json
	 * 
	 * @param url
	 *            发送的地址
	 * @param param
	 *            发送的内容
	 * @return 返回HTTP SERVER的处理结果,如果返回null,发送失败
	 */
	public static String sendPostAppJson(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
			String data = StringUtil.mergeCodeBase64("9e8c23f670efe7dba280b6a6" + ":" + "b1bcae8402ceaeb1e7928bd0");
			URL realUrl = new URL(url);
            URLConnection conn = realUrl.openConnection(); 
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setRequestProperty("Content-Type","application/json");
			conn.setRequestProperty("Authorization", "Basic " + data);
            conn.setDefaultUseCaches(false);
            conn.setDoOutput(true);
//            conn.setDoInput(true); 
            out = new PrintWriter(new OutputStreamWriter(conn.getOutputStream(),"utf-8")); 
            out.print(param); 
            out.flush(); 
            StringBuffer d = new StringBuffer();
			String responseLine = "";
			DataInputStream downdatais = new DataInputStream(
					new BufferedInputStream(conn.getInputStream()));
			BufferedReader bf = new BufferedReader(new InputStreamReader(
					downdatais));
			while ((responseLine = bf.readLine()) != null) {
				d.append(new String(responseLine.getBytes(), "UTF-8") + "\n");//
			}
			bf.close();
			downdatais.close();
			result=d.toString();
        } catch (Exception e) { 
            e.printStackTrace();
        } 
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return result;
    }

	public static String doPost(String url, String jsonStr) {
		CloseableHttpClient httpclient = HttpClientBuilder.create().build();
		HttpPost post = new HttpPost(url);
		String result = "";

		try {
			String data = StringUtil.mergeCodeBase64("9e8c23f670efe7dba280b6a6" + ":" + "b1bcae8402ceaeb1e7928bd0");
			StringEntity s = new StringEntity(jsonStr);
			s.setContentEncoding("UTF-8");
			s.setContentType("application/json");
			s.setContentType("Authorization : Basic " + data);
			post.setEntity(s);
			HttpResponse res = httpclient.execute(post);
			if (res.getStatusLine().getStatusCode() == 200) {
				result = EntityUtils.toString(res.getEntity());
			}
		} catch (Exception var8) {
			var8.printStackTrace();
		}

		return result;
	}






	public static void main(String[] args) {
		String url = "http://www.baidu.com";
//		boolean b = sentPostCode(url, "", "utf-8", null);
		boolean b = sendGetCode(url, "123455", null, null);
		System.out.println(b);
		String url1 = "http://114.55.67.167:8082/play/csm/upPayCode";
		Map<String, String> map = new HashMap<>();
		map.put("jsonData", "eyJwYXlQdyI6IjE1OTY3MTcxNDE1IiwidXBQYXlDb2RlIjoxMjM0NTYsImFndFZlciI6MSwiY2xpZW50VmVyIjoxLCJjdGltZSI6MjAxOTExMDcxODAyOTU5LCJjY3RpbWUiOjIwMTkxMTA3MTgwMjk1OSwic2lnbiI6ImFiY2RlZmciLCJ0b2tlbiI6IjExMTExMSJ9");
		String data = JSON.toJSONString(map);
		String resData = sendPostAppJson(url1, data);
		System.out.println("resData:" + resData);
	}
}
