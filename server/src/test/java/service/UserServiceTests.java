package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryUserDAO;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class UserServiceTests {

    private final UserService service = new UserService(new MemoryUserDAO());
    private final UserData data = new UserData("user","pass","email");

    @Test
    @DisplayName("get valid user")
    public void getValidUser() throws DataAccessException {
        service.createUser(data);
        Assertions.assertEquals(data,service.getUser("user","pass"));
    }

    @Test
    @DisplayName("get invalid user")
    public void getInvalidUser() throws DataAccessException {
        service.createUser(data);
        Assertions.assertThrows(DataAccessException.class, () -> service.getUser("user1","pass"));
    }

    @Test
    @DisplayName("check good user")
    public void checkGoodUser() throws DataAccessException {
        service.createUser(data);
        Assertions.assertEquals(data,service.checkUser("user"));
    }

    @Test
    @DisplayName("check bad user")
    public void checkBadUser() throws DataAccessException {
        service.createUser(data);
        Assertions.assertNull(service.checkUser("user1"));
    }

    @Test
    @DisplayName("create new user")
    public void createNewUser() throws DataAccessException {
        Assertions.assertDoesNotThrow(() -> service.createUser(data));
    }

    @Test
    @DisplayName("create existing user")
    public void createExisUser() throws DataAccessException {
        Assertions.assertNotEquals(service.createUser(data),service.createUser(data));
    }

    @Test
    @DisplayName("delete all")
    public void deleteAll() throws DataAccessException {
        service.createUser(data);
        service.deleteAllUsers();
        Assertions.assertThrows(DataAccessException.class, () -> service.getUser("user","pass"));
    }

}
