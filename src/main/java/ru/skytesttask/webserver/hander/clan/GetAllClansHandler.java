package ru.skytesttask.webserver.hander.clan;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.skytesttask.entity.Clan;
import ru.skytesttask.service.IClanService;
import ru.skytesttask.service.impl.ClanService;
import ru.skytesttask.webserver.util.JsonMapper;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;

public class GetAllClansHandler implements HttpHandler {

    private final IClanService clanService;
    private final JsonMapper<LinkedList> clansListJsonMapper;

    public GetAllClansHandler(IClanService clanService){
        super();
        this.clanService = clanService;
        this.clansListJsonMapper = new JsonMapper<>(LinkedList.class);
    }
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        OutputStream os = exchange.getResponseBody();
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        String answer;
        LinkedList<Clan> clans = clanService.getAll();
        answer = clansListJsonMapper.getJson(clans);
        exchange.sendResponseHeaders(200, answer.getBytes(StandardCharsets.UTF_8).length);

        os.write(answer.getBytes(StandardCharsets.UTF_8));
        os.close();


    }
}

