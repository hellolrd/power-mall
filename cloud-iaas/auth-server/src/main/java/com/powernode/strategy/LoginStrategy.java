package com.powernode.strategy;

import org.springframework.security.core.userdetails.UserDetails;

public interface LoginStrategy {

    UserDetails realLogin(String username);
}
