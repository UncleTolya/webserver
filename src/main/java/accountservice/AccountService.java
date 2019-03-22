package accountservice;

import datasets.UsersDataSet;
import dbservice.DBException;
import dbservice.DBService;
import dbservice.DBServiceImpl;

public class AccountService {
    private DBService dbService;

    public AccountService(DBService dbService) {
        this.dbService = dbService;
    }


    public UsersDataSet getUser(String login, String password) throws DBException {
        UsersDataSet usersDataSet = dbService.getUserByLogin(login);
        if (usersDataSet == null) return null;
        return password.equals(usersDataSet.getPassword()) ? usersDataSet : null;
    }

    public long addNewUser(String login, String password) throws DBException {
        return dbService.addUser(login, password);
    }
}
