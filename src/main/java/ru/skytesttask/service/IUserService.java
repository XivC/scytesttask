package ru.skytesttask.service;

import ru.skytesttask.entity.User;

public interface IUserService {
    User getById(int id);
    void save(int id);

}
