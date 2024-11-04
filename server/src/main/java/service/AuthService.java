package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import model.AuthData;

public class AuthService {

    private final MemoryAuthDAO authAccess;

    public AuthService(MemoryAuthDAO authAccess) {
        this.authAccess = authAccess;
    }

    public void addAuth(AuthData authData) throws DataAccessException {
        authAccess.addAuth(authData);
    }

    public void logout(String authToken) throws DataAccessException {
        authAccess.logout(authToken);
    }

    public void checkAuth(String authToken) throws DataAccessException {
        authAccess.checkAuth(authToken);
    }

    public String getUsername(String authToken) throws DataAccessException {
        return authAccess.getUsername(authToken);
    }

    public void deleteAllAuths() {
        try {
            authAccess.deleteAllAuths();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
