package dataaccess;

import model.AuthData;
import model.UserData;
import java.util.HashMap;
import java.util.UUID;

public class MemoryUserDAO implements UserDAO {

    final private HashMap<String, UserData> users = new HashMap<>();

    @Override
    public AuthData createUser(UserData userData) throws DataAccessException {
        if(users.containsKey(userData.username())) {
            throw new DataAccessException("Username already in database.");
        } else {
            users.put(userData.username(), userData);
        }
        return new AuthData(UUID.randomUUID().toString(),userData.username());
    }

    @Override
    public UserData getUser(String username, String password) throws DataAccessException {
        if(!users.containsKey(username)) {
            throw new DataAccessException(String.format("No username: %s in database.", username));
        } else {
            if(!users.get(username).password().equals(password)) {
                throw new DataAccessException("Incorrect Password.");
            } else {
                return users.get(username);
            }
        }
    }

    @Override
    public void deleteAll() {
        users.clear();
    }
}
