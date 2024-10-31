package dataaccess;

import dataaccess.model.LoginReq;
import dataaccess.model.RegisterReq;
import model.AuthData;

public class SQLUserDAO implements UserDAO {

    @Override
    public AuthData registerUser(RegisterReq registerReq) throws DataAccessException {
        return null;
    }

    @Override
    public AuthData loginUser(LoginReq loginReq) throws DataAccessException {
        return null;
    }

    @Override
    public void deleteAllUsers() {

    }

}
