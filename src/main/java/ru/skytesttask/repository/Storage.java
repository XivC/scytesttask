package ru.skytesttask.repository;

import org.apache.ibatis.jdbc.ScriptRunner;
import org.h2.jdbcx.JdbcConnectionPool;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
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

    public static void init() throws SQLException {
        logger.info("Executing migrations scripts");
        Connection connection = Storage.getConnectionPool().getConnection();
        ScriptRunner scriptRunner = new ScriptRunner(connection);
        File orderFile = getResFile("storage/sql/migrations/order");
        try {
            Scanner orderFileScanner = new Scanner(orderFile);
            while (orderFileScanner.hasNext()) {
                File script = getResFile("storage/sql/migrations/" + orderFileScanner.nextLine());
                logger.info("Executing " + script.getName());
                Reader reader = new BufferedReader(new FileReader(script));

                scriptRunner.runScript(reader);

            }
        } catch (FileNotFoundException ex) {
            logger.warning(ex.getMessage());
        }


    }

    public static File getResFile(String path) {
        ClassLoader classLoader = Storage.class.getClassLoader();
        URL resource = classLoader.getResource(path);
        if (resource == null) {
            throw new IllegalArgumentException("No file found " + path);
        } else {
            try {
                return new File(resource.toURI());
            } catch (URISyntaxException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

}
