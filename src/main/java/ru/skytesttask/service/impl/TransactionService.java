package ru.skytesttask.service.impl;

import ru.skytesttask.entity.*;
import ru.skytesttask.repository.TransactionRepository;
import ru.skytesttask.service.IAccountService;
import ru.skytesttask.service.IClanService;
import ru.skytesttask.service.ITransactionService;
import ru.skytesttask.service.exceptions.ClanNotFoundException;
import ru.skytesttask.service.exceptions.TransactionNotFoundException;
import ru.skytesttask.util.validation.TransactionValidator;
import ru.skytesttask.util.validation.exceptions.TransactionValidationException;
import ru.skytesttask.webserver.util.JsonMapper;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.LinkedList;

public class TransactionService implements ITransactionService {
    private final TransactionRepository transactionRepository;
    private final IAccountService accountService;
    private final IClanService clanService;

    public TransactionService(IAccountService accountService, IClanService clanService) {
        this.transactionRepository = new TransactionRepository();
        this.accountService = accountService;
        this.clanService = clanService;
    }

    public Transaction userAddGoldToClan(User user, int amount) throws TransactionValidationException, ClanNotFoundException {

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

    public Transaction clanAddGoldToClan(Clan clanFrom, Clan clanTo, int amount) throws TransactionValidationException {

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

    public Transaction systemAddGoldToClan(Clan clan, int amount) throws TransactionValidationException {
        Account systemAccount = accountService.create(amount, AccountOwnerType.SYSTEM);

        Transaction transaction = new Transaction(
                0,
                systemAccount.getId(),
                clan.getId(),
                amount,
                LocalDateTime.now(ZoneOffset.UTC),
                null,
                TransactionState.CREATED,
                TransactionType.SYSTEM_TO_CLAN,
                "{}"
        );

        this.tryToPerform(transaction);
        return transaction;
    }

    public Transaction getById(int id) throws TransactionNotFoundException{
        Transaction transaction = transactionRepository.getById(id);
        if (transaction == null) throw new TransactionNotFoundException();
        return transaction;
    }

    public LinkedList<Transaction> getAccountTransactions(Integer accountFromId, Integer accountToId){
        return transactionRepository.getAccountTransactions(accountFromId, accountToId);
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

    public IAccountService getAccountService() {
        return accountService;
    }

    public IClanService getClanService() {
        return clanService;
    }
}
