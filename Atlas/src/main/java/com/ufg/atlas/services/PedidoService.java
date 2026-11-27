//este pedaco de codigo funciona como service para o display da cozinha
//busca os pedidos mais recentes da cozinha
//tambem atua recebendo o comando do cozinheiro ao clicar no pedido e alterando seu status no banco de dados

package com.ufg.atlas.services;

import com.ufg.atlas.dao.ComandaDao;
import com.ufg.atlas.dao.ItemSolicitadoDao;
import com.ufg.atlas.modelos.Comanda;
import com.ufg.atlas.modelos.ItemSolicitado;
import com.ufg.atlas.modelos.PedidoCozinha;
import com.ufg.atlas.modelos.StatusPedidoCozinha;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class PedidoService{

    private static final int QUANTIDADE_PEDIDOS_TELA = 18;

    private final ComandaDao comandaDao;
    private final ItemSolicitadoDao itemSolicitadoDao;

    private int ultimoIdVisto;

    public PedidoService(ComandaDao comandaDao, ItemSolicitadoDao itemSolicitadoDao){
        if (comandaDao == null) {
            throw new IllegalArgumentException("ComandaDao não pode ser nulo.");
        }
        if (itemSolicitadoDao == null) {
            throw new IllegalArgumentException("ItemSolicitadoDao não pode ser nulo.");
        }

        this.comandaDao = comandaDao;
        this.itemSolicitadoDao = itemSolicitadoDao;
    }

    public synchronized List<PedidoCozinha> buscarPedidosRecentes() throws SQLException{
        List<PedidoCozinha> pedidos = new ArrayList<>();

        for (Comanda comanda : comandaDao.listarMaisRecentes(QUANTIDADE_PEDIDOS_TELA)){
            if (comanda.obterEnviadaCozinhaEm() <= 0) {
                continue;
            }

            List<ItemSolicitado> itens = itemSolicitadoDao.listarPorComanda(comanda.obterId());
            pedidos.add(new PedidoCozinha(comanda, itens));
            ultimoIdVisto = Math.max(ultimoIdVisto, comanda.obterId());
        }
    }

    public synchronized void enviarParaCozinha(int idComanda) throws SQLException{
        Comanda comanda = exigirComanda(idComanda);

        if (comanda.obterEnviadaCozinhaEm() <= 0){
            comanda.definirEnviadaCozinhaEm(System.currentTimeMillis());
        }
        if (comanda.obterStatusCozinha() == null){
            comanda.definirStatusCozinha(StatusPedidoCozinha.PEDIDO);
        }

        comandaDao.atualizar(comanda);
    }

    public synchronized StatusPedidoCozinha avancarStatus(int idComanda) throws SQLException{
        Comanda comanda = exigirComanda(idComanda);
        StatusPedidoCozinha atual = obterStatusValido(comanda);

        StatusPedidoCozinha proximo;
        switch (atual){
            case PEDIDO -> proximo = StatusPedidoCozinha.EM_PRODUCAO;
            case EM_PRODUCAO -> proximo = StatusPedidoCozinha.PRONTO;
            case PRONTO, CANCELADO -> {
                return atual;
            }
            default -> throw new IllegalStateException("Status de cozinha não reconhecido: " + atual);
        }

        comanda.definirStatusCozinha(proximo);
        comandaDao.atualizar(comanda);
        return proximo;
    }

    public synchronized void definirStatus(int idComanda, StatusPedidoCozinha novoStatus)
            throws SQLException {
        if (novoStatus == null){
            throw new IllegalArgumentException("O status não pode ser nulo.");
        }

        Comanda comanda = exigirComanda(idComanda);
        comanda.definirStatusCozinha(novoStatus);

        if (novoStatus == StatusPedidoCozinha.CANCELADO && comanda.obterEnviadaCozinhaEm() <= 0){
            comanda.definirEnviadaCozinhaEm(System.currentTimeMillis());
        }

        comandaDao.atualizar(comanda);
    }

    public synchronized int obterUltimoIdVisto(){
        return ultimoIdVisto;
    }

    private StatusPedidoCozinha obterStatusValido(Comanda comanda){
        StatusPedidoCozinha status = comanda.obterStatusCozinha();
        return status == null ? StatusPedidoCozinha.PEDIDO : status;
    }

    private Comanda exigirComanda(int idComanda) throws SQLException{
        for (Comanda comanda : comandaDao.listarTodos()){
            if (comanda.obterId() == idComanda){
                return comanda;
            }
        }
        throw new IllegalArgumentException("Comanda #" + idComanda + " não foi encontrada.");
    }
}
