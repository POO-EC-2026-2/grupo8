package com.ufg.atlas.controllers;

import com.ufg.atlas.dao.AlimentoDao;
import com.ufg.atlas.dao.BebidaDao;
import com.ufg.atlas.database.ConexaoBanco;
import com.ufg.atlas.modelos.Funcionario;
import com.ufg.atlas.modelos.Produto;
import com.ufg.atlas.services.CardapioService;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class CardapioController {

    @FXML
    private VBox listaComidas;

    @FXML
    private VBox listaBebidas;

    @FXML
    private Label quantidadeItens;

    @FXML
    private Label valorParcial;

    private Funcionario funcionario;
    private CardapioService cardapioService;
    private List<Produto> itensEscolhidos = new ArrayList<>();


    public void definirFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
        try {
            AlimentoDao alimentoDao = new AlimentoDao(ConexaoBanco.getConexao());
            BebidaDao bebidaDao = new BebidaDao(ConexaoBanco.getConexao());
            cardapioService = new CardapioService(alimentoDao, bebidaDao);

            List<Produto> comidas = cardapioService.listarTodasComidas(funcionario);
            List<Produto> bebidas = cardapioService.listarTodasBebidas(funcionario);

          
            listaComidas.getChildren().clear();
            for (Produto produto : comidas) {
                listaComidas.getChildren().add(criarBotaoProduto(produto));
            }
            listaBebidas.getChildren().clear();
            for (Produto produto : bebidas) {
                listaBebidas.getChildren().add(criarBotaoProduto(produto));
            }
        } catch (Exception erro) {
            mostrarErro(erro.getMessage());
        }
    }



    private Button criarBotaoProduto(Produto produto) {
        Button botao = new Button(produto.getNome() + " — R$ " + String.format("%.2f", produto.getPreco()));
        botao.setMaxWidth(Double.MAX_VALUE);
        botao.setPrefHeight(55.0);
        botao.setOnAction(evento -> adicionarProduto(produto));
        return botao;
    }

    private void adicionarProduto(Produto produto) {
        itensEscolhidos.add(produto);
        atualizarResumo();
    }

    private void atualizarResumo() {
        double soma = 0;
        for (Produto produto : itensEscolhidos) {
            soma += produto.getPreco();
        }
        quantidadeItens.setText(String.valueOf(itensEscolhidos.size()));
        valorParcial.setText("R$ " + String.format("%.2f", soma));
    }



    private void mostrarErro(String mensagem) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle("Erro");
        alerta.setHeaderText(null);
        alerta.setContentText(mensagem);
        alerta.showAndWait();
    }
}
