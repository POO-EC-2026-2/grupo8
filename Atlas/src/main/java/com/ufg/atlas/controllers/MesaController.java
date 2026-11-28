package com.ufg.atlas.controllers;

import com.ufg.atlas.dao.ComandaDao;
import com.ufg.atlas.dao.MesaDao;
import com.ufg.atlas.database.ConexaoBanco;
import com.ufg.atlas.modelos.Comanda;
import com.ufg.atlas.modelos.Funcionario;
import com.ufg.atlas.modelos.Mesa;
import com.ufg.atlas.services.ComandaService;
import com.ufg.atlas.services.MesaService;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;

public class MesaController {

    @FXML
    private Label nMesa;

    @FXML
    private Label estadoMesa;

    @FXML
    private Label nomeG;

    private Funcionario funcionario;
    private MesaService mesaService;
    private ComandaService comandaService;
    private int numeroMesa;


    public void definirFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
        try {
            mesaService = new MesaService(new MesaDao(ConexaoBanco.getConexao()));
            comandaService = new ComandaService(new ComandaDao(ConexaoBanco.getConexao()));
        } catch (Exception erro) {
            mostrarErro(erro.getMessage());
        }
    }


    public void carregarMesa(int numero) {
        if (mesaService == null || comandaService == null) {
            mostrarErro("A tela ainda não recebeu o funcionário.");
            return;
        }
        try {
            Mesa mesa = mesaService.buscarPorNumero(funcionario, numero);
            if (mesa == null) {
                mostrarErro("Mesa " + numero + " não encontrada.");
                return;
            }
            numeroMesa = numero;
            nMesa.setText("Mesa " + numero);
            estadoMesa.setText(String.valueOf(mesa.obterStatus()));

            Comanda comanda = comandaService.buscarComandaAbertaPorMesa(funcionario, numero);
            if (comanda != null) {
                nomeG.setText(comanda.obterFuncionario().obterNome());
            } else {
                nomeG.setText("--");
            }
        } catch (Exception erro) {
            mostrarErro(erro.getMessage());
        }
    }


    @FXML
    private void abrirCardapio() {
        if (numeroMesa == 0) {
            mostrarErro("Nenhuma mesa foi carregada.");
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ufg/atlas/views/Interface_Cardapio.fxml"));
            Parent tela = loader.load();
            CardapioController cardapioController = loader.getController();
            cardapioController.definirFuncionario(funcionario);
            cardapioController.definirMesa(numeroMesa);
            nMesa.getScene().setRoot(tela);
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
