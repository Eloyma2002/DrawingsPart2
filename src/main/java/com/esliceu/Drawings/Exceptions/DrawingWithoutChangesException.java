package com.esliceu.Drawings.Exceptions;

public class DrawingWithoutChangesException extends RuntimeException{

    @Override
    public String getMessage() {
        return "You cant modify a drawing without changes";
    }
}
