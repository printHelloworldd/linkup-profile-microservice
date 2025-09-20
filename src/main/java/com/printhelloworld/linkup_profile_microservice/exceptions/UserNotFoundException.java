package com.printhelloworld.linkup_profile_microservice.exceptions;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String id) {
        super("User with id " + id + " not found");
    }
}