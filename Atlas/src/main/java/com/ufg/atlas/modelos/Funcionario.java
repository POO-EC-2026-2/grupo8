package com.ufg.atlas.modelos;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "funcionarios")
public class Funcionario
{

    @DatabaseField(generatedId = true)
    protected int id;

    @DatabaseField(canBeNull = false, dataType = DataType.ENUM_STRING)
    protected NivelAcesso nivel;
    
    @DatabaseField(canBeNull = false, dataType = DataType.STRING)
    protected String nome;

    public Funcionario(){}

    public Funcionario( String nome) {
        this.nome = nome;
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