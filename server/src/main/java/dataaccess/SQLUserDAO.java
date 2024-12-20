package dataaccess;

import model.LoginReq;
import model.RegisterReq;
import model.AuthData;
import org.mindrot.jbcrypt.BCrypt;
import exception.DataAccessException;
import java.sql.*;
import java.util.UUID;

public class SQLUserDAO implements UserDAO {

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  users (
              `id` int NOT NULL AUTO_INCREMENT,
              `username` varchar(256) NOT NULL,
              `password` varchar(256) NOT NULL,
              `email` varchar(256) NOT NULL,
              PRIMARY KEY (`id`),
              INDEX(username)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };

    public SQLUserDAO() throws DataAccessException {
        DatabaseInit init = new DatabaseInit();
        init.configureDatabase(createStatements);
    }

    @Override
    public AuthData registerUser(RegisterReq registerReq) throws DataAccessException {
        if(registerReq.username() == null || registerReq.password() == null || registerReq.email() == null) {
            throw new DataAccessException(400, "Error: bad request");
        }
        String username = registerReq.username();
        String password = registerReq.password();
        String email = registerReq.email();
        int count = 0;
        //check if username in database
        try(var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT COUNT(*) FROM users WHERE username = ?";
            try(var ps = conn.prepareStatement(statement)) {
                ps.setString(1,username);
                try(var rs = ps.executeQuery()) {
                    if(rs.next()) {
                        count = rs.getInt(1);
                    }
                }
            }
        } catch(Exception e) {
            throw new DataAccessException(403, e.getMessage());
        }
        if(count > 0) {
            throw new DataAccessException(403, "Error: Username already exists in database");
        }
        //add to database
        try(var conn = DatabaseManager.getConnection()) {
            var statement = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
            try(var ps = conn.prepareStatement(statement)) {
                String hashedPass = BCrypt.hashpw(password,BCrypt.gensalt());
                ps.setString(1,username);
                ps.setString(2,hashedPass);
                ps.setString(3,email);
                ps.executeUpdate();
            }
        } catch(Exception e) {
            throw new DataAccessException(500, String.format("Unable to read data: %s%n",e.getMessage()));
        }
        return new AuthData(username, UUID.randomUUID().toString());
    }

    @Override
    public AuthData loginUser(LoginReq loginReq) throws DataAccessException {
        String username = loginReq.username();
        String givenPass = loginReq.password();
        String password;
        try(var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT * FROM users WHERE username = ?";
            try(var ps = conn.prepareStatement(statement)) {
                ps.setString(1,username);
                try(var rs = ps.executeQuery()) {
                    if(rs.next()) {
                        password = rs.getString("password");
                    } else {
                        throw new DataAccessException(401, "Error: unauthorized");
                    }
                }
            }
        } catch(Exception e) {
            throw new DataAccessException(401, String.format("Unable to read data: %s%n",e.getMessage()));
        }
        if(BCrypt.checkpw(givenPass, password)) {
            String uuid = UUID.randomUUID().toString();
            return new AuthData(username,uuid);
        } else {
            throw new DataAccessException(401, "Error: unauthorized");
        }
    }

    @Override
    public void deleteAllUsers() throws DataAccessException {
        try(var conn = DatabaseManager.getConnection()) {
            var statement = "TRUNCATE users";
            try(var ps = conn.prepareStatement(statement)) {
                ps.executeUpdate();
            }
        } catch(Exception e) {
            throw new DataAccessException(401, String.format("Unable to perform request: %s%n",e.getMessage()));
        }
    }

}
