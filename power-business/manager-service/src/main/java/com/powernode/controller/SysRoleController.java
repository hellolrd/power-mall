package com.powernode.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.powernode.domain.SysMenu;
import com.powernode.domain.SysRole;
import com.powernode.model.Result;
import com.powernode.service.SysRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

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
         @ApiOperation(value = "新增角色")
         @PostMapping
         @PreAuthorize("hasAuthority('sys:role:save')")
         public Result<String> saveSysRole(@RequestBody SysRole sysRole) {
               Boolean res=sysRoleService.saveSysRole(sysRole);
             return Result.success(null);
         }


         @ApiOperation(value = "根据标识查询角色详情")
         @GetMapping("info/{roleId}")
         @PreAuthorize("hasAuthority('sys:role:info')")
         public Result<SysRole> loadSysRoleInfo(@PathVariable Long roleId) {
               SysRole sysRole = sysRoleService.querySysRoleInfoByRoleId(roleId);
             return Result.success(sysRole);
         }

         @ApiOperation(value = "修改角色信息")
         @PutMapping
         @PreAuthorize("hasAuthority('sys:role:update')")
         public Result<String> modifySysRole(@RequestBody SysRole sysRole) {
              Boolean updated=sysRoleService.modifySysRole(sysRole);
             return Result.handle(updated);
         }

         @ApiOperation(value = "删除/批量角色信息")
         @DeleteMapping
         @PreAuthorize("hasAuthority('sys:role:delete')")
         public Result<String> removeSysRole(@RequestBody List<Long> roleIdlist) {
             Boolean res=sysRoleService.removeSysRolelistByIds(roleIdlist);
             return Result.handle(res);
         }



}
