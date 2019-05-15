package com.southintel.zaokin.base.util;


import com.southintel.zaokin.base.constant.Constant;
import com.southintel.zaokin.base.entity.RequestParams;
import com.southintel.zaokin.base.entity.User;
import com.southintel.zaokin.base.entity.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

@Slf4j
public class RequestUtil {

    private static ThreadLocal<String> threadLocal=new ThreadLocal<>();


    /**
     * 获取真实用户ip地址
     * @return
     */
    public static String getIpAddr(HttpServletRequest request){
        String ip = request.getHeader("x-forwarded-for");
        if(StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if(StringUtils.isEmpty(ip)|| "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if(StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    /**
     * @Description:获取当前用户
     * @return
     */
    public static UserDto currentUser(){
        HttpServletRequest request = currentRequest();
        String token = getToken(request);
        String newToken = Constant.AUTH_TOKEN_PREFIX + token;
        UserDto user = JsonUtil.toObject(TokenUtil.get(newToken),UserDto.class);
        return user;
    }
    public static String currentMethod(){
        HttpServletRequest request = currentRequest();
        return request.getMethod();
    }

    public static HttpServletRequest currentRequest(){
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        HttpServletRequest request = sra.getRequest();
        return request;
    }

    public static HttpServletResponse currentResponse(){
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        HttpServletResponse response = sra.getResponse();
        return response;
    }

    private static RequestParams getRequestParams(HttpServletRequest request){
        String queryString = getQueryString(request);
        RequestParams requestParams = JsonUtil.toObject(queryString, RequestParams.class);
        return requestParams;
    }

    /**
     * 需要返回的值进行非空判断，以免抛出 NullPointException
     * @param request
     * @return
     */
    public static String getToken(HttpServletRequest request){
    	return request.getHeader("Authorization");
    }

    /**
     * 需要返回的值进行非空判断，以免抛出 NullPointException
     * @param request
     * @return
     */
    public static Object getParams(HttpServletRequest request){
        RequestParams requestParams = getRequestParams(request);
        return requestParams.getParams();
    }


    private static String getQueryString(HttpServletRequest request){
        BufferedReader reader=null;
        try {
            InputStream inputStream = request.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            reader=new BufferedReader(inputStreamReader);
            List<String> lines = IOUtils.readLines(reader);
            StringBuffer result=new StringBuffer();
            for(String line:lines){
                result.append(line);
            }
            reader.close();//关闭最外层输入流即可
            return result.toString();//结果字符串
        } catch (IOException e) {
            log.error("获取请求参数失败:",e);
        }finally {
            IOUtils.closeQuietly(reader);
        }
        return null;
    }
}
