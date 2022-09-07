package ru.skytesttask.webserver.hander.transaction;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.skytesttask.entity.Transaction;
import ru.skytesttask.entity.User;
import ru.skytesttask.service.ITransactionService;
import ru.skytesttask.service.exceptions.TransactionNotFoundException;
import ru.skytesttask.service.exceptions.UserNotFoundException;
import ru.skytesttask.service.impl.TransactionService;
import ru.skytesttask.service.impl.UserService;
import ru.skytesttask.webserver.util.JsonMapper;
import ru.skytesttask.webserver.util.Util;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class GetTransactionHandler implements HttpHandler {
    private final ITransactionService transactionService;
    private final JsonMapper<Transaction> transactionJsonMapper;

    public GetTransactionHandler(ITransactionService transactionService){
        super();
        this.transactionService = transactionService;
        this.transactionJsonMapper = new JsonMapper<>(Transaction.class);
    }
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Map<String, String> queryParams = Util.getQueryParams(exchange.getRequestURI().getQuery());
        OutputStream os = exchange.getResponseBody();
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        HashMap<Object, Object> errors = new HashMap<>();
        Integer transactionId = null;
        try {
            transactionId = Integer.valueOf(queryParams.get("id"));
        }
        catch (NumberFormatException ex) {
             errors.put("id", "Id should be number");
        }

        Transaction transaction = null;
        try {
            if (transactionId != null) transaction = transactionService.getById(transactionId);
            else errors.put("request", "You should pass id to find transaction");
        }
        catch (TransactionNotFoundException ex) {
            errors.put("identity", "transaction not found");
        }
        String answer = "";
        if (errors.isEmpty()) {
            if (transaction != null) answer = transactionJsonMapper.getJson(transaction);
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
