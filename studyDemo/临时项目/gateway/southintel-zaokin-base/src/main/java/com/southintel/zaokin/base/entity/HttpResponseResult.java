package com.southintel.zaokin.base.entity;

import lombok.Data;

@Data
public class HttpResponseResult {

    private int code;

    private String msg;

    private String data;
}
