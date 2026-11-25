package com.poo.modelos;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "garçons")
public class Garcom {

    @DatabaseField(canBeNull = false, dataType = DataType.INTEGER)
    private int id;
    
    @DatabaseField(canBeNull = false, dataType = DataType.STRING)
    private String nome;

    public Garcom(int id, String nome) {
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
}
