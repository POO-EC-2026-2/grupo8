Diagrama de Classes

**Responsável:** Lucas Póvoa

## Descrição

Este diagrama representa a estrutura de classes do sistema de gerenciamento de restaurante, apresentando as classes de funcionários, suas especializações, interfaces e relacionamentos principais.

## Código PlantUML

```plantuml
@startuml UML - Diagrama de Classse

left to right direction

' Configuração de espaçamento
skinparam nodesep 65
skinparam ranksep 75
skinparam linetype ortho

abstract class Funcionario{
    -idFuncionario: int
    -senhaFuncionario: int
    -nomeFuncionario: String
    +getBonificacao()
}

class Garcom{

}

class Maitre{

}

class Gerente{
    +cancelarPedido()
    +transferirProduto()
    +devolverProduto()
}

class Sommelier{

}

class Padaria{

}

class Caixa{

}

class Barman{

}

class Cumim{

}

class Cozinheiro{

}

interface LancarPedido

interface FecharConta{
    +fecharConta()
    +realizarPagamento()
}

class Mesa{
    -numeroMesa: int
    -estadoMesa: String
    -valorTotal: double
    -qtdItensLancados: int
}

class Cardapio{

}

class Produto{
    -codigoBarra: int
    -precoProduto: double
    -nomeProduto: String
}

' Herança
Garcom --|> Funcionario
Maitre --|> Funcionario
Gerente --|> Funcionario
Sommelier --|> Funcionario
Padaria --|> Funcionario
Caixa --|> Funcionario
Barman --|> Funcionario
Cumim --|> Funcionario
Cozinheiro --|> Funcionario

' Implementação de interfaces
Garcom --|> LancarPedido
Maitre --|> LancarPedido
Gerente --|> LancarPedido
Sommelier --|> LancarPedido
Padaria --|> LancarPedido

Gerente --|> FecharConta
Caixa --|> FecharConta

' Relacionamentos
Cardapio --|> Mesa
Produto --|> Cardapio

@enduml
```

## Visualização do Diagrama

![Diagrama de Classes](Imagens/diagrama_classes_povoa.png)