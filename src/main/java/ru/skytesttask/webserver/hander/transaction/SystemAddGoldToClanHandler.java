package ru.skytesttask.webserver.hander.transaction;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.skytesttask.entity.Clan;
import ru.skytesttask.entity.Transaction;
import ru.skytesttask.service.IClanService;
import ru.skytesttask.service.ITransactionService;
import ru.skytesttask.service.exceptions.ClanNotFoundException;
import ru.skytesttask.util.validation.exceptions.TransactionValidationException;
import ru.skytesttask.webserver.util.JsonMapper;
import ru.skytesttask.webserver.util.Util;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class SystemAddGoldToClanHandler implements HttpHandler {

    private final ITransactionService transactionService;
    private final IClanService clanService;
    private final JsonMapper<Transaction> transactionJsonMapper;

    public SystemAddGoldToClanHandler(ITransactionService transactionService) {
        this.transactionService = transactionService;
        this.clanService = transactionService.getClanService();
        this.transactionJsonMapper = new JsonMapper<>(Transaction.class);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (!exchange.getRequestMethod().equals("POST")) return;
        Map<String, String> queryParams = Util.getQueryParams(exchange.getRequestURI().getQuery());
        OutputStream os = exchange.getResponseBody();
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        HashMap<Object, Object> errors = new HashMap<>();
        String clanIdString = queryParams.get("clanid");
        String amountString = queryParams.get("amount");
        Integer clanId = null;
        Integer amount = null;

        try {
            clanId = Integer.valueOf(clanIdString);
        } catch (NumberFormatException ex) {
            errors.put("clanid", "Clan id must be integer");
        }

        try {
            amount = Integer.valueOf(amountString);
        } catch (NumberFormatException ex) {
            errors.put("amount", "amount must be integer");
        }

        Clan clan = null;

        try {
            if (clanId != null) clan = clanService.getById(clanId);
            else errors.put("clanid", "clanidfrom is required param");
        } catch (ClanNotFoundException ex) {
            errors.put("clan", "clan with id " + clanIdString + " not found");
        }

        try {
            if (clanId != null) clan = clanService.getById(clanId);
            else errors.put("clanid", "clanid is required param");
        } catch (ClanNotFoundException ex) {
            errors.put("clan", "clan with id " + clanIdString + " not found");
        }

        String answer = "";
        try {
            if (clan != null && amount != null) {

                Transaction transaction = transactionService.systemAddGoldToClan(clan, amount);
                answer = transactionJsonMapper.getJson(transaction);

            }
        } catch (TransactionValidationException ex) {
            errors.putAll(ex.getErrors());
        }

        if (errors.isEmpty()) {
            exchange.sendResponseHeaders(200, answer.getBytes(StandardCharsets.UTF_8).length);

        } else {
            answer = (new JsonMapper<>(HashMap.class)).getJson(errors);
            exchange.sendResponseHeaders(400, answer.getBytes(StandardCharsets.UTF_8).length);
        }
        os.write(answer.getBytes(StandardCharsets.UTF_8));
        os.close();


    }
}
