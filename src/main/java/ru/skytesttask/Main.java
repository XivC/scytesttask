package ru.skytesttask;

import ru.skytesttask.entity.Account;
import ru.skytesttask.entity.AccountOwnerType;
import ru.skytesttask.entity.User;
import ru.skytesttask.repository.AccountRepository;
import ru.skytesttask.repository.Storage;
import ru.skytesttask.repository.UserRepository;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {

        Storage.init();
        UserRepository rep = new UserRepository();
        AccountRepository acrep = new AccountRepository();

        Account account = new Account(0, 0, AccountOwnerType.USER);
        int accid = acrep.create(account);
        User u = new User(0, "John", accid, null);
        rep.create(u);

    }
}
