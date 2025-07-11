package com.powernode.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.powernode.domain.SysUser;
import com.powernode.model.Result;
import com.powernode.service.SysUserService;
import com.powernode.util.AuthUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.pattern.PathPattern;

import java.util.List;

@Api(tags = "系统管理员接口管理")
@RequestMapping("sys/user")
@RestController
public class SysUserController {
    @Autowired
    private SysUserService sysUserService;
    @ApiOperation(value = "查询用户信息")
    @GetMapping("info")
    public Result<SysUser> loadSysUserInfo() {
        Long userId=AuthUtils.getLoginUserId();
        SysUser sysUser = sysUserService.getById(userId);
        return Result.success(sysUser);
    }


        @ApiOperation(value = "多条件分页查询管理员")
        @GetMapping("page")
        @PreAuthorize("hasAuthority('sys:user:page')")
        public Result<Page<SysUser>> loadSysUserPage(@RequestParam Long current,@RequestParam Long size,@RequestParam(required = false) String username){
           Page<SysUser> page = new Page<>(current, size);
        page=sysUserService.page(page,new LambdaQueryWrapper<SysUser>()
                .like(username != null && !username.isEmpty(), SysUser::getUsername, username)
                        .orderByDesc(SysUser::getCreateTime)
                );
        return Result.success(page);
    }

    @ApiOperation(value = "新增管理员")
    @PostMapping
    @PreAuthorize("hasAuthority('sys:user:save')")
    public Result<String> saveSysUser(@RequestBody SysUser sysUser) {
        Integer count=sysUserService.saveSysUser(sysUser);
        return Result.handle(count>0);
    }

    @ApiOperation(value = "根据标识查询管理员信息")
    @GetMapping("info/{id}")
    public Result<SysUser> loadSysUserInfo(@PathVariable("id") Long id) {
        SysUser sysUser = sysUserService.querySysUserInfoByUserId(id);

        return Result.success(sysUser);
    }

      @ApiOperation(value = "修改管理员信息")
      @PutMapping
      @PreAuthorize("hasAuthority('sys:user:update')")
      public Result<String> modifySysUserInfo(@RequestBody SysUser sysUser){
        Integer count=sysUserService.modifySysUserInfo(sysUser);
          return Result.handle(count > 0);
      }

      @ApiOperation(value = "删除单个/批量")
      @DeleteMapping("{userIds}")
      @PreAuthorize("hasAuthority('sys:user:delete')")
      public Result<String> removeSysUsers(@PathVariable("userIds") List<Long> userIds) {
           Boolean removed= sysUserService.removeSysUsers(userIds);
          return Result.success(null);
      }
   }