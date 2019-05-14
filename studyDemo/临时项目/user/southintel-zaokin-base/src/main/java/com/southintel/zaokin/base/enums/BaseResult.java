package com.southintel.zaokin.base.enums;

import com.southintel.zaokin.base.exception.ResultAdapter;

public class BaseResult implements ResultAdapter {

    private BaseResultEnum baseResultEnum;

    public BaseResult(BaseResultEnum baseResultEnum){
        this.baseResultEnum = baseResultEnum;
    }

    @Override
    public int getCode() {
        return baseResultEnum.getCode();
    }

    @Override
    public String getMsg() {
        return baseResultEnum.getMsg();
    }
}
