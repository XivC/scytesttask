package ru.skytesttask.repository;

import ru.skytesttask.entity.Account;
import ru.skytesttask.entity.AccountOwnerType;
import ru.skytesttask.entity.User;
import ru.skytesttask.repository.util.ScriptExecutor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class AccountRepository {
    public Account getById(int id){
        ArrayList<ArrayList<Object>> params = new ArrayList<>();
        ArrayList<Object> scriptParams = new ArrayList<>();
        scriptParams.add(id);
        params.add(scriptParams);
        ScriptExecutor executor = new ScriptExecutor();
        executor.executeScript("account/get_by_id.sql", params);
        ResultSet fetchedAccount = executor.getResultSets().get(0);
        try {
            Account res = null;
            if (fetchedAccount.next()) {
                res = this.getByRow(fetchedAccount);
            }
            executor.closeConnection();
            return res;
        }
        catch (SQLException ex){
            throw new RuntimeException(ex);
        }
    }
    public int create(Account account){
        ArrayList<ArrayList<Object>> params = new ArrayList<>();
        ArrayList<Object> scriptParams = new ArrayList<>();
        scriptParams.add(account.getOwnerId());
        scriptParams.add(account.getOwnerType());
        scriptParams.add(account.getBalance());
        params.add(scriptParams);
        ScriptExecutor executor = new ScriptExecutor();
        executor.executeScript("account/create.sql", params);
       try {
           ResultSet keyResultSet = executor.getGeneratedKeys().get(0);
           keyResultSet.next();
           int res = (int) keyResultSet.getObject(1);
           executor.closeConnection();
           return res;
       }
        catch (SQLException ex){
            throw new RuntimeException(ex);
        }
    }

    public void update(Account account){
        ArrayList<ArrayList<Object>> params = new ArrayList<>();
        ArrayList<Object> scriptParams = new ArrayList<>();
        scriptParams.add(account.getOwnerId());
        scriptParams.add(account.getOwnerType());
        scriptParams.add(account.getBalance());
        scriptParams.add(account.getId());
        params.add(scriptParams);
        ScriptExecutor executor = new ScriptExecutor();
        executor.executeScript("account/update.sql", params);
        executor.closeConnection();

    }

    private Account getByRow(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt(1);
        int ownerId = resultSet.getInt(2);
        AccountOwnerType type = AccountOwnerType.valueOf(resultSet.getString(3));
        int balance = resultSet.getInt(4);
        return new Account(id, ownerId,  balance, type);
    }
}
