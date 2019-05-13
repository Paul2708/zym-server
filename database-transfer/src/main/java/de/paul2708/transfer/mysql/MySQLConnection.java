package de.paul2708.transfer.mysql;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

/**
 * This class provides basic mysql statements based on HikariCP.
 *
 * @author Paul2708
 */
public class MySQLConnection {

    private final String host;
    private final int port;
    private final String userName;
    private final String password;
    private final String database;

    private HikariDataSource dataSource;
    private ExecutorService executor;

    /**
     * Create a new mysql database with host, port, username password and database.
     *
     * @param host host address
     * @param port mysql port, normally <code>3306</code>
     * @param userName mysql user name
     * @param password username password
     * @param database database name
     */
    public MySQLConnection(String host, int port, String userName, String password, String database) {
        this.host = host;
        this.port = port;
        this.userName = userName;
        this.password = password;
        this.database = database;

        this.executor = Executors.newCachedThreadPool();
    }

    /**
     * Creating the pool config and connect to the database.
     */
    public void connect() {
        // Setup pool config
        HikariConfig config = new HikariConfig();

        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.setJdbcUrl("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database + "?serverTimezone=UTC&useSSL=false");

        config.setUsername(this.userName);
        config.setPassword(this.password);
        config.setPoolName("MySQL-Pool");

        // Recommend pool settings
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.setMaximumPoolSize(10);
        config.setConnectionTimeout(2000);
        config.setConnectionTestQuery("SELECT 1");

        this.dataSource = new HikariDataSource(config);
    }

    /**
     * Execute an update sql statement with <code>?</code> parameters.
     *
     * @param query sql query
     * @param objects objects
     * @throws SQLException if the execution produces any error (e.g. syntax errors)
     */
    public void execute(String query, Object... objects) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            setParameters(preparedStatement, objects);

            preparedStatement.executeUpdate();
        }
    }

    /**
     * Execute a sql statement with <code>?</code> parameters asynchronous.
     *
     * @param result handler that handles result and exception
     * @param query sql query
     * @param objects objects
     */
    public void executeAsync(ConnectionResult<Void, SQLException> result, String query, Object... objects) {
        this.executor.execute(() -> {
            try {
                this.execute(query, objects);
                result.success(null);
            } catch (SQLException e) {
                result.exception(e);
            }
        });
    }

    /**
     * Query a sql statement with <code>?</code> parameters.
     *
     * @param resultSetConsumer consumer, that accepts the result set
     * @param query sql query
     * @param objects objects
     * @throws SQLException if the query produces any error (e.g. syntax errors)
     */
    public void query(Consumer<ResultSet> resultSetConsumer, String query, Object... objects) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            setParameters(preparedStatement, objects);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                resultSetConsumer.accept(resultSet);
            }
        }
    }

    /**
     * Query a sql statement with <code>?</code> parameters asynchronous.
     *
     * @param result handler that handles result and exception
     * @param query sql query
     * @param objects objects
     */
    public void queryAsync(ConnectionResult<ResultSet, SQLException> result, String query, Object... objects) {
        executor.execute(() -> {
            try {
                this.query(result::success, query, objects);
            } catch (SQLException e) {
                result.exception(e);
            }
        });
    }

    /**
     * Close the connection.
     */
    public void close() {
        dataSource.close();
    }

    /**
     * Create a prepared statement while replacing all parameters with objects.
     *
     * @param statement prepared statement
     * @param objects parameter
     * @throws SQLException if the prepared statement throws any exceptions
     */
    private void setParameters(PreparedStatement statement, Object... objects) throws SQLException {
        if (objects == null || objects.length == 0) {
            return;
        }

        for (int i = 1; i <= objects.length; i++) {
            statement.setObject(i, objects[i - 1]);
        }
    }
}