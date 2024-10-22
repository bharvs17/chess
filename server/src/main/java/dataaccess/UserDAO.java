package dataaccess;

import model.UserData;

public interface UserDAO {

    public UserData getUser(String username, String password) throws DataAccessException;

    public void createUser(UserData userData) throws DataAccessException;

    public void deleteAll() throws DataAccessException;
}
