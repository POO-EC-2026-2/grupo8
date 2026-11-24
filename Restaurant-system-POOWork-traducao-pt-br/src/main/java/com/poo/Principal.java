package com.poo;

import com.poo.dao.AlimentoDao;
import com.poo.dao.BebidaDao;
import com.poo.modelos.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;
import com.poo.database.ConexaoBanco;
import com.j256.ormlite.table.TableUtils;

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


        Restaurante restaurante = new Restaurante();
        Mesa mesa1 = new Mesa(01);
        Garcom garcom = new Garcom(02, "Gabriel");
        Mesa mesa2 = new Mesa(02);
        Garcom garcom2 = new Garcom(02, "Rafael");

        System.out.println("Teste de push");

        AlimentoDao alimentoDao = new AlimentoDao(
                ConexaoBanco.getConexao()
        );

        BebidaDao bebidaDao = new BebidaDao(
                ConexaoBanco.getConexao()
        );

        Alimento hamburguer = new Alimento(
                01, "Hambúrguer", "Hambúrguer", 30,
                CategoriaAlimento.HAMBURGUER, 01
        );

        alimentoDao.salvar(hamburguer);
        Bebida agua = new Bebida(
                01, "Água", "Somente água", 5, CategoriaBebida.AGUA
        );

        bebidaDao.salvar(agua);

        System.out.println("Objetos salvos com sucesso");

        restaurante.obterCardapio().adicionarProduto(hamburguer);
        restaurante.obterCardapio().adicionarProduto(agua);

        int opcao;

        do {
            System.out.println("""
                    RESTAURANTE UFG

                    1- Abrir comanda
                    2- Ver cardápio
                    3- Solicitar produto
                    4- Ver comanda
                    5- Fechar comanda
                    0- Sair
                    """);

            opcao = leitor.nextInt();

            switch (opcao) {
                case 0:
                    System.out.println("Encerrando...");
                    break;

                case 1:
                    Comanda comanda = restaurante.abrirComanda(mesa1, garcom);
                    System.out.println("Comanda #" + comanda.obterId() + " aberta!");
                    break;

                case 2:
                    restaurante.obterCardapio().exibirProdutos();

                case 3:
                    restaurante.obterCardapio().exibirProdutos();
                    System.out.println("Qual produto você gostaria de solicitar?");
                    int escolhaProduto = leitor.nextInt();
                    System.out.println("Qual é a quantidade?");
                    int quantidade = leitor.nextInt();
                    Produto produtoPesquisado = restaurante.obterCardapio()
                            .buscarProduto(escolhaProduto);
                    ItemSolicitado itemSolicitado = new ItemSolicitado(
                            produtoPesquisado, quantidade
                    );
                    System.out.println("Qual é o número da sua comanda?");
                    restaurante.buscarComanda(leitor.nextInt());
            }

        } while (opcao != 0);

        leitor.close();
    }
}
