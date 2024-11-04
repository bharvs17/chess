package dataaccess;

import model.AuthData;

import java.sql.SQLException;

public class SQLAuthDAO implements AuthDAO {

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  auths (
              `id` int NOT NULL AUTO_INCREMENT,
              `username` varchar(256) NOT NULL,
              `authtoken` varchar(256) NOT NULL,
              PRIMARY KEY (`id`),
              INDEX(authtoken)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };

    public SQLAuthDAO() throws DataAccessException {
        DatabaseInit init = new DatabaseInit();
        init.configureDatabase(createStatements);
    }

    @Override
    public void addAuth(AuthData authData) throws DataAccessException {
        if (authData == null) {
            throw new DataAccessException(500, "authData was null");
        }
        String username = authData.username();
        String authToken = authData.authToken();
        try(var conn = DatabaseManager.getConnection()) {
            var statement = "INSERT INTO auths (username, authtoken) VALUES (?, ?)";
            try(var ps = conn.prepareStatement(statement)) {
                ps.setString(1,username);
                ps.setString(2,authToken);
                ps.executeUpdate();
            }
        } catch(Exception e) {
            throw new DataAccessException(401, String.format("Unable to read data: %s%n",e.getMessage()));
        }
    }

    @Override
    public void logout(String authToken) throws DataAccessException {
        checkAuth(authToken);
        try(var conn = DatabaseManager.getConnection()) {
            var statement = "DELETE FROM auths WHERE authtoken = ?";
            try(var ps = conn.prepareStatement(statement)) {
                ps.setString(1,authToken);
                ps.executeUpdate();
            }
        } catch(Exception e) {
            throw new DataAccessException(401, String.format("Unable to read data: %s%n",e.getMessage()));
        }
    }

    @Override
    public void checkAuth(String authToken) throws DataAccessException {
        int count = 0;
        try(var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT COUNT(*) FROM auths WHERE authtoken = ?";
            try(var ps = conn.prepareStatement(statement)) {
                ps.setString(1,authToken);
                try(var rs = ps.executeQuery()) {
                    if(rs.next()) {
                        count = rs.getInt(1);
                    }
                }
            }
        } catch(Exception e) {
            throw new DataAccessException(401, String.format("Unable to read data: %s%n",e.getMessage()));
        }
        if(count == 0) {
            throw new DataAccessException(401, "Error: unauthorized");
        }
    }

    @Override
    public String getUsername(String authToken) throws DataAccessException {
        checkAuth(authToken);
        String username = "";
        try(var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT * FROM auths WHERE authtoken = ?";
            try(var ps = conn.prepareStatement(statement)) {
                ps.setString(1,authToken);
                try(var rs = ps.executeQuery()) {
                    if(rs.next()) {
                        username = rs.getString("username");
                    }
                }
            }
        } catch(Exception e) {
            throw new DataAccessException(401, String.format("unable to read data: %s%n",e.getMessage()));
        }
        return username;
    }

    @Override
    public void deleteAllAuths() throws DataAccessException {
        try(var conn = DatabaseManager.getConnection()) {
            var statement = "TRUNCATE auths";
            try(var ps = conn.prepareStatement(statement)) {
                ps.executeUpdate();
            }
        } catch(Exception e) {
            throw new DataAccessException(401, String.format("Unable to perform request: %s%n",e.getMessage()));
        }
    }

}
