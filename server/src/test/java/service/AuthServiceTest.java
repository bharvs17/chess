package service;

import exception.DataAccessException;
import dataaccess.MemoryAuthDAO;
import model.AuthData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthServiceTest {

    private AuthService service = new AuthService(new MemoryAuthDAO());
    AuthData validAuth = new AuthData("username","uuid");

    @Test
    void addGoodAuth() throws DataAccessException {
        Assertions.assertDoesNotThrow(() -> service.addAuth(validAuth));
    }

    @Test
    void addBadAuth() throws DataAccessException {
        Assertions.assertThrows(DataAccessException.class, () -> service.addAuth(null));
    }

    @Test
    void validLogout() throws DataAccessException {
        service.addAuth(validAuth);
        Assertions.assertDoesNotThrow(() -> service.logout("uuid"));
    }

    @Test
    void invalidLogout() throws DataAccessException {
        service.addAuth(validAuth);
        Assertions.assertThrows(DataAccessException.class, () -> service.logout("notInDatabase"));
    }

    @Test
    void checkGoodAuth() throws DataAccessException {
        service.addAuth(validAuth);
        Assertions.assertDoesNotThrow(() -> service.checkAuth("uuid"));
    }

    @Test
    void checkBadAuth() throws DataAccessException {
        Assertions.assertThrows(DataAccessException.class, () -> service.checkAuth("notInDatabase"));
    }

    @Test
    void getGoodUsername() throws DataAccessException {
        service.addAuth(validAuth);
        Assertions.assertEquals("username",service.getUsername("uuid"));
    }

    @Test
    void getBadUsername() throws DataAccessException {
        service.addAuth(validAuth);
        Assertions.assertThrows(DataAccessException.class, () -> service.getUsername("notInDatabase"));
    }

    @Test
    void deleteAllAuths() throws DataAccessException {
        service.addAuth(validAuth);
        service.addAuth(new AuthData("user2","alsoUUID"));
        Assertions.assertDoesNotThrow(() -> service.checkAuth("uuid"));
        Assertions.assertDoesNotThrow(() -> service.checkAuth("alsoUUID"));
        service.deleteAllAuths();
        Assertions.assertThrows(DataAccessException.class, () -> service.checkAuth("uuid"));
        Assertions.assertThrows(DataAccessException.class, () -> service.checkAuth("alsoUUID"));
    }
}