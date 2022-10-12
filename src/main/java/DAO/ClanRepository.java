package DAO;

import exceptions.MaxConnectionsException;
import exceptions.NotFoundExcetion;
import model.Clan;

import java.sql.*;

public class ClanRepository implements Repository<Clan> {

    private ConnectionPool connectionPool;

    public ClanRepository(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    public Clan getById(long id) throws SQLException {
        String query = "SELCT id, name, gold FROM clans WHERE id = ?";
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
        return new Clan(rs.getLong("id"),
                        rs.getString("name"),
                        rs.getLong("gold"));
    }

    public int update(Clan clan) throws SQLException, NotFoundExcetion {
        long id = clan.getId();
        try {
            if (getById(id) != null) {
                String query = "UPDATE clans SET gold = ? WHERE id = ?";
                Connection connection = connectionPool.getConnection();
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setLong(1, clan.getGold());
                statement.setLong(2, id);
                int affectedRows = statement.executeUpdate();
                connectionPool.releaseConnection(connection);
                return affectedRows;
            } else {
                throw new NotFoundExcetion();
            }
        } catch (MaxConnectionsException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public long create(Clan clan) throws SQLException {
        String query = "INSERT INTO clans(gold, name) VALUES(?,?)";
        Connection connection;
        try {
            connection = connectionPool.getConnection();
        } catch (MaxConnectionsException e) {
            throw new RuntimeException(e);
        }
        PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        statement.setLong(1, clan.getGold());
        statement.setString(2, clan.getName());
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
    public void delete(long id) throws SQLException {
        String query = "DELETE FROM clans WHERE id = ?";
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
