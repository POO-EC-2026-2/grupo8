package br.ufg.atlas.servidor;

import br.ufg.atlas.modelo.Cargo;
import br.ufg.atlas.modelo.Funcionario;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Clock;
import java.time.Duration;
import java.util.UUID;

/** Autorização, sessões e respostas HTTP que podem ser exercitadas sem serviços externos. */
public final class TesteServico {
    private static int verificacoes;

    private static void conferir(boolean resultado) {
        if (!resultado) throw new AssertionError("Falha na verificação " + (verificacoes + 1));
        verificacoes++;
    }

    private static void esperarErro(int status, Runnable acao) {
        try {
            acao.run();
        } catch (ErroHttp erro) {
            conferir(erro.getStatus() == status);
            return;
        }
        throw new AssertionError("Esperava HTTP " + status);
    }

    private static void testarAcesso() {
        Funcionario garcom = new Funcionario("a", "Atendimento", Cargo.GARCOM, true);
        Funcionario caixa = new Funcionario("c", "Caixa", Cargo.CAIXA, true);
        Funcionario gerente = new Funcionario("g", "Gerência", Cargo.GERENTE, true);
        esperarErro(403, () -> Permissoes.verificar(garcom, "GET", new String[]{"funcionarios"}));
        esperarErro(403, () -> Permissoes.verificar(garcom, "POST", new String[]{"comandas","1","pagar"}));
        esperarErro(403, () -> Permissoes.verificar(caixa, "POST", new String[]{"pedidos"}));
        esperarErro(403, () -> Permissoes.verificar(garcom, "POST", new String[]{"impressoes","1","reservar"}));
        esperarErro(404, () -> Permissoes.verificar(gerente, "POST", new String[]{"mesas","1","apagar"}));
        Permissoes.verificar(caixa, "POST", new String[]{"comandas","1","pagar"});
        Permissoes.verificar(garcom, "POST", new String[]{"pedidos"});
        String segredo = UUID.randomUUID().toString();
        String registro = Senhas.gerar(segredo);
        conferir(!registro.contains(segredo) && Senhas.conferir(segredo, registro));
        conferir(!Senhas.conferir("outra senha", registro));
        conferir(!registro.equals(Senhas.gerar(segredo)));
        Sessoes sessoes = new Sessoes(Clock.systemUTC());
        String primeiro = sessoes.abrir("g");
        String segundo = sessoes.abrir("g");
        conferir(!primeiro.equals(segundo) && sessoes.identificar(primeiro).equals("g"));
        sessoes.encerrar(primeiro);
        esperarErro(401, () -> sessoes.identificar(primeiro));
        sessoes.revogar("g");
        esperarErro(401, () -> sessoes.identificar(segundo));
    }

    private static void resposta(URI servidor, String metodo, String caminho, String corpo,
                                String token, int esperado) throws Exception {
        HttpRequest.Builder pedido = HttpRequest.newBuilder(servidor.resolve(caminho))
                .timeout(Duration.ofSeconds(10)).header("Content-Type", "application/json");
        if (token != null) pedido.header("Authorization", "Bearer " + token);
        pedido.method(metodo, HttpRequest.BodyPublishers.ofString(corpo));
        HttpResponse<String> retorno = HttpClient.newHttpClient().send(pedido.build(), HttpResponse.BodyHandlers.ofString());
        conferir(retorno.statusCode() == esperado);
        conferir(JsonParser.parseString(retorno.body()).getAsJsonObject().has("erro") == (esperado >= 400));
    }

    public static void main(String[] argumentos) throws Exception {
        testarAcesso();
        HttpServer http = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 4);
        Sessoes sessoes = new Sessoes(Clock.systemUTC());
        // Porta reservada como indisponível para este cenário de erro. Nenhuma base de operação é usada.
        ConexaoBanco indisponivel = new ConexaoBanco("jdbc:postgresql://127.0.0.1:1/atlas_teste", "teste", null);
        http.createContext("/", new ApiHttp(indisponivel, sessoes, recurso -> {
            throw new AssertionError("Nenhuma alteração deveria ter sido confirmada.");
        }));
        http.start();
        try {
            URI servidor = URI.create("http://127.0.0.1:" + http.getAddress().getPort());
            resposta(servidor, "GET", "/mesas", "", null, 401);
            resposta(servidor, "GET", "/pedidos", "", "invalido", 401);
            resposta(servidor, "POST", "/login", "[null]", null, 400);
            resposta(servidor, "POST", "/login", "{ sem aspas }", null, 400);
            resposta(servidor, "POST", "/login", "{}", null, 400);
            resposta(servidor, "POST", "/login", "x".repeat(131073), null, 413);
            resposta(servidor, "POST", "/login", "{\"codigo\":\"teste\",\"senha\":\"temporaria\"}", null, 503);
            String token = sessoes.abrir("teste");
            resposta(servidor, "POST", "/sair", "{}", token, 200);
            resposta(servidor, "GET", "/mesas", "", token, 401);
        } finally {
            http.stop(0);
        }
        System.out.println(verificacoes + " verificações de acesso, hash, sessão e HTTP aprovadas.");
    }
}
