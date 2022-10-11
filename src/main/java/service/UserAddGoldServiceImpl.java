package service;

import exceptions.NotFoundExcetion;
import model.Clan;

import java.sql.SQLException;

public class UserAddGoldServiceImpl implements UserAddGoldService, Runnable {
    private final ClanService clanService;
    private long userId;
    private long clanId;
    private long gold;

    public UserAddGoldServiceImpl(ClanService clanService) {
        this.clanService = clanService;
    }

    @Override
    public void addGoldToClan(long userId, long clanId, long gold) throws SQLException, NotFoundExcetion {
        Clan clan = clanService.get(clanId);
        clan.setGold(clan.getGold() + gold);
        clanService.save(clan);
        //TODO logging
    }

    @Override
    public void run() {
        try {
            addGoldToClan(userId, clanId, gold);
        } catch (SQLException | NotFoundExcetion e) {
            //TODO exception handling
            throw new RuntimeException(e);
        }
    }
}
