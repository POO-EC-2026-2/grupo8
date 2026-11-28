package com.ufg.atlas.dao;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.ufg.atlas.modelos.Funcionario;

import java.sql.SQLException;
import java.util.List;

public class FuncionarioDao {

    private final Dao<Funcionario, Integer> dao;

    public FuncionarioDao(ConnectionSource conexao) throws SQLException {
        dao = DaoManager.createDao(conexao, Funcionario.class);
    }
    public void salvar(Funcionario funcionario) throws SQLException {
        dao.create(funcionario);
    }

    public List<Funcionario> listarTodos() throws SQLException {
        return dao.queryForAll();
    }


    public Funcionario listarUmPorNome(String nome) throws SQLException {
        return dao.queryBuilder()
                .where()
                .like("nome", "%" + nome + "%")
                .queryForFirst();
    }

    public List<Funcionario> buscarPorNome(String nome) throws SQLException {
        return dao.queryBuilder()
                .where()
                .like("nome", "%" + nome + "%")
                .query();
    }
    public void atualizar(Funcionario funcionario) throws SQLException {
        dao.update(funcionario);
    }

    public void excluir(int id) throws SQLException {
        dao.deleteById(id);
    }
}
