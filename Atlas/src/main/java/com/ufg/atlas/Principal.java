package com.ufg.atlas;
import com.ufg.atlas.dao.*;
import com.ufg.atlas.modelos.*;
import com.ufg.atlas.services.*;
import com.ufg.atlas.database.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;

import com.ufg.atlas.dao.AlimentoDao;
import com.ufg.atlas.dao.BebidaDao;
import com.ufg.atlas.database.ConexaoBanco;
import com.j256.ormlite.table.TableUtils;
import com.ufg.atlas.modelos.Alimento;
import com.ufg.atlas.modelos.Bebida;

public class Principal {
    public static void main(String[] argumentos) throws SQLException, IOException {
        Scanner leitor = new Scanner(System.in);

        ConexaoBanco.conectar();

        TableUtils.createTableIfNotExists(
                ConexaoBanco.getConexao(),
                Alimento.class
        );

        TableUtils.createTableIfNotExists(
                ConexaoBanco.getConexao(),
                Bebida.class
        );


        System.out.println("Atlas iniciado");

        System.out.println("Teste de push");

        AlimentoDao alimentoDao = new AlimentoDao(
                ConexaoBanco.getConexao()
        );

        BebidaDao bebidaDao = new BebidaDao(
                ConexaoBanco.getConexao()
        );

    }
}