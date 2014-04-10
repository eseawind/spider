package com.etaoshi.spider.analysis;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.IOException;
import java.nio.charset.Charset;

import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.*;

/**
 * 
 * @author jinweile
 *
 */
public class HttpDown {
	
	/**
	 * 下载内容(get)
	 * @param url
	 * @param header
	 * @return
	 * @throws IOException 
	 * @throws HttpException 
	 */
	public static String getdown(String url, Map<String, String> header) throws HttpException, IOException{
		String result = "";
		HttpClient client = new HttpClient();
		GetMethod method = new GetMethod(url);
		if(header != null)
			for(String key : header.keySet()){
				method.addRequestHeader(key, header.get(key));
			}
		int statusCode = client.executeMethod(method);
		if(statusCode != HttpStatus.SC_OK){
			return null;
		}
		String content = method.getResponseBodyAsString();
		String Content_Type = method.getResponseHeader("Content-Type").getValue();
		//获取解析的编码格式
		Charset charset = DeCharSetName(Content_Type, content);
		byte[] responseBody = content.getBytes(method.getResponseCharSet());
		//转码
		result = new String(responseBody, charset);
		
		return result;
	}
	
	/**
	 * 下载内容(post)
	 * @param url
	 * @param header
	 * @param postparams
	 * @return
	 * @throws IOException 
	 * @throws HttpException 
	 */
	public static String postdown(String url, Map<String, String> header, Map<String, String> postparams) throws HttpException, IOException{
		String result = "";
		HttpClient client = new HttpClient();
		PostMethod method = new PostMethod(url);
		if(header != null)
			for(String key : header.keySet()){
				method.addRequestHeader(key, header.get(key));
			}
		if(postparams != null)
			for(String key : postparams.keySet()){
				method.addParameter(key, postparams.get(key));
			}
		int statusCode = client.executeMethod(method);
		if(statusCode != HttpStatus.SC_OK){
			return null;
		}
		String content = method.getResponseBodyAsString();
		String Content_Type = method.getResponseHeader("Content-Type").getValue();
		//获取解析的编码格式
		Charset charset = DeCharSetName(Content_Type, content);
		byte[] responseBody = content.getBytes(method.getResponseCharSet());
		//转码
		result = new String(responseBody, charset);

		return result;
	}
	
	/**
	 * 解析响应的html编码格式
	 * @param content_type
	 * @param content
	 * @return
	 */
	public static Charset DeCharSetName(String content_type, String content){
		Charset currentCharset = Charset.forName("UTF-8");
		String regexstr = "(?=text/html|text/xml|application/x-javascript).*?charset=([^\"]+)";
		Pattern p = Pattern.compile(regexstr, Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
		String contentType = content_type;
		if (contentType != null || !contentType.isEmpty()) {
			Matcher matchers = p.matcher(contentType);
			if (matchers.find()) {
				String charset = matchers.group(1).toUpperCase();
				try {
					currentCharset = Charset.forName(charset);
				} catch (Exception ex) { }
			}
		}

		String ascii = content;
		Matcher matchers1 = p.matcher(ascii);
		if (matchers1.find()) {
			String charset = matchers1.group(1).toUpperCase();
			try {
				currentCharset = Charset.forName(charset);
			} catch (Exception ex) { }
		}

		return currentCharset;
	}
	
}
