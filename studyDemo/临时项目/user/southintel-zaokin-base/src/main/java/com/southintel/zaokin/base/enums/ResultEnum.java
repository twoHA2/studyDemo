package com.southintel.zaokin.base.enums;

/**
 * 100~300
 */
public enum ResultEnum {
    UNKNOWN_ACCOUNT_ERROR(100,"用户名或密码错误"),
    INCORRECT_CREDENTIALS_ERROR(101,"用户名或密码错误"),
    LOCKED_ACCOUNT_ERROR(102,"账号被锁定禁止登陆"),
    EXCESSIVE_ATTEMPTS_ERROR(103,"连续登陆次数超过五次，请半小时之后再试"),
    AUTHENTICATION_ERROR(104,"用户名或密码错误"),
    FILE_UPLOAD_FAILURE(105,"文件上传失败"),
    FILE_DOWNLOAD_FAILURE(106,"文件读取失败"),
    USER_NOT_EXIST(107, "用户不存在"),
    OBJECT_ERROR(108,"传入对象错误"),
    REPOSITORY_NOT_EXIST(109,"库不存在此条某个数据"),
    INSERT_FAIL(110, "新增失败"),
    UPDATE_FAIL(111, "更新失败"),
    PARAMETER_ERROR(112, "参数错误"),
    DELETE_FAIL(113, "删除失败"),
    OLD_PASSWORD_ERROR(114, "旧密码错误"),
    NEW_PASSWORD_NOTNULL(114, "新密码不能为空"),
    COUNT_OR_PHONE_EXIST(114, "登录账号或者电话已被使用"),
    Trade_NOT_EXIST(115, "贸易商不存在"),
    CUSTOMER_NOT_EXIST(116, "客户不存在"),
    MESSAGE_ERROR(117, "短信下发异常"),
    REPOSITORY_HAS_EXIST(120,"该对象已经存在"),
    TEL_ERROR(118,"手机号错误"),
    PARAMS_ERROR(119,"参数不能为空"),
    BUSSESS_ERROR(121,"营业执照不能为空"),
    USERNAME_ERROR(122,"企业名称不能为空"),
    PASSWORD_ERROR(123,"密码不能为空"),
    ADDRESS_ERROR(125,"地址不能为空"),
    EMIAL_ERROR(126,"邮箱错误"),
    MATTER_ERROR(127,"类别不能为空"),
    Not_AGREE(128,"两次输入密码不一致"),
    FAIL(129, "系统繁忙"),
    IDCARD_ERROR(130,"身份证号不合法"),
    LIST_IS_NULL(131,"集合为空"),
    LIST_NOT_NULL(132,"集合不为空"),
    VIDEO_TYPE_ERR(133,"视频类型错误");
    int code;
    String msg;

    ResultEnum(int code, String msg) {
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
