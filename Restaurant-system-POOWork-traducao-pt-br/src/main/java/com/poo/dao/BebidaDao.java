package com.poo.dao;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.poo.modelos.Bebida;

import java.sql.SQLException;

public class BebidaDao {

    private final Dao<Bebida, Integer> dao;

    public BebidaDao(ConnectionSource conexao) throws SQLException {
        dao = DaoManager.createDao(conexao, Bebida.class);
    }

    public void salvar(Bebida bebida) throws SQLException{
          dao.create(bebida);
    }
}