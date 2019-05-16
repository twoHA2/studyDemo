package com.southintel.zaokin.base.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class ZaokinUser {
    //系统用户名
    private String userName;

    //招金会用户类型 0:资产方 1：资金方
    private int userType ;

    //招金会用户头像地址
    private String avatarUrl;

    //名片图片地址
    private String cardUrl;

    //招金汇用户昵称
    private String nickName;

    //邮箱
    private String email;

    //招金汇用户密码
    private String password;

    //用户状态 0:已激活  1：禁用
    private int status;

    //实名认证结果  0：未通过  1：通过
    private int attestationPersion;

    //企业认证结果  0：未通过 1：通过
    private int attestationCompany;

}
