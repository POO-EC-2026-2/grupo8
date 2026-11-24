# API do ATLAS

A URL padrão é `http://localhost:8080`. Em outro dispositivo, use o endereço do computador que executa o Backend na rede. Nas rotas que leem o corpo da requisição, envie um objeto JSON em UTF-8 de até 128 KiB. As consultas de lista retornam arrays. As demais respostas de sucesso retornam objetos. O status de sucesso é HTTP 200. IDs numéricos e valores em centavos são inteiros. Códigos de funcionário são strings. Datas são textos ISO 8601 com deslocamento de fuso.

## Sessão

Envie `POST /login` com `codigo` e `senha`. A resposta contém `codigo`, `nome`, `cargo` e `token`. Envie `Authorization: Bearer <token>` nas chamadas seguintes. A sessão fica em memória e expira oito horas após o login. `POST /sair` encerra a sessão e retorna `{}`. A edição de um funcionário revoga suas sessões existentes.

Os cargos com acesso operacional são `GERENTE`, `CAIXA` e `GARCOM`. O cadastro de funcionários também aceita `MAITRE`, `SOMMELIER`, `PADEIRO`, `BARMAN`, `CUMIM`, `COZINHEIRO` e `AUXILIAR_LIMPEZA`.

Erros retornam `{"erro":"mensagem"}`. A API usa, entre outros, HTTP 400 para dados inválidos, 401 para falhas de autenticação ou sessão, 403 para falta de permissão, 409 para conflito de estado, 413 para um corpo acima do limite e 503 para indisponibilidade do banco ou falha de concorrência que permite nova tentativa.

## Cadastro e salão

| Rota | Cargos | Corpo | Resposta |
|---|---|---|---|
| `GET /mesas` | Gerente, caixa, garçom | — | Lista com `id`, `numero`, `estado` e `comandaId` (nulo quando livre) |
| `POST /mesas` | Gerente | `numero` | `id` |
| `GET /produtos` | Gerente, garçom | — | Lista com `id`, `nome`, `categoria`, `precoCentavos`, `setor` e `disponivel`. O garçom recebe apenas os disponíveis. A lista do gerente inclui `descricao` e `imagem`. |
| `POST /produtos` | Gerente | `nome`, `categoria`, `precoCentavos`, `setor`, `disponivel` | `id` |
| `PUT /produtos/{id}` | Gerente | Campos do cadastro | `id` |
| `GET /funcionarios` | Gerente | — | Lista com `codigo`, `nome`, `cargo`, `ativo` e `expediente` |
| `POST /funcionarios` | Gerente | `codigo`, `nome`, `cargo`, `ativo`, `senha` | `codigo` |
| `PUT /funcionarios/{codigo}` | Gerente | `nome`, `cargo`, `ativo` e `senha` opcional | `codigo` |

Os estados da mesa são `LIVRE`, `OCUPADA` e `CONTA_SOLICITADA`. Os setores são `BAR` e `COZINHA`.

## Importação e ações em massa

Todas as rotas abaixo usam `POST`, exigem sessão de gerente e devolvem `{"quantidade": n}` em caso de sucesso.

| Rota | Operação |
|---|---|
| `/produtos/importar` e `/funcionarios/importar` | Recebem o CSV como `text/csv; charset=utf-8`. Com `?validar=true`, validam e contam os registros sem gravar. Sem o parâmetro, validam novamente e gravam o arquivo inteiro em uma transação. |
| `/produtos/desativar-todos` e `/funcionarios/desativar-todos` | Recebem `{}` e desativam os registros ativos. A operação sobre funcionários preserva a conta do gerente autenticado e revoga as sessões das demais contas desativadas. |
| `/produtos/excluir-todos` e `/funcionarios/excluir-todos` | Recebem `{}` e excluem fisicamente os registros, preservando o histórico. A exclusão de funcionários preserva o gerente autenticado e responde 409 com as contas que ainda têm operações em andamento, quando houver. |

