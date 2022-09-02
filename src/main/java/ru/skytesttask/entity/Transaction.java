package ru.skytesttask.entity;

import org.h2.util.json.JSONObject;

import java.time.LocalDateTime;

public class Transaction {
    private int id;
    private int fromId;
    private int toId;
    private int amount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private TransactionState state;
    private TransactionType type;


    public Transaction(int id, int fromId, int toId, int amount, LocalDateTime createdAt, LocalDateTime updatedAt, TransactionState state, TransactionType type) {
        this.id = id;
        this.fromId = fromId;
        this.toId = toId;
        this.amount = amount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.state = state;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFromId() {
        return fromId;
    }

    public void setFromId(int fromId) {
        this.fromId = fromId;
    }

    public int getToId() {
        return toId;
    }

    public void setToId(int to) {
        this.toId = to;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime created_at) {
        this.createdAt = created_at;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updated_at) {
        this.updatedAt = updated_at;
    }

    public TransactionState getState() {
        return state;
    }

    public void setState(TransactionState state) {
        this.state = state;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }
}
