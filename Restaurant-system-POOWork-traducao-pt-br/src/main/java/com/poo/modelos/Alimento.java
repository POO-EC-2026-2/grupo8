package com.poo.modelos;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;

public class Alimento extends Produto {

    @DatabaseField(canBeNull = false, dataType = DataType.ENUM_STRING)
    private CategoriaAlimento categoria;

    @DatabaseField
    private int quantidadePessoasAtendidas;

    public Alimento(int id, String nome, String descricao, double preco,
                    CategoriaAlimento categoria, int quantidadePessoasAtendidas) {
        super(id, nome, descricao, preco);
        this.categoria = categoria;
        this.quantidadePessoasAtendidas = quantidadePessoasAtendidas;
    }
}
