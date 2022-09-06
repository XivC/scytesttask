package ru.skytesttask.service.impl;

import ru.skytesttask.entity.*;
import ru.skytesttask.repository.TransactionRepository;
import ru.skytesttask.service.exceptions.ClanNotFoundException;
import ru.skytesttask.util.validation.TransactionValidator;
import ru.skytesttask.util.validation.exceptions.TransactionValidationException;
import ru.skytesttask.webserver.util.JsonMapper;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;

public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final AccountService accountService;
    private final ClanService clanService;

    public TransactionService() {
        this.transactionRepository = new TransactionRepository();
        this.accountService = new AccountService();
        this.clanService = new ClanService();
    }

    public Transaction UserAddGoldToClan(User user, int amount) throws TransactionValidationException, ClanNotFoundException {

        Clan userClan = clanService.getById(user.getClanId());

        Transaction transaction = new Transaction(
                0,
                user.getAccountId(),
                userClan.getAccountId(),
                amount,
                LocalDateTime.now(ZoneOffset.UTC),
                null,
                TransactionState.CREATED,
                TransactionType.USER_TO_CLAN,
                "{}"
        );

        this.tryToPerform(transaction);
        return transaction;

    }

    public Transaction ClanAddGoldToClan(Clan clanFrom, Clan clanTo, int amount) throws TransactionValidationException {

        Transaction transaction = new Transaction(
                0,
                clanFrom.getAccountId(),
                clanTo.getAccountId(),
                amount,
                LocalDateTime.now(ZoneOffset.UTC),
                null,
                TransactionState.CREATED,
                TransactionType.CLAN_TO_CLAN,
                "{}"
        );

        this.tryToPerform(transaction);
        return transaction;

    }

    public Transaction SystemAddGoldToClan(Clan clan, int amount) throws TransactionValidationException {
        Account systemAccount = accountService.create(amount, AccountOwnerType.SYSTEM);

        Transaction transaction = new Transaction(
                0,
                systemAccount.getId(),
                clan.getId(),
                amount,
                LocalDateTime.now(ZoneOffset.UTC),
                null,
                TransactionState.CREATED,
                TransactionType.CLAN_TO_CLAN,
                "{}"
        );

        this.tryToPerform(transaction);
        return transaction;
    }


    private void tryToPerform(Transaction transaction) throws TransactionValidationException {
        int transactionId = transactionRepository.create(transaction);
        transaction.setId(transactionId);
        try {
            (new TransactionValidator()).validate(transaction);
        } catch (TransactionValidationException ex) {
            JsonMapper<HashMap> mapper = new JsonMapper<>(HashMap.class);
            transaction.setState(TransactionState.DECLINED);
            transaction.setUpdatedAt(LocalDateTime.now(ZoneOffset.UTC));
            try {
                transaction.setInfoJson(mapper.getJson(ex.getErrors()));
            } catch (IOException ex2) {
                throw new RuntimeException(ex2);
            }
            transactionRepository.update(transaction);
            throw ex;
        }
        transactionRepository.perform(transaction);
        transaction.setState(TransactionState.SUCCEED);
        transaction.setUpdatedAt(LocalDateTime.now(ZoneOffset.UTC));
        transactionRepository.update(transaction);
    }
}
