package ru.skytesttask.webserver;

import com.sun.net.httpserver.HttpServer;
import ru.skytesttask.webserver.hander.clan.CreateClanHandler;
import ru.skytesttask.webserver.hander.clan.GetAllClansHandler;
import ru.skytesttask.webserver.hander.clan.GetClanHandler;
import ru.skytesttask.webserver.hander.transaction.ClanAddGoldToClanHandler;
import ru.skytesttask.webserver.hander.transaction.SystemAddGoldToClanHandler;
import ru.skytesttask.webserver.hander.transaction.UserAddGoldToClanHandler;
import ru.skytesttask.webserver.hander.user.CreateUserHandler;
import ru.skytesttask.webserver.hander.user.GetAllUsersHandler;
import ru.skytesttask.webserver.hander.user.GetUserHandler;
import ru.skytesttask.webserver.hander.user.UserAddToClanHandler;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Server {

        public void start(int port) throws IOException {

                HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
                server.createContext("/api/user/create", new CreateUserHandler());
                server.createContext("/api/user/get", new GetUserHandler());
                server.createContext("/api/user/getall", new GetAllUsersHandler());
                server.createContext("/api/user/addtoclan", new UserAddToClanHandler());
                server.createContext("/api/clan/get", new GetClanHandler());
                server.createContext("/api/clan/create", new CreateClanHandler());
                server.createContext("/api/clan/getall", new GetAllClansHandler());
                server.createContext("/api/account/get", new GetAllClansHandler());
                server.createContext("/api/transaction/usertoclan", new UserAddGoldToClanHandler());
                server.createContext("/api/transaction/clantoclan", new ClanAddGoldToClanHandler());
                server.createContext("/api/transaction/systemtoclan", new SystemAddGoldToClanHandler());

                server.setExecutor(null);
                server.start();
        }

        }
