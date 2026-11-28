//controller para o display da cozinha

package com.ufg.atlas.controllers;

import com.ufg.atlas.modelos.PedidoCozinha;
import com.ufg.atlas.services.PedidoService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class CozinhaController {

    @FXML
    private FlowPane painelPedidos;

    @FXML
    private Label lblUltimoPedido;

    private PedidoService pedidoService;
    private Timer timer;
    private boolean atualizacaoIniciada = false;
    private int ultimoIdMostrado = 0;
    
    private ArrayList<Integer> idsCards = new ArrayList<>();
    private ArrayList<Node> nosCards = new ArrayList<>();
    private ArrayList<CardPedidoController> controllersCards = new ArrayList<>();

    public void setPedidoService(PedidoService pedidoService){
        if (pedidoService == null) {
            throw new IllegalArgumentException("PedidoService nao pode ser nulo.");
        }
        this.pedidoService = pedidoService;
    }

    public void iniciarAtualizacao(){
        if (pedidoService == null){
            throw new IllegalStateException("Configure o PedidoService antes de iniciar a atualizacao.");
        }
        if (atualizacaoIniciada){
            return;
        }

        atualizacaoIniciada = true;
        timer = new Timer(true);

        TimerTask tarefa = new TimerTask(){
            @Override
            public void run(){
                consultarPedidos();
            }
        };
    }

    private void consultarPedidos(){
        if (pedidoService == null || !atualizacaoIniciada){
            return;
        }

        try{
            List<PedidoCozinha> pedidos = pedidoService.buscarPedidosRecentes();
            Platform.runLater(() -> mostrarPedidos(pedidos));
        }catch (SQLException e){
            e.printStackTrace();
            Platform.runLater(() -> {
                if (lblUltimoPedido != null){
                    lblUltimoPedido.setText("Não foi possível atualizar os pedidos. Tentando novamente");
                }
            });
        } catch (Exception e){
            e.printStackTrace();
            Platform.runLater(() -> {
                if (lblUltimoPedido != null){
                    lblUltimoPedido.setText("Não foi possivel atualizar os pedidos. Tentando novamente");
                }
            });
        }
    }

    private void mostrarPedidos(List<PedidoCozinha> pedidos){
        if (!Platform.isFxApplicationThread()){
            Platform.runLater(() -> mostrarPedidos(pedidos));
            return;
        }
        if (painelPedidos == null){
            return;
        }

        if (pedidos == null){
            pedidos = new ArrayList<>();
        }

        ArrayList<Node> cardsParaMostrar = new ArrayList<>();

        for (int i = 0; i < pedidos.size(); i++){
            PedidoCozinha pedido = pedidos.get(i);
            if (pedido == null) {
                continue;
            }

            int id = pedido.getId();
            if (id > ultimoIdMostrado){
                ultimoIdMostrado = id;
            }

            int posicaoExistente = -1;
            for (int j = 0; j < idsCards.size(); j++){
                if (idsCards.get(j) == id){
                    posicaoExistente = j;
                    break;
                }
            }

            CardPedidoController cardController = null;
            Node noCard = null;

            if (posicaoExistente == -1){
                try{
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ufg/atlas/views/CardPedido.fxml"));
                    noCard = loader.load();
                    cardController = loader.getController();
                    cardController.setAcaoClique(idPedido -> tratarCliqueNoCard(idPedido));
                    idsCards.add(id);
                    nosCards.add(noCard);
                    controllersCards.add(cardController);
                } catch (IOException e){
                    e.printStackTrace();
                    continue;
                } catch (Exception e){
                    e.printStackTrace();
                    continue;
                }
            }else{
                cardController = controllersCards.get(posicaoExistente);
                noCard = nosCards.get(posicaoExistente);
            }

            cardController.exibirPedido(pedido);
            cardsParaMostrar.add(noCard);
        }
        for (int i = idsCards.size() - 1; i >= 0; i--){
            int idSalvo = idsCards.get(i);
            boolean aindaExiste = false;

            for (int j = 0; j < pedidos.size(); j++){
                PedidoCozinha p = pedidos.get(j);
                if (p != null && p.getId() == idSalvo){
                    aindaExiste = true;
                    break;
                }
            }

            if (!aindaExiste){
                CardPedidoController removido = controllersCards.get(i);
                if (removido != null){
                    removido.pararRelogio();
                }
                idsCards.remove(i);
                nosCards.remove(i);
                controllersCards.remove(i);
            }
        }

        painelPedidos.getChildren().clear();
        painelPedidos.getChildren().addAll(cardsParaMostrar);
        atualizarRotulo(pedidos.size());
    }

    private void tratarCliqueNoCard(int idPedido){
        if (!atualizacaoIniciada){
            return;
        }

        Thread threadClique = new Thread(() -> {
            try{
                pedidoService.avancarStatus(idPedido);
                consultarPedidos();
            }catch (SQLException e){
                e.printStackTrace();
                Platform.runLater(() -> {
                    if (lblUltimoPedido != null){
                        lblUltimoPedido.setText("Não foi possível atualizar o status do pedido #" + idPedido);
                    }
                });
            }catch (Exception e){
                e.printStackTrace();
                Platform.runLater(() -> {
                    if (lblUltimoPedido != null){
                        lblUltimoPedido.setText("Não foi possível atualizar o status do pedido #" + idPedido);
                    }
                });
            }
        });
        threadClique.setDaemon(true);
        threadClique.start();
    }

    private void atualizarRotulo(int quantidadeExibida){
        if (lblUltimoPedido == null){
            return;
        }
        if (ultimoIdMostrado == 0){
            lblUltimoPedido.setText("Pedidos da cozinha (" + quantidadeExibida + ")");
        }else{
            lblUltimoPedido.setText("Pedidos da cozinha (" + quantidadeExibida + ") — último número: " + ultimoIdMostrado);
        }
    }

    public void pararAtualizacao(){
        atualizacaoIniciada = false;
        if (timer != null){
            timer.cancel();
        }

        if (Platform.isFxApplicationThread()){
            for (int i = 0; i < controllersCards.size(); i++){
                controllersCards.get(i).pararRelogio();
            }
        } else {
            Platform.runLater(() -> {
                for (int i = 0; i < controllersCards.size(); i++){
                    controllersCards.get(i).pararRelogio();
                }
            });
        }
    }

    public int getUltimoIdMostrado(){
        return ultimoIdMostrado;
    }
}
