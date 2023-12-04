package com.esliceu.Drawings.Exceptions;

public class YouCantRecoverThisDrawingException extends RuntimeException{

    @Override
    public String getMessage() {
        return "You cant recover this drawing, is not yours";
    }
}
