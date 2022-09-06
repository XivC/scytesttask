package ru.skytesttask.webserver.hander.user;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.skytesttask.entity.User;
import ru.skytesttask.service.impl.UserService;
import ru.skytesttask.util.validation.exceptions.UserValidationException;
import ru.skytesttask.webserver.util.JsonMapper;
import ru.skytesttask.webserver.util.Util;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class CreateUserHandler implements HttpHandler {

    private final UserService userService;
    private final JsonMapper<User> userJsonMapper;

    public CreateUserHandler(){
        super();
        this.userService = new UserService(); //DI should be better for injecting dependencies.
        this.userJsonMapper = new JsonMapper<>(User.class);
    }
    @Override
    public void handle(HttpExchange exchange) throws IOException {

        Map<String, String> queryParams = Util.getQueryParams(exchange.getRequestURI().getQuery());
        OutputStream os = exchange.getResponseBody();
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        String name = queryParams.get("name");
        String answer;
        try {
            User user = userService.create(name);
            answer = userJsonMapper.getJson(user);
            exchange.sendResponseHeaders(201, answer.getBytes(StandardCharsets.UTF_8).length);
        }
        catch (UserValidationException ex){
            HashMap<Object, Object> errors = ex.getErrors();
            answer = (new JsonMapper<>(HashMap.class)).getJson(errors);
            exchange.sendResponseHeaders(400, answer.getBytes(StandardCharsets.UTF_8).length);
        }
        os.write(answer.getBytes(StandardCharsets.UTF_8));
        os.close();


    }
}
