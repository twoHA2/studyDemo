package com.southintel.zaokin.base.service.impl;


import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.southintel.zaokin.base.constant.Constant;
import com.southintel.zaokin.base.dao.*;
import com.southintel.zaokin.base.entity.*;
import com.southintel.zaokin.base.enums.BaseResult;
import com.southintel.zaokin.base.enums.BaseResultEnum;
import com.southintel.zaokin.base.exception.BusinessException;
import com.southintel.zaokin.base.service.UserService;
import com.southintel.zaokin.base.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserDao userDao;
    @Value("${gatewayService.host}")
    private String HXUrl;

    @Override
    public String login(UserDto userDto) {
        if (userDto == null) throw new BusinessException(new BaseResult(BaseResultEnum.PARAMS_ERROR));
        HttpServletRequest request = RequestUtil.currentRequest();
        String token = RequestUtil.getToken(request);
        String refreshToken = "";
        String authToken = "";
        String userName = "";
        Assert.isNotBlank(userDto.getUserName());
        Assert.isNotBlank(userDto.getPassword());
        //验证用户名密码
        List< User > userList = userDao.getUserByName(userDto.getUserName());
        if (userList == null || userList.size() <= 0) {
            return JsonUtil.toJson(ServerResponse.failWithData(BaseResultEnum.NOTUSER_OR_PASSWORDERROR.getMsg(), null));
        }
        User user = userList.get(0);
        if (user.getUserStatus() == 1) {
            return JsonUtil.toJson(ServerResponse.failWithData(BaseResultEnum.USER_FORBIDDEN.getMsg(), null));
        }
        if (!PasswordUtil.encryptPassword(userDto.getPassword()).equals(user.getPassword())) {
            return JsonUtil.toJson(ServerResponse.failWithData(BaseResultEnum.NOTUSER_OR_PASSWORDERROR.getMsg(), null));
        }
        log.info("返回data数据：" + user.toString());
        return JsonUtil.toJson(ServerResponse.successWithData(user));
    }
    @Transactional
    @Override
    public ServerResponse register(RegisterData registerData) {
        if(registerData == null) throw new BusinessException(new BaseResult(BaseResultEnum.PARAMETER_ERROR));
        //生成唯一ID
        String idt = (Constant.ONLYID + UUID.randomUUID());
         registerData.setUser_name( idt);
        //将注册信息分发各张表中
        userDao.insertUser(registerData);//
        userDao.insertUserNaturePerson(registerData);
        userDao.insertCompanyName(registerData);
        userDao.insertZaokinUser(registerData);
        //注册环信
        JsonObject jso = new JsonObject();
        jso.addProperty("tel",registerData.getTel());
        String s = HttpUtils.doPost(HXUrl + "/policy/hx/register", jso.getAsString());
        if(s==null) throw new BusinessException(new BaseResult(BaseResultEnum.HXZH_ERROR));
        JsonObject result = new JsonParser().parse(s).getAsJsonObject();
        if(!result.isJsonNull() && result.get("retcode").getAsInt()== 0){
            JsonObject data = result.get("data").getAsJsonObject();
            HximUser hximUser = new HximUser();
            hximUser.setUser_name(idt);
            hximUser.setAccount(data.get("account").getAsString());
            hximUser.setPassword(data.get("password").getAsString());
            hximUser.setNickname(data.get("name").getAsString());
            int i = userDao.insertHximUser(hximUser);
        }else{
            throw new BusinessException(new BaseResult(BaseResultEnum.HXZH_ERROR));
        }
        //登陆

        return null;
    }

    @Override
    public ServerResponse isregister(String tel) {
        return null;
    }


}
