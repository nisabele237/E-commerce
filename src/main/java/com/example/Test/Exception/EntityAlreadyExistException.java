package com.example.Test.Exception;

public class EntityAlreadyExistException extends  RuntimeException {
    public EntityAlreadyExistException() {
    }

    public EntityAlreadyExistException(String message) {
        super(message);
    }
}
