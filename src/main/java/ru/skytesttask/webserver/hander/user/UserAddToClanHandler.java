package ru.skytesttask.webserver.hander.user;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.skytesttask.entity.User;
import ru.skytesttask.service.IUserService;
import ru.skytesttask.service.exceptions.ClanNotFoundException;
import ru.skytesttask.service.exceptions.UserNotFoundException;
import ru.skytesttask.service.impl.UserService;
import ru.skytesttask.webserver.util.JsonMapper;
import ru.skytesttask.webserver.util.Util;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class UserAddToClanHandler implements HttpHandler {

    private final IUserService userService;
    private final JsonMapper<User> userJsonMapper;

    public UserAddToClanHandler(IUserService userService){
        super();
        this.userService = userService;
        this.userJsonMapper = new JsonMapper<>(User.class);
    }
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Map<String, String> queryParams = Util.getQueryParams(exchange.getRequestURI().getQuery());
        HashMap<Object, Object> errors = new HashMap<>();
        OutputStream os = exchange.getResponseBody();
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        String answer;
        Integer clanId = null;
        Integer userId = null;
        String clanIdString = queryParams.get("clanid");
        String userIdString = queryParams.get("userid");
        if (clanIdString == null)  errors.put("clanid", "clanid can't be void");
        else {
            try {
                clanId = Integer.valueOf(clanIdString);
            }
            catch (NumberFormatException ex){
                errors.put("clanid", "clanid must beinteger");
            }
        }
        if (userIdString == null)  errors.put("userid", "userid can't be void");
        else {
            try {
                userId = Integer.valueOf(userIdString);
            }
            catch (NumberFormatException ex){
                errors.put("userid", "userid must beinteger");
            }
        }

        User user = null;
        try {
            if (userId != null) user = userService.getById(userId);
        }
        catch (UserNotFoundException ex) {
            errors.put("user", "user with id " + userIdString + " not found");
        }

        try {
            if (user != null && clanId != null) userService.addToClan(user, clanId);
        } catch (ClanNotFoundException ex) {
            errors.put("clan", "clan with id " + clanIdString + " not found");
        }

        if (errors.isEmpty()) {
            answer = userJsonMapper.getJson(user);
            exchange.sendResponseHeaders(200, answer.getBytes(StandardCharsets.UTF_8).length);
        }
        else {
            answer = (new JsonMapper<>(HashMap.class)).getJson(errors);
            exchange.sendResponseHeaders(400, answer.getBytes(StandardCharsets.UTF_8).length);
        }
        os.write(answer.getBytes(StandardCharsets.UTF_8));
        os.close();


    }
}
