# Relatório individual - Pither Duarte

Pither Mikael Gonçalves Duarte (pitherduarte)

Matrícula: **202600679**

· Etapa 1 · atualizado em 26/11/2026

## Resumo

Até 26/11/2026, fiz 8 commits no [repositório do grupo 8](https://github.com/POO-EC-2026-2/grupo8): 6 de conteúdo e 2 merges. Os de 23/11 estão na `main`, os de 25/11 na Backend-Exp e o de 26/11 só na controllers-pither (PR #17). Fiz o diagrama de classes e os controllers das telas de mesas, login e teclado virtual.

## Commits

| Data e hora | Commit | Tipo | O que fiz |
| --- | --- | --- | --- |
| 26/11 12:50 | [52158d0](https://github.com/POO-EC-2026-2/grupo8/commit/52158d00de0f7ccef5c947b00fb7169f2580d773) | Merge | Jntei a local com a remota antes do push|
| 26/11 12:49 | [294e524](https://github.com/POO-EC-2026-2/grupo8/commit/294e524d53669ad3eeec436d8f340a05f23c0731) | Código | Criei o controller das mesas e liguei à tela |
| 26/11 12:49 | [9a926a6](https://github.com/POO-EC-2026-2/grupo8/commit/9a926a6c10e785c0f4d85667c0ebdcba51647d80) | Código | Criei o controller do cardápio e liguei à tela |
| 26/11 09:49 | [df91371](https://github.com/POO-EC-2026-2/grupo8/commit/df913714eb13286e681afb5e65151f1dc5b3f92b) | Documentação | Atualizei meu relatório individual da Etapa 1 |
| 26/11 09:13 | [cac5d08](https://github.com/POO-EC-2026-2/grupo8/commit/cac5d08bbb115ab545c8a09b905eaa5afa2f1e39) | Código | Criei o controller do teclado virtual e liguei à tela |
| 25/11 23:54 | [4590c13](https://github.com/POO-EC-2026-2/grupo8/commit/4590c133616424fc0a545af5d4075adcd5d23dbc) | Documentação | Atualizei meu relatório individual no repositório |
| 25/11 23:42 | [16863b7](https://github.com/POO-EC-2026-2/grupo8/commit/16863b72e227ff2420851170f748032f0c61baad) | Merge | Atualizei minha Backend-Exp local com a remota |
| 25/11 23:41 | [23541aa](https://github.com/POO-EC-2026-2/grupo8/commit/23541aa451ccfb8517a29b94d3440ec8e2867e81) | Código | Criei os controllers das telas de garçom, gerente e login |
| 23/11 23:51 | [48ce876](https://github.com/POO-EC-2026-2/grupo8/commit/48ce8763940c3d2c875b52dbdec80601a1eceff2) | Merge | Fiz o merge do PR #5 (Umls) |
| 23/11 23:50 | [3b25606](https://github.com/POO-EC-2026-2/grupo8/commit/3b25606131dee5bd1358fd10ecd74adec91c9227) | Documentação | Ajustei o documento do diagrama |
| 23/11 23:50 | [db5a153](https://github.com/POO-EC-2026-2/grupo8/commit/db5a1535690663d631ab3c828991fd4171acb868) | Modelagem | Incluí a imagem do diagrama de classes |
| 23/11 23:47 | [f34b241](https://github.com/POO-EC-2026-2/grupo8/commit/f34b2417ae7dfd59dc26933e45dac9d1f79ab6fb) | Modelagem | Criei o documento do diagrama em Markdown com PlantUML |

## Contribuições

### Diagrama de classes do domínio

Modelei o atendimento em um diagrama de classes da atividade semanal.

### Controllers das telas de mesas e login

Criei `GarcomController`, `GerenteController` e `LoginGarcomController` e os liguei a `Interface_Principal.fxml`, `Interface_Principal_Gerente.fxml` e `Interface_Login.fxml`.

Os dois primeiros usam o `MesaService` para listar, contar, filtrar e buscar mesas; o do gerente também cadastra mesas. O de login só limpa os campos.

### Teclado virtual

Criei `TecladoVirtualController` e o liguei a `Teclado_Virtual.fxml`, para as teclas escreverem no campo de texto.

Pendentes: colunas Localização e Garçom, botões Confirmar, Retirar mesa e Consultar comandas, chamada de `definirFuncionario`, abertura da tela do teclado e testes.

### Cardápio

Criei `CardapioController` e o liguei a `Interface_Cardapio.fxml` ([9a926a6](https://github.com/POO-EC-2026-2/grupo8/commit/9a926a6c10e785c0f4d85667c0ebdcba51647d80)). O controller lista as comidas e as bebidas pelo `CardapioService`, mostra cada produto como um botão com nome e preço e, a cada clique, atualiza a quantidade de itens e o valor parcial.
### Mesa

Criei `MesaController` e o liguei a `Interface_Mesas.fxml` ([294e524](https://github.com/POO-EC-2026-2/grupo8/commit/294e524d53669ad3eeec436d8f340a05f23c0731)). O controller busca a mesa pelo número com o `MesaService` e mostra o número e o estado dela. Com o `ComandaService`, mostra o garçom da comanda aberta, ou `--` quando não há comanda.


## Último commit

Meu último commit foi [52158d0](https://github.com/POO-EC-2026-2/grupo8/commit/52158d00de0f7ccef5c947b00fb7169f2580d773).
