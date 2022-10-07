package DAO;

import model.Clan;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ClanRepository {

    public Clan getClanById(long id, Connection connection) throws SQLException {
        String query = "SELCT id, name, gold FROM clans WHERE id = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setLong(1, id);
        ResultSet rs = statement.executeQuery();
        return new Clan(rs.getLong("id"),
                        rs.getString("name"),
                        rs.getLong("gold"));
    }
    
}
