package com.southintel.zaokin.base.enums;

/**
 * 0~100
 */
public enum BaseResultEnum {

    TO_JSON_STRING_ERROR(002,"对象转json失败"),
    JSON_TO_OBJECT_ERROR(003,"json转对象失败"),
    OBTAIN_TOKEN_ERROR(007,"获取Token失败"),
    OBTAIN_SQLSESSION_ERROR(011,"操作数据库失败"),
    OBTAIN_USER_ERROR(013,"获取用户异常"),
    PASSWORD_ERROR(014,"密码错误"),
    USER_FORBIDDEN(015,"用户被禁用"),
    PARAMS_ERROR(19,"参数异常"),
    NOT_AUTHORITY(20,"没有权限"),
    TOKEN_ORERTIME(21,"无效的Token"),
    VERIFCODE_ERROR(22,"验证码不正确"),
	VERIFCODE_EXPIRE(24,"验证码已失效"),
	INVALID_CUSTOMER(26,"该用户不存在"),
	INVALID_IMG_ID(28,"无效的图片"),
    COPY_OBJECT_ERROR(29,"转换对象失败"),
    NOTUSER_OR_PASSWORDERROR(100,"用户名/密码不正确"),
    PARAMETER_ERROR(99, "参数错误或为空"),
    INTERFACE_ERROR(0,"接口异常"),
    HXZH_ERROR(30,"hx账号注册失败!"),
    VERIFCODE_CREATE_ERROR(31,"验证码发送失败");
    int code;
    String msg;

    BaseResultEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
