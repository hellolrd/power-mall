package com.powernode.config;

import cn.hutool.core.util.ObjectUtil;
import com.powernode.constant.AuthConstant;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.apache.http.HttpRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

public class FeignInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if(ObjectUtil.isNotNull(requestAttributes)) {
            HttpServletRequest request = requestAttributes.getRequest();
            if(ObjectUtil.isNotNull(request)) {
                // 获取请求头中的Authorization
                String authorization = request.getHeader(AuthConstant.AUTHORIZATION);

                    // 将Authorization添加到Feign请求头中
                    requestTemplate.header(AuthConstant.AUTHORIZATION, authorization);
                  return;
            }

        }
        requestTemplate.header(AuthConstant.AUTHORIZATION,AuthConstant.BEARER+"");

    }
}
