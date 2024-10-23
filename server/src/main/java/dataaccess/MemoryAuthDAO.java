package dataaccess;

import model.AuthData;
import java.util.HashMap;
import java.util.Map;

public class MemoryAuthDAO implements AuthDAO {

    final private HashMap<String, String> auths = new HashMap<>();

    public void addAuth(AuthData authData) throws DataAccessException {
        if(!auths.containsKey(authData.username())) {
            auths.put(authData.username(), authData.authToken());
        }
    }

    public AuthData getAuth(String username) throws DataAccessException {
        if(!auths.containsKey(username)) {
            throw new DataAccessException(500, "Error: update status code");
        } else {
            return new AuthData(username, auths.get(username));
        }
    }

    public void logout(String authToken) throws DataAccessException {
        checkAuth(authToken);
        for(Map.Entry<String,String> entry : auths.entrySet()) {
            String key = entry.getKey();
            String val = entry.getValue();
            if(val.equals(authToken)) {
                auths.remove(key);
                break;
            }
        }
    }

    public void checkAuth(String authToken) throws DataAccessException {
        if(!auths.containsValue(authToken)) {
            throw new DataAccessException(401, "Error: unauthorized");
        }
    }

    public String getUsername(String authToken) throws DataAccessException {
        checkAuth(authToken);
        for(Map.Entry<String,String> entry : auths.entrySet()) {
            String key = entry.getKey();
            String val = entry.getValue();
            if(val.equals(authToken)) {
                return key;
            }
        }
        return null;
    }

    //OLD METHODS BELOW

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

    @Override
    public void deleteAllAuths() {
        auths.clear();
    }

    @Override
    public boolean tokenInDatabase(String authToken) {
        if(auths.containsKey(authToken)) {
            return true;
        } else {
            return false;
        }
    }

}
