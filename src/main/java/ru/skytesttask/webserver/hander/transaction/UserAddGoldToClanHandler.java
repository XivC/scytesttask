package ru.skytesttask.webserver.hander.transaction;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.skytesttask.entity.Transaction;
import ru.skytesttask.entity.User;
import ru.skytesttask.service.exceptions.ClanNotFoundException;
import ru.skytesttask.service.exceptions.UserNotFoundException;
import ru.skytesttask.service.impl.TransactionService;
import ru.skytesttask.service.impl.UserService;
import ru.skytesttask.util.validation.exceptions.TransactionValidationException;
import ru.skytesttask.webserver.util.JsonMapper;
import ru.skytesttask.webserver.util.Util;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class UserAddGoldToClanHandler implements HttpHandler {

    private final TransactionService transactionService;
    private final UserService userService;
    private final JsonMapper<Transaction> transactionJsonMapper;

    public UserAddGoldToClanHandler(){
        this.transactionService = new TransactionService();
        this.userService = new UserService();
        this.transactionJsonMapper = new JsonMapper<>(Transaction.class);
    }
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Map<String, String> queryParams = Util.getQueryParams(exchange.getRequestURI().getQuery());
        OutputStream os = exchange.getResponseBody();
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        HashMap<Object, Object> errors = new HashMap<>();
        String userIdString = queryParams.get("userid");
        String amountString = queryParams.get("amount");
        Integer userId = null;
        Integer amount = null;

        try {
            userId = Integer.valueOf(userIdString);
        }
        catch (NumberFormatException ex) {
            errors.put("userid", "User id must be integer");
        }

        try{
            amount = Integer.valueOf(amountString);
        }
        catch (NumberFormatException ex) {
            errors.put("amount", "amount must be integer");
        }

        User user = null;
        try {
            if (userId != null) user = userService.getById(userId);
            else errors.put("userid", "userid is required param");
        }
        catch (UserNotFoundException ex){
            errors.put("identity", "user with id " + userIdString + " not found");
        }

        String answer = "";
        try {
            if (user != null && amount != null){

                Transaction transaction = transactionService.UserAddGoldToClan(user, amount);
                answer = transactionJsonMapper.getJson(transaction);

            }
        }
        catch (TransactionValidationException ex){
            errors.putAll(ex.getErrors());
        }
        catch (ClanNotFoundException ex) {
            errors.put("clan", "user not in clan");
        }
        if (errors.isEmpty()){
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
