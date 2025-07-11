package com.powernode.controller;

import com.powernode.domain.SysRole;
import com.powernode.model.Result;
import com.powernode.service.SysRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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
}
