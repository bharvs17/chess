package service;

import exception.DataAccessException;
import dataaccess.MemoryUserDAO;
import model.LoginReq;
import model.RegisterReq;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.engine.descriptor.DynamicDescendantFilter;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    private UserService service = new UserService(new MemoryUserDAO());
    private RegisterReq req = new RegisterReq("username","password","email");

    @Test
    void registerGoodUser() throws DataAccessException {
        Assertions.assertDoesNotThrow(() -> service.registerUser(req));
    }

    @Test
    void registerBadUser() throws DataAccessException {
        service.registerUser(req);
        Assertions.assertThrows(DataAccessException.class, () -> service.registerUser(new RegisterReq("username","password","email@com")));
    }

    @Test
    void loginGoodUser() throws DataAccessException {
        service.registerUser(req);
        Assertions.assertDoesNotThrow(() -> service.loginUser(new LoginReq("username","password")));
    }

    @Test
    void loginBadUser() throws DataAccessException {
        service.registerUser(req);
        Assertions.assertThrows(DataAccessException.class, () -> service.loginUser(new LoginReq("username","wrongpass")));
    }

    @Test
    void deleteAllUsers() throws DataAccessException {
        service.registerUser(req);
        service.registerUser(new RegisterReq("username2","otherpass","otheremail"));
        service.deleteAllUsers();
        Assertions.assertThrows(DataAccessException.class, () -> service.loginUser(new LoginReq("username","password")));
        Assertions.assertThrows(DataAccessException.class, () -> service.loginUser(new LoginReq("username2","otherpass")));
    }
}