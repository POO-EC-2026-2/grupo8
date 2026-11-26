package com.ufg.atlas.modelos;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;


public class Garcom extends Funcionario {

    public Garcom(){
        super();
    }
    
    public Garcom(String nome) {
        super(nome);
        this.nivel = NivelAcesso.NIVEL_2;
    }
}
