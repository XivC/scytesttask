package ru.skytesttask.webserver.hander.account;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.skytesttask.entity.Account;
import ru.skytesttask.service.IAccountService;
import ru.skytesttask.service.exceptions.AccountNotFoundException;
import ru.skytesttask.webserver.util.JsonMapper;
import ru.skytesttask.webserver.util.Util;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class GetAccountHistoryHandler implements HttpHandler {

    private final IAccountService accountService;

    public GetAccountHistoryHandler(IAccountService accountService) {
        super();
        this.accountService = accountService;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Map<String, String> queryParams = Util.getQueryParams(exchange.getRequestURI().getQuery());
        OutputStream os = exchange.getResponseBody();
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        HashMap<Object, Object> errors = new HashMap<>();
        Integer accountId = null;
        try {
            accountId = Integer.valueOf(queryParams.get("id"));
        } catch (NumberFormatException ex) {
            errors.put("id", "id must be integer");
        }

        Account account = null;
        try {
            if (accountId != null) account = accountService.getById(accountId);
            else errors.put("request", "You should pass  id to find account");
        } catch (AccountNotFoundException ex) {
            errors.put("identity", "account not found");
        }
        String answer;
        if (errors.isEmpty()) {
            answer = (new JsonMapper<>(LinkedList.class)).getJson(accountService.getAccountHistory(account));
            exchange.sendResponseHeaders(200, answer.getBytes(StandardCharsets.UTF_8).length);
        } else {
            answer = (new JsonMapper<>(HashMap.class)).getJson(errors);
            exchange.sendResponseHeaders(400, answer.getBytes(StandardCharsets.UTF_8).length);
        }
        os.write(answer.getBytes(StandardCharsets.UTF_8));
        os.close();


    }
}
