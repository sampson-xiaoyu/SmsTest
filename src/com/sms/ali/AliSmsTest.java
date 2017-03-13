package com.sms.ali;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import com.easycode.commons.time.DateFormatUtils;

public class AliSmsTest {
	
	private final static String SEPARATOR = "&";
	
	public static void main(String arg[]){

		try {
			String url = AliSmsConfig.url;
			System.out.println("url:"+url);
			String ret = AliSmsTest.post(url,prepareParams(), 100);
			System.out.println(ret);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	public static String post(String url,String params,int timeOut) throws IOException {

		String ret = "";	
        	String inputLine;
        HttpPost request = new HttpPost(url);
        request.setHeader("Content-Type", "application/x-www-form-urlencoded");
        HttpEntity entity = new StringEntity(params);
        request.setEntity(entity);
     // 创建HttpClient实例
     	HttpClient httpclient = new DefaultHttpClient();
     	HttpResponse response = httpclient.execute(request);
     	HttpEntity responseEntity = response.getEntity();
     	BufferedReader in = new BufferedReader(new InputStreamReader(responseEntity.getContent()));
		while ((inputLine = in.readLine()) != null) {
			ret = ret + inputLine;
		}
		in.close();
		return ret;
	}
	
	public static String prepareParams()  throws UnsupportedEncodingException{
		
		String timeStamp = DateFormatUtils.ISO_DATETIME_FORMAT.format(getCurrentUtcTime());
		System.out.println(timeStamp);
		Map<String,String> param = new HashMap<String,String>();
		param.put("Action", AliSmsConfig.smsAction);
		param.put("SignName", AliSmsConfig.signName);
		param.put("TemplateCode", "SMS_111111");
		param.put("RecNum", "15820462420");
		param.put("ParamString", "{\"name\":\"123\"}");
		param.put("Format", "JSON");
		param.put("Version","2016-09-27");
		param.put("AccessKeyId", AliSmsConfig.accessKeyId);
		param.put("Timestamp", timeStamp);
		param.put("SignatureVersion", AliSmsConfig.signatureVersion);		
		param.put("SignatureMethod", AliSmsConfig.signatureMethod);
		param.put("SignatureNonce", UUID.randomUUID().toString());
		param.put("RegionId", "cn-hangzhou");
		
		String stringToSign = composeStringToSign("POST",param);
		
		System.out.println("签名前的stringToSign："+stringToSign);
		//将待签名字符串使用私钥签名。
        String rsaSign = "";
		try {
			rsaSign = RSA.signHmac(stringToSign, AliSmsConfig.accessKeySecret+"&",AliSmsConfig.input_charset);
		} catch (NoSuchAlgorithmException | InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
        
        param.put("Signature", rsaSign);
        String data = concatQueryString(param);
        
        System.out.println("签名后的data："+data);
        return data;
	}
	
	
	private static long getCurrentUtcTime(){
		// 取得本地时间：
	    Calendar cal = Calendar.getInstance();
	    // 取得时间偏移量：
	    int zoneOffset = cal.get(java.util.Calendar.ZONE_OFFSET);
	    // 取得夏令时差：
	    int dstOffset = cal.get(java.util.Calendar.DST_OFFSET);
	    cal.add(java.util.Calendar.MILLISECOND, -(zoneOffset + dstOffset));

        long mills = cal.getTimeInMillis();
        return mills;
	}
	
	public static String concatQueryString(Map<String, String> parameters) 
			throws UnsupportedEncodingException {
		if (null == parameters)
			return null;
		
		StringBuilder urlBuilder = new StringBuilder("");
		for(Entry<String, String> entry : parameters.entrySet()){
            String key = entry.getKey();
            String val = entry.getValue();
			urlBuilder.append(AcsURLEncoder.encode(key));
			if (val != null){
            	urlBuilder.append("=").append(AcsURLEncoder.encode(val));
			}
			urlBuilder.append("&");
        }
		
		int strIndex = urlBuilder.length();
		if (parameters.size() > 0)
			urlBuilder.deleteCharAt(strIndex - 1);
		
		return urlBuilder.toString();
	}
	
	public static String composeStringToSign(String method, Map<String, String> queries) {
		
		String[] sortedKeys = queries.keySet().toArray(new String[]{});
        Arrays.sort(sortedKeys);
        StringBuilder canonicalizedQueryString = new StringBuilder();
        try { 
	        for(String key : sortedKeys) {
	            canonicalizedQueryString.append("&")
	            .append(AcsURLEncoder.percentEncode(key)).append("=")
	            .append(AcsURLEncoder.percentEncode(queries.get(key)));
	        }
	
	        StringBuilder stringToSign = new StringBuilder();
	        stringToSign.append(method);
	        stringToSign.append(SEPARATOR);
	        stringToSign.append(AcsURLEncoder.percentEncode("/"));
	        stringToSign.append(SEPARATOR);
	        stringToSign.append(AcsURLEncoder.percentEncode(
	                canonicalizedQueryString.toString().substring(1)));
	        
	        return stringToSign.toString();
        } catch (UnsupportedEncodingException exp) {
        	throw new RuntimeException("UTF-8 encoding is not supported.");
        }
        
	}
	
}
