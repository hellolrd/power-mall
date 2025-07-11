package com.powernode.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.powernode.domain.SysRole;
import com.powernode.model.Result;
import com.powernode.service.SysRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api("系统角色管理接口")
@RequestMapping("sys/role")
@RestController
public class SysRoleController {
    @Autowired
    private SysRoleService sysRoleService;

  @ApiOperation(value = "查询角色列表")
  @GetMapping("list")
  @PreAuthorize("hasAuthority('sys:role:list')")
  public Result<List> loadUserInfo() {
     List<SysRole> list=sysRoleService.querySysRoleList();
      return Result.success(list);
  }
         @ApiOperation(value = "多条件分页查询角色列表")
         @GetMapping("page")
         @PreAuthorize("hasAuthority('sys:role:page')")
         public Result<Page<SysRole>> loadSysRolePage(@RequestParam Long current,
                                                      @RequestParam Long size,
                                                      @RequestParam(required = false) String roleName) {
             Page<SysRole> page = new Page<>(current, size);
             page=sysRoleService.page(page,
                     new LambdaQueryWrapper<SysRole>()
                             .like(StringUtils.hasText(roleName), SysRole::getRoleName, roleName)
                             .orderByDesc(SysRole::getCreateTime)
             );


             return Result.success(page);
         }
}
