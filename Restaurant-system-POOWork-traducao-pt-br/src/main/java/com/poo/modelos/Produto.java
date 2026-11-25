package com.poo.modelos;
import com.j256.ormlite.field.DatabaseField;

public abstract class Produto {

    @DatabaseField(id = true) // informa que os atributos serão armazenados no banco de dados
    private int id;

    @DatabaseField(canBeNull = false)
    private String nome;

    @DatabaseField
    private String descricao;

    @DatabaseField
    private double preco;

    protected Produto(){};

    public Produto(int id, String nome, String descricao, double preco) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double v) {
        this.preco = v;
    }


}
