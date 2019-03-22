package dbservice;

import datasets.UsersDataSet;

public interface DBService {
    void printConnectInfo();

    UsersDataSet getUserByLogin(String login) throws DBException;

    long addUser(String login, String password) throws DBException;
}
