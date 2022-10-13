package service;

import DAO.ClanRepository;
import DAO.Repository;
import exceptions.NotFoundExcetion;
import model.Clan;

import java.sql.SQLException;

public class ClanServiceImpl implements ClanService {
    private Repository<Clan> clanRepository;


    public ClanServiceImpl(Repository<Clan> clanRepository) {
        this.clanRepository = clanRepository;
    }

    public Repository getClanRepository() {
        return clanRepository;
    }

    public void setClanRepository(ClanRepository clanRepository) {
        this.clanRepository = clanRepository;
    }

    @Override
    public Clan get(long id) throws SQLException {
        return clanRepository.getById(id);

    }

    @Override
    public int save(Clan clan) throws SQLException, NotFoundExcetion {
        return clanRepository.update(clan);
    }
}
