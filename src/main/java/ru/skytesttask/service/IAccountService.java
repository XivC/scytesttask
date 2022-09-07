package ru.skytesttask.service;

import ru.skytesttask.entity.Account;
import ru.skytesttask.entity.AccountBalanceHistoryItem;
import ru.skytesttask.entity.AccountOwnerType;
import ru.skytesttask.service.exceptions.AccountNotFoundException;

import java.util.LinkedList;

public interface IAccountService {
    Account getById(int id) throws AccountNotFoundException;

    void save(Account account);

    Account create(int balance, AccountOwnerType ownerType);

    LinkedList<AccountBalanceHistoryItem> getAccountHistory(Account account);


}
