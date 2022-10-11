package DAO;

import exceptions.NotFoundExcetion;
import model.User;

import java.sql.SQLException;
//TODO implement all methods
public class UserRepository implements Repository<User> {

    @Override
    public long create(User entity) throws SQLException {
        return 0;
    }

    @Override
    public User getById(long id) throws SQLException {
        return null;
    }

    @Override
    public boolean update(User entity) throws NotFoundExcetion, SQLException {
        return false;
    }

    @Override
    public void delete(long id) throws SQLException {

    }
}
