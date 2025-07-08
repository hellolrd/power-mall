package com.powernode.impl;

import com.powernode.constant.AuthConstant;
import com.powernode.constant.HttpConstants;
import com.powernode.factory.LoginStrategyFactory;
import com.powernode.strategy.LoginStrategy;
import org.apache.http.HttpRequest;
import org.apache.tomcat.util.http.fileupload.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Service
public class UserDetailServiceImpl implements UserDetailsService {
    @Autowired
    private LoginStrategyFactory loginStrategyFactory;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ServletRequestAttributes requestAttributes= (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        String loginType=request.getHeader(AuthConstant.LOGIN_TYPE);
        if(!StringUtils.hasText(loginType)){
                throw new InternalAuthenticationServiceException("登录类型错误");
        }
        LoginStrategy instance= loginStrategyFactory.getInstance(loginType);

        return instance.realLogin(username);
    }
}
