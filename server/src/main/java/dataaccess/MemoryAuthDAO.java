package dataaccess;

import model.AuthData;
import java.util.HashMap;

public class MemoryAuthDAO implements AuthDAO {

    final private HashMap<String, AuthData> auths = new HashMap<>();

    @Override
    public void createAuth(AuthData authData) throws DataAccessException {
        if(auths.containsKey(authData.authToken())) {
            throw new DataAccessException(500, "Auth already in database.");
        } else {
            auths.put(authData.authToken(), authData);
        }
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        if(!auths.containsKey(authToken)) {
            throw new DataAccessException(500, "No such auth in database");
        } else {
            auths.remove(authToken);
        }
    }

    public void deleteAllAuths() {
        auths.clear();
    }

    @Override
    public AuthData getAuth(AuthData authData) throws DataAccessException {
        return auths.getOrDefault(authData.authToken(), null);
    }

    public boolean tokenInDatabase(String authToken) {
        if(auths.containsKey(authToken)) {
            return true;
        } else {
            return false;
        }
    }

}
