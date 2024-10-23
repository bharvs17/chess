package dataaccess;

import model.AuthData;
import java.util.HashMap;

public class MemoryAuthDAO implements AuthDAO {

    final private HashMap<String, AuthData> auths = new HashMap<>();

    @Override
    public AuthData createAuth(AuthData authData) throws DataAccessException {
        if(auths.containsKey(authData.authToken())) {
            throw new DataAccessException(500, "Auth already in database.");
        } else {
            auths.put(authData.authToken(), authData);
        }
        return authData;
    }

    @Override
    public void deleteAuth(AuthData authData) throws DataAccessException {
        if(!auths.containsKey(authData.authToken())) {
            throw new DataAccessException(500, "No such auth in database");
        } else {
            auths.remove(authData.authToken());
        }
    }

    public void deleteAllAuths() {
        auths.clear();
    }

    @Override
    public AuthData getAuth(AuthData authData) throws DataAccessException {
        if(!auths.containsKey(authData.authToken())) {
            throw new DataAccessException(500, "No such auth in database");
        } else {
            return auths.get(authData.authToken());
        }
    }

}
