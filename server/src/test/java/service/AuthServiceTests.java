package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import model.AuthData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class AuthServiceTests {

    private final AuthService authService = new AuthService(new MemoryAuthDAO());
    private final AuthData data = new AuthData("uuid","myusername");

    @Test
    @DisplayName("Create valid Auth")
    public void addValidAuth() throws DataAccessException {
        authService.createAuth(data);
        Assertions.assertEquals(data, authService.getAuth(data.authToken()));
    }

    @Test
    @DisplayName("Create invalid duplicate auth")
    public void addDupeAuth() throws DataAccessException {
        authService.createAuth(data);
        AuthData dupe = new AuthData("uuid","myusername1");
        Assertions.assertThrows(DataAccessException.class, () -> authService.createAuth(dupe));
    }

    @Test
    @DisplayName("Delete valid auth")
    public void deleteValidAuth() throws DataAccessException {
        authService.createAuth(data);
        Assertions.assertDoesNotThrow(() -> authService.deleteAuth("uuid"));
    }

    @Test
    @DisplayName("Delete invalid auth")
    public void deleteBadAuth() throws DataAccessException {
        authService.createAuth(data);
        Assertions.assertThrows(DataAccessException.class, () -> authService.deleteAuth("uid"));
    }

    @Test
    @DisplayName("Get valid auth")
    public void getGoodAuth() throws DataAccessException {
        authService.createAuth(data);
        Assertions.assertEquals(new AuthData("uuid","myusername"),authService.getAuth("uuid"));
    }

    @Test
    @DisplayName("Get invalid auth")
    public void getBadAuth() throws DataAccessException {
        authService.createAuth(data);
        Assertions.assertThrows(DataAccessException.class, () -> authService.getAuth("uid"));
    }

    @Test
    @DisplayName("Token in database")
    public void tokenInDB() throws DataAccessException {
        authService.createAuth(data);
        Assertions.assertTrue(() -> authService.tokenInDatabase("uuid"));
    }

    @Test
    @DisplayName("Token not in db")
    public void tokenNotInDB() throws DataAccessException {
        authService.createAuth(data);

        Assertions.assertFalse(() -> authService.tokenInDatabase("uid"));
    }

    @Test
    @DisplayName("Clear db")
    public void clear() throws DataAccessException {
        authService.createAuth(data);
        authService.createAuth(new AuthData("uuid2","user2"));
        authService.deleteAllAuths();
        Assertions.assertFalse(() -> authService.tokenInDatabase("uuid"));
        Assertions.assertFalse(() -> authService.tokenInDatabase("uuid2"));
    }

}
