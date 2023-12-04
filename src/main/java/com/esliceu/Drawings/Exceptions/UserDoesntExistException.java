package com.esliceu.Drawings.Exceptions;

public class UserDoesntExistException extends RuntimeException {

    @Override
    public String getMessage() {
        return "User not found";
    }
}
