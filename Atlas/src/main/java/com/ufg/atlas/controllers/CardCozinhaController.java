package com.ufg.atlas.controllers;

import com.ufg.atlas.modelos.ItemSolicitado;
import com.ufg.atlas.modelos.PedidoCozinha;
import com.ufg.atlas.modelos.StatusPedidoCozinha;
import com.ufg.atlas.services.PedidoService;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class CardCozinhaController {

    @FXML
    private Label lblMesa;

    @FXML
    private Label lblQuantidadeItens;

    @FXML
    private TableView<ItemSolicitado> tabelaItens;

    @FXML
    private TableColumn<ItemSolicitado, String> colunaItens;

    private PedidoService pedidoService;
    private PedidoCozinha pedido;


    @FXML
    public void initialize() {
        colunaItens.setCellValueFactory(celula -> new SimpleStringProperty(
                celula.getValue().getQuantidade() + "x " + celula.getValue().getProduto().getNome()));
    }


    public void setPedidoService(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }


    public void exibirPedido(PedidoCozinha pedido) {
        this.pedido = pedido;
        lblMesa.setText(String.valueOf(pedido.getNumeroMesa()));

        int quantidade = 0;
        for (ItemSolicitado item : pedido.getItens()) {
            quantidade += item.getQuantidade();
        }
        lblQuantidadeItens.setText(String.valueOf(quantidade));
        tabelaItens.getItems().setAll(pedido.getItens());
    }


    @FXML
    private void marcarEmProducao() {
        mudarStatus(StatusPedidoCozinha.EM_PRODUCAO);
    }


    @FXML
    private void marcarPronto() {
        mudarStatus(StatusPedidoCozinha.PRONTO);
    }


    private void mudarStatus(StatusPedidoCozinha status) {
        if (pedidoService == null || pedido == null) {
            mostrarErro("O card ainda não recebeu o pedido.");
            return;
        }
        try {
            pedidoService.definirStatus(pedido.getId(), status);
        } catch (Exception erro) {
            mostrarErro(erro.getMessage());
        }
    }


    private void mostrarErro(String mensagem) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle("Erro");
        alerta.setHeaderText(null);
        alerta.setContentText(mensagem);
        alerta.showAndWait();
    }
}
