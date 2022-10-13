package DAO;

import exceptions.MaxConnectionsException;
import exceptions.NotFoundExcetion;
import model.Transaction;
import utils.TransactionActor;

import java.sql.*;
import java.time.Instant;
import java.time.LocalDateTime;

public class TransactionRepository implements Repository<Transaction> {
    ConnectionPool connectionPool;

    public TransactionRepository(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    @Override
    public long create(Transaction transaction) throws SQLException {
        String query = "INSERT INTO transactions(clanId, actor, actorId, gold, timestamp) VALUES(?,?,?,?,?)";
        Connection connection;
        try {
            connection = connectionPool.getConnection();
        } catch (MaxConnectionsException e) {
            throw new RuntimeException(e);
        }
        PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        statement.setLong(1, transaction.getClanId());
        statement.setString(2, transaction.getActor().toString());
        statement.setLong(3, transaction.getActorId());
        statement.setLong(4, transaction.getGold());
        statement.setLong(5, Instant.now().toEpochMilli());
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
    public Transaction getById(long id) throws SQLException {
        String query = "SELECT id, name, gold FROM transactions WHERE id = ?";
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
        connection.commit();
        connectionPool.releaseConnection(connection);
        Transaction transaction = new Transaction(id,
                                            rs.getLong("clanId"),
                                            rs.getLong("actorId"),
                                            TransactionActor.valueOf(rs.getString("actorId")),
                                            rs.getLong("gold"),
                                            rs.getLong("timestamp")
                                            );
        return transaction;
    }

    @Override
    public int update(Transaction entity) throws NotFoundExcetion, SQLException {
        return 0;
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
            connection.commit();
            connectionPool.releaseConnection(connection);
        } catch (MaxConnectionsException e) {
            throw new RuntimeException(e);
        }
    }
}
