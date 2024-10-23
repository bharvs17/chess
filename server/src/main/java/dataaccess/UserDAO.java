package dataaccess;

import dataaccess.model.LoginReq;
import dataaccess.model.RegisterReq;
import model.AuthData;
import model.UserData;

public interface UserDAO {

    public AuthData registerUser(RegisterReq registerReq) throws DataAccessException;

    public AuthData loginUser(LoginReq loginReq) throws DataAccessException;

    //OLD METHODS BELOW

    public AuthData createUser(UserData userData) throws DataAccessException;

    public UserData getUser(String username, String password) throws DataAccessException;

    public UserData checkUser(String username);

    public void deleteAllUsers();
}
