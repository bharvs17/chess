package service;


import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import dataaccess.model.LoginReq;
import dataaccess.model.RegisterReq;
import model.AuthData;

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
