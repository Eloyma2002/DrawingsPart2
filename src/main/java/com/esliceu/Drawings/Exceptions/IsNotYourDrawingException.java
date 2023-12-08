package com.esliceu.Drawings.Exceptions;

public class IsNotYourDrawingException extends RuntimeException {

    @Override
    public String getMessage() {
        return "Is not you drawing";
    }
}
