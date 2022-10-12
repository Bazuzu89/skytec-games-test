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
            throw new RuntimeException(e);
            //TODO add message
        }
        Repository<Clan> clanRepository = new ClanRepository(connectionPool);
        Repository<Task> taskRepository = new TaskRepository(connectionPool);
        Repository<Transaction> transactionRepository = new TransactionRepository(connectionPool);
        ClanService clanService = new ClanServiceImpl(clanRepository);
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(10,
                                                                        100,
                                                                        200,
                                                                        TimeUnit.MILLISECONDS,
                                                                        new LinkedBlockingQueue<Runnable>());

        for (int i = 0; i < 100_000; i++) {
            if (Randomizer.generateBoolean()) {
                long clanId = Randomizer.generateClanId();
                threadPoolExecutor.submit(new TaskServiceImpl(clanService, taskRepository, transactionRepository, clanId));
            } else {
                threadPoolExecutor.submit(new UserAddGoldServiceImpl(clanService, transactionRepository));
            }
        }
    }

}