package ru.skytesttask;

import ru.skytesttask.entity.*;
import ru.skytesttask.repository.AccountRepository;
import ru.skytesttask.repository.TransactionRepository;
import ru.skytesttask.repository.util.Storage;
import ru.skytesttask.repository.UserRepository;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

public class Main {
    public static void main(String[] args) throws SQLException {

        Storage.init();
        UserRepository rep = new UserRepository();
        AccountRepository acrep = new AccountRepository();

        Account account = new Account(0, 0, 1000, AccountOwnerType.USER);
        int accid = acrep.create(account);
        User u = new User(0, "John", accid, null);
        account.setId(accid);
        int userId = rep.create(u);
        account.setOwnerId(userId);
        acrep.update(account);
        Transaction transaction = new Transaction(
               0,
               1,
               2,
               400,
                LocalDateTime.now(ZoneOffset.UTC),
                null,
                TransactionState.CREATED,
                TransactionType.USER_TO_CLAN
        );
        TransactionRepository transactionRepository = new TransactionRepository();
        transactionRepository.create(transaction);
        transactionRepository.perform(transaction);



    }
}
