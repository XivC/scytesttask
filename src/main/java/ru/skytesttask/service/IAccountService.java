package ru.skytesttask.service;

import ru.skytesttask.entity.Account;
import ru.skytesttask.entity.Payable;

interface IAccountService {
    Account getById(int id);
    void save(Account account);

}
