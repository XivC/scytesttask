package ru.skytesttask;

import ru.skytesttask.repository.Storage;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {

        Storage.init();
    }
}
