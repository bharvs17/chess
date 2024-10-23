package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import model.AuthData;

public class AuthService {

    private final MemoryAuthDAO authAccess;

    public AuthService(MemoryAuthDAO authAccess) {
        this.authAccess = authAccess;
    }

    public void createAuth(AuthData authData) throws DataAccessException {
        authAccess.createAuth(authData);
    }

    public void deleteAuth(String authToken) throws DataAccessException {
        authAccess.deleteAuth(authToken);
    }

    public void deleteAllAuths() {
        authAccess.deleteAllAuths();
    }

    public AuthData getAuth(AuthData authData) throws DataAccessException {
        return authAccess.getAuth(authData);
    }

    public AuthData getAuth(String authToken) throws DataAccessException {
        return authAccess.getAuth(authToken);
    }

    public boolean tokenInDatabase(String authToken) {
        return authAccess.tokenInDatabase(authToken);
    }

}
