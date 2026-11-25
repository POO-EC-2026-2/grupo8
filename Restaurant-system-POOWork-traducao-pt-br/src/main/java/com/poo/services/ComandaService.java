package com.poo.service;

import com.poo.dao.ComandaDao;
import com.poo.dao.ItemSolicitadoDao;
import com.poo.dao.MesaDao;
import com.poo.modelos.Comanda;
import com.poo.modelos.Funcionario;
import com.poo.modelos.ItemSolicitado;
import com.poo.modelos.Mesa;
import com.poo.modelos.NivelAcesso;
import com.poo.modelos.Produto;
import com.poo.modelos.StatusComanda;
import com.poo.modelos.StatusMesa;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ComandaService {

    private final ComandaDao comandaDao;
    private final MesaDao mesaDao;
    private final ItemSolicitadoDao itemSolicitadoDao;

    
    //Inicializa o serviço de comandas usando apenas o repositório principal de comandas
    public ComandaService(ComandaDao comandaDao) {
        this(comandaDao, null, null);
    }

    
    //Inicializa o serviço de comandas utilizando uma parametragem completa
    public ComandaService(ComandaDao comandaDao, MesaDao mesaDao, ItemSolicitadoDao itemSolicitadoDao) {
        if (comandaDao == null) {
            throw new IllegalArgumentException("ComandaDao não pode ser nulo.");
        }
        this.comandaDao = comandaDao;
        this.mesaDao = mesaDao;
        this.itemSolicitadoDao = itemSolicitadoDao;
    }

    
    //Traduz o nível do funcionário para um inteiro
    //Método para facilizar a manipulação dos demais métodos
    private int obterValorNivel(NivelAcesso nivel) {
        if (nivel == null) {
            return -1;
        }
        switch (nivel) {
            case NIVEL_0:
                return 0;
            case NIVEL_1:
                return 1;
            case NIVEL_2:
                return 2;
            case NIVEL_3:
                return 3;
            default:
                return nivel.ordinal();
        }
    }
    
    
    //Métodos de verificação de nível do funcionário
    public boolean podeFecharAlta(Funcionario funcionario) {
        if (funcionario == null) {
            return false;
        }
        return obterValorNivel(funcionario.obterNivel()) >= 3;
    }
    
    private void validarPermissaoAlta(Funcionario funcionario) {
        if (funcionario == null) {
            throw new IllegalArgumentException("O funcionário responsável pela operação não pode ser nulo.");
        }
        if (!podeFecharAlta(funcionario)) {
            throw new SecurityException("Acesso negado para '" + funcionario.obterNome()
                    + "' (nível: " + funcionario.obterNivel()
                    + "). Apenas funcionários de NIVEL_3 ou superior podem interferir na comanda.");
        }
    }
    
    public boolean podeInterferirNaComanda(Funcionario funcionario) {
        if (funcionario == null) {
            return false;
        }
        return obterValorNivel(funcionario.obterNivel()) >= 2;
    }

    private void validarPermissaoInterferencia(Funcionario funcionario) {
        if (funcionario == null) {
            throw new IllegalArgumentException("O funcionário responsável pela operação não pode ser nulo.");
        }
        if (!podeInterferirNaComanda(funcionario)) {
            throw new SecurityException("Acesso negado para '" + funcionario.obterNome()
                    + "' (nível: " + funcionario.obterNivel()
                    + "). Apenas funcionários de NIVEL_2 ou superior podem interferir na comanda.");
        }
    }

    public boolean podeVisualizarComanda(Funcionario funcionario) {
        if (funcionario == null) {
            return false;
        }
        return obterValorNivel(funcionario.obterNivel()) >= 1;
    }

    private void validarPermissaoVisualizacao(Funcionario funcionario) {
        if (funcionario == null) {
            throw new IllegalArgumentException("O funcionário não pode ser nulo.");
        }
        if (!podeVisualizarComanda(funcionario)) {
            throw new SecurityException("Acesso negado para '" + funcionario.obterNome()
                    + "' (nível: " + funcionario.obterNivel()
                    + "). Indivíduos de NIVEL_0 não possuem acesso às comandas.");
        }
    }

    
    //Exige que a comanda exista, em caso de não existência o processo é interrompido
    private Comanda exigirComandaExistente(int idComanda) throws SQLException {
        Comanda comanda = buscarPorIdInterno(idComanda);
        if (comanda == null) {
            throw new IllegalArgumentException("Comanda #" + idComanda + " não encontrada.");
        }
        return comanda;
    }
    
    
    //Confere se a comanda em questão está aberta
    private void validarComandaAberta(Comanda comanda) {
        if (comanda == null) {
            throw new IllegalArgumentException("A comanda não pode ser nula.");
        }
        if (comanda.obterStatus() != StatusComanda.ABERTA) {
            throw new IllegalStateException("A comanda #" + comanda.obterId() + " não está aberta para alterações.");
        }
    }
    
    
    //Verifica se o funcionário pode interferir na comanda 
    //Abre uma comanda
    public Comanda abrirComanda(Funcionario operador, Mesa mesa) throws SQLException {
        validarPermissaoInterferencia(operador);
        if (mesa == null) {
            throw new IllegalArgumentException("A mesa não pode ser nula.");
        }
        if (mesa.obterStatus() == StatusMesa.OCUPADA) {
            throw new IllegalStateException("A mesa " + mesa.obterNumero() + " já está ocupada.");
        }
        if (mesa.obterStatus() == StatusMesa.INDISPONIVEL) {
            throw new IllegalStateException("A mesa " + mesa.obterNumero() + " está indisponível.");
        }
        if (buscarComandaAbertaPorMesa(mesa.obterNumero()) != null) {
            throw new IllegalStateException("Já existe uma comanda aberta para a mesa " + mesa.obterNumero() + ".");
        }

        Comanda novaComanda = new Comanda(mesa, operador);
        mesa.definirStatus(StatusMesa.OCUPADA);

        comandaDao.salvar(novaComanda);
        if (mesaDao != null) {
            mesaDao.atualizar(mesa);
        }

        return novaComanda;
    }

    
    //Verifica se o funcionário pode interferir na comanda e define o mecanismo de lançamento de novo item na comanda
    public ItemSolicitado adicionarItem(Funcionario funcionario, Comanda comanda, Produto produto, int quantidade)
            throws SQLException {
        validarPermissaoInterferencia(funcionario);
        validarComandaAberta(comanda);
        if (produto == null) {
            throw new IllegalArgumentException("O produto não pode ser nulo.");
        }
        if (quantidade <= 0) {
            throw new IllegalArgumentException("A quantidade deve ser maior que zero.");
        }

        ItemSolicitado item = new ItemSolicitado(produto, quantidade);
        comanda.adicionarItem(item);

        if (itemSolicitadoDao != null) {
            itemSolicitadoDao.salvar(item);
        }
        comandaDao.atualizar(comanda);

        return item;
    }

    //Utiliza o método anterior para registrar um item na comanda
    public ItemSolicitado adicionarItem(Funcionario funcionario, int idComanda, Produto produto, int quantidade)
            throws SQLException {
        validarPermissaoInterferencia(funcionario);
        Comanda comanda = exigirComandaExistente(idComanda);
        return adicionarItem(funcionario, comanda, produto, quantidade);
    }

    
    //Verifica se o funcionário pode fechar a comanda 
    //Fecha a comanda
    public void fecharComanda(Funcionario funcionario, Comanda comanda) throws SQLException {
        validarPermissaoAlta(funcionario);
        validarComandaAberta(comanda);
    
        comanda.fechar();
        Mesa mesa = comanda.obterMesa();
        if (mesa != null && mesaDao != null) {
            mesaDao.atualizar(mesa);
        }
    
        comandaDao.atualizar(comanda);
    }
    
    
    //Fecha commanda pelo iD, utilizando o método anterior 
    public void fecharComanda(Funcionario funcionario, int idComanda) throws SQLException {
        validarPermissaoAlta(funcionario);
        Comanda comanda = exigirComandaExistente(idComanda);
        fecharComanda(funcionario, comanda);
    }

    
    //Verifica se o funcionário pode excluir a comanda 
    //Exclui a comanda
    public void excluirComanda(Funcionario funcionario, int idComanda) throws SQLException {
        validarPermissaoAlta(funcionario);
        Comanda comanda = exigirComandaExistente(idComanda);
        if (comanda.obterStatus() == StatusComanda.ABERTA) {
            throw new IllegalStateException("Não é permitido excluir uma comanda que ainda está aberta.");
        }
        comandaDao.excluir(idComanda);
    }

    
    //Verifica se o funcionário pode visualizar a comanda  
    //Calcula o valor total da conta pelos itens cadastrados na comanda
    public double calcularTotal(Funcionario funcionario, Comanda comanda) {
        validarPermissaoVisualizacao(funcionario);
        if (comanda == null) {
            throw new IllegalArgumentException("A comanda não pode ser nula.");
        }
        return comanda.calcularTotal();
    }

    
    //Verifica se o funcionário pode visualizar a comanda  
    //Calcula o valor total da conta pelos itens cadastrados na comanda pelo iD da comanda
    public double calcularTotal(Funcionario funcionario, int idComanda) throws SQLException {
        validarPermissaoVisualizacao(funcionario);
        Comanda comanda = exigirComandaExistente(idComanda);
        return comanda.calcularTotal();
    }

    
    //Busca comanda pelo iD da comanda
    private Comanda buscarPorIdInterno(int idComanda) throws SQLException {
        for (Comanda comanda : comandaDao.listarTodos()) {
            if (comanda.obterId() == idComanda) {
                return comanda;
            }
        }
        return null;
    }
    
    //Verifica se o funcionário pode visualizar a comanda  
    //Busca a comanda pelo iD da comanda
    public Comanda buscarPorId(Funcionario funcionario, int idComanda) throws SQLException {
        validarPermissaoVisualizacao(funcionario);
        return buscarPorIdInterno(idComanda);
    }

    
    //Busca comanda aberta da mesa associada
    public Comanda buscarComandaAbertaPorMesa(int numeroMesa) throws SQLException {
        for (Comanda comanda : comandaDao.listarTodos()) {
            Mesa mesa = comanda.obterMesa();
            if (mesa != null
                    && mesa.obterNumero() == numeroMesa
                    && comanda.obterStatus() != StatusComanda.FECHADA) {
                return comanda;
            }
        }
        return null;
    }
    
    //Verifica se o funcionário pode visualizar a comanda 
    //Utiliza o método anterior para procurar comanda por mesa
    public Comanda buscarComandaAbertaPorMesa(Funcionario funcionario, int numeroMesa) throws SQLException {
        validarPermissaoVisualizacao(funcionario);
        return buscarComandaAbertaPorMesa(numeroMesa);
    }

    
    //Verifica se o funcionário pode visualizar a comanda 
    //Lista todas as comandas 
    public List<Comanda> listarTodas(Funcionario funcionario) throws SQLException {
        validarPermissaoVisualizacao(funcionario);
        return comandaDao.listarTodos();
    }

    //Verifica se o funcionário pode visualizar a comanda 
    //Lista todas as comandas abertas
    public List<Comanda> listarAbertas(Funcionario funcionario) throws SQLException {
        validarPermissaoVisualizacao(funcionario);
        List<Comanda> abertas = new ArrayList<>();
        for (Comanda comanda : comandaDao.listarTodos()) {
            if (comanda.obterStatus() == StatusComanda.ABERTA) {
                abertas.add(comanda);
            }
        }
        return abertas;
    }
}
