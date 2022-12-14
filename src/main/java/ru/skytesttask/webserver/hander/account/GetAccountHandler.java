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
import java.util.Map;

public class GetAccountHandler implements HttpHandler {

    private final IAccountService accountService;
    private final JsonMapper<Account> accountJsonMapper;

    public GetAccountHandler(IAccountService accountService) {
        super();
        this.accountService = accountService;
        this.accountJsonMapper = new JsonMapper<>(Account.class);
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
            answer = accountJsonMapper.getJson(account);
            exchange.sendResponseHeaders(200, answer.getBytes(StandardCharsets.UTF_8).length);
        } else {
            answer = (new JsonMapper<>(HashMap.class)).getJson(errors);
            exchange.sendResponseHeaders(400, answer.getBytes(StandardCharsets.UTF_8).length);
        }

        os.write(answer.getBytes(StandardCharsets.UTF_8));
        os.close();


    }
}
