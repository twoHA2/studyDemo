package com.southintel.zaokin.base.exception;

import com.southintel.zaokin.base.entity.ServerResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

/**
 * 捕捉因业务逻辑失败抛出的BusinessException，返回json
 * {
 *    "data": null,
 *   "errmsg": "xxx",
 *   "retcode": 0
 * }
 * 原springmvc抛出的自身抛出的异常被捕捉改变，
 *
 */
@Slf4j
@RestControllerAdvice
public class HandleException extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value = Exception.class)
    public ServerResponse ResultBean (HttpServletRequest request, HttpServletResponse response ,
                                      Exception e){
        ServerResponse serverResponse=null;
        Map map=new HashedMap();
        //仅处理业务Exception,返回给页面或其他系统
        if(e instanceof  BusinessException){
            map.put("path",request.getServletPath());
            map.put("date",new Date());
            map.put("details",e.toString());
            serverResponse= new ServerResponse(((BusinessException) e).getCode(),e.getMessage(),map);
            log.error(String.format("接口%s调用异常-->", request.getServletPath()) + e);
            e.printStackTrace();
        }else{
            map.put("path",request.getServletPath());
            map.put("date",new Date());
            map.put("details",e.toString());
            serverResponse= new ServerResponse(0,e.getMessage(),map);
            log.error(String.format("接口%s调用异常-->", request.getServletPath()) + e);
        }
        return serverResponse;
    }

    /**
     * 重写ResponseEntityExceptionHandler类的方法，
     * 对springmvc抛出的各种异常进行封装
     * @param ex
     * @param body
     * @param headers
     * @param status
     * @param request
     * @return
     */
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, @Nullable Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        if (HttpStatus.INTERNAL_SERVER_ERROR.equals(status)) {
            request.setAttribute("javax.servlet.error.exception", ex, 0);
        }
        Map map=new HashedMap();
        ServletWebRequest request1= (ServletWebRequest) request;
        map.put("path",request1.getRequest().getServletPath());
        map.put("date",new Date());
        map.put("details",ex.toString());
        body= new ServerResponse(0,"接口调用失败!",map);
        return new ResponseEntity(body, headers, status);
    }
}
