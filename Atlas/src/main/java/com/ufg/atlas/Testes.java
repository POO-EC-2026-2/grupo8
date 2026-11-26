package com.ufg.atlas;

import com.ufg.atlas.dao.*;
import com.ufg.atlas.modelos.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;

import com.ufg.atlas.dao.AlimentoDao;
import com.ufg.atlas.dao.BebidaDao;
import com.ufg.atlas.database.ConexaoBanco;
import com.j256.ormlite.table.TableUtils;
import com.ufg.atlas.modelos.Alimento;
import com.ufg.atlas.modelos.Bebida;
import com.ufg.atlas.services.CardapioService;

public class Testes {
    public static void main(String[] args) throws SQLException, IOException {
        Scanner scanner = new Scanner(System.in);


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
        FuncionarioDao funcionarioDao = new FuncionarioDao(ConexaoBanco.getConexao());
        GerenteDao gerenteDao = new GerenteDao(ConexaoBanco.getConexao());




        CardapioService cardapioService = new CardapioService(alimentoDao, bebidaDao);

        Gerente garcom = gerenteDao.buscarUmPorNome("Rafael");

        System.out.print("Insira o nome: ");
        String nome = scanner.nextLine();

        System.out.print("Insira a descrição: ");
        String descricao = scanner.nextLine();

        System.out.print("Insira o preço: ");
        double preco = scanner.nextDouble();
        scanner.nextLine();

        Alimento comida = new Alimento(nome,descricao,preco,CategoriaAlimento.HAMBURGUER,5);

        cardapioService.cadastrarProduto(garcom, comida);
        System.out.println("Deu certo");
        cardapioService.carregarCardapioComida(garcom);



    }

}

