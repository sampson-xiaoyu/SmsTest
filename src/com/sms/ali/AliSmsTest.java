package com.sms.ali;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import com.easycode.commons.time.DateFormatUtils;

public class AliSmsTest {
	
	
	public static void main(String arg[]){
		
		String url = AliSmsConfig.url;
		String ret = "";
		
		try {
			url = url + prepareParams();
			ret = AliSmsTest.get(url, 100);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(ret);
	}
	
	public static String get(String url,int timeOut) throws IOException {
		System.out.println("url:"+url);
		String ret = "";	
        	String inputLine;
        HttpPost request = new HttpPost(url);
     // 创建HttpClient实例
     	HttpClient httpclient = new DefaultHttpClient();
     	HttpResponse response = httpclient.execute(request);
     	HttpEntity entity = response.getEntity();
     	BufferedReader in = new BufferedReader(new InputStreamReader(entity.getContent()));
		while ((inputLine = in.readLine()) != null) {
			ret = ret + inputLine;
		}
		in.close();
		return ret;
	}
	
	public static String prepareParams()  throws UnsupportedEncodingException{
		
		DateFormatUtils.ISO_DATETIME_TIME_ZONE_FORMAT.format(getCurrentUtcTime());
		String timeStamp = DateFormatUtils.ISO_DATETIME_TIME_ZONE_FORMAT.format(getCurrentUtcTime());
		System.out.println(timeStamp);
		Map<String,String> param = new HashMap<String,String>();
		param.put("Action", AliSmsConfig.smsAction);
		param.put("SignName", AliSmsConfig.signName);
		param.put("TemplateCode", "SMS_1595010");
		param.put("RecNum", URLEncoder.encode("15820462420,18210609475",AliSmsConfig.input_charset));
		param.put("ParamString", URLEncoder.encode("{\"no\":\"123456\"}",AliSmsConfig.input_charset));
		param.put("Format", "JSON");
		param.put("Version","2016-09-27");
		param.put("AccessKeyId", AliSmsConfig.accessKeyId);
		param.put("Timestamp", timeStamp);
		param.put("SignatureVersion", AliSmsConfig.signatureVersion);		
		param.put("SignatureMethod", AliSmsConfig.signatureMethod);
		param.put("SignatureNonce", UUID.randomUUID().toString());
		
		String data= createLinkString(param);
		//将待签名字符串使用私钥签名。
        String rsa_sign= URLEncoder.encode(RSA.sign(data, AliSmsConfig.accessKeySecret, AliSmsConfig.input_charset),AliSmsConfig.input_charset);
        
        data=data+"&Signature="+rsa_sign;
        
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
	
	private static String createLinkString(Map<String, String> params) {

        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);

        String prestr = "";

        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);

            if (i == keys.size() - 1) {//拼接时，不包括最后一个&字符
                prestr = prestr + key + "=" +value;
            } else {
                prestr = prestr + key + "=" +value + "&";
            }
        }

        return prestr;
    }
	
	 private static String createLinkStringToPay(Map<String, String> params) {

	        List<String> keys = new ArrayList<String>(params.keySet());
	        Collections.sort(keys);

	        String prestr = "";

	        for (int i = 0; i < keys.size(); i++) {
	            String key = keys.get(i);
	            String value = params.get(key);

	            if (i == keys.size() - 1) {//拼接时，不包括最后一个&字符
	                prestr = prestr + key + "=" + "\""+value+"\"";
	            } else {
	                prestr = prestr + key + "="  + "\""+value+"\"" + "&";
	            }
	        }

	        return prestr;
	    }
	
}
