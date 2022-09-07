package ru.skytesttask.webserver.hander.transaction;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.skytesttask.service.ITransactionService;
import ru.skytesttask.webserver.util.JsonMapper;
import ru.skytesttask.webserver.util.Util;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class GetAccountTransactionsHandler implements HttpHandler {

    private final ITransactionService transactionService;

    public GetAccountTransactionsHandler(ITransactionService transactionService) {
        super();
        this.transactionService = transactionService;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Map<String, String> queryParams = Util.getQueryParams(exchange.getRequestURI().getQuery());
        OutputStream os = exchange.getResponseBody();
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        HashMap<Object, Object> errors = new HashMap<>();
        String accountFromIdString = queryParams.get("accountfromid");
        String accountToIdString = queryParams.get("accounttoid");
        Integer accountFromId = null;
        Integer accountToId = null;

        try {
            if (accountFromIdString != null) accountFromId = Integer.valueOf(accountFromIdString);
        } catch (NumberFormatException ex) {
            errors.put("accountfromid", "Id should be number");
        }

        try {
            if (accountToIdString != null) accountToId = Integer.valueOf(accountToIdString);
        } catch (NumberFormatException ex) {
            errors.put("accounttoid", "Id should be number");
        }
        String answer;
        if (errors.isEmpty()) {
            answer = (new JsonMapper<>(LinkedList.class)).getJson(
                    transactionService.getAccountTransactions(accountFromId, accountToId)
            );
            exchange.sendResponseHeaders(200, answer.getBytes(StandardCharsets.UTF_8).length);
        } else {
            answer = (new JsonMapper<>(HashMap.class)).getJson(errors);
            exchange.sendResponseHeaders(400, answer.getBytes(StandardCharsets.UTF_8).length);
        }

        os.write(answer.getBytes(StandardCharsets.UTF_8));
        os.close();


    }
}
