package com.powernode.service;

import com.powernode.domain.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface SysUserService extends IService<SysUser>{


    Integer saveSysUser(SysUser sysUser);

    SysUser querySysUserInfoByUserId(Long id);

    Integer modifySysUserInfo(SysUser sysUser);

    Boolean removeSysUsers(List<Long> userIds);
}
