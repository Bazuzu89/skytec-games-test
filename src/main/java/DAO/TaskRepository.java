package DAO;

import exceptions.MaxConnectionsException;
import exceptions.NotFoundExcetion;
import model.Task;
import model.User;

import java.sql.*;

//TODO implement all methods
public class TaskRepository implements Repository<Task> {

    ConnectionPool connectionPool;

    public TaskRepository(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    @Override
    public long create(Task task) throws SQLException {
        String query = "INSERT INTO tasks(description, gold) VALUES(?,?)";
        Connection connection;
        try {
            connection = connectionPool.getConnection();
        } catch (MaxConnectionsException e) {
            throw new RuntimeException(e);
        }
        PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, task.getDescription());
        statement.setLong(2, task.getGold());
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
    public Task getById(long id) throws SQLException {
        String query = "SELCT id, description, gold FROM users WHERE id = ?";
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
        return new Task(rs.getLong("id"),
                rs.getString("description"),
                rs.getLong("gold"));
    }

    @Override
    public int update(Task task) throws NotFoundExcetion, SQLException {
        long id = task.getId();
        try {
            if (getById(id) != null) {
                String query = "UPDATE tasks SET gold = ?, description = ? WHERE id = ?";
                Connection connection = connectionPool.getConnection();
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setLong(1, task.getGold());
                statement.setString(2, task.getDescription());
                statement.setLong(3, id);
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
        String query = "DELETE FROM tasks WHERE id = ?";
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
