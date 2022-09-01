package ru.skytesttask.util.validation;


import java.util.HashMap;


public abstract class ValidationException extends Exception {
    protected HashMap<Object, Object> errors;
    public ValidationException(HashMap<Object, Object> errors){
        this.errors = errors;
    }
    public HashMap<Object, Object> getErrors(){
        return this.errors;
    }

}
