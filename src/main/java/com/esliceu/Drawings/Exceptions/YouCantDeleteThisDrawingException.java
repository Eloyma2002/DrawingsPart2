package com.esliceu.Drawings.Exceptions;

public class YouCantDeleteThisDrawingException extends RuntimeException {

    @Override
    public String getMessage() {
        return "You cant delete this drawing, is not yours";
    }
}
