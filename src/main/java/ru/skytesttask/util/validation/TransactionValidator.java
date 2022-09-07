package ru.skytesttask.util.validation;

import ru.skytesttask.entity.Account;
import ru.skytesttask.entity.Transaction;
import ru.skytesttask.entity.TransactionType;
import ru.skytesttask.entity.User;
import ru.skytesttask.repository.AccountRepository;
import ru.skytesttask.repository.ClanRepository;
import ru.skytesttask.repository.UserRepository;
import ru.skytesttask.util.validation.exceptions.TransactionValidationException;

import java.util.HashMap;

/*
Due to the potentially large number of validation conditions,
something like validation chain should be better for transaction validation.

 */
public class TransactionValidator extends Validator<Transaction> {

    @Override
    public void validate(Transaction transaction) throws TransactionValidationException {
        AccountRepository accountRepository = new AccountRepository();

        HashMap<Object, Object> errors = new HashMap<>();

        if (transaction.getAmount() < 0) errors.put("amount", "Amount can't be less then 0");
        if (transaction.getFromId() == transaction.getToId())
            errors.put("id", "cant process transaction between equal ids");
        try {
            Account accountFrom = accountRepository.getById(transaction.getFromId());
            if (
                    accountFrom.getBalance() < transaction.getAmount()
                            &&
                            transaction.getType() != TransactionType.SYSTEM_TO_CLAN) {
                errors.put("balance", "Payer have no enough gold");
            }
            if (transaction.getType() == TransactionType.USER_TO_CLAN) {
                UserRepository userRepository = new UserRepository();
                ClanRepository clanRepository = new ClanRepository();
                User user = userRepository.getById(accountFrom.getOwnerId());
                if (user.getClanId() == null) errors.put("clan", "user not in clan");
                else if (clanRepository.getById(user.getClanId()).getAccountId() != transaction.getToId()) {
                    errors.put("clan", "User not a member of this clan");
                }

            }

        } catch (NullPointerException ex) {
            throw new RuntimeException(ex);   //Due to user can't create a transaction, we think that all ids in transaction are valid.
            //Invalid id in transaction is an internal error and we shouldn't show it to user
            //Logging is needed here to catch corrupted ids.
        }

        if (!errors.isEmpty()) {
            errors.put("transaction", transaction);
            throw new TransactionValidationException(errors);
        }


    }
}
