package com.ufg.atlas.modelos;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "itens_solicitados_comanda")
public class ItemSolicitado {

    @DatabaseField(foreign = true, foreignAutoRefresh = true, canBeNull = false)
    private Produto produto;
    
    @DatabaseField(foreign = true, foreignAutoRefresh = false, canBeNull = false)
    private Comanda comanda;
    
    @DatabaseField(canBeNull = false, dataType = DataType.INTEGER)
    private int quantidade;

    public ItemSolicitado(Produto produto, int quantidade) {
        this.produto = produto;
        this.quantidade = quantidade;
    }

    public double calcularTotal() {
        return produto.getPreco() * quantidade;
    }
}

//Foi adicionada a informação de qual comanda pertencem os itens lançados em determinada mesa
