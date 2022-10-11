package service;

import exceptions.NotFoundExcetion;

import java.sql.SQLException;

public interface TaskService {

    void completeTask(long clanId, long taskId) throws SQLException, NotFoundExcetion;
}
