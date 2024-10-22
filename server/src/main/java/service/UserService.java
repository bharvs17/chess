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

    public AuthData createUser(UserData userData) throws DataAccessException {
        return userAccess.createUser(userData);
    }

    public void deleteAllUsers() {
        userAccess.deleteAllUsers();
    }

}
