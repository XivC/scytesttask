package ru.skytesttask.util.validation;

import ru.skytesttask.util.validation.exceptions.ValidationException;

public abstract class Validator <T> {
    public abstract void validate(T object) throws ValidationException;
}
