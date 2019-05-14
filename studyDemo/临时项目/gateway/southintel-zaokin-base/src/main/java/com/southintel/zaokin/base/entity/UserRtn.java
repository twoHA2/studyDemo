package com.southintel.zaokin.base.entity;

import lombok.Data;

@Data
public class UserRtn {

    //用户名
    private String userName;

    //长token
    private String refreshToken;

    //短token
    private String authTokne;

}
