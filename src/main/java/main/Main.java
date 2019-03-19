package main;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import servlets.AllRequestServlet;
import servlets.MirrorRequestServlet;


import java.sql.Connection;
import java.sql.Driver;

public class Main {

    public static void main(String[] args) throws Exception {
        AllRequestServlet allRequestServlet = new AllRequestServlet();
        MirrorRequestServlet mirrorRequestServlet = new MirrorRequestServlet();

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addServlet(
                new ServletHolder(allRequestServlet), "/all");
        context.addServlet(
                new ServletHolder(mirrorRequestServlet), "/mirror");

        Server server = new Server(8080);
        server.setHandler(context);

        server.start();
        java.util.logging.Logger.getGlobal().info("Server started");
        server.join();

    }

    public static void getDB() throws Exception {
        Connection connection = DBConnector.getConnection();
    }
}
