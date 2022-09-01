package ru.skytesttask.repository;

import ru.skytesttask.entity.Account;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;

public class AccountRepository {
    public Account getById(int id){
        return null;
    }
    public int create(Account account){
        ArrayList<ArrayList<Object>> params = new ArrayList<>();
        ArrayList<Object> scriptParams = new ArrayList<>();
        scriptParams.add(account.getOwnerId());
        scriptParams.add(account.getOwnerType());
        params.add(scriptParams);
        ScriptExecutor executor = new ScriptExecutor();
        executor.executeScript("account/create.sql", params);
       try {
           ResultSet keyResultSet = executor.getGeneratedKeys().get(0);
           keyResultSet.next();
           return (int) keyResultSet.getObject(1);
       }
        catch (SQLException ex){
            throw new RuntimeException(ex);
        }
    }
}
