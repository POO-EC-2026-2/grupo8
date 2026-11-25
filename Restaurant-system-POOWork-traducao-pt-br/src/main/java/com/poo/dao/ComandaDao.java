package com.poo.dao;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.poo.modelos.Comanda;

import java.sql.SQLException;
import java.util.List;

public class ComandaDao {
    
    private final Dao<Comanda, Integer> dao;
    
    public ComandaDao(ConnectionSource conexao) throws SQLException {
        dao = DaoManager.createDao(conexao, Comanda.class);
    }
    public void salvar(Comanda comanda) throws SQLException {
        dao.create(comanda);
    }
    
    public List<Comanda> listarTodos() throws SQLException {
        return dao.queryForAll();
    }
    
    public List<Comanda> buscarPorNome(String nome) throws SQLException {
        return dao.queryBuilder()
                .where()
                .like("nome", "%" + nome + "%")
                .query();
    }
    public void atualizar(Comanda comanda) throws SQLException {
        dao.update(comanda);
    }
    
    public void excluir(int id) throws SQLException {
        dao.deleteById(id);
    }
}