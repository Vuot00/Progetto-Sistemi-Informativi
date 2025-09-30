package com.agenzia.gui;

import javax.swing.*;
import java.awt.*;
import com.agenzia.models.Utente;

public class MainMenuGUI {
    private JFrame frame;
    private Utente utente;

    public MainMenuGUI(Utente utente) {
        this.utente = utente;
        frame = new JFrame("Menu Principale - " + utente.getUsername());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 300);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new GridLayout(3, 1, 10, 10));

        JLabel lblWelcome = new JLabel("Benvenuto, " + utente.getUsername() + " (" + utente.getRuolo() + ")", SwingConstants.CENTER);
        frame.add(lblWelcome);

        // Pulsanti dinamici in base al ruolo
        JPanel panel = new JPanel(new GridLayout(1, 3, 10, 10));
        if (utente.getRuolo().equals("Amministratore") || utente.getRuolo().equals("Agente")) {
            JButton btnImmobili = new JButton("Gestione Immobili");
            btnImmobili.addActionListener(e -> new DatabaseImmobili());
            panel.add(btnImmobili);
        }
        if (utente.getRuolo().equals("Agente") || utente.getRuolo().equals("Amministratore")) {
            JButton btnCalendario = new JButton("Calendario");
            btnCalendario.addActionListener(e -> new CalendarioCentralizzato());
            panel.add(btnCalendario);
        }
        if (utente.getRuolo().equals("Agente")) {
            JButton btnArchivio = new JButton("Archivio Comunicazioni");
            btnArchivio.addActionListener(e -> new CommunicationArchiveGUI());
            panel.add(btnArchivio);
        }
        if (utente.getRuolo().equals("Agente")) {
            JButton btnDocumenti = new JButton("Gestione Documenti");
            btnDocumenti.addActionListener(e -> new DocumentManagementGUI());
            panel.add(btnDocumenti);
        }

        frame.add(panel);
        frame.setVisible(true);
    }
}


