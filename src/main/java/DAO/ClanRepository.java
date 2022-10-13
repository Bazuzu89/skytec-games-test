package DAO;

import exceptions.NotFoundException;
import model.Clan;

import java.sql.*;

public class ClanRepository implements Repository<Clan> {

    private ConnectionPool connectionPool;
    public static int updateCounts = 0;

    public ClanRepository(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    public Clan getById(long id) throws SQLException {
        String query = "SELECT id, name, gold FROM clans WHERE id = ?";
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
        Clan clan = new Clan(rs.getLong("id"),
                rs.getString("name"),
                rs.getLong("gold"));
        statement.close();
        return clan;
    }

    public int update(Clan clan) throws SQLException, NotFoundException {
        long id = clan.getId();
            if (getById(id) != null) {
                try {
                    Connection connection = connectionPool.getConnection();
                    String querySelect = "SELECT gold, id FROM clans WHERE id = ? FOR UPDATE";
                    PreparedStatement statementSelect = connection.prepareStatement(querySelect,
                                                                        ResultSet.TYPE_SCROLL_SENSITIVE,
                                                                        ResultSet.CONCUR_UPDATABLE);
                    statementSelect.setLong(1, id);
                    ResultSet rs = statementSelect.executeQuery();
                    if (rs.next()) {
                        long goldToAdd = clan.getGold();
                        long clanGold = rs.getLong("gold");
                        rs.updateLong("gold", clanGold + goldToAdd);
                        rs.updateRow();
                    } else {
                        return 0;
                    }
                    rs.close();
                    connection.commit();
                    connectionPool.releaseConnection(connection);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return 1;
            } else {
                throw new NotFoundException(String.format("Clan with id %d is not found in DB", id));
            }
    }

    @Override
    public long create(Clan clan) throws SQLException {
        String query = "INSERT INTO clans(gold, name) VALUES(?,?)";
        Connection connection;
        connection = connectionPool.getConnection();
        PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        statement.setLong(1, clan.getGold());
        statement.setString(2, clan.getName());
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
    public void delete(long id) throws SQLException {
        String query = "DELETE FROM clans WHERE id = ?";
        Connection connection;
        connection = connectionPool.getConnection();
        PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        statement.setLong(1, id);
        statement.executeUpdate();
        connection.commit();
        connectionPool.releaseConnection(connection);
    }
}
