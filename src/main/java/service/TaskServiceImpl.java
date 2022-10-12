package service;

import DAO.Repository;
import exceptions.NotFoundExcetion;
import model.Clan;
import model.Task;
import model.Transaction;
import utils.Randomizer;
import utils.TransactionActor;

import java.sql.SQLException;

public class TaskServiceImpl implements TaskService, Runnable {
    private final ClanService clanService;

    private final Repository<Task> taskRepository;
    
    private final Repository<Transaction> transactionRepository;

    private long clanId;


    public TaskServiceImpl(ClanService clanService, Repository<Task> repository, Repository<Transaction> transactionRepository, long clanId) {
        this.clanService = clanService;
        this.taskRepository = repository;
        this.transactionRepository = transactionRepository;
        this.clanId = clanId;
    }


    @Override
    public void completeTask(long clanId, long taskId) throws SQLException, NotFoundExcetion {
        Clan clan = clanService.get(clanId);
        long goldToAdd = taskRepository.getById(taskId).getGold();
        clan.setGold(clan.getGold() + goldToAdd);
        clanService.save(clan);
        Transaction transaction = new Transaction(clanId, taskId, TransactionActor.TASK, goldToAdd);
        transactionRepository.create(transaction);
        //TODO timestamp
    }


    @Override
    public void run() {
        try {
            if (Randomizer.generateBoolean()) {
                long taskId = taskRepository.create(Task.createTask());
                completeTask(clanId, taskId);
            } else {
            }
        } catch (SQLException | NotFoundExcetion e) {
            //TODO exception handling
            throw new RuntimeException(e);
        }
    }
}
