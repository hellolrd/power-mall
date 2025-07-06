package com.powernode.filter;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.powernode.config.WhiteUrlsConfig;
import com.powernode.constant.AuthConstant;
import com.powernode.constant.BusinessEnum;
import com.powernode.constant.HttpConstants;
import com.powernode.model.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Date;

@Component
@Slf4j
public class AuthFilter implements GlobalFilter, Ordered {
    @Autowired
    private WhiteUrlsConfig whiteUrlsConfig;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getPath().toString();
        if(whiteUrlsConfig.getAllowUrls().contains(path)) {
            // 如果是白名单中的路径，直接放行
            return chain.filter(exchange);
        }

        String authorizationValue = request.getHeaders().getFirst(AuthConstant.AUTHORIZATION);
        if(StringUtils.hasText(authorizationValue)) {
            String tokenValue = authorizationValue.replaceFirst(AuthConstant.BEARER, "");

            if (StringUtils.hasText(tokenValue) && stringRedisTemplate.hasKey(AuthConstant.LOGIN_TOKEN_PREFIX + tokenValue)) {
                // 如果token存在于Redis中，放行
                return chain.filter(exchange);
            }
        }
        // 打印 拦截非法请求日志
        log.error("拦截非法请求，拦截时间:{}，请求api接口:{}",new Date(),path);

        ServerHttpResponse response = exchange.getResponse();
        response.getHeaders().set(HttpConstants.CONTENT_TYPE, HttpConstants.APPLICATION_JSON);

        Result<Object> result = Result.fail(BusinessEnum.UN_AUTHORIZATION);
        ObjectMapper objectMapper = new ObjectMapper();
        byte[] bytes = new byte[0];
        try {
            bytes = objectMapper.writeValueAsBytes(result);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        DataBuffer wrap = response.bufferFactory().wrap(bytes);

        return response.writeWith(Mono.just(wrap));

    }

    @Override
    public int getOrder() {
        return 0;
    }
}
