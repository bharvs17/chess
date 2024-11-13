package dataaccess;

import model.LoginReq;
import model.RegisterReq;
import model.AuthData;

import java.sql.SQLException;

public interface UserDAO {

    AuthData registerUser(RegisterReq registerReq) throws DataAccessException;

    AuthData loginUser(LoginReq loginReq) throws DataAccessException;

    void deleteAllUsers() throws DataAccessException;

}
