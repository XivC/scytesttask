package ru.skytesttask.service;

import ru.skytesttask.entity.User;

public interface UserService {
    User getById(int id);
    void save(int id);

}
