package com.powernode.controller;

import com.powernode.domain.SysMenu;
import com.powernode.model.Result;
import com.powernode.model.SecurityUser;
import com.powernode.service.SysMenuService;
import com.powernode.util.AuthUtils;
import com.powernode.vo.MenuAndAuth;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@Api(tags = "系统权限接口管理")
@RequestMapping("sys/menu")
@RestController
public class SysMenuController {
    @Autowired
    private SysMenuService sysMenuService;
    
    @ApiOperation("查询用户的菜单权限和操作权限")
    @GetMapping("nav")
    public Result<MenuAndAuth> loadUserMenuAndAuth() {
       Long loginUserId = AuthUtils.getLoginUserId();
       Set<String> perms = AuthUtils.getLoginUserPerms();
       Set<SysMenu> menus= sysMenuService.queryUserMenuListByUserId(loginUserId);
       return Result.success(MenuAndAuth.builder()
               .menuList(menus)
               .authorities(perms)
               .build());

    }
}
