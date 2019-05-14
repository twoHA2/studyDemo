package com.southintel.zaokin.base.constant;

public interface Constant {


    //长token的前缀
    String REFRESH_TOKEN_PREFIX = "refresh_token_";
    //长token的有效期(一个月)
    long REFRESH_TOKEN_EXPIRE = 60*60*24*7;
    //短token的前缀
    String AUTH_TOKEN_PREFIX = "auth_token_";
    //短token的有效期(一周)
    long AUTH_TOKEN_EXPIRE = 60*60*6;
    //获取新的短token的方法名
    String REFRESH_AUTH_TOKEN = "getAuthToken";



    //密码机密相关常量
    String ALGORITHM_NAME = "md5";
    int HASH_ITERATIONS = 2;
    String SALT = "gjakg;jdfdasjk%^$32";

    //对开放的方法不进行拦截
    String OPEN_PERMISSION_METHOD = "login";
    String LOGOUT_METHOD = "logout";

}
