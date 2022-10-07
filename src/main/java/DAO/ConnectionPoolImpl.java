package DAO;

import exceptions.MaxConnectionsException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ConnectionPoolImpl implements ConnectionPool {

    private String url;
    private String user;
    private String password;
    private List<Connection> connectionPool;
    private List<Connection> connectionsInUse = new ArrayList<>();
    private static int INITIAL_POOL_SIZE = 10;
    private static int MAX_POOL_SIZE = 100;

    public ConnectionPoolImpl(String url, String user, String password, List<Connection> pool) {
        this.connectionPool = pool;
        this.url = url;
        this.user = user;
        this.password = password;
    }


    public static ConnectionPool create(String url,
                                        String user,
                                        String password) throws SQLException {
        List<Connection> pool = new ArrayList<>(INITIAL_POOL_SIZE);
        for (int i = 0; i < INITIAL_POOL_SIZE; i++) {
            pool.add(createConnection(url, user, password));
        }
        return new ConnectionPoolImpl(url, user, password, pool);
    }

    private static Connection createConnection(String url,
                                               String user,
                                               String password) throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    @Override
    public Connection getConnection() throws SQLException, MaxConnectionsException {
        if (connectionPool.isEmpty()) {
            if (connectionsInUse.size() < MAX_POOL_SIZE) {
                connectionPool.add(createConnection(url, user, password));
            } else {
                throw new MaxConnectionsException();
            }
        }
        Connection connection = connectionPool.remove(connectionPool.size() - 1);
        connectionsInUse.add(connection);
        return connection;
    }

    @Override
    public boolean releaseConnection(Connection connection) {
        connectionPool.add(connection);
        return connectionsInUse.remove(connection);
    }

    public int getSize() {
        return connectionPool.size() + connectionsInUse.size();
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public String getUser() {
        return user;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public List<Connection> getConnectionPool() {
        return connectionPool;
    }

    public List<Connection> getConnectionsInUse() {
        return connectionsInUse;
    }
}
