package com.ufg.atlas.dao;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.ufg.atlas.modelos.Mesa;

import java.sql.SQLException;
import java.util.List;

public class MesaDao {
    
    private final Dao<Mesa, Integer> dao;
    
    public MesaDao(ConnectionSource conexao) throws SQLException {
        dao = DaoManager.createDao(conexao, Mesa.class);
    }
    public void create(Mesa mesa) throws SQLException {
        dao.create(mesa);
    }
    
    public List<Mesa> listAll() throws SQLException {
        return dao.queryForAll();
    }
    
    public List<Mesa> findByName(String nome) throws SQLException {
        return dao.queryBuilder()
                .where()
                .like("nome", "%" + nome + "%")
                .query();
    }
    public void update(Mesa mesa) throws SQLException {
        dao.update(mesa);
    }
    
    public void delete(int id) throws SQLException {
        dao.deleteById(id);
    }
}