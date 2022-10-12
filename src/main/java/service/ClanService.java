package service;

import exceptions.NotFoundExcetion;
import model.Clan;

import java.sql.SQLException;

public interface ClanService {

    Clan get(long id) throws SQLException;

    int save(Clan clan) throws SQLException, NotFoundExcetion;
}
