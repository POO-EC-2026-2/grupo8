package com.ufg.atlas;

import com.ufg.atlas.dao.*;
import com.ufg.atlas.modelos.*;

import java.io.IOException;
import java.sql.SQLException;

import com.ufg.atlas.dao.AlimentoDao;
import com.ufg.atlas.dao.BebidaDao;
import com.ufg.atlas.database.ConexaoBanco;
import com.j256.ormlite.table.TableUtils;
import com.ufg.atlas.modelos.Alimento;
import com.ufg.atlas.modelos.Bebida;

public class Testes {
    public static void main(String[] args) throws SQLException, IOException {


        ConexaoBanco.conectar();

        TableUtils.createTableIfNotExists(
                ConexaoBanco.getConexao(),
                Alimento.class
        );

        TableUtils.createTableIfNotExists(
                ConexaoBanco.getConexao(),
                Bebida.class
        );

        TableUtils.createTableIfNotExists(
                ConexaoBanco.getConexao(),
                Garcom.class
        );

        TableUtils.createTableIfNotExists(
                ConexaoBanco.getConexao(),
                Gerente.class
        );

        TableUtils.createTableIfNotExists(
                ConexaoBanco.getConexao(),
                Mesa.class
        );

        TableUtils.createTableIfNotExists(
                ConexaoBanco.getConexao(),
                Comanda.class
        );

        TableUtils.createTableIfNotExists(
                ConexaoBanco.getConexao(),
                ItemSolicitado.class
        );

        System.out.println("Atlas iniciado" + "\n" + "Tabelas criadas");

        AlimentoDao alimentoDao = new AlimentoDao(ConexaoBanco.getConexao());
        BebidaDao bebidaDao = new BebidaDao(ConexaoBanco.getConexao());
        GarcomDao garcomDao = new GarcomDao(ConexaoBanco.getConexao());
        MesaDao mesaDao = new MesaDao(ConexaoBanco.getConexao());
        ItemSolicitadoDao itemSolicitadoDao = new ItemSolicitadoDao(ConexaoBanco.getConexao());













    }
}
