package dataaccess;

import model.AuthData;
import model.UserData;

public interface UserDAO {

    public AuthData createUser(UserData userData) throws DataAccessException;

    public UserData getUser(String username, String password) throws DataAccessException;

    public void deleteAllUsers();
}
