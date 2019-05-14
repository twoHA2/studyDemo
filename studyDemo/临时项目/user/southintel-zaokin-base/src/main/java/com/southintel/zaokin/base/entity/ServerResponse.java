package com.southintel.zaokin.base.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class ServerResponse implements Serializable {

    private static final long serialVersionUID = -4023079543873157755L;

    private int retcode;
    private String errmsg;
    private Object data;

    public ServerResponse(){}

    public ServerResponse(int code, String msg) {
        this.retcode = code;
        this.errmsg = msg;
    }

    public ServerResponse(int code, String msg, Object data) {
        this.retcode = code;
        this.errmsg = msg;
        this.data = data;
    }

    public static  ServerResponse success(){
        ServerResponse serverResponse = new ServerResponse(001,"success");
        return serverResponse;
    }

    public static  ServerResponse fail(){
        ServerResponse serverResponse = new ServerResponse(000,"fail");
        return serverResponse;
    }

    public static  ServerResponse successWithData(Object object){
        ServerResponse serverResponse = new ServerResponse(001,"success");
        serverResponse.setData(object);
        object = null;
        return serverResponse;
    }
    public static  ServerResponse failWithData(String msg,Object object){
        ServerResponse serverResponse = new ServerResponse(000,msg);
        serverResponse.setData(object);
        object = null;
        return serverResponse;
    }
}