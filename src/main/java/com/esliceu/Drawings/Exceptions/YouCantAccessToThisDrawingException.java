package com.esliceu.Drawings.Exceptions;

public class YouCantAccessToThisDrawingException extends RuntimeException{

    @Override
    public String getMessage() {
        return "The owner of this drawing has set it to private";
    }
}
