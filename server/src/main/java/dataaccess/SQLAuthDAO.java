package dataaccess;

import model.AuthData;

public class SQLAuthDAO implements AuthDAO {

    @Override
    public void addAuth(AuthData authData) throws DataAccessException {

    }

    @Override
    public void logout(String authToken) throws DataAccessException {

    }

    @Override
    public void checkAuth(String authToken) throws DataAccessException {

    }

    @Override
    public String getUsername(String authToken) throws DataAccessException {
        return "";
    }

    @Override
    public void deleteAllAuths() {

    }

}
