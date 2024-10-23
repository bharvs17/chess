package service;


import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.AuthData;
import model.UserData;

public class UserService {

    private final UserDAO userAccess;

    public UserService(UserDAO userAccess) {
        this.userAccess = userAccess;
    }

    public UserData getUser(String username, String password) throws DataAccessException {
        return userAccess.getUser(username, password);
    }

    public UserData checkUser(String username) {
        return userAccess.checkUser(username);
    }

    public AuthData createUser(UserData userData) throws DataAccessException {
        return userAccess.createUser(userData);
    }

    public void deleteAllUsers() {
        userAccess.deleteAllUsers();
    }

}
