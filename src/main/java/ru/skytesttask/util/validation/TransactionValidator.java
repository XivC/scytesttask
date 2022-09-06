package ru.skytesttask.util.validation;

import ru.skytesttask.entity.Account;
import ru.skytesttask.entity.Transaction;
import ru.skytesttask.entity.TransactionType;
import ru.skytesttask.entity.User;
import ru.skytesttask.service.exceptions.AccountNotFoundException;
import ru.skytesttask.service.exceptions.ClanNotFoundException;
import ru.skytesttask.service.exceptions.UserNotFoundException;
import ru.skytesttask.service.impl.AccountService;
import ru.skytesttask.service.impl.ClanService;
import ru.skytesttask.service.impl.UserService;
import ru.skytesttask.util.validation.exceptions.TransactionValidationException;

import java.util.HashMap;

/*
Due to the potentially large number of validation conditions,
something like validation chain should be better for transaction validation.

 */
public class TransactionValidator extends Validator<Transaction> {
    @Override
    public void validate(Transaction transaction) throws TransactionValidationException {
        AccountService accountService = new AccountService();

        HashMap<Object, Object> errors = new HashMap<>();

        if (transaction.getAmount() < 0) errors.put("amount", "Amount can't be less then 0");
        if (transaction.getFromId() == transaction.getToId()) errors.put("id", "cant process transaction between equal ids");
        try {
            Account accountFrom = accountService.getById(transaction.getFromId());
            if (
                    accountFrom.getBalance() < transaction.getAmount()
                    &&
                    transaction.getType() != TransactionType.SYSTEM_TO_CLAN) {
                errors.put("balance", "Payer have no enough gold");
            }
            if (transaction.getType() == TransactionType.USER_TO_CLAN) {
                UserService userService = new UserService();
                ClanService clanService = new ClanService();
                User user = userService.getById(accountFrom.getOwnerId());
                if (user.getClanId() == null) errors.put("clan", "user not in clan");
                else if (clanService.getById(user.getClanId()).getAccountId() != transaction.getToId()){
                    errors.put("clan", "User not a member of this clan");
                }

            }

        }
        catch (AccountNotFoundException | UserNotFoundException | ClanNotFoundException ex) {
            throw new RuntimeException(ex);
        }

        if (!errors.isEmpty()) {
            errors.put("transactionid", transaction.getId());
            throw new TransactionValidationException(errors);
        }


    }
}
