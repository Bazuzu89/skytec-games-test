package DAO;

import exceptions.NotFoundException;

import java.sql.SQLException;

public interface Repository<T> {

    long create(T entity) throws SQLException;
    T getById(long id) throws SQLException;
    int update(T entity) throws NotFoundException, SQLException;
    void delete(long id) throws SQLException;
}
