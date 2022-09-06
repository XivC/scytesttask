package ru.skytesttask.webserver.hander.user;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.skytesttask.entity.User;
import ru.skytesttask.service.impl.UserService;
import ru.skytesttask.webserver.util.JsonMapper;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;

public class GetAllUsersHandler implements HttpHandler {

    private final UserService userService;
    private final JsonMapper<LinkedList> usersListJsonMapper;

    public GetAllUsersHandler(){
        super();
        this.userService = new UserService(); //DI should be better for injecting dependencies.
        this.usersListJsonMapper = new JsonMapper<>(LinkedList.class);
    }
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        OutputStream os = exchange.getResponseBody();
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        String answer;
        LinkedList<User> users = userService.getAll();
        answer = usersListJsonMapper.getJson(users);
        exchange.sendResponseHeaders(200, answer.getBytes(StandardCharsets.UTF_8).length);

        os.write(answer.getBytes(StandardCharsets.UTF_8));
        os.close();


    }
}

