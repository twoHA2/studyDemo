package com.southintel.zaokin.base.controller;


import com.southintel.zaokin.base.entity.ServerResponse;
import com.southintel.zaokin.base.enums.BaseResultEnum;
import com.southintel.zaokin.base.util.HttpUtils;
import com.southintel.zaokin.base.util.JsonUtil;
import com.southintel.zaokin.base.util.RequestUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/")
public class BackendController {
    @Value("${userService.host}")
    private String userService;
    @Value("${zaokinService.host}")
    private String zaokinService;

    @RequestMapping("/**")
    public String policy(){
        HttpServletRequest request = RequestUtil.currentRequest();
        String url = request.getRequestURI();
        if(StringUtils.isEmpty(url))return JsonUtil.toJson(new ServerResponse(BaseResultEnum.URL_NOFIND.getCode(),BaseResultEnum.URL_NOFIND.getMsg()));
        if(url.startsWith("/user")){
            //转发user服务
            return doUserHandler(request);
        }else if(url.startsWith("/zaokin")){
            //转发到zaokin
            return doZaokinHandler(request);
        }else{
            return JsonUtil.toJson(new ServerResponse(BaseResultEnum.URL_NOFIND.getCode(),BaseResultEnum.URL_NOFIND.getMsg()));
        }
    }

    /**
     * 处理user请求
     * @param request
     * @return
     */
    public String  doUserHandler(HttpServletRequest request){
        return doHandler(request, userService);
    }
    /**
     * 处理zaokin请求
     * @param request
     * @return
     */
    public String  doZaokinHandler(HttpServletRequest request){
        return doHandler(request, zaokinService);
    }

    private String doHandler(HttpServletRequest request, String Service) {
        String url = request.getRequestURI();
        String data = RequestUtil.getBody(request);
        String params = request.getQueryString();
        String token = RequestUtil.getToken(request);
        if (HttpMethod.GET.matches(request.getMethod())) {
            if (!StringUtils.isEmpty(params)) {
                url = url + "?" + params;
            }
            return HttpUtils.doGet(Service + url, token);
        } else if (HttpMethod.POST.matches(request.getMethod()) || HttpMethod.PUT.matches(request.getMethod())) {
            return HttpUtils.doPost(Service + url, data, token);
        } else {
            return JsonUtil.toJson(new ServerResponse(BaseResultEnum.REQUEST_NOFIND.getCode(), BaseResultEnum.REQUEST_NOFIND.getMsg()));
        }
    }
}
