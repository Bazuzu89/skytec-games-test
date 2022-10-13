package service;

import DAO.Repository;
import exceptions.NotFoundException;
import model.Clan;
import model.Task;
import model.Transaction;
import utils.TransactionActor;

import java.sql.SQLException;
import java.util.concurrent.ThreadLocalRandom;

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
    public void completeTask(long clanId, long taskId) throws SQLException, NotFoundException {
        Clan clan = new Clan();
        long goldToAdd = taskRepository.getById(taskId).getGold();
        clan.setId(clanId);
        clan.setGold(goldToAdd);
        clanService.save(clan);
        Transaction transaction = new Transaction(clanId, taskId, TransactionActor.TASK, goldToAdd);
        transactionRepository.create(transaction);
    }


    @Override
    public void run() {
        try {
            long taskId = ThreadLocalRandom.current().nextLong(1,100);//taskRepository.create(Task.createTask());
            completeTask(clanId, taskId);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}
