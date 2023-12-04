package com.esliceu.Drawings.Exceptions;

public class ErrorWithTrashListException extends RuntimeException{

    @Override
    public String getMessage() {
        return "An error occurred while trying to obtain the trash list";
    }
}
