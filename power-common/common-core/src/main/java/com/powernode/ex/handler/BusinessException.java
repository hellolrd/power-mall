package com.powernode.ex.handler;

public class BusinessException extends RuntimeException{
    public BusinessException(String message) {
        super(message);
    }
}
