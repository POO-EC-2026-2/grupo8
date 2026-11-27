package com.ufg.atlas.modelos;

import java.util.ArrayList;
import java.utill.Collections;
import java.utill.list;

public class PedidoCozinha {
    private final Comanda comanda;
    private final List<ItemSolicitado> itens;
    private StatusPedidoCozinha status;

    
    public PedidoCozinha(Comanda comanda, List<ItemSolicitado> itens) {
        this.comanda = comanda;
        this.itens = new ArrayList<>(itens);
    }

    public int getId(){
        return comanda.obterId();
    }
    
    public int getNumeroMesa(){
        return comanda.obtermesa().obterNumero();
    }

    public Comanda getComanda(){
        return comanda;
    }

    public List<ItemSolicitado> getItens() {
        return Collections.unmodifiableList(itens);
    }

    public StatusPedidoCozinha getStatus(){
        return comanda.obterStatusCozinha();
    }
    
    public long getEnvidadaCozinhaEm(){
        return comanda.obterEnviadaCozinhaEm();
    }
}
