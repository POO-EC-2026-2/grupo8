package com.ufg.atlas.dao;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.ufg.atlas.modelos.Garcom;
import com.ufg.atlas.modelos.Gerente;

import java.sql.SQLException;
import java.util.List;

public class GerenteDao {
    
    private final Dao<Gerente, Integer> dao;
    
    public GerenteDao(ConnectionSource conexao) throws SQLException {
        dao = DaoManager.createDao(conexao, Gerente.class);
    }
    public void create(Gerente gerente) throws SQLException {
        dao.create(gerente);
    }
    
    public List<Gerente> listAll() throws SQLException {
        return dao.queryForAll();
    }

    public Gerente findOneByName(String nome) throws SQLException {
        return dao.queryBuilder()
                .where()
                .like("nome", "%" + nome + "%")
                .queryForFirst();
    }

    public List<Gerente> findByName(String nome) throws SQLException {
        return dao.queryBuilder()
                .where()
                .like("nome", "%" + nome + "%")
                .query();
    }
    public void update(Gerente gerente) throws SQLException {
        dao.update(gerente);
    }
    
    public void delete(int id) throws SQLException {
        dao.deleteById(id);
    }
}
