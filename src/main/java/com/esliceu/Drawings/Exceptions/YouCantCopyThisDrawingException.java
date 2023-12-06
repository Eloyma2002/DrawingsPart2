package com.esliceu.Drawings.Exceptions;

public class YouCantCopyThisDrawingException extends RuntimeException {

    @Override
    public String getMessage() {
        return "You cant copy this drawing";
    }
}
