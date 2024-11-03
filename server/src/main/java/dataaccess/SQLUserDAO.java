package dataaccess;

import dataaccess.model.LoginReq;
import dataaccess.model.RegisterReq;
import model.AuthData;
import java.sql.*;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

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

    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            conn.setCatalog("chess");
            for (var statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    System.out.printf("statement is: %s%n", statement);
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException(500, String.format("Unable to configure database: %s", ex.getMessage()));
        }
    }

    public SQLUserDAO() throws DataAccessException {
        configureDatabase();
    }

    @Override
    public AuthData registerUser(RegisterReq registerReq) throws DataAccessException {
        return null;
    }

    @Override
    public AuthData loginUser(LoginReq loginReq) throws DataAccessException {
        return null;
    }

    @Override
    public void deleteAllUsers() {

    }

}
