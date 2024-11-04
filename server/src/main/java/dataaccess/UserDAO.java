package dataaccess;

import dataaccess.model.LoginReq;
import dataaccess.model.RegisterReq;
import model.AuthData;

public interface UserDAO {

    AuthData registerUser(RegisterReq registerReq) throws DataAccessException;

    AuthData loginUser(LoginReq loginReq) throws DataAccessException;

    void deleteAllUsers() throws DataAccessException;

}
