package ru.skytesttask.webserver;

import com.sun.net.httpserver.HttpServer;
import ru.skytesttask.service.impl.AccountService;
import ru.skytesttask.service.impl.ClanService;
import ru.skytesttask.service.impl.TransactionService;
import ru.skytesttask.service.impl.UserService;
import ru.skytesttask.webserver.hander.account.GetAccountHandler;
import ru.skytesttask.webserver.hander.account.GetAccountHistoryHandler;
import ru.skytesttask.webserver.hander.clan.CreateClanHandler;
import ru.skytesttask.webserver.hander.clan.GetAllClansHandler;
import ru.skytesttask.webserver.hander.clan.GetClanHandler;
import ru.skytesttask.webserver.hander.transaction.*;
import ru.skytesttask.webserver.hander.user.CreateUserHandler;
import ru.skytesttask.webserver.hander.user.GetAllUsersHandler;
import ru.skytesttask.webserver.hander.user.GetUserHandler;
import ru.skytesttask.webserver.hander.user.UserAddToClanHandler;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Server {

        public void start(int port) throws IOException {

                HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
                server.createContext("/api/user/create", new CreateUserHandler(new UserService()));
                server.createContext("/api/user/get", new GetUserHandler(new UserService()));
                server.createContext("/api/user/getall", new GetAllUsersHandler(new UserService()));
                server.createContext("/api/user/addtoclan", new UserAddToClanHandler(new UserService()));
                server.createContext("/api/clan/get", new GetClanHandler(new ClanService()));
                server.createContext("/api/clan/create", new CreateClanHandler(new ClanService()));
                server.createContext("/api/clan/getall", new GetAllClansHandler(new ClanService()));
                server.createContext("/api/account/get", new GetAccountHandler(new AccountService()));
                server.createContext("/api/account/history", new GetAccountHistoryHandler(new AccountService()));
                server.createContext("/api/transaction/get", new GetTransactionHandler(new TransactionService(new AccountService(), new ClanService())));
                server.createContext("/api/transaction/usertoclan", new UserAddGoldToClanHandler(new TransactionService(new AccountService(), new ClanService()), new UserService()));
                server.createContext("/api/transaction/clantoclan", new ClanAddGoldToClanHandler(new TransactionService(new AccountService(), new ClanService())));
                server.createContext("/api/transaction/systemtoclan", new SystemAddGoldToClanHandler(new TransactionService(new AccountService(), new ClanService())));
                server.createContext("/api/transaction/getbyaccount", new GetAccountTransactionsHandler(new TransactionService(new AccountService(), new ClanService())));
                server.setExecutor(null);
                server.start();
        }

        }
