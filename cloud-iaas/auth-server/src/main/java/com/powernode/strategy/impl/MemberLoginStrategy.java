package com.powernode.strategy.impl;

import com.powernode.constant.AuthConstant;
import com.powernode.strategy.LoginStrategy;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service(AuthConstant.MEMBER_USER_LOGIN)
public class MemberLoginStrategy implements LoginStrategy {
    @Override
    public UserDetails realLogin(String username) {
        return null;
    }
}
