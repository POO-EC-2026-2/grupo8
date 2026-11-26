package com.ufg.atlas.modelos;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "bebidas")

public class Bebida extends Produto {

    @DatabaseField(canBeNull = false, dataType = DataType.ENUM_STRING)
    private CategoriaBebida categoria;

    protected Bebida(){};

    public Bebida(int id, String nome, String descricao, double preco,
                  CategoriaBebida categoria) {
        super(id, nome, descricao, preco);
        this.categoria = categoria;
    }
}
