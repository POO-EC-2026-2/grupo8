package com.ufg.atlas.modelos;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

public class Gerente extends Funcionario {

    public Gerente(){}
    
    public Gerente(String nome) {
        super(nome);
        this.nivel = NivelAcesso.NIVEL_3;
    }
}