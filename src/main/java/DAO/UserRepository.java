package DAO;

import exceptions.NotFoundException;
import model.User;

import java.sql.*;

public class UserRepository implements Repository<User> {

    private ConnectionPool connectionPool;

    public UserRepository(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    @Override
    public long create(User user) throws SQLException {
        String query = "INSERT INTO users(gold, name) VALUES(?,?)";
        Connection connection;
        connection = connectionPool.getConnection();
        PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        statement.setLong(1, user.getGold());
        statement.setString(2, user.getName());
        int affectedRows = statement.executeUpdate();
        connection.commit();
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
        String query = "SELECT id, name, gold FROM users WHERE id = ?";
        Connection connection;
        connection = connectionPool.getConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setLong(1, id);
        ResultSet rs = statement.executeQuery();
        if (!rs.next()) {
            return null;
        }
        connection.commit();
        connectionPool.releaseConnection(connection);
        return new User(rs.getLong("id"),
                rs.getString("name"),
                rs.getLong("gold"));
    }

    @Override
    public int update(User user) throws NotFoundException, SQLException {
        long id = user.getId();
        if (getById(id) != null) {
            String query = "UPDATE users SET gold = ? WHERE id = ?";
            Connection connection = connectionPool.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setLong(1, user.getGold());
            statement.setLong(2, id);
            int affectedRows = statement.executeUpdate();
            connection.commit();
            connectionPool.releaseConnection(connection);
            return affectedRows;
        } else {
            throw new NotFoundException(String.format("User with id %d is not found in DB", id));
        }
    }

    @Override
    public void delete(long id) throws SQLException {
        String query = "DELETE FROM users WHERE id = ?";
        Connection connection;
        connection = connectionPool.getConnection();
        PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        statement.setLong(1, id);
        statement.executeUpdate();
        connection.commit();
        connectionPool.releaseConnection(connection);
    }
}
