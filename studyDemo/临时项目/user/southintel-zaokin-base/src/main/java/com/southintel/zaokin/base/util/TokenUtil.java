package com.southintel.zaokin.base.util;


import com.southintel.zaokin.base.cache.ICache;
import com.southintel.zaokin.base.constant.Constant;
import com.southintel.zaokin.base.entity.User;
import com.southintel.zaokin.base.entity.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class TokenUtil {

    private static ICache cache;

    /**
     * 校验token是否合法
     * @param token
     * @return
     */
    public static boolean isValid(String token){
        String userInfo = cache().get(token);
        if(StringUtils.isEmpty(userInfo)){
            return false;
        }
        /*refreshToken(token);*/
        return true;
    };

    /**
     *  根据用户信息生成一个返回给前台Token
     * @return
     */
    public static String obtainRefreshToken(UserDto user){
        String k =System.currentTimeMillis()+"";
        String key = Constant.AUTH_TOKEN_PREFIX +k ;
        cache().set(key, user);
        return k;
    }

    public static void setValue(String key, Object object ,long expire  ){
        if(expire<0){
            cache().set(key,object);
        }else{
            cache().set(key,object,expire);
        }
    }


    /**
     * 用户主动登出时 要删除token
     * @param token
     */
    public static void deleteToken(String token){
        cache().delete(token);
    }

    public static String get(String token){ return cache().get(token); }

    private static ICache cache(){
        if(cache == null){
            cache = ApplicationContextHolder.getBean(ICache.class);
        }
        return cache;
    }

}
