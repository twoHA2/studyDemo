package com.southintel.zaokin.base.exception;


import com.southintel.zaokin.base.enums.ResultEnum;

public class BusinessException extends RuntimeException {

    private int code;

    public BusinessException(int code, String msg){
        super(msg);
        this.code=code;
    }

    public BusinessException(ResultAdapter resultAdapter){
        super(resultAdapter.getMsg());
        this.code=resultAdapter.getCode();
    }
    public BusinessException(ResultEnum resultEnum){
        super(resultEnum.getMsg());
        this.code=resultEnum.getCode();
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
