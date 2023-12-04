package com.esliceu.Drawings.Exceptions;

public class PasswordNotValidException extends RuntimeException {

    @Override
    public String getMessage() {
        return "Invalid password, min 5 digits and no spaces";
    }
}
