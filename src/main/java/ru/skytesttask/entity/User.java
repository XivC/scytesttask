package ru.skytesttask.entity;

public class User implements Payable {
    private int id;
    private String name;
    private Integer accountId;
    private Integer clanId;

    public User(int id, String name, Integer accountId, Integer clanId) {
        this.id = id;
        this.name = name;
        this.accountId = accountId;
        this.clanId = clanId;
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

    public void setAccountId(int account) {
        this.accountId = account;
    }

    public Integer getClanId() {
        return clanId;
    }

    public void setClanId(int clanId) {
        this.clanId = clanId;
    }
}
