package com.powernode.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.powernode.constant.ManagerConstants;
import com.powernode.domain.SysRoleMenu;
import com.powernode.mapper.SysRoleMenuMapper;
import com.powernode.service.SysRoleMenuService;
import com.powernode.util.AuthUtils;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.powernode.mapper.SysRoleMapper;
import com.powernode.domain.SysRole;
import com.powernode.service.SysRoleService;
import org.springframework.transaction.annotation.Transactional;

@Service
@CacheConfig(cacheNames = "com.powernode.service.impl.SysRoleServiceImpl")
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService{

    @Autowired
    private SysRoleMapper sysRoleMapper;
    @Autowired
    private SysRoleMenuService sysRoleMenuService;

    @Autowired
    private SysRoleMenuMapper sysRoleMenuMapper;
    @Override
    @Cacheable(key = ManagerConstants.SYS_ALL_ROLE_KEY)
    public List<SysRole> querySysRoleList() {
       return sysRoleMapper.selectList(new LambdaQueryWrapper<SysRole>().orderByDesc(SysRole::getCreateTime));

    }

    @Override
    @Transactional
    @CacheEvict(key= ManagerConstants.SYS_ALL_ROLE_KEY)
    public Boolean saveSysRole(SysRole sysRole) {
        sysRole.setCreateTime(new Date());
        sysRole.setCreateUserId(AuthUtils.getLoginUserId());
        int i = sysRoleMapper.insert(sysRole);
        if(i > 0) {
            // 清除角色缓存
            Long roleId = sysRole.getRoleId();
            List<SysRoleMenu> sysRoleMenuList = new ArrayList<>();
            List<Long> menuIdlist = sysRole.getMenuIdList();
            if(menuIdlist != null && !menuIdlist.isEmpty()) {
                menuIdlist.forEach(menuId -> {
                    SysRoleMenu sysRoleMenu = new SysRoleMenu();
                    sysRoleMenu.setRoleId(roleId);
                    sysRoleMenu.setMenuId(menuId);
                    sysRoleMenuList.add(sysRoleMenu);
                });
                // 批量插入角色菜单关系
                sysRoleMenuService.saveBatch(sysRoleMenuList);
            }
        }
        return i>0;
    }

    @Override
    public SysRole querySysRoleInfoByRoleId(Long roleId) {
        SysRole sysRole = sysRoleMapper.selectById(roleId);
        if(sysRole != null) {
            // 查询角色对应的菜单ID列表
            List<Long> menuIdList = sysRoleMenuMapper.selectList(new LambdaQueryWrapper<SysRoleMenu>()
                    .eq(SysRoleMenu::getRoleId, roleId)
            ).stream()
                    .map(SysRoleMenu::getMenuId).collect(Collectors.toList());
            sysRole.setMenuIdList(menuIdList);
        }
        return sysRole;
    }
}
