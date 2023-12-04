package com.esliceu.Drawings.Exceptions;

public class NonCorrectViewTypeException extends RuntimeException{

    @Override
    public String getMessage() {
        return "Error assigning display type";
    }
}
