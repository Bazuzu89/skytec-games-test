package DAO;

import exceptions.NotFoundException;
import model.Task;

import java.sql.*;

public class TaskRepository implements Repository<Task> {

    ConnectionPool connectionPool;

    public TaskRepository(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    @Override
    public long create(Task task) throws SQLException {
        String query = "INSERT INTO tasks(description, gold) VALUES(?,?)";
        Connection connection;
        connection = connectionPool.getConnection();
        PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, task.getDescription());
        statement.setLong(2, task.getGold());
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
    public Task getById(long id) throws SQLException {
        String query = "SELECT id, description, gold FROM tasks WHERE id = ?";
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
        return new Task(rs.getLong("id"),
                rs.getString("description"),
                rs.getLong("gold"));
    }

    @Override
    public int update(Task task) throws NotFoundException, SQLException {
        long id = task.getId();
        if (getById(id) != null) {
            String query = "UPDATE tasks SET gold = ?, description = ? WHERE id = ?";
            Connection connection = connectionPool.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setLong(1, task.getGold());
            statement.setString(2, task.getDescription());
            statement.setLong(3, id);
            int affectedRows = statement.executeUpdate();
            connection.commit();
            connectionPool.releaseConnection(connection);
            return affectedRows;
        } else {
            throw new NotFoundException(String.format("Task with id %d is not found in DB", id));
        }
    }

    @Override
    public void delete(long id) throws SQLException {
        String query = "DELETE FROM tasks WHERE id = ?";
        Connection connection;
        connection = connectionPool.getConnection();
        PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        statement.setLong(1, id);
        statement.executeUpdate();
        connection.commit();
        connectionPool.releaseConnection(connection);
    }
}
