package com.ufg.atlas.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class TecladoVirtualController {

    @FXML
    private TextField campoTexto;


    @FXML
    private void digitarTecla(ActionEvent evento) {
        Button tecla = (Button) evento.getSource();
        campoTexto.appendText(tecla.getText());
    }


    @FXML
    private void digitarEspaco() {
        campoTexto.appendText(" ");
    }


    @FXML
    private void apagarUltimo() {
        String texto = campoTexto.getText();
        if (texto != null && !texto.isEmpty()) {
            campoTexto.setText(texto.substring(0, texto.length() - 1));
        }
    }

    @FXML
    private void limparTexto() {
        campoTexto.clear();
    }
}
