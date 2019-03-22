package dbservice;

import dbservice.dao.UsersDAO;
import datasets.UsersDataSet;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.internal.SessionFactoryImpl;
import org.hibernate.service.ServiceRegistry;

import java.io.IOException;
import java.io.InputStream;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Logger;

public class DBServiceImpl implements DBService {
    private static final String hibernate_show_sql = "true";
    private static final String hibernate_hbm2ddl_auto = "update";


    private final SessionFactory sessionFactory;
    private Configuration configuration;

    public DBServiceImpl() {
        this.configuration = getConfiguration();
        sessionFactory = createSessionFactory(configuration);
    }

    private Configuration getConfiguration() {
        configuration = new Configuration();

        Properties props = new Properties();
        Properties settings = new Properties();

        try (InputStream in = Files.newInputStream(Paths.get("database.properties"))) {
            props.load(in);
            settings.put(Environment.DRIVER, "com.mysql.cj.jdbc.Driver");
            settings.put(Environment.URL, props.getProperty("url"));
            settings.put(Environment.USER, props.getProperty("username"));
            settings.put(Environment.PASS, props.getProperty("password"));
            settings.put(Environment.DIALECT, "org.hibernate.dialect.MySQL8Dialect");
            settings.put(Environment.SHOW_SQL, hibernate_show_sql);
            settings.put(Environment.HBM2DDL_AUTO, hibernate_hbm2ddl_auto);
            settings.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");
            configuration.setProperties(settings);
            configuration.addAnnotatedClass(datasets.UsersDataSet.class);
        } catch (Exception e) {
            System.out.println("Exception in getConfiguration DBServiceImpl  method");
            e.printStackTrace();
        }
        return configuration;
    }

    public UsersDataSet getUserById(long id) throws DBException {
        try {
            Session session = sessionFactory.openSession();
            UsersDAO dao = new UsersDAO(session);
            UsersDataSet dataSet = dao.get(id);
            session.close();
            return dataSet;
        } catch (HibernateException e) {
            throw new DBException(e);
        }
    }

    public UsersDataSet getUserByLogin(String login) throws DBException {
        try {
            Session session = sessionFactory.openSession();
            UsersDAO dao = new UsersDAO(session);
            UsersDataSet dataSet = dao.get(login);
            session.close();
            return dataSet;
        } catch (HibernateException e) {
            throw new DBException(e);
        }
    }

    public long addUser(String login, String password) throws DBException {
        try {
            Session session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();
            UsersDAO dao = new UsersDAO(session);
            long id = dao.insertUser(login, password);
            transaction.commit();
            session.close();
            return id;
        } catch (HibernateException e) {
            throw new DBException(e);
        }
    }

    private static SessionFactory createSessionFactory(Configuration configuration) {
        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties()).build();
        return configuration.buildSessionFactory(serviceRegistry);
    }

    public void printConnectInfo() {
            try (Session session = sessionFactory.openSession()) {
                session.doWork(connection -> {
                    System.out.println("DB name: " + connection.getMetaData().getDatabaseProductName());
                    System.out.println("DB version: " + connection.getMetaData().getDatabaseProductVersion());
                    System.out.println("Driver: " + connection.getMetaData().getDriverName());
                    System.out.println("Autocommit: " + connection.getAutoCommit());
                });
            }

    }
}
