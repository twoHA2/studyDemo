package com.southintel.zaokin.base.service.impl;


import com.google.gson.JsonParser;
import com.google.gson.JsonObject;
import com.southintel.zaokin.base.constant.Constant;
import com.southintel.zaokin.base.dao.UserDao;
import com.southintel.zaokin.base.entity.*;
import com.southintel.zaokin.base.enums.BaseResult;
import com.southintel.zaokin.base.enums.BaseResultEnum;
import com.southintel.zaokin.base.enums.ResultEnum;
import com.southintel.zaokin.base.exception.BusinessException;
import com.southintel.zaokin.base.service.UserService;
import com.southintel.zaokin.base.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Service
public class UserServiceImpl implements UserService{

    @Autowired
    UserDao userDao;

    @Value("${userService.host}")
    private String userHost;


    @Override
    public ServerResponse login(RequestParams< UserDto > params) {
        HttpServletRequest request = RequestUtil.currentRequest();
        String token = RequestUtil.getToken(request);
        String refreshToken = "";
        String authToken = "";
        String userName = "";
        UserRtn userRtn = new UserRtn();
        //此时获取新的authtoken
        if (!StringUtils.isEmpty(token)){
            //拼接token
            String newRefreshToken = Constant.REFRESH_TOKEN_PREFIX + token;
            //验证token是否失效
            boolean valid = TokenUtil.isValid(newRefreshToken);// 校验token是否有效
            if (!valid) { // token 失效
                throw new BusinessException(new BaseResult(BaseResultEnum.TOKEN_ORERTIME));
            }
            User user = JsonUtil.toObject(TokenUtil.get(newRefreshToken),User.class);
            userName = user.getUserName();
            authToken = TokenUtil.obtainAuthToken(user);
            //返回信息
            userRtn.setUserName(userName);
            userRtn.setRefreshToken(token);
            userRtn.setAuthTokne(authToken);
        } else {
            String json = HttpUtils.doPost(userHost + "/user/login", JsonUtil.toJson(params));
            JsonObject result = new JsonParser().parse(json).getAsJsonObject();
            if(result.get("retcode").getAsInt()!=1){
                return  new ServerResponse(0,result.get("errmsg")!=null?result.get("errmsg").toString().substring(1,result.get("errmsg").toString().length()-1):"转发获取数据异常");
            }
            User user = JsonUtil.toObject(result.get("data").toString(),User.class);
            userName = user.getUserName();
            refreshToken = TokenUtil.obtainRefreshToken(user);
            authToken = TokenUtil.obtainAuthToken(user);
            //返回信息
            userRtn.setUserName(userName);
            userRtn.setRefreshToken(refreshToken);
            userRtn.setAuthTokne(authToken);
        }
        log.info("返回data数据：" + userRtn.toString());
        return ServerResponse.successWithData(userRtn);
    }

    @Override
    public ServerResponse register(RegisterData registerData) {
         Assert.isAllFieldNull(registerData);
        String json = HttpUtils.doPost(userHost + "/user/register", JsonUtil.toJson(registerData));
        if(json ==null)throw new BusinessException(new BaseResult(BaseResultEnum.INTERFACE_ERROR));
        return JsonUtil.toObject(json,ServerResponse.class);
    }

    @Override
    public ServerResponse isregister(String tel) {
        if(!PatternUtil.isMobile(tel)){
            throw new BusinessException(ResultEnum.TEL_ERROR.getCode(),ResultEnum.TEL_ERROR.getMsg());
        }
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("tel",tel);
        String json = HttpUtils.doPost(userHost + "/user/isregister",jsonObject.getAsString());
        if(json ==null)throw new BusinessException(new BaseResult(BaseResultEnum.INTERFACE_ERROR));
        return JsonUtil.toObject(json,ServerResponse.class);
    }

}
