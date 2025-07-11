package com.powernode.vo;

import com.powernode.domain.SysMenu;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@ApiModel(description = "菜单和操作信息的组合类")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MenuAndAuth {

    @ApiModelProperty(value = "菜单权限集合")
    private Set<SysMenu> menuList; // 菜单权限集合，JSON格式的字符串
    @ApiModelProperty(value = "操作权限集合")
    private Set<String> authorities; // 操作权限集合，JSON格式的字符串
}
