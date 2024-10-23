package dataaccess;

import model.AuthData;

public interface AuthDAO {

    public void addAuth(AuthData authData) throws DataAccessException;

    public AuthData getAuth(String username) throws DataAccessException;

    public void logout(String authToken) throws DataAccessException;

    public void checkAuth(String authToken) throws DataAccessException;

    public String getUsername(String authToken) throws DataAccessException;

    //OLD BELOW

    public void createAuth(AuthData authData) throws DataAccessException;

    public void deleteAuth(String authToken) throws DataAccessException;

    public void deleteAllAuths();

    public boolean tokenInDatabase(String authToken);

}
