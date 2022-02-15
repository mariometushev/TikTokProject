package com.example.tiktokproject.exceptions;

public class NotFoundUserException extends RuntimeException{
    public NotFoundUserException(String msg) {
        super(msg);
    }
}
