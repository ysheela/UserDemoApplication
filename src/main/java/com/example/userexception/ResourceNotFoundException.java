package com.example.userexception;

public class ResourceNotFoundException extends RuntimeException {
    private String resourceId;

    public ResourceNotFoundException(String message, String resourceId) {
        super(message);
        this.resourceId = resourceId;
    }
}
