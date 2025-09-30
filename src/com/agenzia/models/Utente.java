package com.agenzia.models;

public class Utente {
    private String username;
    private String ruolo;

    public Utente(String username, String ruolo) {
        this.username = username;
        this.ruolo = ruolo;
    }

    public String getUsername() { return username; }
    public String getRuolo() { return ruolo; }
}


