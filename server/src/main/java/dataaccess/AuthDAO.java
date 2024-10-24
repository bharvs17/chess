package dataaccess;

import model.AuthData;

public interface AuthDAO {

    void addAuth(AuthData authData) throws DataAccessException;

    void logout(String authToken) throws DataAccessException;

    void checkAuth(String authToken) throws DataAccessException;

    String getUsername(String authToken) throws DataAccessException;

    void deleteAllAuths();

}
