import DAO.*;
import model.Clan;
import model.Task;
import model.Transaction;
import service.ClanService;
import service.ClanServiceImpl;
import service.TaskServiceImpl;
import service.UserAddGoldServiceImpl;
import utils.Randomizer;

import java.sql.SQLException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Skytec {
    private final static String password = "postgres";
    private final static String user = "postgres";
    private final static String url = "jdbc:postgresql://localhost:5432/skytec_task";

    public static void main(String[] args) {
        Skytec skytec = new Skytec();
        ConnectionPool connectionPool;
        try {
            connectionPool = ConnectionPoolImpl.create(url, user, password);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to create connection to DB!");
        }
        Repository<Clan> clanRepository = new ClanRepository(connectionPool);
        Repository<Task> taskRepository = new TaskRepository(connectionPool);
        Repository<Transaction> transactionRepository = new TransactionRepository(connectionPool);
        ClanService clanService = new ClanServiceImpl(clanRepository);
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(10,
                                                                        100,
                                                                        1000,
                                                                        TimeUnit.MILLISECONDS,
                                                                        new LinkedBlockingQueue<Runnable>());

        for (int i = 0; i < 100; i++) {
            try {
                taskRepository.create(Task.createTask());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        for (int i = 0; i < 100; i++) {
            long clanId = Randomizer.generateClanId();
            long userId = Randomizer.generateUserId();
            long gold = Randomizer.generateGoldAmount();
            if (Randomizer.generateBoolean()) {
                threadPoolExecutor.submit(new TaskServiceImpl(clanService, taskRepository, transactionRepository, clanId));
            } else {
                threadPoolExecutor.submit(new UserAddGoldServiceImpl(clanService, transactionRepository, userId, clanId, gold));
            }
        }
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        threadPoolExecutor.shutdown();
    }

}