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



}
