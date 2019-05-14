package com.southintel.zaokin.base.entity;

import com.southintel.zaokin.base.enums.ResultEnum;

import java.io.Serializable;

public class ServerResponse implements Serializable {
    private int retcode;
    private String errmsg;
    private Object data;

    public int getRetcode() {
        return retcode;
    }

    public void setRetcode(int retcode) {
        this.retcode = retcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public ServerResponse(){}
    public ServerResponse(ResultEnum resultEnum){
        this.retcode = resultEnum.getCode();
        this.errmsg = resultEnum.getMsg();
    }

    public ServerResponse(int code, String msg) {
        this.retcode = code;
        this.errmsg = msg;
    }
    public ServerResponse(int code, String msg, Object data) {
        this.retcode = code;
        this.errmsg = msg;
        this.data = data;
    }
    public static ServerResponse success(){
        ServerResponse serverResponse = new ServerResponse(001,"success");
        return serverResponse;
    }

    public static ServerResponse fail(){
        ServerResponse serverResponse = new ServerResponse(000,"fail");
        return serverResponse;
    }

    public static ServerResponse successWithData(Object object){
        ServerResponse serverResponse = new ServerResponse(001,"success");
        serverResponse.setData(object);
        object = null;
        return serverResponse;
    }
}
