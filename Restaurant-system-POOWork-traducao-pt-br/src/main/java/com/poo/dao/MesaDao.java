package com.poo.dao;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.poo.modelos.Mesa;

import java.sql.SQLException;
import java.util.List;

public class MesaDao {
    
    private final Dao<Mesa, Integer> dao;
    
    public MesaDao(ConnectionSource conexao) throws SQLException {
        dao = DaoManager.createDao(conexao, Mesa.class);
    }
    public void salvar(Mesa mesa) throws SQLException {
        dao.create(mesa);
    }
    
    public List<Mesa> listarTodos() throws SQLException {
        return dao.queryForAll();
    }
    
    public List<Mesa> buscarPorNome(String nome) throws SQLException {
        return dao.queryBuilder()
                .where()
                .like("nome", "%" + nome + "%")
                .query();
    }
    public void atualizar(Mesa mesa) throws SQLException {
        dao.update(mesa);
    }
    
    public void excluir(int id) throws SQLException {
        dao.deleteById(id);
    }
}