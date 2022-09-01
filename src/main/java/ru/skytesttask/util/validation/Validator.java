package ru.skytesttask.util.validation;

public abstract class Validator <T> {
    public abstract void validate(T object) throws ValidationException;
}
