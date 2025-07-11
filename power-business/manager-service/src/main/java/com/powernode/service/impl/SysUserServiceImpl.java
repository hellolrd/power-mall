package com.powernode.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.powernode.domain.SysRole;
import com.powernode.domain.SysUserRole;
import com.powernode.mapper.SysRoleMapper;
import com.powernode.mapper.SysUserRoleMapper;
import com.powernode.service.SysUserRoleService;
import com.powernode.util.AuthUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.powernode.domain.SysUser;
import com.powernode.mapper.SysUserMapper;
import com.powernode.service.SysUserService;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService{

    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private SysRoleMapper sysRoleMapper;
    @Autowired
    private SysUserRoleService sysUserRoleService;

    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer saveSysUser(SysUser sysUser) {
        // 这里可以添加业务逻辑，比如密码加密等
        sysUser.setCreateUserId(AuthUtils.getLoginUserId());
        sysUser.setCreateTime(new Date());
        sysUser.setShopId(1L);
        sysUser.setPassword(bCryptPasswordEncoder.encode(sysUser.getPassword()));
        int i = sysUserMapper.insert( sysUser);
        if(i>0){
            Long userId = sysUserMapper.selectOne(new LambdaQueryWrapper<SysUser>()
                    .eq(SysUser::getUsername, sysUser.getUsername())).getUserId();
            // 关联角色
            List<Long> roleIds = sysUser.getRoleIdList();

            if(roleIds != null && !roleIds.isEmpty()){
                List<SysUserRole> sysUserRoleList= new ArrayList<>();
                roleIds.forEach(roleId -> {
                    SysUserRole sysUserRole = new SysUserRole();
                    sysUserRole.setUserId(userId);
                    sysUserRole.setRoleId(roleId);
                    sysUserRoleList.add(sysUserRole);
                });
                // 批量插入用户角色关联
                 sysUserRoleService.saveBatch(sysUserRoleList);
            }


        }

        return i;

    }


    @Override
    public SysUser querySysUserInfoByUserId(Long id) {
        SysUser sysUser = sysUserMapper.selectById(id);

        // 查询用户角色
        List<SysUserRole> sysUserRoleList = sysUserRoleMapper.selectList(new LambdaQueryWrapper<SysUserRole>()
                .eq(SysUserRole::getUserId, id)
        );
        if (CollectionUtil.isNotEmpty(sysUserRoleList) && sysUserRoleList.size() != 0) {
            List<Long> roldIdList = sysUserRoleList.stream().map(SysUserRole::getRoleId
            ).collect(Collectors.toList());
          sysUser.setRoleIdList(roldIdList);
        }

        return sysUser;
    }




//    修改管理员信息
//    1.删除原有角色用户关联
//    2.重新插入角色用户关联
//    3.更新用户信息
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer modifySysUserInfo(SysUser sysUser) {
       Long userId = sysUser.getUserId();
       // 删除原有角色用户关联
        sysUserRoleMapper.delete(new LambdaQueryWrapper<SysUserRole>()
                .eq(SysUserRole::getUserId, userId));
        // 重新插入角色用户关联
        List<Long> roleIds = sysUser.getRoleIdList();

        if(roleIds != null && !roleIds.isEmpty()){
            List<SysUserRole> sysUserRoleList= new ArrayList<>();
            roleIds.forEach(roleId -> {
                SysUserRole sysUserRole = new SysUserRole();
                sysUserRole.setUserId(userId);
                sysUserRole.setRoleId(roleId);
                sysUserRoleList.add(sysUserRole);
            });
            // 批量插入用户角色关联
            sysUserRoleService.saveBatch(sysUserRoleList);
        }


           if(sysUser.getPassword() != null && !sysUser.getPassword().isEmpty()) {
               sysUser.setPassword(bCryptPasswordEncoder.encode(sysUser.getPassword()));
        }

        return sysUserMapper.updateById(sysUser);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean removeSysUsers(List<Long> userIds) {
        sysUserRoleMapper.delete(new LambdaQueryWrapper<SysUserRole>()
                .in(SysUserRole::getUserId, userIds));

        return sysUserMapper.deleteBatchIds(userIds)== userIds.size();
    }
}
