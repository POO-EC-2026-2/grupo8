package com.ufg.atlas.dao;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.ufg.atlas.modelos.Garcom;

import java.sql.SQLException;
import java.util.List;

public class GarcomDao {
    
    private final Dao<Garcom, Integer> dao;
    
    public GarcomDao(ConnectionSource conexao) throws SQLException {
        dao = DaoManager.createDao(conexao, Garcom.class);
    }
    public void salvar(Garcom garcom) throws SQLException {
        dao.create(garcom);
    }
    
    public List<Garcom> listarTodos() throws SQLException {
        return dao.queryForAll();
    }
    
    public List<Garcom> buscarPorNome(String nome) throws SQLException {
        return dao.queryBuilder()
                .where()
                .like("nome", "%" + nome + "%")
                .query();
    }
    public void atualizar(Garcom garcom) throws SQLException {
        dao.update(garcom);
    }
    
    public void excluir(int id) throws SQLException {
        dao.deleteById(id);
    }
}