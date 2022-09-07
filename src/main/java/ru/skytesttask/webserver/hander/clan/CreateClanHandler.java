package ru.skytesttask.webserver.hander.clan;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.skytesttask.entity.Clan;
import ru.skytesttask.service.IClanService;
import ru.skytesttask.util.validation.exceptions.ClanValidationException;
import ru.skytesttask.webserver.util.JsonMapper;
import ru.skytesttask.webserver.util.Util;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class CreateClanHandler implements HttpHandler {

    private final IClanService clanService;
    private final JsonMapper<Clan> clanJsonMapper;

    public CreateClanHandler(IClanService clanService) {
        super();
        this.clanService = clanService;
        this.clanJsonMapper = new JsonMapper<>(Clan.class);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (!exchange.getRequestMethod().equals("POST")) return;
        Map<String, String> queryParams = Util.getQueryParams(exchange.getRequestURI().getQuery());
        OutputStream os = exchange.getResponseBody();
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        String name = queryParams.get("name");
        String answer;
        try {
            Clan clan = clanService.create(name);
            answer = clanJsonMapper.getJson(clan);
            exchange.sendResponseHeaders(201, answer.getBytes(StandardCharsets.UTF_8).length);
        } catch (ClanValidationException ex) {
            HashMap<Object, Object> errors = ex.getErrors();
            answer = (new JsonMapper<>(HashMap.class)).getJson(errors);
            exchange.sendResponseHeaders(400, answer.getBytes(StandardCharsets.UTF_8).length);
        }
        os.write(answer.getBytes(StandardCharsets.UTF_8));
        os.close();


    }
}
