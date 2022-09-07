package ru.skytesttask.repository.util;

import org.h2.jdbcx.JdbcConnectionPool;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Logger;


public class Storage {
    private static JdbcConnectionPool pool;

    public static final Logger logger = Logger.getLogger(
            Storage.class.getName());

    public static JdbcConnectionPool getConnectionPool() {
        if (pool == null) {
            pool = JdbcConnectionPool.create("jdbc:h2:~/scytestdb", "sa", "");

            int b = 1;

        }
        int a = pool.getActiveConnections();
        return pool;
    }

    public static void init() {

        try {
            Scanner orderFileScanner = Util.getResFile("storage/sql/migrations/order");
            ScriptExecutor executor = new ScriptExecutor();
            while (orderFileScanner.hasNext()) {

                String script = "migrations/" + orderFileScanner.nextLine();
                logger.info("Executing " + script);
                executor.executeScript(script, new ArrayList<>());


            }
            executor.closeConnection();
        } catch (FileNotFoundException ex) {
            logger.warning(ex.getMessage());
        }


    }




}
