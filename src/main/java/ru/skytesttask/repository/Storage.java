package ru.skytesttask.repository;

import org.apache.ibatis.jdbc.ScriptRunner;
import org.h2.jdbcx.JdbcConnectionPool;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.logging.Logger;


public class Storage {
    private static JdbcConnectionPool pool;

    public static final Logger logger = Logger.getLogger(
            Storage.class.getName());

    public static JdbcConnectionPool getConnectionPool() {
        if (pool == null) {
            pool = JdbcConnectionPool.create("jdbc:h2:~/scytestdb", "sa", "");

        }
        return pool;
    }

    public static void init() {


        File orderFile = Util.getResFile("storage/sql/migrations/order");
        try {
            Scanner orderFileScanner = new Scanner(orderFile);
            ScriptExecutor executor = new ScriptExecutor();
            while (orderFileScanner.hasNext()) {
                String script = "migrations/" + orderFileScanner.nextLine();
                logger.info("Executing " + script);
                executor.executeScript(script, new ArrayList<>());

            }
        } catch (FileNotFoundException ex) {
            logger.warning(ex.getMessage());
        }


    }




}
