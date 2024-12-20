package service;


import exception.DataAccessException;
import dataaccess.UserDAO;
import model.LoginReq;
import model.RegisterReq;
import model.AuthData;

import java.sql.SQLException;

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

    public void deleteAllUsers() {
        try {
            userAccess.deleteAllUsers();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
