package com.poo.modelos;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;

public class Bebida extends Produto {

    @DatabaseField(canBeNull = false, dataType = DataType.ENUM_STRING)
    private CategoriaBebida categoria;

    public Bebida(int id, String nome, String descricao, double preco,
                  CategoriaBebida categoria) {
        super(id, nome, descricao, preco);
        this.categoria = categoria;
    }
}
