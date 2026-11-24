package com.poo.dao;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.poo.modelos.Alimento;

import java.sql.SQLException;

public class AlimentoDao {

    private final Dao<Alimento, Integer> dao;

    public AlimentoDao(ConnectionSource conexao) throws SQLException {
        dao = DaoManager.createDao(conexao, Alimento.class);
    }
    public void salvar(Alimento alimento) throws SQLException {
        dao.create(alimento);
    }
}