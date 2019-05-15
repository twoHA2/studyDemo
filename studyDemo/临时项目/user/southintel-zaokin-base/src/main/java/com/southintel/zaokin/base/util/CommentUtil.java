package com.southintel.zaokin.base.util;

public class CommentUtil {
    /**
     * 生成6位随机数验证码
     * @return
     */
    public static String vcode(){
        String vcode = "";
        for (int i = 0; i < 6; i++) {
            vcode = vcode + (int)(Math.random() * 9);
        }
        return vcode;
    }
}
