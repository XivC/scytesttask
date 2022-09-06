package ru.skytesttask.webserver.hander.clan;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.skytesttask.entity.Clan;
import ru.skytesttask.service.exceptions.ClanNotFoundException;
import ru.skytesttask.service.impl.ClanService;
import ru.skytesttask.webserver.util.JsonMapper;
import ru.skytesttask.webserver.util.Util;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class GetClanHandler implements HttpHandler {
    private final ClanService clanService;
    private final JsonMapper<Clan> clanJsonMapper;

    public GetClanHandler(){
        super();
        this.clanService = new ClanService(); //DI should be better for injecting dependencies.
        this.clanJsonMapper = new JsonMapper<>(Clan.class);
    }
    @Override
    public void handle(HttpExchange exchange) throws IOException {

        Map<String, String> queryParams = Util.getQueryParams(exchange.getRequestURI().getQuery());
        OutputStream os = exchange.getResponseBody();
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        HashMap<Object, Object> errors = new HashMap<>();
        Integer clanId = null;
        String name = queryParams.get("name");;
        try {
            clanId = Integer.valueOf(queryParams.get("id"));
        }
        catch (NumberFormatException ex) {
            if (name == null) errors.put("id", "Id should be number");
        }
        Clan clan = null;
        try {
            if (clanId != null) clan = clanService.getById(clanId);
            else if (name != null) clan = clanService.getByName(name);
            else errors.put("request", "You should pass both id or name to find clan");
        }
        catch (ClanNotFoundException ex) {
            errors.put("identity", "clan not found");
        }
        String answer;
        if (errors.isEmpty()) {
            answer = clanJsonMapper.getJson(clan);
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
