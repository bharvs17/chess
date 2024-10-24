package dataaccess;

import model.AuthData;
import java.util.HashMap;
import java.util.Map;

public class MemoryAuthDAO implements AuthDAO {

    final private HashMap<String, String> auths = new HashMap<>();

    @Override
    public void addAuth(AuthData authData) throws DataAccessException { //changed
        auths.put(authData.authToken(), authData.username());
    }

    @Override
    public void logout(String authToken) throws DataAccessException {
        checkAuth(authToken);
        auths.remove(authToken);
    }

    @Override
    public void checkAuth(String authToken) throws DataAccessException {
        if(!auths.containsKey(authToken)) {
            throw new DataAccessException(401, "Error: unauthorized");
        }
    }

    @Override
    public String getUsername(String authToken) throws DataAccessException {
        checkAuth(authToken);
        return auths.get(authToken);
    }

    @Override
    public void deleteAllAuths() {
        auths.clear();
    }

}
