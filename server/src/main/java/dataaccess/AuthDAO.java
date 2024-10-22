package dataaccess;

import model.AuthData;

public interface AuthDAO {

    public AuthData createAuth(AuthData authData) throws DataAccessException;

    public void deleteAuth(AuthData authData) throws DataAccessException;

    public AuthData getAuth(AuthData authData) throws DataAccessException;

}
