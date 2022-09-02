package ru.skytesttask.repository;

import ru.skytesttask.entity.User;
import ru.skytesttask.repository.util.ScriptExecutor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UserRepository {
    public User getByName(String name){
            ArrayList<ArrayList<Object>> params = new ArrayList<>();
            ArrayList<Object> scriptParams = new ArrayList<>();
            scriptParams.add(name);
            params.add(scriptParams);
            ScriptExecutor executor = new ScriptExecutor();
            executor.executeScript("user/get_by_name.sql", params );
            ResultSet targetResult = executor.getResultSets().get(0);
            try {
                if (!targetResult.next()) return null;
                User res = getByRow(targetResult);
                executor.closeConnection();
                return res;

            }
            catch (SQLException ex){
                throw new RuntimeException(ex);
            }


    }

    public int create(User user){
        ArrayList<ArrayList<Object>> params = new ArrayList<>();
        ArrayList<Object> scriptParams = new ArrayList<>();
        scriptParams.add(user.getName());
        scriptParams.add(user.getClanId());
        scriptParams.add(user.getAccountId());
        params.add(scriptParams);
        ScriptExecutor executor = new ScriptExecutor();
        executor.executeScript("user/create.sql", params);
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

    public void update(User user) {
        ArrayList<ArrayList<Object>> params = new ArrayList<>();
        ArrayList<Object> scriptParams = new ArrayList<>();
        scriptParams.add(user.getName());
        scriptParams.add(user.getClanId());
        scriptParams.add(user.getAccountId());
        scriptParams.add(user.getId());
        params.add(scriptParams);
        ScriptExecutor executor = new ScriptExecutor();
        executor.executeScript("user/update.sql", params);
        executor.closeConnection();
    }

    public User getById(int userId){
        ArrayList<ArrayList<Object>> params = new ArrayList<>();
        ArrayList<Object> scriptParams = new ArrayList<>();
        scriptParams.add(userId);
        params.add(scriptParams);
        ScriptExecutor executor = new ScriptExecutor();
        executor.executeScript("user/get_by_id.sql", params);
        ResultSet targetResult = executor.getResultSets().get(0);
        try {
            if (!targetResult.next()) {
                executor.closeConnection();
                return null;
            }
            User res = getByRow(targetResult);
            executor.closeConnection();
            return res;

        }
        catch (SQLException ex){
            throw new RuntimeException(ex);
        }

    }

    private User getByRow(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt(1);
        String name = resultSet.getString(2);
        int clanId = resultSet.getInt(3);
        int accountId = resultSet.getInt(4);
        return new User(id, name, accountId, clanId);
    }

}
