package ru.skytesttask.util.validation;

import java.util.HashMap;

public class UserValidationException extends ValidationException{
    public UserValidationException(HashMap<Object, Object> errors) {
        super(errors);
    }
}
