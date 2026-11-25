package com.poo.modelos;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "gerentes")
public class Gerente extends Funcionario {
    
    Gerente(int id, String nome, NivelAcesso nivel) {
        super(id, nome);
        this.nivel = NivelAcesso.NIVEL_3;
    }
}