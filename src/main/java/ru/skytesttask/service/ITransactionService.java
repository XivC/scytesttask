package ru.skytesttask.service;

import ru.skytesttask.entity.Account;

public interface ITransactionService {

    void perform(Account from, Account to, int amount);





}
