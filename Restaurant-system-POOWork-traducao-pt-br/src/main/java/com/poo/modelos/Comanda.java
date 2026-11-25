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
    private Funcionario funcionario;

    @DatabaseField(canBeNull = false, dataType = DataType.ENUM_STRING)
    private StatusComanda status;

    private ArrayList<ItemSolicitado> itens;

    public Comanda(Mesa mesa, Funcionario funcionario) {
        if (mesa == null) {
            throw new IllegalArgumentException("A mesa não pode ser nula.");
        }
        if (funcionario == null) {
            throw new IllegalArgumentException("O funcionário responsável não pode ser nulo.");
        }
        NivelAcesso nivel = funcionario.obterNivel();
        if (nivel == null || (nivel != NivelAcesso.NIVEL_2 && nivel != NivelAcesso.NIVEL_3
                && nivel.ordinal() < NivelAcesso.NIVEL_2.ordinal())) {
            throw new SecurityException("Apenas funcionários de NIVEL_2 ou superior podem abrir/assumir uma comanda.");
        }

        this.id = proximoId++;
        this.mesa = mesa;
        this.funcionario = funcionario;
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
        this.status = StatusComanda.FECHADA;
    }

    public Mesa obterMesa() {
        return mesa;
    }

    public Funcionario obterFuncionario() {
        return funcionario;
    }

    public int obterId() {
        return id;
    }
}
