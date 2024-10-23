package dataaccess;

import model.AuthData;
import java.util.HashMap;
import java.util.Map;

public class MemoryAuthDAO implements AuthDAO {

    final private HashMap<String, String> auths = new HashMap<>();

    @Override
    public void addAuth(AuthData authData) throws DataAccessException {
        if(!auths.containsKey(authData.username())) {
            auths.put(authData.username(), authData.authToken());
        }
    }

    @Override
    public AuthData getAuth(String username) throws DataAccessException {
        if(!auths.containsKey(username)) {
            throw new DataAccessException(500, "Error: update status code");
        } else {
            return new AuthData(username, auths.get(username));
        }
    }

    @Override
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

    @Override
    public void checkAuth(String authToken) throws DataAccessException {
        if(!auths.containsValue(authToken)) {
            throw new DataAccessException(401, "Error: unauthorized");
        }
    }

    @Override
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

    @Override
    public void deleteAllAuths() {
        auths.clear();
    }

}
