package com.poo.modelos;

import java.util.ArrayList;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "comandas")
public class Comanda {

    private static int proximoId = 1;

    @DatabaseField(canBeNull = false, dataType = DataType.INTEGER)
    private int id;
    
    @DatabaseField(foreign = true, foreignAutoRefresh = true, canBeNull = false)
    private Mesa mesa;
    
    @DatabaseField(foreign = true, foreignAutoRefresh = true, canBeNull = false)
    private Garcom garcom;
    
    @DatabaseField(canBeNull = false, dataType = DataType.ENUM_STRING)
    private StatusComanda status;
    
    private ArrayList<ItemSolicitado> itens;

    public Comanda(Mesa mesa, Garcom garcom) {
        this.id = proximoId++;
        this.mesa = mesa;
        this.garcom = garcom;
        this.itens = new ArrayList<>();
        this.status = StatusComanda.ABERTA;
    }

    public void adicionarItem(ItemSolicitado item) {
        itens.add(item);
    }

    public double calcularTotal() {
        double total = 0;

        for (ItemSolicitado item : itens) {
            total += item.calcularTotal();
        }
        return total;
    }

    public StatusComanda obterStatus() {
        return status;
    }

    public void fechar() {
        this.status = StatusComanda.FINALIZADA;
    }

    public Mesa obterMesa() {
        return mesa;
    }

    public int obterId() {
        return id;
    }
}
