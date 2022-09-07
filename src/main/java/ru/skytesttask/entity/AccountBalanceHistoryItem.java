package ru.skytesttask.entity;

public class AccountBalanceHistoryItem implements Payable{
    Integer id;
    Integer balanceBefore;
    Integer balanceAfter;
    Integer accountId;
    Integer transactionId;

    public AccountBalanceHistoryItem(Integer id, Integer balanceBefore, Integer balanceAfter, Integer accountId, Integer transactionId) {
        this.id = id;
        this.balanceBefore = balanceBefore;
        this.balanceAfter = balanceAfter;
        this.accountId = accountId;
        this.transactionId = transactionId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBalanceBefore() {
        return balanceBefore;
    }

    public void setBalanceBefore(Integer balanceBefore) {
        this.balanceBefore = balanceBefore;
    }

    public Integer getBalanceAfter() {
        return balanceAfter;
    }

    public void setBalanceAfter(Integer balanceAfter) {
        this.balanceAfter = balanceAfter;
    }

    @Override
    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public Integer getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Integer transactionId) {
        this.transactionId = transactionId;
    }
}
