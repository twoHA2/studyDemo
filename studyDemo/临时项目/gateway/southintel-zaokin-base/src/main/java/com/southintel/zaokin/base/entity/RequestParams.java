package com.southintel.zaokin.base.entity;

import lombok.Data;

import javax.validation.Valid;
import java.io.Serializable;

/**
 * @Auther: xumeng
 * @Date: 2018/5/9/ 11:06
 * @Description:
 */
@Data
public class RequestParams<T> implements Serializable{

    private static final long serialVersionUID = 7261975058597294376L;

    //业务参数
    @Valid
    private T params;

    //偏移值
    private int startIndex;

    //页面大小
    private int pageSize;

    //开始日期
    private String beginDate;

    //结束日期
    private String endDate;

    //发文部门

    //行业分类

    //公文类型

    //发文日期

    private int userId;
}
