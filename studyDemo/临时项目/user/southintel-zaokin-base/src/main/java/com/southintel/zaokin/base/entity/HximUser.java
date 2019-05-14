package com.southintel.zaokin.base.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class HximUser {

    //id
    private int id;

    //系统用户名
    private String user_name;

    //用户IM账号
    private String account;

    //用户IM账号类型
    private String accoutn_type;

    //登陆密码
    private String password;

    //用户IM昵称
    private String nickname;

    //创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    //用户状态
    private int status;

}
