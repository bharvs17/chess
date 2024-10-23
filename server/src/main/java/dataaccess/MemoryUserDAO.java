package dataaccess;

import model.AuthData;
import model.UserData;
import java.util.HashMap;
import java.util.UUID;

public class MemoryUserDAO implements UserDAO {

    final private HashMap<String, UserData> users = new HashMap<>();

    @Override
    public AuthData createUser(UserData userData) throws DataAccessException { //also returns AuthData for a player logging in
        if(users.containsKey(userData.username())) {
            return new AuthData(UUID.randomUUID().toString(),userData.username());
        } else {
            users.put(userData.username(), userData);
        }
        return new AuthData(UUID.randomUUID().toString(),userData.username());
    }

    @Override
    public UserData getUser(String username, String password) throws DataAccessException {
        if(!users.containsKey(username)) {
            throw new DataAccessException(500, String.format("No username: %s in database.", username));
        } else {
            if(!users.get(username).password().equals(password)) {
                throw new DataAccessException(401, "Incorrect Password.");
            } else {
                return users.get(username);
            }
        }
    }

    @Override
    public UserData checkUser(String username) {
        return users.getOrDefault(username, null);
    }

    @Override
    public void deleteAllUsers() {
        users.clear();
    }
}
