package com.ufg.atlas.controllers;

import com.ufg.atlas.dao.MesaDao;
import com.ufg.atlas.database.ConexaoBanco;
import com.ufg.atlas.modelos.Funcionario;
import com.ufg.atlas.modelos.Mesa;
import com.ufg.atlas.modelos.StatusMesa;
import com.ufg.atlas.services.MesaService;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.util.List;

public class GarcomController {

    @FXML
    private Label totalMesas;

    @FXML
    private Label mesasLivres;

    @FXML
    private Label mesasOcupadas;

    @FXML
    private TextField campoNumeroMesa;

    @FXML
    private ComboBox<StatusMesa> comboEstados;

    @FXML
    private TableView<Mesa> tabelaMesas;

    @FXML
    private TableColumn<Mesa, String> colunaNumero;

    @FXML
    private TableColumn<Mesa, String> colunaLocalizacao;

    @FXML
    private TableColumn<Mesa, String> colunaEstado;

    @FXML
    private TableColumn<Mesa, String> colunaGarcom;

    private Funcionario funcionario;
    private MesaService mesaService;


    @FXML
    public void initialize() {
        colunaNumero.setCellValueFactory(celula -> new SimpleStringProperty(String.valueOf(celula.getValue().obterNumero())));
        colunaEstado.setCellValueFactory(celula -> new SimpleStringProperty(String.valueOf(celula.getValue().obterStatus())));
        comboEstados.getItems().addAll(StatusMesa.values());

        tabelaMesas.setOnMouseClicked(evento -> {
            Mesa mesa = tabelaMesas.getSelectionModel().getSelectedItem();
            if (evento.getClickCount() == 2 && mesa != null) {
                abrirMesa(mesa.obterNumero());
            }
        });
    }


    private void abrirMesa(int numero) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ufg/atlas/views/Interface_Mesas.fxml"));
            Parent tela = loader.load();
            MesaController mesaController = loader.getController();
            mesaController.definirFuncionario(funcionario);
            mesaController.carregarMesa(numero);
            tabelaMesas.getScene().setRoot(tela);
        } catch (Exception erro) {
            mostrarErro(erro.getMessage());
        }
    }


    public void definirFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
        try {
            mesaService = new MesaService(new MesaDao(ConexaoBanco.getConexao()));
        } catch (Exception erro) {
            mostrarErro(erro.getMessage());
            return;
        }
        carregarTabela();
        atualizarContadores();
    }


    private void carregarTabela() {
        try {
            List<Mesa> mesas = mesaService.listarTodas(funcionario);
            tabelaMesas.getItems().setAll(mesas);
        } catch (Exception erro) {
            mostrarErro(erro.getMessage());
        }
    }


    private void atualizarContadores() {
        try {
            totalMesas.setText(String.valueOf(mesaService.listarTodas(funcionario).size()));
            mesasLivres.setText(String.valueOf(mesaService.listarMesasLivres(funcionario).size()));
            mesasOcupadas.setText(String.valueOf(mesaService.listarMesasOcupadas(funcionario).size()));
        } catch (Exception erro) {
            mostrarErro(erro.getMessage());
        }
    }


    @FXML
    private void filtrarPorEstado() {
        StatusMesa status = comboEstados.getValue();
        if (status == null) {
            return;
        }
        if (mesaService == null) {
            mostrarErro("A tela ainda não recebeu o funcionário.");
            return;
        }
        try {
            List<Mesa> mesas = mesaService.listarPorStatus(funcionario, status);
            tabelaMesas.getItems().setAll(mesas);
        } catch (Exception erro) {
            mostrarErro(erro.getMessage());
        }
    }


    @FXML
    private void buscarMesa() {
        if (mesaService == null) {
            mostrarErro("A tela ainda não recebeu o funcionário.");
            return;
        }
        int numero;
        try {
            numero = Integer.parseInt(campoNumeroMesa.getText().trim());
        } catch (NumberFormatException erro) {
            mostrarErro("O número da mesa deve ser um número inteiro.");
            return;
        }
        try {
            Mesa mesa = mesaService.buscarPorNumero(funcionario, numero);
            if (mesa == null) {
                mostrarAviso("Mesa " + numero + " não encontrada.");
                return;
            }
            tabelaMesas.getItems().setAll(mesa);
        } catch (Exception erro) {
            mostrarErro(erro.getMessage());
        }
    }


    @FXML
    private void limparFiltros() {
        comboEstados.setValue(null);
        campoNumeroMesa.clear();
        if (mesaService == null) {
            mostrarErro("A tela ainda não recebeu o funcionário.");
            return;
        }
        carregarTabela();
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
