package com.southintel.zaokin.base.constant;

public interface Constant {

    //token的有效期(一个月)
    long REFRESH_TOKEN_EXPIRE = 60*60*24*30;
    //token的前缀
    String AUTH_TOKEN_PREFIX = "auth_token_";

    //注册token存储前缀
    String REGISTER_SMSCODE_PREFIX = "register_";
    //登陆token存储前缀
    String LOGIN_SMSCODE_PREFIX = "login_";
    //验证码过期时间
    long SMSCODE_EXPIRE = 60*2;

    //密码机密相关常量
    String ALGORITHM_NAME = "md5";
    int HASH_ITERATIONS = 2;
    String SALT = "gjakg;jdfdasjk%^$32";
    String ONLYID = "ZAOKIN" ;


    //对开放的方法不进行拦截
    String OPEN_PERMISSION_METHOD = "login";
    String LOGOUT_METHOD = "logout";


    //认证类型
    String PERSON_TYPE = "person";
    String COMPANY_TYPE = "company";

}
