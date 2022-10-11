package DAO;

import exceptions.NotFoundExcetion;

import java.sql.SQLException;

public interface Repository<T> {

    long create(T entity) throws SQLException;
    T getById(long id) throws SQLException;
    boolean update(T entity) throws NotFoundExcetion, SQLException;
    void delete(long id) throws SQLException;
}
