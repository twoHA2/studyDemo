package com.southintel.zaokin.base.controller;

import com.southintel.zaokin.base.entity.RequestParams;
import com.southintel.zaokin.base.entity.ServerResponse;
import com.southintel.zaokin.base.entity.UserDto;
import com.southintel.zaokin.base.entity.RegisterData;
import com.southintel.zaokin.base.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @RequestMapping(value = "/register",method = RequestMethod.POST)
    public ServerResponse register(@RequestBody RegisterData registerData) {
        return userService.register(registerData);
    }

    @RequestMapping("/login")
    public ServerResponse login(@RequestBody RequestParams< UserDto > params){
        return userService.login(params);
    }

    @RequestMapping(value = "/isregister",method = RequestMethod.POST)
    public ServerResponse isregister(HttpServletRequest request){
        return userService.isregister(request.getParameter("tel"));
    }
}
