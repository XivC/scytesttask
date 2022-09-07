package ru.skytesttask.service;

import ru.skytesttask.entity.Clan;
import ru.skytesttask.entity.Transaction;
import ru.skytesttask.entity.User;
import ru.skytesttask.service.exceptions.ClanNotFoundException;
import ru.skytesttask.service.exceptions.TransactionNotFoundException;
import ru.skytesttask.util.validation.exceptions.TransactionValidationException;

import java.util.LinkedList;

public interface ITransactionService {

    Transaction userAddGoldToClan(User user, int amount) throws TransactionValidationException, ClanNotFoundException;

    Transaction clanAddGoldToClan(Clan clanFrom, Clan clanTo, int amount) throws TransactionValidationException;

    Transaction systemAddGoldToClan(Clan clan, int amount) throws TransactionValidationException;

    Transaction getById(int id) throws TransactionNotFoundException;

    LinkedList<Transaction> getAccountTransactions(Integer accountFromId, Integer accountToId);

    IAccountService getAccountService();

    IClanService getClanService();

}
