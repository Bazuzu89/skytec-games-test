package service;

import exceptions.NotFoundExcetion;
import model.Clan;

import java.sql.Connection;
import java.sql.SQLException;

public interface ClanService {

    Clan get(long id) throws SQLException;

    boolean save(Clan clan) throws SQLException, NotFoundExcetion;
}
