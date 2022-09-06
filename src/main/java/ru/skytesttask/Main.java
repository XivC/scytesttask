package ru.skytesttask;

import ru.skytesttask.repository.util.Storage;
import ru.skytesttask.webserver.Server;

public class Main {
    public static void main(String[] args) throws Exception {
        Storage.init();
        Server server = new Server();
        server.start(80);
    }

}
