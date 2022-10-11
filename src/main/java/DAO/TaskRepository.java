package DAO;

import exceptions.NotFoundExcetion;
import model.Task;

import java.sql.SQLException;
//TODO implement all methods
public class TaskRepository implements Repository<Task> {

    @Override
    public long create(Task entity) throws SQLException {
        return 0;
    }

    @Override
    public Task getById(long id) throws SQLException {
        return null;
    }

    @Override
    public boolean update(Task entity) throws NotFoundExcetion, SQLException {
        return false;
    }

    @Override
    public void delete(long id) throws SQLException {

    }
}
