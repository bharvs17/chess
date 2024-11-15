package dataaccess;

import exception.DataAccessException;
import model.AuthData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class SQLAuthDAOTest {

    SQLAuthDAO adao;

    public SQLAuthDAOTest() throws DataAccessException {
        adao = new SQLAuthDAO();
    }

    @BeforeEach
    public void clearALl() throws DataAccessException {
        adao.deleteAllAuths();
    }

    @Test
    void addGoodAuth() {
        Assertions.assertDoesNotThrow(() -> adao.addAuth(new AuthData("username","token")));
    }

    @Test
    void addNullAuth() {
        AuthData data = null;
        Assertions.assertThrows(DataAccessException.class, () -> adao.addAuth(data));
    }

    @Test
    void logoutValidAuth() throws DataAccessException {
        adao.addAuth(new AuthData("username","token"));
        Assertions.assertDoesNotThrow(() -> adao.logout("token"));
    }

    @Test
    void logoutBadAuth() throws DataAccessException {
        adao.addAuth(new AuthData("username","token"));
        Assertions.assertThrows(DataAccessException.class, () -> adao.logout("toke"));
    }

    @Test
    void checkGoodAuth() throws DataAccessException {
        adao.addAuth(new AuthData("user","token"));
        Assertions.assertDoesNotThrow(() -> adao.checkAuth("token"));
    }

    @Test
    void checkBadAuth() throws DataAccessException {
        adao.addAuth(new AuthData("user","token"));
        Assertions.assertThrows(DataAccessException.class, () -> adao.checkAuth("toke"));
    }

    @Test
    void getGoodUsername() throws DataAccessException {
        adao.addAuth(new AuthData("username","token"));
        Assertions.assertDoesNotThrow(() -> adao.getUsername("token"));
    }

    @Test
    void getBadUsername() throws DataAccessException {
        adao.addAuth(new AuthData("username","token"));
        Assertions.assertThrows(DataAccessException.class, () -> adao.getUsername("toke"));
    }

    @Test
    void deleteAllAuths() throws DataAccessException {
        adao.addAuth(new AuthData("user","t"));
        Assertions.assertDoesNotThrow(() -> adao.deleteAllAuths());
    }
}