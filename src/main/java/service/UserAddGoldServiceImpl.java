package service;

import DAO.Repository;
import exceptions.NotFoundExcetion;
import model.Clan;
import model.Transaction;
import utils.TransactionActor;

import java.sql.SQLException;

public class UserAddGoldServiceImpl implements UserAddGoldService, Runnable {
    private final ClanService clanService;

    private final Repository<Transaction> transactionRepository;
    private long userId;
    private long clanId;
    private long gold;

    public UserAddGoldServiceImpl(ClanService clanService, Repository<Transaction> transactionRepository) {
        this.clanService = clanService;
        this.transactionRepository = transactionRepository;
    }

    @Override
    public void addGoldToClan(long userId, long clanId, long gold) throws SQLException, NotFoundExcetion {
        Clan clan = clanService.get(clanId);
        clan.setGold(clan.getGold() + gold);
        clanService.save(clan);
        Transaction transaction = new Transaction(clanId, userId, TransactionActor.USER, gold);
        transactionRepository.create(transaction);
        //TODO timestamp
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
