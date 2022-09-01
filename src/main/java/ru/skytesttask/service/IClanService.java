package ru.skytesttask.service;

import ru.skytesttask.entity.Clan;

public interface IClanService {
    Clan getById(int id);

    void save(Clan clan);




}
