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

    

}
