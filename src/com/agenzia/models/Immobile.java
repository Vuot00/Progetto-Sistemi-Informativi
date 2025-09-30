package com.agenzia.models;

public class Immobile {
    private String ID;
    private String indirizzo;
    private String stato;
    private int metratura;
    private double prezzo;

    public Immobile(String ID, String indirizzo, String stato, int metratura, double prezzo) {
        this.ID = ID;
        this.indirizzo = indirizzo;
        this.stato = stato;
        this.metratura = metratura;
        this.prezzo = prezzo;
    }

    public String getID() {
        return ID;
    }

    public String getIndirizzo() {
        return indirizzo;
    }

    public String getStato() {
        return stato;
    }

    public int getMetratura() {
        return metratura;
    }

    public double getPrezzo() {
        return prezzo;
    }
}
