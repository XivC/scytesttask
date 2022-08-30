package ru.skytesttask.service;

import ru.skytesttask.entity.Clan;

public interface ClanService {
    Clan getById(int id);

    void save(Clan clan);




}
