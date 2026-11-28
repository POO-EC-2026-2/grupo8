package com.ufg.atlas.controllers;

import com.ufg.atlas.dao.MesaDao;
import com.ufg.atlas.database.ConexaoBanco;
import com.ufg.atlas.modelos.Funcionario;
import com.ufg.atlas.services.MesaService;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

public class GarcomMobileController {

    @FXML
    private TextField txtBuscarMesa;

    private Funcionario funcionario;


    public void definirFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }


    @FXML
    private void buscarMesa() {
        if (funcionario == null) {
            mostrarErro("A tela ainda não recebeu o funcionário.");
            return;
        }
        try {
            int numero = Integer.parseInt(txtBuscarMesa.getText().trim());
            MesaService mesaService = new MesaService(new MesaDao(ConexaoBanco.getConexao()));
            if (mesaService.buscarPorNumero(funcionario, numero) == null) {
                mostrarErro("Mesa " + numero + " não encontrada.");
                return;
            }
            abrirTelaMesa(numero);
        } catch (NumberFormatException erro) {
            mostrarErro("Digite o número da mesa.");
        } catch (Exception erro) {
            mostrarErro(erro.getMessage());
        }
    }


    private void abrirTelaMesa(int numero) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ufg/atlas/views/Interface_Mobile_Mesas.fxml"));
        Parent tela = loader.load();
        MesaMobileController mesaController = loader.getController();
        mesaController.definirFuncionario(funcionario);
        mesaController.carregarMesa(numero);
        txtBuscarMesa.getScene().setRoot(tela);
    }


    private void mostrarErro(String mensagem) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle("Erro");
        alerta.setHeaderText(null);
        alerta.setContentText(mensagem);
        alerta.showAndWait();
    }
}
