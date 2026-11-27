package com.ufg.atlas.dao;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.ufg.atlas.modelos.ItemSolicitado;

import java.sql.SQLException;
import java.util.List;

public class ItemSolicitadoDao {
    
    private final Dao<ItemSolicitado, Integer> dao;
    
    public ItemSolicitadoDao(ConnectionSource conexao) throws SQLException {
        dao = DaoManager.createDao(conexao, ItemSolicitado.class);
    }
    public void create(ItemSolicitado itemSolicitado) throws SQLException {
        dao.create(itemSolicitado);
    }
    
    public List<ItemSolicitado> listarTodos() throws SQLException {
        return dao.queryForAll();
    }
    
    public List<ItemSolicitado> buscarPorNome(String nome) throws SQLException {
        return dao.queryBuilder()
                .where()
                .like("nome", "%" + nome + "%")
                .query();
    }
    public void atualizar(ItemSolicitado itemSolicitado) throws SQLException {
        dao.update(itemSolicitado);
    }
    
    public void excluir(int id) throws SQLException {
        dao.deleteById(id);
    }
}