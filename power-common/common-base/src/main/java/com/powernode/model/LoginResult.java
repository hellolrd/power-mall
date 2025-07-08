package com.powernode.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "登录结果对象")
public class LoginResult {
    @ApiModelProperty(value = "访问令牌")
    private String accessToken; // 访问令牌
    @ApiModelProperty(value = "过期时间")
    private Long expiresIn; // 过期时间，单位为秒
}
