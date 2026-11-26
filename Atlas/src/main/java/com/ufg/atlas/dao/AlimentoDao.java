package com.ufg.atlas.dao;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.ufg.atlas.modelos.Alimento;

import java.sql.SQLException;
import java.util.List;

public class AlimentoDao {

    private final Dao<Alimento, Integer> dao;

    public AlimentoDao(ConnectionSource conexao) throws SQLException {
        dao = DaoManager.createDao(conexao, Alimento.class);
    }
    public void salvar(Alimento alimento) throws SQLException {
        dao.create(alimento);
    }

    public List<Alimento> listarTodos() throws SQLException {
        return dao.queryForAll();
    }

    public List<Alimento> buscarPorNome(String nome) throws SQLException {
        return dao.queryBuilder()
                .where()
                .like("nome", "%" + nome + "%")
                .query();
    }
    public void atualizar(Alimento alimento) throws SQLException {
        dao.update(alimento);
    }

    public void excluir(int id) throws SQLException {
        dao.deleteById(id);
    }
}