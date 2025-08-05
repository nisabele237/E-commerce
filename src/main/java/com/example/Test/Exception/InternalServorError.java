package com.example.Test.Exception;

public class InternalServorError extends RuntimeException {
    public InternalServorError() {
    }

    public InternalServorError(String message) {
        super(message);
    }
}
