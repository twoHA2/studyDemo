package com.southintel.zaokin.base.util;


import com.southintel.zaokin.base.constant.Constant;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

public class PasswordUtil {

    /**
     * 对业务模块的用户密码加密
     * @param password
     * @return
     */
    public static String encryptPassword(String password){
        String credentialsSalt =  Constant.SALT;

        String newPassword = new SimpleHash(
                Constant.ALGORITHM_NAME,
                password,
                ByteSource.Util.bytes(credentialsSalt),
                Constant.HASH_ITERATIONS).toHex();

        return newPassword;
    }


}
