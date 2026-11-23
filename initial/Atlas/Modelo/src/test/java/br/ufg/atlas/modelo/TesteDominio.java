package br.ufg.atlas.modelo;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

/** Pode ser executado diretamente pelo main no BlueJ ou pelo JDK. */
public final class TesteDominio {
    private static int verificacoes;

    private static void conferir(boolean condicao, String caso) {
        if (!condicao) throw new AssertionError(caso);
        verificacoes++;
    }

    private static void exigirRecusa(Runnable operacao) {
        try {
            operacao.run();
        } catch (IllegalArgumentException | IllegalStateException esperada) {
            verificacoes++;
            return;
        }
        throw new AssertionError("A operação deveria ter sido recusada.");
    }

    public static void main(String[] argumentos) {
        Restaurante restaurante = new Restaurante();
        Mesa mesa = restaurante.cadastrarMesa(7);
        conferir(restaurante.buscarMesa(7) == mesa && mesa.getComandaAberta() == null, "Mesa livre");
        exigirRecusa(() -> restaurante.cadastrarMesa(7));
        exigirRecusa(() -> new Mesa(0));
        Produto suco = new Produto(1, "Suco", "Bebidas", 750, Setor.BAR, true);
        Produto sopa = new Produto(2, "Sopa", "Entradas", 1450, Setor.COZINHA, true);
        restaurante.getCardapio().adicionar(suco);
        restaurante.getCardapio().adicionar(sopa);
        conferir(restaurante.getCardapio().buscarProduto(1) == suco, "Referência ao produto");
        exigirRecusa(() -> restaurante.getCardapio().adicionar(suco));
        exigirRecusa(() -> suco.alterarPreco(-1));
        exigirRecusa(() -> new ItemPedido(suco, 0, ""));
        exigirRecusa(() -> new ItemPedido(suco, 1000, ""));
        ItemPedido bebida = new ItemPedido(suco, 2, "Sem gelo");
        ItemPedido prato = new ItemPedido(sopa, 1, "");
        Funcionario garcom = new Funcionario("G1", "Garçom de teste", Cargo.GARCOM, true);
        Funcionario outro = new Funcionario("G2", "Outro garçom", Cargo.GARCOM, true);
        Funcionario gerente = new Funcionario("M1", "Gerência", Cargo.GERENTE, true);
        Funcionario caixa = new Funcionario("C1", "Caixa", Cargo.CAIXA, true);
        Funcionario inativo = new Funcionario("I1", "Inativo", Cargo.GERENTE, false);
        conferir(!inativo.podeReceber() && !inativo.podeAdministrar(), "Inativo");
        for (Cargo cargo : Cargo.values()) {
            Funcionario pessoa = new Funcionario("F", "Teste", cargo, true);
            conferir(pessoa.podeAtender() == (cargo == Cargo.GARCOM), "Permissão de atendimento");
            conferir(pessoa.podeReceber() == (cargo == Cargo.CAIXA || cargo == Cargo.GERENTE), "Caixa");
        }
        exigirRecusa(() -> new Pedido(caixa, List.of(bebida)));
        exigirRecusa(() -> new Pedido(garcom, List.of()));
        Pedido bar = new Pedido(garcom, List.of(bebida));
        Pedido cozinha = new Pedido(garcom, List.of(prato));
        ArrayList<ItemPedido> selecao = new ArrayList<>(List.of(prato, bebida));
        Pedido misto = new Pedido(garcom, selecao);
        selecao.clear();
        misto.getItens().clear();
        suco.alterarPreco(900);
        suco.definirDisponibilidade(false);
        exigirRecusa(() -> new ItemPedido(suco, 1, ""));
        conferir(bar.calcularTotal() == 1500 && cozinha.calcularTotal() == 1450, "Pedidos por setor");
        conferir(misto.calcularTotal() == 2950 && bebida.getPrecoCentavos() == 750, "Preço congelado");
        Comanda comanda = mesa.abrirComanda();
        conferir(mesa.abrirComanda() == comanda, "Comanda compartilhada pela mesa");
        comanda.incluirPedido(bar);
        comanda.incluirPedido(cozinha);
        comanda.incluirPedido(misto);
        exigirRecusa(() -> comanda.incluirPedido(misto));
        conferir(misto.getComanda() == comanda, "Comanda do pedido");
        exigirRecusa(() -> restaurante.cadastrarMesa(8).abrirComanda().incluirPedido(misto));
        conferir(comanda.calcularTotal() == 5900, "Soma exata");
        exigirRecusa(() -> misto.registrarEntrega(garcom));
        exigirRecusa(() -> misto.marcarPronto(garcom));
        misto.marcarPronto(gerente);
        exigirRecusa(() -> misto.registrarEntrega(outro));
        misto.registrarEntrega(garcom);
        conferir(misto.getEstado().equals("ENTREGUE"), "Entrega");
        Pagamento integral = new Pagamento(5900, FormaPagamento.PIX);
        exigirRecusa(() -> comanda.fecharConta(caixa, integral));
        comanda.solicitarConta();
        exigirRecusa(() -> comanda.incluirPedido(bar));
        exigirRecusa(() -> comanda.fecharConta(garcom, integral));
        exigirRecusa(() -> comanda.fecharConta(caixa, new Pagamento(5899, FormaPagamento.PIX)));
        comanda.fecharConta(caixa, integral);
        exigirRecusa(() -> comanda.incluirPedido(bar));
        conferir(comanda.getPagamento() == integral && mesa.getComandaAberta() == null, "Mesa liberada");
        conferir(mesa.abrirComanda() != comanda && comanda.calcularTotal() == 5900, "Próximo atendimento");

        // Verificações do comprovante térmico para papel 58mm
        List<ItemComprovante> itensComprovante = List.of(
                new ItemComprovante("FILÉ À PARMEGIANA", 2, "Acompanhamentos:\n- Arroz branco\n- Batata frita\n\nObservação:\n- Sem cebola"),
                new ItemComprovante("RISOTO DE CAMARÃO", 1, "Pouco sal\nServir bem quente"),
                new ItemComprovante("PASTEL DE QUEIJO", 2, "Sem queijo")
        );
        OffsetDateTime criacao = LocalDateTime.of(2026, 11, 22, 12, 35, 42)
                .atZone(ZoneId.systemDefault()).toOffsetDateTime();
        String comprovante = ComprovanteTermico.formatar58mm(18, 482, "Gabriel Gonçalves", criacao, "COZINHA", itensComprovante);
        for (String linha : comprovante.split("\\R", -1)) {
            conferir(linha.length() <= ComprovanteTermico.LARGURA_58MM, "Largura de linha <= 32 colunas para 58mm");
        }
        if (!System.getenv().containsKey("ATLAS_UNIDADE")) {
            conferir(comprovante.startsWith("PIQUIRAS FLAMBOYANT\n"), "Cabeçalho com a unidade padrão");
        }
        conferir(comprovante.contains("ATLAS INC ®"), "Marca no rodapé do comprovante");
        conferir(comprovante.contains("DATA: 22/11/2026") && comprovante.contains("HORA: 12:35:42"), "Data e hora formatadas");
        conferir(comprovante.contains("COMANDA: 018") && comprovante.contains("PEDIDO: 00482"), "Numeração com zeros à esquerda");
        conferir(comprovante.contains("GARÇOM: Gabriel Gonçalves"), "Identificação do garçom");
        conferir(comprovante.contains("PEDIDOS DA COZINHA"), "Banner do setor de cozinha");
        conferir(comprovante.contains("02x  FILÉ À PARMEGIANA") && comprovante.contains("Acompanhamentos:")
                && comprovante.contains("- Arroz branco") && comprovante.contains("- Batata frita"), "Acompanhamentos com marcadores");
        conferir(comprovante.contains("Observação:") && comprovante.contains("- Sem cebola")
                && comprovante.contains("01x  RISOTO DE CAMARÃO") && comprovante.contains("- Pouco sal")
                && comprovante.contains("02x  PASTEL DE QUEIJO") && comprovante.contains("- Sem queijo"), "Observações dos itens");
        conferir(comprovante.contains("FIM DOS PEDIDOS"), "Encerramento do comprovante");

        String comprovanteBar = ComprovanteTermico.formatar58mm(1, 2, "Atendente", criacao, "BAR",
                List.of(new ItemComprovante("Suco", 1, "")));
        conferir(comprovanteBar.contains("PEDIDOS DO BAR") && comprovanteBar.contains("01x  Suco"), "Comprovante do bar");

        String comprovanteLongo = ComprovanteTermico.formatar58mm(1, 2, "Atendente ".repeat(5), criacao, "BAR",
                List.of(new ItemComprovante("X".repeat(60), 1, "Sem acompanhamentos\nObs.: servir gelado")));
        conferir(comprovanteLongo.lines().allMatch(linha -> linha.length() <= ComprovanteTermico.LARGURA_58MM)
                && comprovanteLongo.chars().filter(c -> c == 'X').count() == 60, "Quebra de nomes longos sem perder texto");
        conferir(comprovanteLongo.contains("Sem acompanhamentos") && comprovanteLongo.contains("servir gelado"),
                "Observação livre preservada antes do rótulo");

        System.out.println(verificacoes + " verificações de domínio aprovadas.");
    }
}
