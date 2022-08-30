package ru.skytesttask.service;

import ru.skytesttask.entity.Account;

public interface TransactionService {

    void perform(Account from, Account to, int amount);





}
