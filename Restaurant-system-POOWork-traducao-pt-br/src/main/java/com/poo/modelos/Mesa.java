package com.poo.modelos;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "mesas")
public class Mesa { 
    
    @DatabaseField(canBeNull = false, dataType = DataType.INTEGER)
    private int numero;
    
    @DatabaseField(canBeNull = false, dataType = DataType.ENUM_STRING)
    private StatusMesa status;

    public Mesa(int numero) {
        this.numero = numero;
        this.status = StatusMesa.LIVRE;
    }

    public StatusMesa obterStatus() {
        return status;
    }

    public void definirStatus(StatusMesa status) {
        this.status = status;
    }

    public int obterNumero() {
        return numero;
    }

    public void definirNumero(int numero) {
        this.numero = numero;
    }
}
