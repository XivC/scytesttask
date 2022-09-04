package ru.skytesttask.service.impl;

import ru.skytesttask.entity.Account;
import ru.skytesttask.entity.AccountOwnerType;
import ru.skytesttask.repository.AccountRepository;
import ru.skytesttask.service.IAccountService;
import ru.skytesttask.service.exceptions.AccountNotFoundException;

public class AccountService  implements IAccountService {

    private final AccountRepository accountRepository;

    public AccountService(){
        this.accountRepository = new AccountRepository();
    }
    @Override
    public Account getById(int id) throws AccountNotFoundException {
        Account account = accountRepository.getById(id);
        if (account == null) throw new AccountNotFoundException();
        return account;
    }

    @Override
    public void save(Account account) {
        accountRepository.update(account);
    }

    @Override
    public Account create(int balance, AccountOwnerType ownerType) {
        Account account = new Account(0,0, balance, ownerType);
        account.setId(accountRepository.create(account));
        return account;

    }
}
