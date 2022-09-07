package ru.skytesttask.service;

import ru.skytesttask.entity.Clan;
import ru.skytesttask.service.exceptions.ClanNotFoundException;
import ru.skytesttask.util.validation.exceptions.ClanValidationException;

import java.util.LinkedList;

public interface IClanService {
    Clan getById(int id) throws ClanNotFoundException;

    Clan getByName(String name) throws ClanNotFoundException;

    void save(Clan clan) throws ClanValidationException;

    Clan create(String ClanName) throws ClanValidationException;

    LinkedList<Clan> getAll();


}
