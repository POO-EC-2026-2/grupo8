package com.ufg.atlas.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class LoginGarcomController {

    @FXML
    private TextField campoEmail;

    @FXML
    private TextField campoSenha;


    @FXML
    private void limparCampos() {
        campoEmail.clear();
        campoSenha.clear();
    }
}
