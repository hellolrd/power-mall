package com.powernode.factory;

import com.powernode.strategy.LoginStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class LoginStrategyFactory {
    @Autowired
    private Map<String,LoginStrategy> loginStrategyMap=new HashMap<>();

    public LoginStrategy getInstance(String loginType){
        return loginStrategyMap.get(loginType);
    }

}
