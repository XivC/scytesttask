package ru.skytesttask;

import ru.skytesttask.entity.*;
import ru.skytesttask.repository.AccountRepository;
import ru.skytesttask.repository.ClanRepository;
import ru.skytesttask.repository.TransactionRepository;
import ru.skytesttask.repository.util.Storage;
import ru.skytesttask.repository.UserRepository;
import ru.skytesttask.service.impl.ClanService;
import ru.skytesttask.service.impl.TransactionService;
import ru.skytesttask.service.impl.UserService;
import ru.skytesttask.util.validation.exceptions.ValidationException;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.HashMap;

public class Main {
    public static void main(String[] args) throws Exception {

        Storage.init();
        UserService userService = new UserService();
        ClanService clanService = new ClanService();
        TransactionService transactionService = new TransactionService();

        User user = userService.create("Vova2");
        Clan clan = clanService.create("Clan");

        userService.addToClan(user, clan.getId());
        try {
            transactionService.UserAddGoldToClan(user, 100);
        }
        catch (ValidationException ex){
            HashMap<Object, Object> errors = ex.getErrors();
            int a = 1;
        }




    }
}
