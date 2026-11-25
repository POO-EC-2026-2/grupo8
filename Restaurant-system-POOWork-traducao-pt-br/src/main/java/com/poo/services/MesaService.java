package com.poo.service;

import com.poo.dao.MesaDao;
import com.poo.modelos.Funcionario;
import com.poo.modelos.Mesa;
import com.poo.modelos.NivelAcesso;
import com.poo.modelos.StatusMesa;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MesaService {

    private final MesaDao mesaDao;


    //Inicializa o serviço de mesas
    public MesaService(MesaDao mesaDao) {
        if (mesaDao == null) {
            throw new IllegalArgumentException("MesaDao não pode ser nulo.");
        }
        this.mesaDao = mesaDao;
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
    
    
    //Verifica o nível mimímo do funcionário
    private void validarNivelMinimo(Funcionario funcionario, int nivelMinimo, String acao) {
        if (funcionario == null) {
            throw new IllegalArgumentException("O funcionário não pode ser nulo.");
        }
        if (obterValorNivel(funcionario.obterNivel()) < nivelMinimo) {
            throw new SecurityException("Acesso negado para '" + funcionario.obterNome()
                    + "' (nível: " + funcionario.obterNivel()
                    + "). É necessário NIVEL_" + nivelMinimo + " ou superior para " + acao + ".");
        }
    }
    
    
    //Exige que a mesa exista, em caso de não existência o processo é interrompido
    private Mesa exigirMesaExistente(int numero) throws SQLException {
        Mesa mesa = buscarPorNumeroInterno(numero);
        if (mesa == null) {
            throw new IllegalArgumentException("Mesa " + numero + " não encontrada.");
        }
        return mesa;
    }

    
    //Verifica se o funcionário pode cadastrar uma mesa
    //Cadastra uma nova mesa no banco de dados a partir do número identificador da mesa
    public Mesa cadastrarMesa(Funcionario operador, int numero) throws SQLException {
        validarNivelMinimo(operador, 3, "cadastrar mesas");
        if (numero <= 0) {
            throw new IllegalArgumentException("O número da mesa deve ser maior que zero.");
        }
        if (buscarPorNumeroInterno(numero) != null) {
            throw new IllegalStateException("Já existe uma mesa cadastrada com o número " + numero + ".");
        }

        Mesa novaMesa = new Mesa(numero);
        mesaDao.salvar(novaMesa);
        return novaMesa;
    }

    
    //Verifica se o funcionário pode cadastrar uma mesa
    //Cadastra uma mesa previamente construída no banco de dados 
    public void cadastrarMesa(Funcionario operador, Mesa mesa) throws SQLException {
        validarNivelMinimo(operador, 3, "cadastrar mesas");
        if (mesa == null) {
            throw new IllegalArgumentException("A mesa não pode ser nula.");
        }
        if (mesa.obterNumero() <= 0) {
            throw new IllegalArgumentException("O número da mesa deve ser maior que zero.");
        }
        if (buscarPorNumeroInterno(mesa.obterNumero()) != null) {
            throw new IllegalStateException("Já existe uma mesa cadastrada com o número " + mesa.obterNumero() + ".");
        }

        mesaDao.salvar(mesa);
    }

    
    //Verifica se o funcionário pode visualizar as mesas
    //Buscar mesa por número
    public Mesa buscarPorNumero(Funcionario funcionario, int numero) throws SQLException {
        validarNivelMinimo(funcionario, 1, "visualizar mesas");
        return buscarPorNumeroInterno(numero);
    }

    
    //Verifica se o funcionário pode visualizar as mesas
    //Lista todas as mesas
    public List<Mesa> listarTodas(Funcionario funcionario) throws SQLException {
        validarNivelMinimo(funcionario, 1, "visualizar mesas");
        return mesaDao.listarTodos();
    }

    
    //Verifica se o funcionário pode visualizar as mesas
    //Listar mesas por status (OCUPADA, LIVRE ou INDISPONIVEL)
    public List<Mesa> listarPorStatus(Funcionario funcionario, StatusMesa status) throws SQLException {
        validarNivelMinimo(funcionario, 1, "visualizar mesas");
        if (status == null) {
            throw new IllegalArgumentException("O status para filtro não pode ser nulo.");
        }

        List<Mesa> resultado = new ArrayList<>();
        for (Mesa mesa : mesaDao.listarTodos()) {
            if (mesa.obterStatus() == status) {
                resultado.add(mesa);
            }
        }
        return resultado;
    }

    
    //Verifica se o funcionário pode visualizar as mesas
    //Listar mesas livre
    public List<Mesa> listarMesasLivres(Funcionario funcionario) throws SQLException {
        validarNivelMinimo(funcionario, 1, "visualizar mesas");
        return listarPorStatus(funcionario, StatusMesa.LIVRE);
    }

    
    //Verifica se o funcionário pode visualizar as mesas
    //Listar mesas ocupadas
    public List<Mesa> listarMesasOcupadas(Funcionario funcionario) throws SQLException {
        validarNivelMinimo(funcionario, 1, "visualizar mesas");
        return listarPorStatus(funcionario, StatusMesa.OCUPADA);
    }

    
    //Verifica se o funcionário pode mudar o estado da mesa
    //Muda o estado da mesa de LIVRE para OCUPADA
    //Ocorre quando um pedido é lançado
    public void ocuparMesa(Funcionario operador, int numero) throws SQLException {
        validarNivelMinimo(operador, 2, "ocupar mesas");
        Mesa mesa = exigirMesaExistente(numero);
        if (mesa.obterStatus() == StatusMesa.OCUPADA) {
            throw new IllegalStateException("A mesa " + numero + " já está ocupada.");
        }
        if (mesa.obterStatus() == StatusMesa.INDISPONIVEL) {
            throw new IllegalStateException("A mesa " + numero + " está indisponível.");
        }

        mesa.definirStatus(StatusMesa.OCUPADA);
        mesaDao.atualizar(mesa);
    }

    
    //Verifica se o funcionário pode mudar o estado da mesa
    //Muda o estado da mesa para LIVRE
    //Ocorre quando a conta é lançada no sistema como paga
    public void liberarMesa(Funcionario operador, int numero) throws SQLException {
        validarNivelMinimo(operador, 3, "liberar mesas");
        Mesa mesa = exigirMesaExistente(numero);
        mesa.definirStatus(StatusMesa.LIVRE);
        mesaDao.atualizar(mesa);
    }

    
    //Verifica se o funcionário pode mudar o estado da mesa
    //Muda o estado da mesa para INDISPONIVEL
    public void indisponibilizarMesa(Funcionario operador, int numero) throws SQLException {
        validarNivelMinimo(operador, 3, "indisponibilizar mesas");
        Mesa mesa = exigirMesaExistente(numero);
        if (mesa.obterStatus() == StatusMesa.OCUPADA) {
            throw new IllegalStateException("Não é possível indisponibilizar uma mesa ocupada.");
        }

        mesa.definirStatus(StatusMesa.INDISPONIVEL);
        mesaDao.atualizar(mesa);
    }


    //Verifica se o funcionário pode excluir a mesa
    //Exclui a mesa
    public void excluirMesa(Funcionario operador, int numero) throws SQLException {
        validarNivelMinimo(operador, 3, "excluir mesas");
        Mesa mesa = exigirMesaExistente(numero);
        if (mesa.obterStatus() == StatusMesa.OCUPADA) {
            throw new IllegalStateException("Não é permitido excluir uma mesa ocupada.");
        }
        mesaDao.excluir(numero);
    }

    
    //Busca uma mesa pelo seu iD
    private Mesa buscarPorNumeroInterno(int numero) throws SQLException {
        for (Mesa mesa : mesaDao.listarTodos()) {
            if (mesa.obterNumero() == numero) {
                return mesa;
            }
        }
        return null;
    }
}
