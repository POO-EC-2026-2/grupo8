package com.ufg.atlas.controllers;

import com.ufg.atlas.modelos.ItemSolicitado;
import com.ufg.atlas.modelos.PedidoCozinha;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class CardPedidoMobileController {

    @FXML
    private Label lblNumeroPedido;

    @FXML
    private Label lblStatus;

    @FXML
    private TableView<ItemSolicitado> tabelaItens;

    @FXML
    private TableColumn<ItemSolicitado, String> colunaItens;


    @FXML
    public void initialize() {
        colunaItens.setCellValueFactory(celula -> new SimpleStringProperty(
                celula.getValue().getQuantidade() + "x " + celula.getValue().getProduto().getNome()));
    }


    public void exibirPedido(PedidoCozinha pedido) {
        lblNumeroPedido.setText("Pedido #" + pedido.getId());
        lblStatus.setText(String.valueOf(pedido.getStatus()));
        tabelaItens.getItems().setAll(pedido.getItens());
    }
}
