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

    public void deleteAuth(AuthData authData) throws DataAccessException {
        authAccess.deleteAuth(authData);
    }

    public void deleteAllAuths() {
        authAccess.deleteAllAuths();
    }

    public AuthData getAuth(AuthData authData) throws DataAccessException {
        return authAccess.getAuth(authData);
    }

}
