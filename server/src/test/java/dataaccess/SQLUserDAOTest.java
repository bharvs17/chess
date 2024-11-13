package dataaccess;

import model.LoginReq;
import model.RegisterReq;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SQLUserDAOTest {

    SQLUserDAO udao;

    SQLUserDAOTest() throws DataAccessException {
        udao = new SQLUserDAO();
    }

    @BeforeEach
    public void clearALl() throws DataAccessException {
        udao.deleteAllUsers();
    }

    @Test
    void registerGoodUser() throws DataAccessException {
        Assertions.assertDoesNotThrow(() -> udao.registerUser(new RegisterReq("user","pass","email")));
    }

    @Test
    void registerDupeUser() throws DataAccessException {
        udao.registerUser(new RegisterReq("user","pass","email"));
        Assertions.assertThrows(DataAccessException.class, () -> udao.registerUser(new RegisterReq("user","p","e")));
    }

    @Test
    void loginGoodUser() throws DataAccessException {
        udao.registerUser(new RegisterReq("user","pass","email"));
        Assertions.assertDoesNotThrow(() -> udao.loginUser(new LoginReq("user","pass")));
    }

    @Test
    void loginBadPassUser() throws DataAccessException {
        udao.registerUser(new RegisterReq("user","pass","email"));
        Assertions.assertThrows(DataAccessException.class, () -> udao.loginUser(new LoginReq("user","pas")));
    }

    @Test
    void deleteAllUsers() throws DataAccessException {
        udao.registerUser(new RegisterReq("username","p","email"));
        Assertions.assertDoesNotThrow(() -> udao.deleteAllUsers());
    }
}