package com.southintel.zaokin.base.controller;

import com.southintel.zaokin.base.entity.*;
import com.southintel.zaokin.base.service.UserService;
import com.southintel.zaokin.base.util.BeanUtil;
import com.southintel.zaokin.base.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

//    @RequestMapping("/login")
//    public String login(@RequestBody  RequestParams<UserDto> params){
//        UserDto userDto = params.getParams();
//        return userService.login(userDto);
//    }
    @RequestMapping(value = "/register")
    public ServerResponse register(@RequestBody RegisterData registerData){
        return userService.register(registerData);
    }
    @RequestMapping(value = "/isregister",method = RequestMethod.POST)
    public ServerResponse isregister(HttpServletRequest request){
        return userService.isregister(request.getParameter("tel"));
    }
}
