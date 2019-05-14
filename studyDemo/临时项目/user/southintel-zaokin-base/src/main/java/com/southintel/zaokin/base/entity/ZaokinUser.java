package com.southintel.zaokin.base.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class ZaokinUser {
    //id
    private int id;
    //系统用户名
    private String user_name;

    //招金会用户类型 0:资产方 1：资金方
    private int type ;

    //招金会用户头像地址
    private String avatar_url;

    //名片图片地址
    private String card_url;

    //招金汇用户昵称
    private String nickname;

    //邮箱
    private String email;

    //招金汇用户密码
    private String password;

    //创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    //用户状态 0:已激活  1：禁用
    private int status;

    //实名认证结果  0：未通过  1：通过
    private int attestation_persion;

    //企业认证结果  0：未通过 1：通过
    private int attestation_company;

}
