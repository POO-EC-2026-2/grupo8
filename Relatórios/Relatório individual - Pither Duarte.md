# Relatório individual - Pither Duarte
 
Pither Mikael Gonçalves Duarte (pitherduarte)
 
Matrícula: **202600679**
 
· Etapa 1 · atualizado em 25/11/2026
 
## Resumo
 
Até 25/11/2026, fiz 6 commits no [repositório do grupo 8](https://github.com/POO-EC-2026-2/grupo8): 4 de conteúdo e 2 merges. Os de 23/11 estão na `main`. Os de 25/11, só na Backend-Exp. Fiz o diagrama de classes e os controllers das telas de mesas e login.
 
## Commits
 
| Data e hora | Commit | Tipo | O que fiz |
| --- | --- | --- | --- |
| 25/11 23:42 | [16863b7](https://github.com/POO-EC-2026-2/grupo8/commit/16863b72e227ff2420851170f748032f0c61baad) | Merge | Atualizei minha Backend-Exp local com a remota |
| 25/11 23:41 | [23541aa](https://github.com/POO-EC-2026-2/grupo8/commit/23541aa451ccfb8517a29b94d3440ec8e2867e81) | Código | Criei os controllers das telas de garçom, gerente e login |
| 23/11 23:51 | [48ce876](https://github.com/POO-EC-2026-2/grupo8/commit/48ce8763940c3d2c875b52dbdec80601a1eceff2) | Merge | Fiz o merge do PR #5 (Umls) |
| 23/11 23:50 | [3b25606](https://github.com/POO-EC-2026-2/grupo8/commit/3b25606131dee5bd1358fd10ecd74adec91c9227) | Documentação | Ajustei o documento do diagrama |
| 23/11 23:50 | [db5a153](https://github.com/POO-EC-2026-2/grupo8/commit/db5a1535690663d631ab3c828991fd4171acb868) | Modelagem | Incluí a imagem do diagrama de classes |
| 23/11 23:47 | [f34b241](https://github.com/POO-EC-2026-2/grupo8/commit/f34b2417ae7dfd59dc26933e45dac9d1f79ab6fb) | Modelagem | Criei o documento do diagrama em Markdown com PlantUML |
 
## Contribuições
 
### Diagrama de classes do domínio
 
Modelei o atendimento com 6 classes: `Mesa`, `Comanda`, `Pedido`, `ItemPedido`, `Produto` e `Funcionario`.
 
Enviei o PlantUML e a imagem `Imagens/01_uml_pither_classes.png`. Ele difere do código, que usa `ItemSolicitado` e não tem `Pedido`.
 
### Controllers das telas de mesas e login
 
Criei `GarcomController`, `GerenteController` e `LoginGarcomController` e os liguei a `Interface_Principal.fxml`, `Interface_Principal_Gerente.fxml` e `Interface_Login.fxml`.
 
Os dois primeiros usam o `MesaService` para listar, contar, filtrar e buscar mesas; o do gerente também cadastra mesas. O de login só limpa os campos.
 
Pendentes: colunas Localização e Garçom, botões Confirmar, Retirar mesa e Consultar comandas, chamada de `definirFuncionario` e testes.
 
## Último commit
 
Meu último commit foi [16863b7](https://github.com/POO-EC-2026-2/grupo8/commit/16863b72e227ff2420851170f748032f0c61baad) (merge); o último de conteúdo foi [23541aa](https://github.com/POO-EC-2026-2/grupo8/commit/23541aa451ccfb8517a29b94d3440ec8e2867e81).
 
