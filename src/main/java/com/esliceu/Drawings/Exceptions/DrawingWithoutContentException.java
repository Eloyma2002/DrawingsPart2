package com.esliceu.Drawings.Exceptions;

public class DrawingWithoutContentException extends RuntimeException {

    @Override
    public String getMessage() {
        return "You cannot save a drawing without content";
    }
}
