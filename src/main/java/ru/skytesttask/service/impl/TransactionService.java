package ru.skytesttask.service.impl;

import ru.skytesttask.entity.*;
import ru.skytesttask.repository.TransactionRepository;
import ru.skytesttask.service.exceptions.AccountNotFoundException;
import ru.skytesttask.service.exceptions.ClanNotFoundException;
import ru.skytesttask.util.validation.TransactionValidator;
import ru.skytesttask.util.validation.exceptions.TransactionValidationException;
import ru.skytesttask.util.validation.exceptions.ValidationException;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.TimeZone;

public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final AccountService accountService;
    private final ClanService clanService;

    public TransactionService() {
        this.transactionRepository = new TransactionRepository();
        this.accountService = new AccountService();
        this.clanService = new ClanService();
    }

    public void UserAddGoldToClan(User user, int amount) throws TransactionValidationException {
        try {
            Clan userClan = clanService.getById(user.getClanId());

            Transaction transaction = new Transaction(
                    0,
                    user.getAccountId(),
                    userClan.getAccountId(),
                    amount,
                    LocalDateTime.now(ZoneOffset.UTC),
                    null,
                    TransactionState.CREATED,
                    TransactionType.USER_TO_CLAN
            );

            this.tryToPerform(transaction);

        } catch (ClanNotFoundException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void ClanAddGoldToClan(Clan clanFrom, Clan clanTo, int amount) throws TransactionValidationException {

        Transaction transaction = new Transaction(
                0,
                clanFrom.getAccountId(),
                clanTo.getAccountId(),
                amount,
                LocalDateTime.now(ZoneOffset.UTC),
                null,
                TransactionState.CREATED,
                TransactionType.CLAN_TO_CLAN
        );

        this.tryToPerform(transaction);

    }

    public void SystemAddGoldToClan(Clan clan, int amount) throws  TransactionValidationException{
        Account systemAccount = accountService.create(amount, AccountOwnerType.SYSTEM);

        Transaction transaction = new Transaction(
                0,
                systemAccount.getId(),
                clan.getId(),
                amount,
                LocalDateTime.now(ZoneOffset.UTC),
                null,
                TransactionState.CREATED,
                TransactionType.CLAN_TO_CLAN
        );

        this.tryToPerform(transaction);
    }


    private void tryToPerform(Transaction transaction) throws TransactionValidationException {
        int transactionId = transactionRepository.create(transaction);
        transaction.setId(transactionId);
        try {
            (new TransactionValidator()).validate(transaction);
        } catch (TransactionValidationException ex) {
            transaction.setState(TransactionState.DECLINED);
            transaction.setUpdatedAt(LocalDateTime.now(ZoneOffset.UTC));
            transactionRepository.update(transaction);
            throw ex;
        }
        transactionRepository.perform(transaction);
        transaction.setState(TransactionState.SUCCEED);
        transaction.setUpdatedAt(LocalDateTime.now(ZoneOffset.UTC));
        transactionRepository.update(transaction);
    }
}
