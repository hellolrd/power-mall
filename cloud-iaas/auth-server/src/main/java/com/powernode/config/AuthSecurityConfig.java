package com.powernode.config;

import cn.hutool.crypto.digest.BCrypt;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.powernode.constant.AuthConstant;
import com.powernode.constant.BusinessEnum;
import com.powernode.constant.HttpConstants;
import com.powernode.impl.UserDetailServiceImpl;
import com.powernode.model.LoginResult;
import com.powernode.model.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.util.ConditionalOnBootstrapEnabled;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import reactor.core.publisher.Flux;

import java.io.PrintWriter;
import java.time.Duration;
import java.util.UUID;

@Configuration
public class AuthSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserDetailServiceImpl userDetailService;
    @Autowired
    private StringRedisTemplate   stringRedisTemplate;
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().disable();
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.formLogin()
                .loginProcessingUrl(AuthConstant.LOGIN_URL)
                .successHandler(authenticationSuccessHandler())
                .failureHandler(authenticationFailureHandler());

        http.logout()
                .logoutUrl(AuthConstant.LOGOUT_URL)
                .logoutSuccessHandler(logoutSuccessHandler());

        http.authorizeRequests().anyRequest().authenticated();


    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return (request, response, authentication) -> {
            response.setContentType(HttpConstants.CONTENT_TYPE);
            response.setCharacterEncoding(HttpConstants.UTF_8);

            String token= UUID.randomUUID().toString();
            String userJsonStr= JSONObject.toJSONString(authentication.getPrincipal());
            //将用户信息存入redis
            stringRedisTemplate.opsForValue().set(AuthConstant.LOGIN_TOKEN_PREFIX + token, userJsonStr, Duration.ofSeconds(AuthConstant.token_time));
            LoginResult loginResult = new LoginResult(token, AuthConstant.token_time);
            Result<Object> result = Result.success(loginResult);

            ObjectMapper objectMapper = new ObjectMapper();
            String s= objectMapper.writeValueAsString(result);
            PrintWriter writer = response.getWriter();
            writer.write(s);
            writer.flush();
            writer.close();
        };
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return (request, response, exception) -> {
            response.setContentType(HttpConstants.APPLICATION_JSON);
            response.setCharacterEncoding(HttpConstants.UTF_8);

            Result<Object> result = new Result<>();
            result.setCode(BusinessEnum.OPERATION_FAIL.getCode());
            if (exception instanceof BadCredentialsException){
                result.setMsg("用户名或密码有误");
            } else if (exception instanceof UsernameNotFoundException) {
                result.setMsg("用户不存在");
            } else if (exception instanceof AccountExpiredException) {
                result.setMsg("帐号异常，请联系管理员");}
             else if (exception instanceof AccountStatusException) {
                    result.setMsg("帐号异常，请联系管理员");
                } else if (exception instanceof InternalAuthenticationServiceException){
                    result.setMsg(exception.getMessage());
            }else {
                result.setMsg(BusinessEnum.OPERATION_FAIL.getMsg());
             }
            //返回结果
            ObjectMapper objectMapper = new ObjectMapper();
            String s = objectMapper.writeValueAsString( result);
            PrintWriter writer = response.getWriter();
            writer.write(s);
            writer.flush();
            writer.close();
        };
    }

    @Bean
    public LogoutSuccessHandler logoutSuccessHandler() {
        return (request, response, authentication) -> {
            response.setContentType(HttpConstants.APPLICATION_JSON);
            response.setCharacterEncoding(HttpConstants.UTF_8);
            //从请求头中获取token
            String authorization = request.getHeader(AuthConstant.AUTHORIZATION);
            String token = authorization.replaceFirst(AuthConstant.BEARER, "");

            stringRedisTemplate.delete( AuthConstant.LOGIN_TOKEN_PREFIX+token);

            Result<Object> result = Result.success(  null);

            ObjectMapper objectMapper = new ObjectMapper();
            String s = objectMapper.writeValueAsString(result);
            PrintWriter writer = response.getWriter();
            writer.write(s);
            writer.flush();
            writer.close();

        };
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
