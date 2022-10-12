package DAO;

import exceptions.MaxConnectionsException;
import exceptions.NotFoundExcetion;
import model.User;

import java.sql.*;

//TODO implement all methods
public class UserRepository implements Repository<User> {

    private ConnectionPool connectionPool;

    public UserRepository(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    @Override
    public long create(User user) throws SQLException {
        String query = "INSERT INTO users(gold, name) VALUES(?,?)";
        Connection connection;
        try {
            connection = connectionPool.getConnection();
        } catch (MaxConnectionsException e) {
            throw new RuntimeException(e);
        }
        PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        statement.setLong(1, user.getGold());
        statement.setString(2, user.getName());
        int affectedRows = statement.executeUpdate();
        connectionPool.releaseConnection(connection);
        ResultSet rs = statement.getGeneratedKeys();
        long id;
        if (affectedRows > 0) {
            rs.next();
            id = rs.getLong(1);
        } else {
            id = -1;
        }
        return id;
    }

    @Override
    public User getById(long id) throws SQLException {
        String query = "SELCT id, name, gold FROM users WHERE id = ?";
        Connection connection;
        try {
            connection = connectionPool.getConnection();
        } catch (MaxConnectionsException e) {
            throw new RuntimeException(e);
        }
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setLong(1, id);
        ResultSet rs = statement.executeQuery();
        if (!rs.next()) {
            return null;
        }
        connectionPool.releaseConnection(connection);
        return new User(rs.getLong("id"),
                rs.getString("name"),
                rs.getLong("gold"));
    }

    @Override
    public int update(User user) throws NotFoundExcetion, SQLException {
        long id = user.getId();
        try {
            if (getById(id) != null) {
                String query = "UPDATE users SET gold = ? WHERE id = ?";
                Connection connection = connectionPool.getConnection();
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setLong(1, user.getGold());
                statement.setLong(2, id);
                int affectedRows = statement.executeUpdate();
                connectionPool.releaseConnection(connection);
                return affectedRows;
            } else {
                throw new NotFoundExcetion();
                //TODO pass the message with the exception
            }
        } catch (MaxConnectionsException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(long id) throws SQLException {
        String query = "DELETE FROM users WHERE id = ?";
        Connection connection;
        try {
            connection = connectionPool.getConnection();
            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setLong(1, id);
            statement.executeUpdate();
            connectionPool.releaseConnection(connection);
        } catch (MaxConnectionsException e) {
            throw new RuntimeException(e);
        }
    }
}
