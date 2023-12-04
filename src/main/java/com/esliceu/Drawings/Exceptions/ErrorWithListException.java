package com.esliceu.Drawings.Exceptions;

public class ErrorWithListException extends RuntimeException {

    @Override
    public String getMessage() {
        return "An error occurred while trying to obtain the list of drawings";
    }
}
