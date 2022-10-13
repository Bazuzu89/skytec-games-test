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

    public UserAddGoldServiceImpl(ClanService clanService, Repository<Transaction> transactionRepository, long userId, long clanId, long gold) {
        this.clanService = clanService;
        this.transactionRepository = transactionRepository;
        this.userId = userId;
        this.clanId = clanId;
        this.gold = gold;
    }

    @Override
    public void addGoldToClan(long userId, long clanId, long gold) throws SQLException, NotFoundExcetion {
        Clan clan = new Clan();
        clan.setGold(gold);
        clan.setId(clanId);
        clanService.save(clan);
        Transaction transaction = new Transaction(clanId, userId, TransactionActor.USER, gold);
        transactionRepository.create(transaction);
    }

    @Override
    public void run() {
        try {
            addGoldToClan(userId, clanId, gold);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NotFoundExcetion e) {
            System.out.println(e.getMessage());
        }
    }
}
