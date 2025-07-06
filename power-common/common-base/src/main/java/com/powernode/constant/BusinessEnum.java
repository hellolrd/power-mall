package com.powernode.constant;

public enum BusinessEnum {
    OPERATION_FAIL(-1,"操作失败"),
    SERVER_INNER_ERROR(9999,"服务内部异常"),
    UN_AUTHORIZATION(401,"未授权"),
    ACCESS_DENY_FAIL(403,"权限不足，请联系管理员");
    private Integer code; // 状态码
    private String msg; // 提示信息




    BusinessEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }


    public String getMsg() {
        return msg;
    }


}
