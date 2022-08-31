package ru.skytesttask.entity;

import org.h2.util.json.JSONObject;

import java.time.LocalDateTime;

public class Transaction {
    private int id;
    private Account from;
    private Account to;
    private int amount;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
    private TransactionState state;
    private TransactionType type;
    private JSONObject info;

}
