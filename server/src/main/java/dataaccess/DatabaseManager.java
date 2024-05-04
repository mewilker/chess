package dataaccess;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Responsible for creating connections to the database. Connections should be closed after use, either by calling
 * {@link #closeConnection(Connection)} on the Database instance or directly on the connection.
 */
public class DatabaseManager {

    // Change these fields, if necessary, to match your database configuration
    public static final String DB_NAME;
    private static final String DB_USERNAME;
    private static final String DB_PASSWORD;

    private static final String CONNECTION_URL;

    /*
     * Load the database information for the db.properties file.
     */
    static {
        try {
            try (var propStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("db.properties")) {
                if (propStream == null) throw new Exception("Unable to load db.properties");
                Properties props = new Properties();
                props.load(propStream);
                DB_NAME = props.getProperty("db.name");
                DB_USERNAME = props.getProperty("db.user");
                DB_PASSWORD = props.getProperty("db.password");

                var host = props.getProperty("db.host");
                var port = Integer.parseInt(props.getProperty("db.port"));
                CONNECTION_URL = String.format("jdbc:mysql://%s:%d", host, port);
            }
        } catch (Exception ex) {
            throw new RuntimeException("unable to process db.properties. " + ex.getMessage());
        }
    }

    public DatabaseManager() throws DataAccessException {
        try {
            var statement = "CREATE DATABASE IF NOT EXISTS " + DB_NAME;
            var conn = DriverManager.getConnection(CONNECTION_URL, DB_USERNAME, DB_PASSWORD);
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.executeUpdate();
            }
            conn.setCatalog(DB_NAME);
            statement ="""
                CREATE TABLE  IF NOT EXISTS users (
                    username VARCHAR(255) NOT NULL,
                    password VARCHAR(255) NOT NULL,
                    email VARCHAR(255) NOT NULL,
                    PRIMARY KEY (username)
                )
                """;
            try(PreparedStatement prepStatement =conn.prepareStatement(statement)){
                prepStatement.executeUpdate();
            }
            statement ="""
                    CREATE TABLE IF NOT EXISTS authTokens(
                    	username VARCHAR (255) NOT NULL,
                        token VARCHAR(255) NOT NULL,
                        PRIMARY KEY (token),
                        FOREIGN KEY(username) references users(username)
                    )
                    """;
            try(PreparedStatement prepStatement =conn.prepareStatement(statement)){
                prepStatement.executeUpdate();
            }
            statement ="""
                    CREATE TABLE IF NOT EXISTS games(
                    	id INT NOT NULL AUTO_INCREMENT,
                        name VARCHAR(255) NOT NULL,
                        whiteUsername VARCHAR(255),
                        blackUsername VARCHAR(255),
                        gameobj TEXT(65535) NOT NULL,
                        PRIMARY KEY(id),
                        FOREIGN KEY(whiteUsername) references users(username),
                        FOREIGN KEY(blackUsername) references users(username)
                    )
                    """;
            try(PreparedStatement prepStatement =conn.prepareStatement(statement)){
                prepStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    /**
     * Gets a connection to the database.
     *
     * @return Connection the connection.
     * @throws DataAccessException if a data access error occurs.
     */
    public Connection getConnection() throws DataAccessException {
        try {
            Connection conn =  DriverManager.getConnection(CONNECTION_URL, DB_USERNAME, DB_PASSWORD);
            conn.setCatalog(DB_NAME);
            return conn;
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    /**
     * Closes the specified connection.
     *
     * @param connection the connection to be closed.
     * @throws DataAccessException if a data access error occurs while closing the connection.
     */
    public void closeConnection(Connection connection) throws DataAccessException {
        if(connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new DataAccessException(e.getMessage());
            }
        }
    }

    /**
     * MAKENNA ADDED <P>
     * rolls back connections as nessary<p>
     * requires autocommit to be set to false
     * 
     * @param connection
     * @throws DataAccessException with sql Exception message
     */
    public void rollback(Connection connection) throws DataAccessException{
        if (connection != null){
            try{
                connection.rollback();
            }
            catch(SQLException e){
                throw new DataAccessException(e.getMessage());
            }
        }
    }
}
