package com.southintel.zaokin.base.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class UserNaturePerson {
    //id
    private int id;

    //系统用户名
    private String user_name;

    //用户人名
    private String pserson_name;

    //用户性别
    private String gender;

    //用户出生年月日
    private String birth_date;

    //用户证件类型
    private String document_type;

    //用户证件号码
    private String credential_no;

    //身份证正面
    private String front_url;

    //身份证反面
    private String side_url;

    //用户所属公司名称
    private String company;

    //城市
    private String city;

    //地区
    private String area;

    //用户所谓部门
    private String department;

    //用户担任职位
    private String position;

    //创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    //用户状态 0：正常   1：禁用
    private int status;
}
