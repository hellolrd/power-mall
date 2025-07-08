package com.powernode.constant;

public interface AuthConstant {
    String AUTHORIZATION="Authorization";

    String BEARER="bearer ";

    String LOGIN_TOKEN_PREFIX = "login_token:";

    String LOGIN_URL="/doLogin";
    String LOGOUT_URL="/doLogout";

    String LOGIN_TYPE = "loginType";
    String SYS_USER_LOGIN="sysUserLogin";

    String MEMBER_USER_LOGIN="memberUserLogin";

    Long token_time=14400L; //4小时有效期

}
