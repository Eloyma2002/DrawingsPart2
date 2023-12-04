package com.esliceu.Drawings.Exceptions;

public class UserExistException extends RuntimeException {

    @Override
    public String getMessage() {
        return "User already exist";
    }
}
