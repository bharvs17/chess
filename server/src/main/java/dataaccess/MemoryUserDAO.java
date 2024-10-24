package dataaccess;

import dataaccess.model.LoginReq;
import dataaccess.model.RegisterReq;
import model.AuthData;
import model.UserData;
import java.util.HashMap;
import java.util.UUID;

public class MemoryUserDAO implements UserDAO {

    final private HashMap<String, UserData> users = new HashMap<>();
    String author = "h";

    @Override
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

    @Override
    public AuthData loginUser(LoginReq loginReq) throws DataAccessException {
        if(!users.containsKey(loginReq.username())) {
            throw new DataAccessException(401, "Error: unauthorized");
        } else if(!users.get(loginReq.username()).password().equals(loginReq.password())) {
            throw new DataAccessException(401, "Error: unauthorized");
        } else {
            String randUUID = UUID.randomUUID().toString();
            return new AuthData(loginReq.username(),randUUID);
        }
    }

    @Override
    public void deleteAllUsers() {
        users.clear();
    }

}
