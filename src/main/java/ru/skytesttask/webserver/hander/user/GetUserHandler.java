package ru.skytesttask.webserver.hander.user;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.skytesttask.entity.User;
import ru.skytesttask.service.IUserService;
import ru.skytesttask.service.exceptions.UserNotFoundException;
import ru.skytesttask.webserver.util.JsonMapper;
import ru.skytesttask.webserver.util.Util;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class GetUserHandler implements HttpHandler {
    private final IUserService userService;
    private final JsonMapper<User> userJsonMapper;

    public GetUserHandler(IUserService userService) {
        super();
        this.userService = userService;
        this.userJsonMapper = new JsonMapper<>(User.class);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        Map<String, String> queryParams = Util.getQueryParams(exchange.getRequestURI().getQuery());
        OutputStream os = exchange.getResponseBody();
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        HashMap<Object, Object> errors = new HashMap<>();
        Integer userId = null;
        String name = queryParams.get("name");
        try {
            userId = Integer.valueOf(queryParams.get("id"));
        } catch (NumberFormatException ex) {
            if (name == null) errors.put("id", "Id should be number");
        }
        User user = null;
        try {
            if (userId != null) user = userService.getById(userId);
            else if (name != null) user = userService.getByName(name);
            else errors.put("request", "You should pass both id or name to find user");
        } catch (UserNotFoundException ex) {
            errors.put("identity", "user not found");
        }
        String answer;
        if (errors.isEmpty()) {
            answer = userJsonMapper.getJson(user);
            exchange.sendResponseHeaders(200, answer.getBytes(StandardCharsets.UTF_8).length);
        } else {
            answer = (new JsonMapper<>(HashMap.class)).getJson(errors);
            exchange.sendResponseHeaders(400, answer.getBytes(StandardCharsets.UTF_8).length);
        }

        os.write(answer.getBytes(StandardCharsets.UTF_8));
        os.close();


    }
}
