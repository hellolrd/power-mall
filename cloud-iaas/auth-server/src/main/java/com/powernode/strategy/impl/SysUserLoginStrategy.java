package com.powernode.strategy.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.powernode.constant.AuthConstant;
import com.powernode.domain.LoginSysUser;
import com.powernode.mapper.LoginSysUserMapper;
import com.powernode.model.SecurityUser;
import com.powernode.strategy.LoginStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service(AuthConstant.SYS_USER_LOGIN)
public class SysUserLoginStrategy implements LoginStrategy {
    @Autowired
    private LoginSysUserMapper loginSysUserMapper;
    @Override
    public UserDetails realLogin(String username) {
       LoginSysUser loginSysUser= loginSysUserMapper.selectOne(new LambdaQueryWrapper<LoginSysUser>().eq(LoginSysUser::getUsername, username));
        if(ObjectUtil.isNotNull( loginSysUser)){
                Set<String> perms=loginSysUserMapper.selectPermsByUserId(loginSysUser.getUserId());
            SecurityUser securityUser = new SecurityUser();
            securityUser.setUserId(loginSysUser.getUserId());
            securityUser.setUsername(loginSysUser.getUsername());
            securityUser.setPassword(loginSysUser.getPassword());
            securityUser.setShopId(loginSysUser.getShopId());
            securityUser.setStatus(loginSysUser.getStatus());
            securityUser.setLoginType(AuthConstant.SYS_USER_LOGIN);

            //判断用户权限是否有值
            if (CollectionUtil.isNotEmpty(perms) && perms.size() != 0) {
                securityUser.setPerms(perms);
            }
                return securityUser;


        }
        return null;
    }
}
