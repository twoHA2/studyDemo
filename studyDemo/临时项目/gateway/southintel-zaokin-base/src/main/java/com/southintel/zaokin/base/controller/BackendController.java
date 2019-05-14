package com.southintel.zaokin.base.controller;


import com.google.gson.JsonObject;
import com.southintel.zaokin.base.entity.ServerResponse;
import com.southintel.zaokin.base.enums.ResultEnum;
import com.southintel.zaokin.base.util.HttpUtils;
import com.southintel.zaokin.base.util.JsonUtil;
import com.southintel.zaokin.base.util.RequestUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/policy")
public class BackendController {


    @Value("${backendService.authorization}")
    private String authorization;

    @Value("${backendService.host}")
    private String backendHost;

    @RequestMapping("/**")
    public String policy(){
        HttpServletRequest request = RequestUtil.currentRequest();
        String url = request.getRequestURI();
        String data = RequestUtil.getBody(request);
        String params = request.getQueryString();
        if (HttpMethod.GET.matches(request.getMethod())){
            if (!StringUtils.isEmpty(params)){
                url = url + "?" + params;
            }
            return HttpUtils.doGet(backendHost + url,null);
        } else if (HttpMethod.POST.matches(request.getMethod())){
            return HttpUtils.doPost(backendHost + url,data,null);
        } else {
            return JsonUtil.toJson(new ServerResponse(ResultEnum.REQUEST_METHOD_ERROR));
        }
    }


}
