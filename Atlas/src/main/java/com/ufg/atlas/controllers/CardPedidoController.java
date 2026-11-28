package com.ufg.atlas.controllers;

import com.ufg.atlas.modelos.ItemSolicitado;
import com.ufg.atlas.modelos.PedidoCozinha;
import com.ufg.atlas.modelos.StatusPedidoCozinha;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.util.function.Consumer;

public class CardPedidoController {

    @FXML
    private Label lblNumeroPedido;

    @FXML
    private Label lblMesa;

    @FXML
    private VBox listaItens;

    @FXML
    private Label lblStatus;

    @FXML
    private Label lblTempo;

    private PedidoCozinha pedido;
    private Consumer<Integer> acaoClique;
    private Timeline relogio;


    public void setAcaoClique(Consumer<Integer> acaoClique) {
        this.acaoClique = acaoClique;
    }


    public void exibirPedido(PedidoCozinha pedido) {
        this.pedido = pedido;
        lblNumeroPedido.setText("Pedido #" + pedido.getId());
        lblMesa.setText("Mesa " + pedido.getNumeroMesa());
        lblStatus.setText(String.valueOf(pedido.getStatus()));
        mostrarItens();
        atualizarTempo();

        if (pedidoFinalizado()) {
            pararRelogio();
        } else {
            iniciarRelogio();
        }
    }


    private void mostrarItens() {
        listaItens.getChildren().clear();
        for (ItemSolicitado item : pedido.getItens()) {
            listaItens.getChildren().add(new Label(item.getQuantidade() + "x " + item.getProduto().getNome()));
            if (item.getObservacao() != null && !item.getObservacao().isEmpty()) {
                listaItens.getChildren().add(new Label(item.getObservacao()));
            }
        }
    }


    private boolean pedidoFinalizado() {
        StatusPedidoCozinha status = pedido.getStatus();
        return status == StatusPedidoCozinha.PRONTO || status == StatusPedidoCozinha.CANCELADO;
    }


    private void iniciarRelogio() {
        if (relogio != null) {
            return;
        }
        relogio = new Timeline(new KeyFrame(Duration.seconds(1), evento -> atualizarTempo()));
        relogio.setCycleCount(Timeline.INDEFINITE);
        relogio.play();
    }


    public void pararRelogio() {
        if (relogio != null) {
            relogio.stop();
            relogio = null;
        }
    }


    private void atualizarTempo() {
        long segundos = (System.currentTimeMillis() - pedido.getEnviadaCozinhaEm()) / 1000;
        if (segundos < 0) {
            segundos = 0;
        }
        lblTempo.setText(String.format("Na cozinha: %02d:%02d", segundos / 60, segundos % 60));
    }


    @FXML
    private void clicarCard() {
        if (pedido != null && acaoClique != null) {
            acaoClique.accept(pedido.getId());
        }
    }
}
