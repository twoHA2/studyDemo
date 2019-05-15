package com.southintel.zaokin.base.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class UserDto {
    private String person_name;//人名
    private int type ;//用户类型
    private String avatar_url;//用户头像地址
    private String company;//公司
    private String department;//部门
    private String position;//职位
    private String tel ; // 电话
    private int status;//认证状态
    private String user_name ;//系统用户名
    private String brief_introduction;//公司简介
}
