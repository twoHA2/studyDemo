package com.southintel.zaokin.base.service;


import com.southintel.zaokin.base.entity.RequestParams;
import com.southintel.zaokin.base.entity.ServerResponse;
import com.southintel.zaokin.base.entity.UserDto;
import com.southintel.zaokin.base.entity.RegisterData;

public interface UserService {

    //用户登录
    ServerResponse login(RequestParams< UserDto > params);

    ServerResponse register(RegisterData registerData);

    ServerResponse isregister(String tel);
}
