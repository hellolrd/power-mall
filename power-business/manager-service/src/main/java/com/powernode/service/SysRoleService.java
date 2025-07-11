package com.powernode.service;

import com.powernode.domain.SysRole;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface SysRoleService extends IService<SysRole>{


    List<SysRole> querySysRoleList();
}
