package ru.skytesttask.util.validation.exceptions;

import java.util.HashMap;

public class TransactionValidationException extends ValidationException {
    public TransactionValidationException(HashMap<Object, Object> errors) {
        super(errors);
    }
}
