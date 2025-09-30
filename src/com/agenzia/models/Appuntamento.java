package com.agenzia.models;

import java.time.LocalDate;
import java.time.LocalTime;

public class Appuntamento {
    private LocalDate data;
    private LocalTime ora;
    private Agente agente;
    private Acquirente cliente;
    private Immobile immobile;

    public Appuntamento(LocalDate data, LocalTime ora, Agente agente, Acquirente cliente, Immobile immobile) {
        this.data = data;
        this.ora = ora;
        this.agente = agente;
        this.cliente = cliente;
        this.immobile = immobile;
    }

    public LocalDate getData() {
        return data;
    }

    public LocalTime getOra() {
        return ora;
    }

    public Agente getAgente() {
        return agente;
    }

    public Acquirente getCliente() {
        return cliente;
    }

    public Immobile getImmobile() {
        return immobile;
    }


}
