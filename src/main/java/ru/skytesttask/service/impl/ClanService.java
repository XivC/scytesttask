package ru.skytesttask.service.impl;

import ru.skytesttask.entity.Account;
import ru.skytesttask.entity.AccountOwnerType;
import ru.skytesttask.entity.Clan;
import ru.skytesttask.repository.ClanRepository;
import ru.skytesttask.service.IClanService;
import ru.skytesttask.service.exceptions.ClanNotFoundException;
import ru.skytesttask.util.validation.ClanValidator;
import ru.skytesttask.util.validation.exceptions.ClanValidationException;

import java.util.LinkedList;

public class ClanService implements IClanService {

    private final ClanRepository clanRepository;

    public ClanService() {
        clanRepository = new ClanRepository();
    }

    @Override
    public Clan getById(int id) throws ClanNotFoundException {

        Clan clan = clanRepository.getById(id);
        if (clan == null) throw new ClanNotFoundException();
        return clan;
    }

    @Override
    public Clan getByName(String name) throws ClanNotFoundException {
        Clan clan = clanRepository.getByName(name);
        if (clan == null) throw new ClanNotFoundException();
        return clan;
    }

    @Override
    public void save(Clan clan) throws ClanValidationException {
        (new ClanValidator()).validate(clan);
        clanRepository.update(clan);

    }

    @Override
    public Clan create(String clanName) throws ClanValidationException {
        AccountService accountService = new AccountService();
        Clan clan = new Clan(0, clanName, 0);
        (new ClanValidator()).validate(clan);
        Account account = accountService.create(1000, AccountOwnerType.CLAN);
        clan.setAccountId(account.getId());
        int clanId = clanRepository.create(clan);
        clan.setId(clanId);
        clanRepository.update(clan);
        account.setOwnerId(clanId);
        accountService.save(account);
        return clan;

    }

    @Override
    public LinkedList<Clan> getAll() {
        return clanRepository.getAll();
    }
}
