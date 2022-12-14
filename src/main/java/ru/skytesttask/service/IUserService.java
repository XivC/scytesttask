package ru.skytesttask.service;

import ru.skytesttask.entity.User;
import ru.skytesttask.service.exceptions.ClanNotFoundException;
import ru.skytesttask.service.exceptions.UserNotFoundException;
import ru.skytesttask.util.validation.exceptions.UserValidationException;

import java.util.LinkedList;

public interface IUserService {
    User getById(int id) throws UserNotFoundException;

    void save(User user);

    User create(String name) throws UserValidationException;

    void addToClan(User user, int clanId) throws ClanNotFoundException;

    User getByName(String name) throws UserNotFoundException;

    LinkedList<User> getAll();


}
