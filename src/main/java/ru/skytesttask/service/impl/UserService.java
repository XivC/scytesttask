package ru.skytesttask.service.impl;

import ru.skytesttask.entity.Account;
import ru.skytesttask.entity.AccountOwnerType;
import ru.skytesttask.entity.User;
import ru.skytesttask.repository.UserRepository;
import ru.skytesttask.service.IUserService;
import ru.skytesttask.service.exceptions.ClanNotFoundException;
import ru.skytesttask.service.exceptions.UserNotFoundException;
import ru.skytesttask.util.validation.UserValidator;
import ru.skytesttask.util.validation.exceptions.UserValidationException;

public class UserService implements IUserService {

    private final UserRepository repository;

    public UserService(){
        this.repository = new UserRepository();
    }

    @Override
    public User getById(int id) throws UserNotFoundException {
        User user = repository.getById(id);
        if (user == null) throw new UserNotFoundException();
        return user;
    }

    @Override
    public User getByName(String name) throws UserNotFoundException{
        User user = repository.getByName(name);
        if (user == null) throw new UserNotFoundException();
        return user;
    }

    @Override
    public void save(User user) {
        repository.update(user);
    }

    @Override
    public User create(String name) throws UserValidationException {
        AccountService accountService = new AccountService();
        User user = new User(0, name, 0, null);
        (new UserValidator()).validate(user);
        Account account = accountService.create(1000, AccountOwnerType.USER);
        user.setAccountId(account.getId());
        int userId = repository.create(user);
        user.setId(userId);
        account.setOwnerId(userId);
        accountService.save(account);
        return user;

    }

    @Override
    public void addToClan(User user, int clanId) throws ClanNotFoundException {
        ClanService clanService = new ClanService();
        clanService.getById(clanId);
        user.setClanId(clanId);
        repository.update(user);
    }
}
