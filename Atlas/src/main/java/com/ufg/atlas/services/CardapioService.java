package com.ufg.atlas.services;

import com.ufg.atlas.dao.AlimentoDao;
import com.ufg.atlas.dao.BebidaDao;
import com.ufg.atlas.modelos.Alimento;
import com.ufg.atlas.modelos.Bebida;
import com.ufg.atlas.modelos.Cardapio;
import com.ufg.atlas.modelos.Funcionario;
import com.ufg.atlas.modelos.NivelAcesso;
import com.ufg.atlas.modelos.Produto;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CardapioService {

    private final AlimentoDao alimentoDao;
    private final BebidaDao bebidaDao;

    //Inicializa o serviço de cardápio conectando ao banco de dados de bebidas e comidas
    public CardapioService(AlimentoDao alimentoDao, BebidaDao bebidaDao) {
        if (alimentoDao == null || bebidaDao == null) {
            throw new IllegalArgumentException("AlimentoDao e BebidaDao não podem ser nulos.");
        }
        this.alimentoDao = alimentoDao;
        this.bebidaDao = bebidaDao;
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
    
    
    //Costrói instâncias de cardápio com os registros do banco
    private Cardapio carregarCardapioComida() throws SQLException {
        Cardapio cardapioComida = new Cardapio();
        for (Alimento alimento : alimentoDao.listarTodos()) {
            cardapioComida.adicionarProduto(alimento);
        }
        return cardapioComida;
    }
    
    private Cardapio carregarCardapioBebida() throws SQLException {
        Cardapio cardapioBebida = new Cardapio();
        for (Bebida bebida : bebidaDao.listarTodos()) {
            cardapioBebida.adicionarProduto(bebida);
        }
        return cardapioBebida;
    }
    
    private Cardapio carregarCardapioInterno() throws SQLException {
        Cardapio cardapio = new Cardapio();
        for (Alimento alimento : alimentoDao.listarTodos()) {
            cardapio.adicionarProduto(alimento);
        }
        for (Bebida bebida : bebidaDao.listarTodos()) {
            cardapio.adicionarProduto(bebida);
        }
        return cardapio;
    }

    
    //Verifica se o funcionário pode visualizar o cardápio 
    //Monta e retorna Cardápio preenchido com todos os itens do cardápio
    //Retorna um objeto com os prdutos carregados
    public Cardapio carregarCardapioComida(Funcionario funcionario) throws SQLException {
        validarNivelMinimo(funcionario, 1, "consultar o cardápio");
        return carregarCardapioComida();
    }
    
    public Cardapio carregarCardapioBebida(Funcionario funcionario) throws SQLException {
        validarNivelMinimo(funcionario, 1, "consultar o cardápio");
        return carregarCardapioBebida();
    }
    
    public Cardapio carregarCardapioCompleto(Funcionario funcionario) throws SQLException {
        validarNivelMinimo(funcionario, 1, "consultar o cardápio");
        return carregarCardapioInterno();
    }
    
    
    //Verifica se o funcionário pode visualizar o cardápio
    //Lista todos os produtos disponíveis no cardápio 
    //Retorna uma lista dos produtos
    public List<Produto> listarTodasComidas(Funcionario funcionario) throws SQLException {
        validarNivelMinimo(funcionario, 1, "listar produtos do cardápio");
        return carregarCardapioComida().obterProdutos();
    }
    
    public List<Produto> listarTodasBebidas(Funcionario funcionario) throws SQLException {
        validarNivelMinimo(funcionario, 1, "listar produtos do cardápio");
        return carregarCardapioBebida().obterProdutos();
    }
    
    public List<Produto> listarTodosProdutos(Funcionario funcionario) throws SQLException {
        validarNivelMinimo(funcionario, 1, "listar produtos do cardápio");
        return carregarCardapioInterno().obterProdutos();
    }
    
    
    //Verifica se o funcionário pode interferir no cardápio
    //Buscar produto por iD no cardápio
    //Usado para o momento de lançar pedido
    public Produto buscarProdutoPorId(Funcionario funcionario, int id) throws SQLException {
        validarNivelMinimo(funcionario, 2, "buscar produtos no cardápio");
        return carregarCardapioInterno().buscarProduto(id);
    }

    
    //Verifica se o funcionário pode interferir no cardápio
    //Buscar produto por nome no cardápio
    //Usado para o momento de lançar pedido
    public List<Produto> buscarProdutosPorNome(Funcionario funcionario, String termo) throws SQLException {
        validarNivelMinimo(funcionario, 2, "buscar produtos no cardápio");
        if (termo == null || termo.trim().isEmpty()) {
            return carregarCardapioInterno().obterProdutos();
        }

        String busca = termo.trim();
        List<Produto> encontrados = new ArrayList<>();
        encontrados.addAll(alimentoDao.buscarPorNome(busca));
        encontrados.addAll(bebidaDao.buscarPorNome(busca));
        return encontrados;
    }

    
    //Verifica se o funcionário pode adicionar produto do cardápio
    //Cadastrar novo produto no cardápio
    public void cadastrarProduto(Funcionario operador, Produto produto) throws SQLException {
        validarNivelMinimo(operador, 3, "cadastrar produtos no cardápio");
        if (produto == null) {
            throw new IllegalArgumentException("O produto não pode ser nulo.");
        }
        if (produto.getNome() == null || produto.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do produto é obrigatório.");
        }
        if (produto.getPreco() <= 0) {
            throw new IllegalArgumentException("O preço do produto deve ser maior que zero.");
        }

        if (produto instanceof Alimento) {
            alimentoDao.create((Alimento) produto);
        } else if (produto instanceof Bebida) {
            bebidaDao.create((Bebida) produto);
        } else {
            throw new IllegalArgumentException("Tipo de produto não suportado: " + produto.getClass().getSimpleName());
        }
    }

    
    //Verifica se o funcionário pode remover produto do cardápio
    //Remover produto do cardápio
    public void removerProduto(Funcionario operador, int id) throws SQLException {
        validarNivelMinimo(operador, 3, "remover produtos do cardápio");
        Produto produto = carregarCardapioInterno().buscarProduto(id);
        if (produto == null) {
            throw new IllegalArgumentException("Produto com ID " + id + " não encontrado no cardápio.");
        }

        if (produto instanceof Alimento) {
            alimentoDao.excluir(id);
        } else if (produto instanceof Bebida) {
            bebidaDao.excluir(id);
        }
    }
    
    
}
