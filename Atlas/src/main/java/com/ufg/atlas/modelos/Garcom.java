package com.ufg.atlas.modelos;

import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "garçons")
public class Garcom extends Funcionario {

    public Garcom(){}
    
    public Garcom(int id, String nome, NivelAcesso nivel) {
        super(id, nome);
        this.nivel = NivelAcesso.NIVEL_2;
    }
}
