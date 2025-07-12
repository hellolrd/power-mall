package com.powernode.service.impl;

import com.powernode.constant.ManagerConstants;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.powernode.domain.SysMenu;
import com.powernode.mapper.SysMenuMapper;
import com.powernode.service.SysMenuService;
@Service
@CacheConfig(cacheNames = "com.powernode.service.impl.SysMenuServiceImpl")
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService{

    @Autowired
    private SysMenuMapper sysMenuMapper;

    @Override
    @Cacheable(key = "#loginUserId")
    public Set<SysMenu> queryUserMenuListByUserId(Long loginUserId) {
       Set<SysMenu> menus=sysMenuMapper.selectUserMenuListByUserId(loginUserId);
       return transformTree(menus,0L);
    }

    private Set<SysMenu> transformTree(Set<SysMenu> menus, long pid) {

//      Set<SysMenu> roots= menus.stream()
//                .filter(m-> m.getParentId() .equals(pid))
//                .collect(Collectors.toSet());
//
//      roots.forEach(root -> {
//            Set<SysMenu> child = menus.stream()
//                    .filter(m -> m.getParentId().equals(root.getMenuId()))
//                    .collect(Collectors.toSet());
//            root.setList(child);
//        });
        Set<SysMenu> roots = menus.stream()
                .filter(m -> m.getParentId().equals(pid))
                .collect(Collectors.toSet());

        roots.forEach(r -> r.setList(transformTree(menus, r.getMenuId())));


        return roots;
    }


    @Override
    @Cacheable(key= ManagerConstants.SYS_ALL_MENU_KEY)
    public List<SysMenu> queryAllSysMenuList() {
        return sysMenuMapper.selectList(null);
    }

    @Override
    @CacheEvict(key = ManagerConstants.SYS_ALL_MENU_KEY)
    public Boolean saveSysMenu(SysMenu sysMenu) {
        return sysMenuMapper.insert(sysMenu) > 0;
    }

    @Override
    @CacheEvict(key = ManagerConstants.SYS_ALL_MENU_KEY)
    public Boolean modifySysMenu(SysMenu sysMenu) {
        Integer type= sysMenu.getType();
        if(type==0){
            sysMenu.setParentId(0L);
        }
        return sysMenuMapper.updateById(sysMenu) > 0;
    }
}
