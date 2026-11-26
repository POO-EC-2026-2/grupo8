package com.ufg.atlas.modelos;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;


public abstract class Funcionario
{
    @DatabaseField(canBeNull = false, dataType = DataType.ENUM_STRING)
    protected NivelAcesso nivel;
    
    @DatabaseField(canBeNull = false, dataType = DataType.INTEGER)
    protected int id;
    
    @DatabaseField(canBeNull = false, dataType = DataType.STRING)
    protected String nome;

    public Funcionario(){}

    public Funcionario(int id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public int obterId() {
        return id;
    }

    public void definirId(int id) {
        this.id = id;
    }

    public String obterNome() {
        return nome;
    }

    public void definirNome(String nome) {
        this.nome = nome;
    }

    public NivelAcesso obterNivel() {
        return nivel;
    }
}