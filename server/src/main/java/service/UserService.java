package service;


import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import dataaccess.model.LoginReq;
import dataaccess.model.RegisterReq;
import model.AuthData;
import model.UserData;

public class UserService {

    private final UserDAO userAccess;

    public UserService(UserDAO userAccess) {
        this.userAccess = userAccess;
    }

    public AuthData registerUser(RegisterReq registerReq) throws DataAccessException {
        return userAccess.registerUser(registerReq);
    }

    public AuthData loginUser(LoginReq loginReq) throws DataAccessException {
        return userAccess.loginUser(loginReq);
    }

    //OLD METHODS BELOW

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
