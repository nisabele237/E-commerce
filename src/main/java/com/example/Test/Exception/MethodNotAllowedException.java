package com.example.Test.Exception;

public class MethodNotAllowedException extends  RuntimeException{
    public MethodNotAllowedException() {
    }

    public MethodNotAllowedException(String message) {
        super(message);
    }
}
