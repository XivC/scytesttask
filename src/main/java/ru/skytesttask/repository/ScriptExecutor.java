package ru.skytesttask.repository;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

public class ScriptExecutor {
    private LinkedList<ResultSet> resultSets;
    private LinkedList<ResultSet> generatedKeys;

    public LinkedList<ResultSet> getResultSets() {
        return resultSets;
    }

    public LinkedList<ResultSet> getGeneratedKeys() {
        return generatedKeys;
    }

    public void executeScript(String scriptPath, ArrayList<ArrayList<Object>> params) {
        LinkedList<ResultSet> result = new LinkedList<>();
        LinkedList<ResultSet> keys = new LinkedList<>();
        try {
            Connection connection = Storage.getConnectionPool().getConnection();
            File scriptFile = Util.getResFile("storage/sql/" + scriptPath);
            try {
                Scanner scanner = new Scanner(scriptFile).useDelimiter(";");
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
                        }
                        else if (param instanceof String){
                            statement.setString(paramsCounter, (String) param);
                        }
                        else if (param instanceof Enum<?>) {
                            statement.setString(paramsCounter, ((Enum<?>) param).name());
                        }
                        else statement.setObject(paramsCounter, param);
                        paramsCounter++;
                    }
                    statement.execute();
                    keys.add(statement.getGeneratedKeys());
                    result.add(statement.getResultSet());


                    queryCounter++;

                }
                this.resultSets = result;
                this.generatedKeys = keys;



            } catch (FileNotFoundException ex) {
                throw new RuntimeException("File " + scriptPath + "not found");
            }
        }
        catch (SQLException ex){
            throw new RuntimeException(ex);
        }

    }



}
