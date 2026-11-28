package com.ufg.atlas.controllers;

import com.ufg.atlas.modelos.Mesa;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class CardMesaMobileController {

    @FXML
    private Label lblMesa;

    @FXML
    private Label lblStatus;


    public void exibirMesa(Mesa mesa) {
        lblMesa.setText("Mesa " + mesa.obterNumero());
        lblStatus.setText(String.valueOf(mesa.obterStatus()));
    }
}
