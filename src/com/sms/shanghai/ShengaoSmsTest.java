package com.sms.shanghai;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ShengaoSmsTest {

	public static void main(String[] args) {
		//sendNotifySms();
		sendBussinessSms();
	}
	
	public static void sendNotifySms(){
		String ret = "";
        SimpleDateFormat sft = new SimpleDateFormat("yyyyMMddHHmmss");
		    Date date = new Date();
		         //用户账号
		String account = "yddkj1";  
		         //用户密码
		String password = "ydd~123"; 
		         //发送号码——多个号码间用半角英的","分隔
		String phonelist = "18210609475";  
		         //发送时间——含有年月日时分秒格式的字符串，提交格式错误或为空视为立即发送
		String sendtime = null;  
		         //短信内容
		String content = "【她店】验证码：121355";
		         //批次编号——格式为：用户账号_当前时间(年月日时分秒)_http_n(n>=5)位随机数
		String taskId = account +"_" + sft.format(date)+"_http_"+ Math.round((Math.random()) * 100000); 
		         //url地址
		String url = "http://sms3.biztoall.net:8088/smshttp/infoSend?";
		
		try {
			  account = URLEncoder.encode(account, "utf-8");
		      password = URLEncoder.encode(password, "utf-8");
		      content = URLEncoder.encode(content, "utf-8");
		      taskId = URLEncoder.encode(taskId, "utf-8");
		
		
		url = url + "account=" + account + "&password=" + password + "&content="
		            + content + "&sendtime=" + sendtime+"&phonelist="+phonelist+"&taskId="+taskId;
		} catch (UnsupportedEncodingException e) {
		      e.printStackTrace();
		}
		try {
		      ret =ShengaoSmsTest.get(url, 2000);
		} catch (Exception e) {
		      e.printStackTrace();
		}
		System.out.println(ret);

	}
	
	public static void sendBussinessSms(){
		String ret = "";
        SimpleDateFormat sft = new SimpleDateFormat("yyyyMMddHHmmss");
		    Date date = new Date();
		         //用户账号
		String account = "yddkj";  
		         //用户密码
		String password = "ydd~123"; 
		         //发送号码——多个号码间用半角英的","分隔
		String phonelist = "15820462420";  
		         //发送时间——含有年月日时分秒格式的字符串，提交格式错误或为空视为立即发送
		String sendtime = null;  
		         //短信内容
		String content = "【她店】您的宝贝卖出了。退订回复N";
		         //批次编号——格式为：用户账号_当前时间(年月日时分秒)_http_n(n>=5)位随机数
		String taskId = account +"_" + sft.format(date)+"_http_"+ Math.round((Math.random()) * 100000); 
		         //url地址
		String url = "http://sms2.biztoall.net:8088/smshttp/infoSend?";
		
		try {
			  account = URLEncoder.encode(account, "utf-8");
		      password = URLEncoder.encode(password, "utf-8");
		      content = URLEncoder.encode(content, "utf-8");
		      taskId = URLEncoder.encode(taskId, "utf-8");
		
		
		url = url + "account=" + account + "&password=" + password + "&content="
		            + content + "&sendtime=" + sendtime+"&phonelist="+phonelist+"&taskId="+taskId;
		} catch (UnsupportedEncodingException e) {
		      e.printStackTrace();
		}
		try {
		      ret =ShengaoSmsTest.get(url, 2000);
		} catch (Exception e) {
		      e.printStackTrace();
		}
		System.out.println(ret);

	}
	
	public static String get(String url,int timeOut) throws IOException {
		System.out.println("url:"+url);
		String ret = "";	
        	String inputLine;
		URL url1 = new URL(url);
		URLConnection connection = url1.openConnection();
		connection.setConnectTimeout(timeOut);
		connection.setReadTimeout(timeOut);
		BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(),"UTF-8"));
		while ((inputLine = in.readLine()) != null) {
			ret = ret + inputLine;
		}
		in.close();
		return ret;
	}
}


