package com.ufg.atlas.controllers;

import com.ufg.atlas.dao.AlimentoDao;
import com.ufg.atlas.dao.BebidaDao;
import com.ufg.atlas.database.ConexaoBanco;
import com.ufg.atlas.modelos.Funcionario;
import com.ufg.atlas.modelos.Produto;
import com.ufg.atlas.services.CardapioService;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class CardapioGarcomController {

    @FXML
    private TableView<Produto> tabelaComidas;

    @FXML
    private TableColumn<Produto, String> colunaComidaItem;

    @FXML
    private TableColumn<Produto, String> colunaComidaPreco;

    @FXML
    private TableView<Produto> tabelaBebidas;

    @FXML
    private TableColumn<Produto, String> colunaBebidaItem;

    @FXML
    private TableColumn<Produto, String> colunaBebidaPreco;


    @FXML
    public void initialize() {
        colunaComidaItem.setCellValueFactory(celula -> new SimpleStringProperty(celula.getValue().getNome()));
        colunaComidaPreco.setCellValueFactory(celula -> new SimpleStringProperty(formatarPreco(celula.getValue())));
        colunaBebidaItem.setCellValueFactory(celula -> new SimpleStringProperty(celula.getValue().getNome()));
        colunaBebidaPreco.setCellValueFactory(celula -> new SimpleStringProperty(formatarPreco(celula.getValue())));
    }


    public void definirFuncionario(Funcionario funcionario) {
        try {
            AlimentoDao alimentoDao = new AlimentoDao(ConexaoBanco.getConexao());
            BebidaDao bebidaDao = new BebidaDao(ConexaoBanco.getConexao());
            CardapioService cardapioService = new CardapioService(alimentoDao, bebidaDao);

            tabelaComidas.getItems().setAll(cardapioService.listarTodasComidas(funcionario));
            tabelaBebidas.getItems().setAll(cardapioService.listarTodasBebidas(funcionario));
        } catch (Exception erro) {
            mostrarErro(erro.getMessage());
        }
    }


    private String formatarPreco(Produto produto) {
        return "R$ " + String.format("%.2f", produto.getPreco());
    }


    private void mostrarErro(String mensagem) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle("Erro");
        alerta.setHeaderText(null);
        alerta.setContentText(mensagem);
        alerta.showAndWait();
    }
}
