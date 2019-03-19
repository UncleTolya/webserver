package main;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class DBConnector {

    public static Connection getConnection() throws Exception {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
            Properties props = new Properties();
            try (InputStream in = Files.newInputStream(Paths.get("database.properties"))) {
                props.load(in);
            }
            String url = props.getProperty("url");
            String username = props.getProperty("username");
            String password = props.getProperty("password");
            return DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            System.out.println("Exception in getConnection method");
            throw new Exception(e);
        }
    }

}
