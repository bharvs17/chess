package dataaccess;

import dataaccess.model.LoginReq;
import dataaccess.model.RegisterReq;
import model.AuthData;
import model.UserData;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class MemoryUserDAO implements UserDAO {

    final private HashMap<String, UserData> users = new HashMap<>();

    public AuthData registerUser(RegisterReq registerReq) throws DataAccessException {
        if(registerReq.username() == null || registerReq.password() == null || registerReq.email() == null) {
            throw new DataAccessException(400, "Error: bad request");
        }
        if(users.containsKey(registerReq.username())) {
            throw new DataAccessException(403, "Error: already taken");
        } else {
            users.put(registerReq.username(), new UserData(registerReq.username(),registerReq.password(),registerReq.email()));
        }
        return new AuthData(registerReq.username(),UUID.randomUUID().toString());
    }

    public AuthData loginUser(LoginReq loginReq) throws DataAccessException {
        if(!users.containsKey(loginReq.username())) {
            throw new DataAccessException(401, "Error: unauthorized");
        } else if(!users.get(loginReq.username()).password().equals(loginReq.password())) {
            throw new DataAccessException(401, "Error: unauthorized");
        } else {
            return new AuthData(loginReq.username(),UUID.randomUUID().toString());
        }
    }

    //OLD METHODS BELOW

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
                return null;
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
