package com.ufg.atlas.dao;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.ufg.atlas.modelos.Bebida;

import java.sql.SQLException;
import java.util.List;

public class BebidaDao {

    private final Dao<Bebida, Integer> dao;

    public BebidaDao(ConnectionSource conexao) throws SQLException {
        dao = DaoManager.createDao(conexao, Bebida.class);
    }

    public void create(Bebida bebida) throws SQLException{
          dao.create(bebida);
    }

    public List<Bebida> listAll() throws SQLException {
        return dao.queryForAll();
    }

    public List<Bebida> findByName(String nome) throws SQLException {
        return dao.queryBuilder()
                .where()
                .like("nome", "%" + nome + "%")
                .query();
    }

    public void update(Bebida bebida) throws SQLException {
        dao.update(bebida);
    }

    public void delete(int id) throws SQLException {
        dao.deleteById(id);
    }
}