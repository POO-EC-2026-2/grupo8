package com.ufg.atlas.modelos;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "itens_solicitados_comanda")
public class ItemSolicitado {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(foreign = true, foreignAutoRefresh = true, canBeNull = false)
    private Produto produto;
    
    @DatabaseField(foreign = true, foreignAutoRefresh = false, canBeNull = false)
    private Comanda comanda;
    
    @DatabaseField(canBeNull = false, dataType = DataType.INTEGER)
    private int quantidade;

    @DatabaseField
    private String observacao;

    public ItemSolicitado(Produto produto, int quantidade) {
        this.produto = produto;
        this.quantidade = quantidade;
    }

    public ItemSolicitado(Produto produto, Comanda comanda, int quantidade, String observacao){
        this.produto = produto;
        this.comanda = comanda;
        this.quantidade = quantidade;
        this.observacao = observacao;
    }

    public ItemSolicitado(){};

    public double calcularTotal() {
        return produto.getPreco() * quantidade;
    }

    public int getId(){
        return id;
    }

    public Produto getProduto(){
        return produto;
    }
    public Comanda getComanda(){
        return comanda;
    }
    public int getQuantidade(){
        return quantidade;
    }
    public String getObservacao(){
        return observacao;
    }
}

//Foi adicionada a informação de qual comanda pertencem os itens lançados em determinada mesa
