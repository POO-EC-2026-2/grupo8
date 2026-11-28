package com.ufg.atlas.dao;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.ufg.atlas.modelos.Comanda;

import java.sql.SQLException;
import java.util.List;

public class ComandaDao {
    
    private final Dao<Comanda, Integer> dao;
    
    public ComandaDao(ConnectionSource conexao) throws SQLException {
        dao = DaoManager.createDao(conexao, Comanda.class);
    }
    public void create(Comanda comanda) throws SQLException {
        dao.create(comanda);
    }
    
    public List<Comanda> listAll() throws SQLException {
        return dao.queryForAll();
    }
    
    public List<Comanda> findByName(String nome) throws SQLException {
        return dao.queryBuilder()
                .where()
                .like("nome", "%" + nome + "%")
                .query();
    }

    public Comanda findOneByName(String nome) throws SQLException {
    return dao.queryBuilder()
                .where()
                .like("nome", "%" + nome + "%")
                .queryForFirst();
    }

    public List<Comanda> listLatest(int limite) throws SQLException {
    return dao.queryBuilder()
            .orderBy("id", false)
            .limit((long) limite)
            .query();
    }
    //usado para o display da cozinha

    
    public void update(Comanda comanda) throws SQLException {
        dao.update(comanda);
    }
    
    public void delete(int id) throws SQLException {
        dao.deleteById(id);
    }
}
