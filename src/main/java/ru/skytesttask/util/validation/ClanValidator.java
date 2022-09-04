package ru.skytesttask.util.validation;

import ru.skytesttask.entity.Clan;
import ru.skytesttask.repository.ClanRepository;
import ru.skytesttask.util.validation.exceptions.ClanValidationException;

import java.util.HashMap;

public class ClanValidator extends Validator<Clan>{
    @Override
    public void validate(Clan clan) throws ClanValidationException {
        HashMap<Object, Object> errors = new HashMap<>();
        ClanRepository repository = new ClanRepository();
        if (clan.getName() == null || clan.getName().isEmpty()){
            errors.put("name", "Clan name can't be void");
        }
        if (repository.getByName(clan.getName()) != null){
            errors.put("identity", "Clan with name " + clan.getName() + "already exists");
        }
        if (!errors.isEmpty()) throw new ClanValidationException(errors);

    }
}
