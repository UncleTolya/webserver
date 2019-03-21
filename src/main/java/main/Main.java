package main;

import accountservice.AccountService;
import datasets.UsersDataSet;
import dbservice.DBException;
import dbservice.DBService;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import servlets.SignInServlet;
import servlets.SignUpServlet;

import java.math.BigInteger;
import java.util.logging.Logger;


public class Main {

    public static void main(String[] args) throws Exception {
        DBService dbService = new DBService();
//        dbService.createTable();
        dbService.printConnectInfo();
//
//        try {
//            long userId = dbService.addUser("tully", "pass");
//            System.out.println("Added datasets id: " + userId);
//
//            UsersDataSet dataSet = dbService.getUserById(userId);
//            System.out.println("UsersDataSet data set: " + dataSet);
//
//        } catch (DBException e) {
//            e.printStackTrace();
//        }

        AccountService accountService = new AccountService(dbService);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addServlet(new ServletHolder(new SignUpServlet(accountService)), "/signup");
        context.addServlet(new ServletHolder(new SignInServlet(accountService)), "/signin");

        ResourceHandler resource_handler = new ResourceHandler();
        resource_handler.setResourceBase("index.html");

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{resource_handler, context});

        Server server = new Server(8080);
        server.setHandler(handlers);

        server.start();
        Logger.getLogger("name").info("Server started");
        server.join();
    }
}
