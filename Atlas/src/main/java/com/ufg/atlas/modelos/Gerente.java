package com.ufg.atlas.modelos;

import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "gerentes")
public class Gerente extends Funcionario {

    public Gerente(){}
    
    public Gerente(int id, String nome, NivelAcesso nivel) {
        super(id, nome);
        this.nivel = NivelAcesso.NIVEL_3;
    }
}