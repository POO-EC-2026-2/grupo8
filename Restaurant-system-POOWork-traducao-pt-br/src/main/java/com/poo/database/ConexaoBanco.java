package com.poo.database;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;

    public class ConexaoBanco {

        private static final String URL =
                "jdbc:sqlite:database/atlas.db";

        private static ConnectionSource conexao;

        public static void conectar() throws SQLException, IOException {

            Files.createDirectories(Path.of("database"));

            conexao = new JdbcConnectionSource(URL);

            System.out.println("Banco conectado com sucesso!");
        }

        public static ConnectionSource getConexao() {
            return conexao;
        }

        public static void fechar() throws IOException {
            if (conexao != null) {
                conexao.closeQuietly();
                conexao = null;
            }
        }
    }
