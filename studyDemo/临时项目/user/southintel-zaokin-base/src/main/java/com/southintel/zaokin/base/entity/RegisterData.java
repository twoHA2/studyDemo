package com.southintel.zaokin.base.entity;

import lombok.Data;

@Data
public class RegisterData {
    //用户类型
    private int type;

   //用户账户绑定手机号
    private String tel;

    //用户人名
    private String name;

    //所属公司名称
    private String  company;

    //所属部门
    private String department;

    //职位
    private String position;

    //头像地址
    private String avatar_url;

    //名片地址
    private String card_url;

    //公司简介
    private String brief_introduction;

    private String sms_code;

}
