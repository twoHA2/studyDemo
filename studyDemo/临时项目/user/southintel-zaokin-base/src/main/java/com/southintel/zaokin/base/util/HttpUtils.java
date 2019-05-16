package com.southintel.zaokin.base.util;


import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.southintel.zaokin.base.enums.BaseResult;
import com.southintel.zaokin.base.enums.BaseResultEnum;
import com.southintel.zaokin.base.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

@Slf4j
public class HttpUtils {

	public static String doPost(String url,String data){
		return doPost(url,data,null);
	}

	public static String doPost(String url, String data, String authorization) {
		log.info("开始转发请求,url:" + url + ",data:" + data);
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(url);
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(30000).setConnectTimeout(30000)
				.setConnectionRequestTimeout(30000).build();
		httpPost.setConfig(requestConfig);


		String context = StringUtils.EMPTY;
		if (!StringUtils.isEmpty(data)) {
			StringEntity body = new StringEntity(data, "utf-8");
			httpPost.setEntity(body);
			// 设置回调接口接收的消息头
			httpPost.addHeader("Content-Type", "application/json");
		}
		if (!StringUtils.isEmpty(authorization)){
			httpPost.addHeader("Authorization",authorization);
		}

		CloseableHttpResponse response = null;
		try {
			response = httpClient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			context = EntityUtils.toString(entity, "utf-8");
			JsonObject asJsonObject = new JsonParser().parse(context).getAsJsonObject();
			if (response.getStatusLine().getStatusCode() != 200) {
				if(asJsonObject != null){
					String message = asJsonObject.get("errmsg")!=null?asJsonObject.get("errmsg").toString():"";
					log.error("转发获取数据异常,context:" + context);
					throw new BusinessException(response.getStatusLine().getStatusCode(),!StringUtils.isEmpty(message) ?message.substring(1,message.length()-1):"转发获取数据异常");
				}else{
					throw new BusinessException(response.getStatusLine().getStatusCode(),"转发获取数据异常");
				}
			}
		} catch (Exception e) {
			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			}
			e.printStackTrace();
			throw new RuntimeException(e.getMessage()==null?"转发获取数据异常":e.getMessage(), e);
		} finally {
			try {
				response.close();
				httpPost.abort();
				httpClient.close();
			} catch (Exception e) {
				e.getStackTrace();
			}
		}
		log.info("收到返回数据context:" + context);
		return context;
	}

	public static String doGet(String url,String authorization) {
		CloseableHttpClient httpClient = HttpClients.createDefault();


		HttpGet httpget = new HttpGet(url);
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(30000).setConnectTimeout(30000)
				.setConnectionRequestTimeout(30000).build();
		httpget.setConfig(requestConfig);


		String context = StringUtils.EMPTY;
		if (!StringUtils.isEmpty(authorization)){
			httpget.addHeader("Authorization",authorization);
		}
		CloseableHttpResponse response = null;
		try {
			response = httpClient.execute(httpget);
			HttpEntity entity = response.getEntity();
			context = EntityUtils.toString(entity);
			if (response.getStatusLine().getStatusCode() != 200) {
				log.error("转发获取数据异常,context:" + context);
				throw new BusinessException(new BaseResult(BaseResultEnum.INTERFACE_ERROR));
			}
		} catch (Exception e) {
			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			}
			throw new RuntimeException("转发获取数据异常", e);
		} finally {
			try {
				response.close();
				httpget.abort();
				httpClient.close();
			} catch (Exception e) {
				e.getStackTrace();
			}
		}
		return context;
	}

	public static String unicodetoString(String unicode){
		if(unicode==null||"".equals(unicode)){
			return null;
		}
		StringBuilder sb = new StringBuilder();
		int i = -1;
		int pos = 0;
		while((i=unicode.indexOf("\\u", pos)) != -1){
			sb.append(unicode.substring(pos, i));
			if(i+5 < unicode.length()){
				pos = i+6;
				sb.append((char)Integer.parseInt(unicode.substring(i+2, i+6), 16));
			}
		}
		return sb.toString();
	}

	public static String stringtoUnicode(String string) {
		if(string==null||"".equals(string)){
			return null;
		}		StringBuffer unicode = new StringBuffer();		for (int i = 0; i < string.length(); i++) {			char c = string.charAt(i);			unicode.append("\\u" + Integer.toHexString(c));		}		return unicode.toString();	}

}
