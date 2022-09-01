package ru.skytesttask.util.validation;

import ru.skytesttask.entity.User;
import ru.skytesttask.repository.AccountRepository;
import ru.skytesttask.repository.UserRepository;

import java.util.HashMap;

public class UserValidator extends Validator<User> {
    @Override
    public void validate(User user) throws UserValidationException {
        HashMap<Object, Object> errors = new HashMap<>();
        UserRepository userRepository = new UserRepository();
        if (user.getName() == null || user.getName().isEmpty()) {
            errors.put("name", "Name can't be void");
        }

        if (userRepository.getByName(user.getName()) != null){
            errors.put("identity", "User with this name already exists");
        }

        if (!errors.isEmpty()) throw new UserValidationException(errors);
    }
}
