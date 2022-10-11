package service;

import DAO.Repository;
import exceptions.NotFoundExcetion;
import model.Clan;
import model.Task;
import utils.Randomizer;

import java.sql.SQLException;

public class TaskServiceImpl implements TaskService, Runnable {
    private final ClanService clanService;

    private final Repository<Task> repository;

    private long clanId;
    private long taskId;

    public TaskServiceImpl(ClanService clanService, Repository<Task> repository) {
        this.clanService = clanService;
        this.repository = repository;
    }


    @Override
    public void completeTask(long clanId, long taskId) throws SQLException, NotFoundExcetion {
        if (Randomizer.generateBoolean()) {
            Clan clan = clanService.get(clanId);
            clan.setGold(clan.getGold() + repository.getById(taskId).getGold());
            clanService.save(clan);
            //TODO success task save
        } else {
            //TODO failed task save
        }
    }


    @Override
    public void run() {
        try {
            completeTask(clanId, taskId);
        } catch (SQLException | NotFoundExcetion e) {
            //TODO exception handling
            throw new RuntimeException(e);
        }
    }
}
