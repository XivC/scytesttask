package ru.skytesttask.entity;

public class Account {
    private int id;
    private Integer ownerId;
    private Integer balance;
    private AccountOwnerType ownerType;


    public Account(int id, Integer ownerId, Integer balance, AccountOwnerType ownerType) {
        this.id = id;
        this.ownerId = ownerId;
        this.balance = balance;
        this.ownerType = ownerType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Integer ownerId) {
        this.ownerId = ownerId;
    }

    public AccountOwnerType getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(AccountOwnerType ownerType) {
        this.ownerType = ownerType;
    }

    public Integer getBalance() {
        return balance;
    }

    public void setBalance(Integer balance) {
        this.balance = balance;
    }

}
