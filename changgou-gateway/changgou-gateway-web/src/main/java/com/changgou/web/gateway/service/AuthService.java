package com.changgou.web.gateway.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpCookie;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;

/**
 * @Author: LJJ
 * @Program: changgou
 * @Description:
 * @Create: 2020-03-07 14:54:54
 * @Modified By:
 */
@Service
public class AuthService {
    @Autowired
    private StringRedisTemplate redisTemplate;

    //从cookie中获取jti
    public String getJtiFromCookie(ServerHttpRequest request){
        HttpCookie httpCookie = request.getCookies().getFirst("uid");
        if(httpCookie != null) {
            return httpCookie.getValue();
        }
        return null;
    }

    //从redis中获取jwt
    public String getJwtFromRedis(String jti) {
        return redisTemplate.boundValueOps(jti).get();
    }
}
