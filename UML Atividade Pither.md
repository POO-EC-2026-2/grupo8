# 01 — Diagrama de Classes do Domínio

**Responsável:** Pither Mikael Gonçalves Duarte (`@pitherduarte`)
**Atividade:** [LAB] AT-11-17 — Básico de UML com PlantUML

Este diagrama apresenta as classes centrais do atendimento no ATLAS: a mesa, a comanda aberta nela, os pedidos lançados pelo garçom, os itens de cada pedido e os produtos do cardápio. Mostra também a relação entre o pedido e o funcionário responsável por ele.

```plantuml
@startuml 01_uml_pither_classes
skinparam classAttributeIconSize 0

class Mesa {
  -numero : int
  +abrirComanda() : Comanda
}

class Comanda {
  -estado : String
  +incluirPedido(pedido : Pedido)
  +solicitarConta()
  +fecharConta(caixa : Funcionario, pagamento : Pagamento)
  +calcularTotal() : long
}

class Pedido {
  -estado : String
  +calcularTotal() : long
  +marcarPronto(gerente : Funcionario)
  +registrarEntrega(garcom : Funcionario)
}

class ItemPedido {
  -quantidade : int
  -observacao : String
  +calcularSubtotal() : long
}

class Produto {
  -nome : String
  -precoCentavos : long
  -disponivel : boolean
}

class Funcionario {
  -codigo : String
  -nome : String
  -cargo : Cargo
}

Mesa "1" -- "0..1" Comanda
Comanda "1" o-- "0..*" Pedido
Pedido "1" *-- "1..*" ItemPedido
ItemPedido "*" --> "1" Produto
Pedido "*" --> "1" Funcionario : garçom
@enduml
```

![Diagrama de Classes do Domínio](https://github.com/POO-EC-2026-2/grupo8/raw/Umls/Imagens/01_uml_pither_classes.png)
