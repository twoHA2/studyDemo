package com.southintel.zaokin.base.entity;

import lombok.Data;

@Data
public class CollectionReq {
    private int startIndex;
    private int pageSize;
    private String beginDate;
    private String endDate;

    private String user_name;
}
