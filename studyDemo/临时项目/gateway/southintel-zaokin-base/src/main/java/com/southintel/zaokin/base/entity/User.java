package com.southintel.zaokin.base.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class User {

    //用户id
    private int id;

    //用户名
    private String userName;

    //密码
    private String password;

    //手机号
    private String tel;

    //密码盐
    private int salt;

    //创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    //用户状态  0启用 1禁用
    private int userStatus;
}