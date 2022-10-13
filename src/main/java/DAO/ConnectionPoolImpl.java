package DAO;


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
    private static int MAX_POOL_SIZE = 90;

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
        Connection connection = DriverManager.getConnection(url, user, password);
        connection.setAutoCommit(false);
        return connection;
    }

    @Override
    public Connection getConnection() throws SQLException {
        Connection connection = null;
        do {
            synchronized (this) {
                if (connectionPool.isEmpty()) {
                    if (connectionsInUse.size() < MAX_POOL_SIZE) {
                        connectionPool.add(createConnection(url, user, password));
                        connection = connectionPool.remove(connectionPool.size() - 1);
                        connectionsInUse.add(connection);
                    }
                } else {
                    connection = connectionPool.remove(connectionPool.size() - 1);
                    connectionsInUse.add(connection);
                }
            }
            if (connection == null) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        } while (connection == null);
        return connection;
    }

    @Override
    public boolean releaseConnection(Connection connection) {
        synchronized (this) {
            connectionPool.add(connection);
        }
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
