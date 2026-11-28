package com.ufg.atlas.controllers;

import com.ufg.atlas.modelos.Funcionario;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;

public class IntermediariaGerenteController {

    @FXML
    private Label nomeGerente;

    private Funcionario funcionario;


    public void definirFuncionario(Funcionario funcionario) {
        if (funcionario == null) {
            mostrarErro("O funcionário não pode ser nulo.");
            return;
        }
        this.funcionario = funcionario;
        nomeGerente.setText(funcionario.obterNome());
    }


    @FXML
    private void consultarMesas() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ufg/atlas/views/Interface_Principal_Gerente.fxml"));
            Parent tela = loader.load();
            GerenteController gerenteController = loader.getController();
            gerenteController.definirFuncionario(funcionario);
            nomeGerente.getScene().setRoot(tela);
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
