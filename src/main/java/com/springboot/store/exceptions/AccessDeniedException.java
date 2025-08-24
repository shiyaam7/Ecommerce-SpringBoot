package com.springboot.store.exceptions;

public class AccessDeniedException extends RuntimeException {
    public AccessDeniedException() {
        super("You dont have access to this order");
    }
}
