package ru.skytesttask.service;

import ru.skytesttask.entity.Clan;
import ru.skytesttask.service.exceptions.ClanNotFoundException;
import ru.skytesttask.util.validation.exceptions.ClanValidationException;

public interface IClanService {
    Clan getById(int id) throws ClanNotFoundException;

    void save(Clan clan) throws ClanValidationException ;

    Clan create(String ClanName) throws ClanValidationException;




}
