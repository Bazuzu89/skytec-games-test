package service;

import exceptions.NotFoundException;

import java.sql.SQLException;

public interface TaskService {

    void completeTask(long clanId, long taskId) throws SQLException, NotFoundException;
}
