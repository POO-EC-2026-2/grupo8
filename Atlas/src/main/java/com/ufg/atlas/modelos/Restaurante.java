package com.ufg.atlas.modelos;

import java.util.ArrayList;

public class Restaurante {

    private ArrayList<Mesa> mesas;
    private ArrayList<Funcionario> funcionarios;
    private ArrayList<Comanda> comandas;
    private Cardapio cardapio;
    private Cozinha cozinha;

    public Restaurante() {
        this.mesas = new ArrayList<>();
        this.funcionarios = new ArrayList<>();
        this.comandas = new ArrayList<>();
        this.cardapio = new Cardapio();
        this.cozinha = new Cozinha();
    }

    public Cardapio obterCardapio() {
        return cardapio;
    }
}
