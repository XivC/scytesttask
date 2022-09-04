package ru.skytesttask.repository;

import ru.skytesttask.entity.Account;
import ru.skytesttask.entity.Clan;
import ru.skytesttask.repository.util.ScriptExecutor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ClanRepository {
    public Clan getById(int id){
        ArrayList<ArrayList<Object>> params = new ArrayList<>();
        ArrayList<Object> scriptParams = new ArrayList<>();
        scriptParams.add(id);
        params.add(scriptParams);
        ScriptExecutor executor = new ScriptExecutor();
        executor.executeScript("clan/get_by_id.sql", params);
        ResultSet fetched = executor.getResultSets().get(0);
        try {
            Clan res = null;
            if (fetched.next()) {
                res = this.getByRow(fetched);
            }
            executor.closeConnection();
            return res;
        }
        catch (SQLException ex){
            throw new RuntimeException(ex);
        }
    }

    public Clan getByName(String name){
        ArrayList<ArrayList<Object>> params = new ArrayList<>();
        ArrayList<Object> scriptParams = new ArrayList<>();
        scriptParams.add(name);
        params.add(scriptParams);
        ScriptExecutor executor = new ScriptExecutor();
        executor.executeScript("clan/get_by_name.sql", params);
        ResultSet fetched = executor.getResultSets().get(0);
        try {
            Clan res = null;
            if (fetched.next()) {
                res = this.getByRow(fetched);
            }
            executor.closeConnection();
            return res;
        }
        catch (SQLException ex){
            throw new RuntimeException(ex);
        }
    }

    public int create(Clan clan){
        ArrayList<ArrayList<Object>> params = new ArrayList<>();
        ArrayList<Object> scriptParams = new ArrayList<>();
        scriptParams.add(clan.getName());
        scriptParams.add(clan.getAccountId());
        params.add(scriptParams);
        ScriptExecutor executor = new ScriptExecutor();
        executor.executeScript("clan/create.sql", params);
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

    public void update(Clan clan){
        ArrayList<ArrayList<Object>> params = new ArrayList<>();
        ArrayList<Object> scriptParams = new ArrayList<>();
        scriptParams.add(clan.getName());
        scriptParams.add(clan.getAccountId());
        scriptParams.add(clan.getId());
        params.add(scriptParams);
        ScriptExecutor executor = new ScriptExecutor();
        executor.executeScript("clan/update.sql", params);
        executor.closeConnection();

    }

    private Clan getByRow(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt(1);
        String name = resultSet.getString(2);
        int accountId = resultSet.getInt(3);
        return new Clan(id, name, accountId);
    }
}
