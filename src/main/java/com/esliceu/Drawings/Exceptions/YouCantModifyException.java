package com.esliceu.Drawings.Exceptions;

public class YouCantModifyException extends RuntimeException{

    @Override
    public String getMessage() {
        return "You cant modify this drawing";
    }
}
