package com.ufg.atlas.controllers;

import com.ufg.atlas.dao.ComandaDao;
import com.ufg.atlas.dao.ItemSolicitadoDao;
import com.ufg.atlas.dao.MesaDao;
import com.ufg.atlas.database.ConexaoBanco;
import com.ufg.atlas.modelos.Funcionario;
import com.ufg.atlas.modelos.Garcom;
import com.ufg.atlas.modelos.Mesa;
import com.ufg.atlas.services.ComandaService;
import com.ufg.atlas.services.MesaService;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;

public class MesaMobileController {

    @FXML
    private Label nMesa;

    @FXML
    private Label estadoMesa;

    private Funcionario funcionario;
    private MesaService mesaService;
    private ComandaService comandaService;
    private int numeroMesa;


    public void definirFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
        try {
            MesaDao mesaDao = new MesaDao(ConexaoBanco.getConexao());
            mesaService = new MesaService(mesaDao);
            comandaService = new ComandaService(new ComandaDao(ConexaoBanco.getConexao()), mesaDao,
                    new ItemSolicitadoDao(ConexaoBanco.getConexao()));
        } catch (Exception erro) {
            mostrarErro(erro.getMessage());
        }
    }


    public void carregarMesa(int numero) {
        if (mesaService == null) {
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
        } catch (Exception erro) {
            mostrarErro(erro.getMessage());
        }
    }


    @FXML
    private void abrirMesa() {
        if (comandaService == null || numeroMesa == 0) {
            mostrarErro("Nenhuma mesa foi carregada.");
            return;
        }
        if (!(funcionario instanceof Garcom)) {
            mostrarAviso("Apenas garçons podem abrir a mesa.");
            return;
        }
        try {
            Mesa mesa = mesaService.buscarPorNumero(funcionario, numeroMesa);
            comandaService.abrirComanda((Garcom) funcionario, mesa);
        } catch (Exception erro) {
            mostrarErro(erro.getMessage());
            return;
        }
        carregarMesa(numeroMesa);
    }


    private void mostrarErro(String mensagem) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle("Erro");
        alerta.setHeaderText(null);
        alerta.setContentText(mensagem);
        alerta.showAndWait();
    }


    private void mostrarAviso(String mensagem) {
        Alert alerta = new Alert(Alert.AlertType.WARNING);
        alerta.setTitle("Aviso");
        alerta.setHeaderText(null);
        alerta.setContentText(mensagem);
        alerta.showAndWait();
    }
}
