package com.southintel.zaokin.base.service;


import com.southintel.zaokin.base.entity.RequestParams;
import com.southintel.zaokin.base.entity.ServerResponse;
import com.southintel.zaokin.base.entity.UserDto;
import com.southintel.zaokin.base.entity.RegisterData;

public interface UserService {

    //用户登录
    ServerResponse login(RequestParams< UserDto > params);
    //注册
    ServerResponse register(RegisterData registerData);
    //验证是否已注册
    ServerResponse isregister(String tel);
    //发送验证码
    ServerResponse sendSMSCode(String tel, int type);
}
