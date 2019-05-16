package com.southintel.zaokin.base.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class Company {
    //id
    private int id;

    //公司名称
    private String company_name;

    //企业类型 国企 民营 外资
    private String type;

    //公司logo地址
    private String logo_url;

    //公司执照图片地址
    private String permit_url;

    //营业执照号码
    private String business_license;

    //创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    //用户状态 0:正常 1：禁用
    private int status;

}
