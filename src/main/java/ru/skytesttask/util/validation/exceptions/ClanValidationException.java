package ru.skytesttask.util.validation.exceptions;

import java.util.HashMap;

public class ClanValidationException extends ValidationException {
    public ClanValidationException(HashMap<Object, Object> errors) {
        super(errors);
    }
}
