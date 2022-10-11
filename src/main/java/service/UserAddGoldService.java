package service;

import exceptions.NotFoundExcetion;

import java.sql.SQLException;

public interface UserAddGoldService {

    void addGoldToClan(long userId, long clanId, long gold) throws SQLException, NotFoundExcetion;
}
