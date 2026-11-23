# Verificação do ATLAS

## Testes repetidos na revisão editorial de 20/11/2026

Os builds e testes abaixo foram executados em cópias temporárias. O Maven 3.9.11 usou JDK 21 e alvo Java 17. O Gradle Wrapper 8.9 usou JDK 21 e SDK Android 35.

| Verificação | Resultado |
|---|---|
| Build Maven e testes Java | `mvn install` concluiu nos módulos Modelo, Backend e Desktop. `TesteDominio` passou em 50 verificações e `TesteServico` em 29. O erro JDBC `08001` foi provocado no teste da resposta HTTP 503. |
| Integração HTTP/JDBC | `TesteFluxoHttp` passou com PostgreSQL 19 temporário em loopback. O teste removeu o esquema e o servidor local foi encerrado. |
| Recursos do Desktop | Os nove FXML carregaram do JAR com JavaFX 21.0.2 e aplicação de CSS. O teste conferiu o rótulo de login e os textos acessíveis do login e do painel. |
| Build e lint Android | `:app:assembleDebug` e `:app:lintDebug` concluíram. O lint emitiu 33 avisos e zero erros. A inspeção do APK confirmou o rótulo `ATLAS`. |

A instalação e os fluxos em aparelho Android, a abertura no Scene Builder, a publicação e a reconexão com Mosquitto e a homologação em LAN com impressoras físicas continuam pendentes.

## Registro anterior de 20/11/2026

O relatório original registra testes com o JDK 21 e o JavaFX 23.0.2 do BlueJ, bibliotecas locais e compilação com alvo Java 17. Os resultados abaixo se referem às condições desse ambiente.

| Área | Resultado registrado | Cobertura |
|---|---|---|
| Modelo | Aprovado | `TesteDominio` passou em 50 verificações em memória. Incluiu pedidos de bar, cozinha e mistos, total histórico, permissões, entrega e liberação da mesa. |
| Backend local | Aprovado | `TesteServico` passou em 29 verificações de acesso, senha, sessões e HTTP. A falha JDBC `08001` exercitou a resposta 503. Em teste separado, o servidor iniciou sem broker e respondeu 401 a uma requisição sem sessão. |
| Desktop JavaFX | Aprovado com JavaFX do BlueJ | Os nove FXML carregaram com CSS, também a partir de um JAR temporário. Foram verificadas navegação, consulta com Bearer e atualização de listas com respostas HTTP controladas. |
| Impressão no Desktop | Aprovado com fila simulada | O Java Print Service foi exercitado com fila ausente e paginação em memória. Um comprovante longo gerou cinco páginas. Foram verificadas as ações por estado, a escolha explícita na conferência e a seleção durante a atualização da lista. |
| Timeout MQTT | Aprovado com destino TCP de teste | Um destino local sem resposta exercitou o encerramento e a nova tentativa no Backend e no Desktop. O Backend continuou atendendo HTTP. |
| Android | Aprovado na inspeção estática | Manifesto, 12 XML e referências de recursos das dez fontes foram conferidos. A compilação auxiliar com um JAR instrumentado do Robolectric foi interrompida pela ausência de `InstrumentedInterface` e `ShadowedObject`. |

O registro anterior também descreve ajustes presentes no código atual.

- **Modelo e Backend.** O construtor de `Pedido` percorre a lista para rejeitar itens nulos, inclusive quando recebe uma lista imutável. As transições para preparo e entrega declaram o estado exigido e o estado resultante. No Backend, `exec.mainClass` permite selecionar as classes de teste, e o cliente MQTT interrompe a conexão pendente após timeout.
- **Desktop.** Os recursos ficam em `Desktop/src/main/resources/desktop`, acessíveis por `/desktop/` no classpath. O Paho usa cinco segundos como limite para operações síncronas. A conferência de impressão exige escolher um resultado antes de confirmar, e `ControleListagem` vincula as regras de habilitação aos botões.
- **Mobile.** Ao retornar ao primeiro plano, a tela de atendimento consulta novamente os dados da área aberta. O resumo conserva o rascunho e a chave de um envio pendente. O cliente MQTT interrompe a tentativa de conexão após o timeout.

Naquele ambiente, ficaram pendentes o fluxo HTTP/JDBC com PostgreSQL, os builds Maven e Gradle, a instalação em celular, a operação com Mosquitto e a impressão física. O [registro da modelagem UML](../../ATLAS-Documentacao/UML/README.md#validação-da-modelagem) descreve uma verificação posterior com PostgreSQL temporário e compilação auxiliar do Android.

## Repetir os testes Java

Na raiz do projeto, execute `mvn install`. Os testes abaixo têm método `main` e são executados explicitamente.

```powershell
mvn -pl Modelo org.codehaus.mojo:exec-maven-plugin:3.3.0:java '-Dexec.mainClass=br.ufg.atlas.modelo.TesteDominio' '-Dexec.classpathScope=test'
mvn -pl Backend exec:java '-Dexec.mainClass=br.ufg.atlas.servidor.TesteServico' '-Dexec.classpathScope=test'
```

Para repetir a integração HTTP/JDBC, use um PostgreSQL de teste. O usuário precisa ter permissão para criar esquemas no banco indicado. Ajuste os valores do exemplo e defina `ATLAS_TESTE_BANCO_SENHA` se houver senha.

```powershell
$env:ATLAS_TESTE_BANCO_URL = 'jdbc:postgresql://localhost:5432/atlas_teste'
$env:ATLAS_TESTE_BANCO_USUARIO = 'atlas_teste'
mvn -pl Backend exec:java '-Dexec.mainClass=br.ufg.atlas.servidor.TesteFluxoHttp' '-Dexec.classpathScope=test'
```

O teste aplica `BancoDeDados/esquema.sql` e a migração `001_importacao_e_operacoes_em_massa.sql` em um esquema exclusivo e o remove ao terminar. O cenário inclui comandas compartilhadas, reenvio concorrente com a mesma chave, repetição após o fechamento, tarefas de impressão por setor, entrega e pagamento. O teste também exercita os CSVs, o rollback, as ações em massa, a revogação de sessões, os avisos após a confirmação e as consultas históricas após exclusões.