A importação aceita até 512 KiB. Os modelos de CSV estão no [README](../README.md#arquivos-csv). A resposta de erro indica a linha e o campo quando a falha pertence a um registro. A aplicação confirmada publica o recurso correspondente no tópico MQTT já usado pelo Backend.

## Comandas e pedidos

| Rota | Cargos | Corpo | Resposta |
|---|---|---|---|
| `POST /comandas` | Garçom | `mesaId` | `id` da comanda aberta ou já em atendimento na mesa |
| `GET /comandas` | Gerente, caixa, garçom | — | Lista com `id`, `mesaNumero`, `estado` e `totalCentavos`. Gerente e caixa também recebem as encerradas. |
| `GET /comandas/{id}` | Gerente, caixa, garçom | — | `id`, `mesaNumero`, `estado`, `totalCentavos` e `pedidos`. O garçom pode consultar comandas em atendimento, incluindo pedidos de colegas na mesma comanda. |
| `POST /comandas/{id}/solicitar` | Garçom | `{}` | `id` |
| `POST /comandas/{id}/pagar` | Gerente, caixa | `valorCentavos` e `forma` (`DINHEIRO`, `CARTAO` ou `PIX`) | `id` da comanda |
| `POST /pedidos` | Garçom | `comandaId`, `chave` (UUID) e `itens`. Cada item contém `produtoId`, `quantidade` e `observacao` opcional. | `id` do pedido gravado |
| `GET /pedidos` | Gerente, garçom | — | Lista de pedidos. O garçom recebe apenas os próprios. |
| `POST /pedidos/{id}/pronto` | Gerente | `{}` | `id` |
| `POST /pedidos/{id}/entregar` | Garçom | `{}` | `id` do pedido próprio em estado `PRONTO` |

Cada mesa tem até uma comanda em atendimento. A comanda passa de `ABERTA` a `CONTA_SOLICITADA` e depois a `ENCERRADA`. A solicitação aceita repetição em `CONTA_SOLICITADA` e impede novos pedidos. A API aceita o pagamento integral, inclusive zero em uma conta sem consumo. O pagamento e o encerramento ocorrem na mesma transação. Após o encerramento, a mesa volta a aparecer como `LIVRE`.

Um pedido consultado contém `id`, `comandaId`, `mesaNumero`, `funcionarioCodigo`, `funcionarioNome`, `estado`, `criadoEm`, `totalCentavos` e `itens`. Cada item contém `produtoId`, `nome`, `quantidade`, `observacao`, `precoCentavos` e `setor`. A API conserva o preço, o nome e o setor do lançamento. O pedido passa por `REGISTRADO`, `PRONTO` e `ENTREGUE`.

Ao repetir um envio, use a mesma chave, a mesma comanda e os mesmos itens na mesma ordem. Para o mesmo garçom, a API retorna o ID original mesmo depois de a comanda ser encerrada. Reutilizar a chave com outra comanda ou outros itens retorna HTTP 409.

## Impressão

As rotas de impressão exigem cargo de gerente. Um pedido novo gera uma tarefa por setor presente nos seus itens.

| Rota | Corpo | Resposta |
|---|---|---|
| `GET /impressoes` | — | Lista com `id`, `pedidoId`, `mesaNumero`, `setor`, `estado`, `texto`, `detalhe` e `tentativa` |
| `POST /impressoes/{id}/reservar` | `{}` em `PENDENTE`. Em `FALHA`, envie `repetir: true` e o número da tentativa anterior em `tentativa`. | `id` e novo número de `tentativa` |
| `POST /impressoes/{id}/resultado` | `estado` (`ENVIADA`, `FALHA` ou `INCERTA`), `tentativa` e, quando aplicável, `detalhe` e `conferido: true` | `id` |

A reserva coloca a tarefa em `EM_TENTATIVA`. O gerente responsável pode registrar o resultado, inclusive `FALHA`. Qualquer gerente pode conferir a tentativa com `conferido: true` e um detalhe que descreva o que foi observado. Uma tarefa `INCERTA` pode ser conferida como `ENVIADA` ou `FALHA`. Um resultado com número de tentativa desatualizado retorna HTTP 409.

## MQTT

O Backend agenda o aviso após o commit das operações que alteram dados. A mensagem em `atlas/eventos` contém o nome do recurso alterado em UTF-8. Ao conectar ao broker, o Backend publica `atualizacao` no mesmo tópico. As mensagens usam QoS 1 e são publicadas sem retenção. Os clientes usam o aviso para consultar a API novamente. Uma desconexão pode impedir a entrega. O Desktop também consulta a API periodicamente. O Android permite atualização manual e consulta novamente ao voltar ao primeiro plano.
