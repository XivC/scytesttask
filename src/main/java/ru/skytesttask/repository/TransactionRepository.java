package ru.skytesttask.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.skytesttask.entity.Transaction;
import ru.skytesttask.entity.TransactionState;
import ru.skytesttask.entity.TransactionType;
import ru.skytesttask.repository.util.ScriptExecutor;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;

public class TransactionRepository {
    public Transaction getById(int id) {
        ArrayList<ArrayList<Object>> params = new ArrayList<>();
        ArrayList<Object> scriptParams = new ArrayList<>();
        scriptParams.add(id);
        params.add(scriptParams);
        ScriptExecutor executor = new ScriptExecutor();
        executor.executeScript("transaction/get_by_id.sql", params);
        ResultSet fetchedAccount = executor.getResultSets().get(0);
        try {
            Transaction res = null;
            if (fetchedAccount.next()) {
                res = this.getByRow(fetchedAccount);
            }
            executor.closeConnection();
            return res;
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public int create(Transaction transaction) {
        ArrayList<ArrayList<Object>> params = new ArrayList<>();
        ArrayList<Object> scriptParams = new ArrayList<>();
        scriptParams.add(transaction.getFromId());
        scriptParams.add(transaction.getToId());
        scriptParams.add(transaction.getAmount());
        scriptParams.add(transaction.getCreatedAt());
        scriptParams.add(transaction.getState());
        scriptParams.add(transaction.getType());
        scriptParams.add(transaction.getInfoJson());
        params.add(scriptParams);
        ScriptExecutor executor = new ScriptExecutor();
        executor.executeScript("transaction/create.sql", params);
        try {
            ResultSet keyResultSet = executor.getGeneratedKeys().get(0);
            keyResultSet.next();
            int res = (int) keyResultSet.getObject(1);
            executor.closeConnection();
            return res;
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void update(Transaction transaction) {
        ArrayList<ArrayList<Object>> params = new ArrayList<>();
        ArrayList<Object> scriptParams = new ArrayList<>();
        scriptParams.add(transaction.getFromId());
        scriptParams.add(transaction.getToId());
        scriptParams.add(transaction.getAmount());
        scriptParams.add(transaction.getCreatedAt());
        scriptParams.add(transaction.getUpdatedAt());
        scriptParams.add(transaction.getState());
        scriptParams.add(transaction.getType());
        scriptParams.add(transaction.getInfoJson());
        scriptParams.add(transaction.getId());

        params.add(scriptParams);
        ScriptExecutor executor = new ScriptExecutor();
        executor.executeScript("transaction/update.sql", params);
        executor.closeConnection();
    }

    public void perform(Transaction transaction) {
        ArrayList<ArrayList<Object>> params = new ArrayList<>();
        ArrayList<Object> scriptParams1 = new ArrayList<>();
        ArrayList<Object> scriptParams2 = new ArrayList<>();
        ArrayList<Object> scriptParams3 = new ArrayList<>();
        ArrayList<Object> scriptParams4 = new ArrayList<>();
        scriptParams1.add(transaction.getAmount());
        scriptParams2.add(transaction.getFromId());
        scriptParams3.add(transaction.getToId());
        scriptParams4.add(transaction.getId());
        params.add(scriptParams1);
        params.add(scriptParams2);
        params.add(scriptParams3);
        params.add(scriptParams4);
        ScriptExecutor executor = new ScriptExecutor();
        executor.executeTransaction("transaction/perform.sql", params);
        executor.closeConnection();
    }

    public LinkedList<Transaction> getAccountTransactions(Integer from, Integer to) {
        LinkedList<Transaction> result = new LinkedList<>();
        ArrayList<ArrayList<Object>> params = new ArrayList<>();
        ArrayList<Object> scriptParams = new ArrayList<>();
        params.add(scriptParams);
        ScriptExecutor executor = new ScriptExecutor();
        if (from == null && to == null) return result;
        if (from == null) {
            scriptParams.add(to);
            executor.executeScript("transaction/get_account_to_transactions.sql", params);
        } else if (to == null) {
            scriptParams.add(from);
            executor.executeScript("transaction/get_account_from_transactions.sql", params);
        } else {
            scriptParams.add(from);
            scriptParams.add(to);
            executor.executeScript("transaction/get_account_from_to_transactions.sql", params);
        }
        ResultSet resultSet = executor.getResultSets().get(0);
        try {
            while (resultSet.next()) {
                result.add(getByRow(resultSet));
            }
            return result;
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }


    private Transaction getByRow(ResultSet resultSet) throws SQLException {
        ObjectMapper mapper = new ObjectMapper();
        String info;
        try {
            info = mapper.readTree(resultSet.getString(9)).asText();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        return new Transaction(
                resultSet.getInt(1),
                resultSet.getInt(2),
                resultSet.getInt(3),
                resultSet.getInt(4),
                resultSet.getObject(5, LocalDateTime.class),
                resultSet.getObject(6, LocalDateTime.class),
                TransactionState.valueOf(resultSet.getString(7)),
                TransactionType.valueOf(resultSet.getString(8)),
                info


        );
    }

}
