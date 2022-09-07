package ru.skytesttask.repository.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

public class ScriptExecutor {
    private LinkedList<ResultSet> resultSets;
    private LinkedList<ResultSet> generatedKeys;
    private final Connection connection;

    public ScriptExecutor(){
        try {
            this.connection = Storage.getConnectionPool().getConnection();
        }
        catch (SQLException ex){
            throw new RuntimeException(ex);
        }
    }
    public LinkedList<ResultSet> getResultSets() {
        return resultSets;
    }

    public LinkedList<ResultSet> getGeneratedKeys() {
        return generatedKeys;
    }

    public void closeConnection(){
        try {
            this.connection.close();
        }
        catch (SQLException ex){
            throw new RuntimeException(ex);
        }
    }

    private void executeScript(String scriptPath, ArrayList<ArrayList<Object>> params, boolean autocommit) {
        LinkedList<ResultSet> result = new LinkedList<>();
        LinkedList<ResultSet> keys = new LinkedList<>();
        try {

            connection.setAutoCommit(autocommit);
            try {
                Scanner scanner = Util.getResFile("storage/sql/" + scriptPath).useDelimiter(";");
                int queryCounter = 0;

                while (scanner.hasNext()) {
                    String query = scanner.next();
                    ArrayList<Object> queryParams;
                    try {
                        queryParams = params.get(queryCounter);
                    } catch (IndexOutOfBoundsException ex) {
                        queryParams = new ArrayList<>();
                    }
                    PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                    int paramsCounter = 1;
                    for (Object param : queryParams) {
                        if (param instanceof Integer) {
                            statement.setInt(paramsCounter, (Integer) param);
                        } else if (param instanceof String) {
                            statement.setString(paramsCounter, (String) param);
                        } else if (param instanceof Enum<?>) {
                            statement.setString(paramsCounter, ((Enum<?>) param).name());
                        } else statement.setObject(paramsCounter, param);
                        paramsCounter++;
                    }
                    statement.execute();
                    keys.add(statement.getGeneratedKeys());
                    result.add(statement.getResultSet());


                    queryCounter++;

                }
                if (!autocommit) {connection.commit();}

                this.resultSets = result;
                this.generatedKeys = keys;



            } catch (FileNotFoundException ex) {
                throw new RuntimeException("File " + scriptPath + "not found");

            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }


    }


    public void executeScript(String scriptPath, ArrayList<ArrayList<Object>> params) {
        this.executeScript(scriptPath, params, true);
    }

    public void executeTransaction(String scriptPath, ArrayList<ArrayList<Object>> params){
        this.executeScript(scriptPath, params, false);
    }
}
