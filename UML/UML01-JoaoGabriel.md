![Diagrama UML](Imagens/UMLJoao01.png)

@startuml
title ATLAS — transporte do pedido e impressão
autonumber

actor "Garçom" as Garcom
box "Telefone Android" #EAF3FF
  participant "Aplicativo ATLAS" as Android
end box
box "Computador que executa o Backend" #EAF7EA
  participant "Backend do ATLAS" as API
end box
box "Banco de dados" #F5F5F5
  database "PostgreSQL" as DB
end box
box "Computador da produção" #FFF4E5
  participant "Desktop ATLAS" as Desktop
  participant "Fila de impressão\ndo sistema operacional" as Fila
end box
participant "Impressora do setor conectada via USB" as Impressora
actor "Cozinha" as Cozinha
participant "Broker MQTT para a responsividade" as MQTT

note over Android,API
  O pedido trafega via Wi-Fi ate a rede local posteriormente ao servidor
  usando HTTP, formatado como uma mensagem JSON.
end note

Garcom -> Android: Monta e confirma o pedido
Android -> Android: Guarda rascunho e identificador único do envio
Android -> API: HTTP: POST /pedidos\nJSON: comanda, itens, quantidades e observações
API -> API: Confere acesso e dados recebidos
API -> DB: Grava pedido e tarefas de impressão
DB --> API: Confirma gravação
API --> Android: HTTP: resposta com número do pedido
Android --> Garcom: Mostra confirmação

note over Android,API
  Se a resposta não chegar, o telefone pode reenviar o mesmo conteúdo
  com o mesmo identificador único. O Backend reconhece a repetição
  e devolve o pedido já registrado.
end note

API ->> MQTT: Publica aviso de atualização
MQTT ->> Desktop: Entrega aviso pela rede
MQTT ->> Android: Entrega aviso pela rede
API -> DB: Marca tarefa em tentativa de envio
DB --> API: Número da tentativa
API --> Desktop: Confirma reserva
Desktop -> Fila: Entrega texto do comprovante ao sistema de impressão
Fila -> Impressora: Encaminha trabalho via USB
Impressora --> Cozinha: Imprime o comprovante
Fila --> Desktop: Informa aceitação ou erro no envio
@enduml
