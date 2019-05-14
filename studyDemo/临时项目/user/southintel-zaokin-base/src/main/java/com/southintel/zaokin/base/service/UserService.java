package com.southintel.zaokin.base.service;


import com.southintel.zaokin.base.entity.*;

public interface UserService {

    //用户登录
    String login(UserDto userDto);

    ServerResponse register(RegisterData registerData);

    ServerResponse isregister(String tel);
}
