package com.southintel.zaokin.base.entity;

import lombok.Data;

@Data
public class Project {
    private int id; // id
    private String project_name; //项目名
    private String project_introduction; //项目简介
    private String enterprise_nature; //企业性质
    private int financing_period;//融资期限
    private int financing_amount; //融资金额
    private String financing_mode;//融资方式
    private int financing_cost;//融资成本
    private String credit_mode;//增信方式
    private String subject_rating;//主体评级
    private String tab;//标签
    private String due_date;//截至日期
    private String company_id;//项目管理公司
    private String create_time;//创建时间

    private String user_name;

}
