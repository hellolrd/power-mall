package com.powernode.service;

import com.powernode.domain.SysMenu;
import com.powernode.domain.SysRole;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface SysRoleService extends IService<SysRole>{


    List<SysRole> querySysRoleList();

    Boolean saveSysRole(SysRole sysRole);


    SysRole querySysRoleInfoByRoleId(Long roleId);

    Boolean modifySysRole(SysRole sysRole);

    Boolean removeSysRolelistByIds(List<Long> roleIdlist);


}
