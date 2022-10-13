package service;

import exceptions.NotFoundException;
import model.Clan;

import java.sql.SQLException;

public interface ClanService {

    Clan get(long id) throws SQLException;

    int save(Clan clan) throws SQLException, NotFoundException;
}
