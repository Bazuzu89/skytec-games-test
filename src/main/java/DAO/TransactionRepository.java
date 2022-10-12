package DAO;

import exceptions.MaxConnectionsException;
import exceptions.NotFoundExcetion;
import model.Transaction;
import utils.TransactionActor;

import java.sql.*;

public class TransactionRepository implements Repository<Transaction> {
    ConnectionPool connectionPool;

    public TransactionRepository(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    @Override
    public long create(Transaction transaction) throws SQLException {
        String query = "INSERT INTO transaction(description, clanId, actor, actorId, gold) VALUES(?,?,?,?,?)";
        Connection connection;
        try {
            connection = connectionPool.getConnection();
        } catch (MaxConnectionsException e) {
            throw new RuntimeException(e);
        }
        PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, transaction.getDescription());
        statement.setLong(2, transaction.getClanId());
        statement.setString(3, transaction.getActor().toString());
        statement.setLong(4, transaction.getActorId());
        statement.setLong(5, transaction.getGold());
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
    public Transaction getById(long id) throws SQLException {
        String query = "SELCT id, name, gold FROM transactions WHERE id = ?";
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
        Transaction transaction = new Transaction(id,
                rs.getLong("clanId"),
                rs.getLong("actorId"),
                TransactionActor.valueOf(rs.getString("actorId")),
                rs.getLong("gold")
                );
        return transaction;
    }

    @Override
    public int update(Transaction transaction) throws NotFoundExcetion, SQLException {
        long id = transaction.getId();
        try {
            if (getById(id) != null) {
                String query = "UPDATE transactions SET gold = ?, description = ? WHERE id = ?";
                Connection connection = connectionPool.getConnection();
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setLong(1, transaction.getGold());
                statement.setString(2, transaction.getDescription());
                statement.setLong(3, transaction.getId());
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
        String query = "DELETE FROM transactions WHERE id = ?";
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
