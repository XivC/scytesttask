package ru.skytesttask.entity;

public class Clan implements Payable{
    private int id;
    private String name;
    private Integer accountId;


    public Clan(int id, String name, Integer accountId) {
        this.id = id;
        this.name = name;
        this.accountId = accountId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer account) {
        this.accountId = account;
    }
}
