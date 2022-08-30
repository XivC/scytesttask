package ru.skytesttask.service;

import ru.skytesttask.entity.Account;
import ru.skytesttask.entity.Payable;

interface AccountService {
    Account getById(int id);
    void save(Account account);

}
