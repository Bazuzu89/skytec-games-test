import DAO.ClanRepository;
import DAO.Repository;
import DAO.TaskRepository;
import model.Clan;
import model.Task;
import service.ClanService;
import service.ClanServiceImpl;
import service.TaskServiceImpl;
import service.UserAddGoldServiceImpl;
import utils.Randomizer;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Skytec {
    private final String password = "postgres";
    private final String user = "postgres";
    private final String url = "jdbc:postgresql://localhost:5432/skytec_task";

    public static void main(String[] args) {
        Skytec skytec = new Skytec();
        Repository<Clan> clanRepository = new ClanRepository();
        Repository<Task> taskRepository = new TaskRepository();
        ClanService clanService = new ClanServiceImpl(clanRepository);
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(10,
                                                                        100,
                                                                        200,
                                                                        TimeUnit.MILLISECONDS,
                                                                        new LinkedBlockingQueue<Runnable>());

        for (int i = 0; i < 100_000; i++) {
            if (Randomizer.generateBoolean()) {
                threadPoolExecutor.submit(new TaskServiceImpl(clanService, taskRepository));
            } else {
                threadPoolExecutor.submit(new UserAddGoldServiceImpl(clanService));
            }
        }
    }

}