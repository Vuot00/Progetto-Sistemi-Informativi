package com.agenzia.gui;

import com.agenzia.database.DatabaseManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class DatabaseImmobili {
    private JFrame frame;
    private JTable table;
    private DefaultTableModel model;
    private JTextField txtID, txtIndirizzo, txtStato, txtMetratura, txtPrezzo;
    private JButton btnAggiungi, btnRimuovi, btnModifica;

    public DatabaseImmobili() {
        frame = new JFrame("Gestione Immobili");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 600);
        frame.setLocationRelativeTo(null);
        frame.getContentPane().setBackground(new Color(240, 248, 255));

        model = new DefaultTableModel();
        model.setColumnIdentifiers(new String[]{"ID", "Indirizzo", "Stato", "Metratura", "Prezzo"});

        table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setDefaultEditor(Object.class, null);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(table);

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Inserisci i dati dell'immobile"));

        txtID = new JTextField();
        txtIndirizzo = new JTextField();
        txtStato = new JTextField();
        txtMetratura = new JTextField();
        txtPrezzo = new JTextField();

        btnAggiungi = new JButton("Aggiungi");
        btnRimuovi = new JButton("Rimuovi");
        btnModifica = new JButton("Modifica");

        btnAggiungi.setBackground(new Color(144, 238, 144));
        btnRimuovi.setBackground(new Color(255, 99, 71));
        btnModifica.setBackground(new Color(100, 149, 237));

        panel.add(new JLabel("ID:"));
        panel.add(txtID);
        panel.add(new JLabel("Indirizzo:"));
        panel.add(txtIndirizzo);
        panel.add(new JLabel("Stato:"));
        panel.add(txtStato);
        panel.add(new JLabel("Metratura:"));
        panel.add(txtMetratura);
        panel.add(new JLabel("Prezzo:"));
        panel.add(txtPrezzo);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnAggiungi);
        buttonPanel.add(btnRimuovi);
        buttonPanel.add(btnModifica);

        btnAggiungi.addActionListener(e -> aggiungiImmobile());
        btnRimuovi.addActionListener(e -> rimuoviImmobile());
        btnModifica.addActionListener(e -> modificaImmobile());

        frame.setLayout(new BorderLayout(10, 10));
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(panel, BorderLayout.NORTH);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        caricaDati();
        frame.setVisible(true);
    }

    private void caricaDati() {
        model.setRowCount(0); // Pulizia della tabella
        List<String[]> immobili = DatabaseManager.getAllImmobili();
        for (String[] immobile : immobili) {
            model.addRow(immobile);
        }
    }

    private void aggiungiImmobile() {
        String id = txtID.getText();
        String indirizzo = txtIndirizzo.getText();
        String stato = txtStato.getText();
        String metraturaText = txtMetratura.getText();
        String prezzoText = txtPrezzo.getText();

        if (id.isEmpty() || indirizzo.isEmpty() || stato.isEmpty() || metraturaText.isEmpty() || prezzoText.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Tutti i campi devono essere compilati!", "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int metratura = Integer.parseInt(metraturaText);
            double prezzo = Double.parseDouble(prezzoText);
            DatabaseManager.addImmobile(id, indirizzo, stato, metratura, prezzo);
            caricaDati();
            pulisciCampi();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Metratura e Prezzo devono essere numerici!", "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void rimuoviImmobile() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(frame, "Seleziona un immobile da rimuovere!", "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String id = (String) model.getValueAt(selectedRow, 0);
        DatabaseManager.deleteImmobile(id);
        caricaDati();
    }

    private void modificaImmobile() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(frame, "Seleziona un immobile da modificare!", "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String id = (String) model.getValueAt(selectedRow, 0);
        String indirizzo = txtIndirizzo.getText();
        String stato = txtStato.getText();
        String metraturaText = txtMetratura.getText();
        String prezzoText = txtPrezzo.getText();

        if (indirizzo.isEmpty() || stato.isEmpty() || metraturaText.isEmpty() || prezzoText.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Tutti i campi devono essere compilati!", "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int metratura = Integer.parseInt(metraturaText);
            double prezzo = Double.parseDouble(prezzoText);
            DatabaseManager.updateImmobile(id, indirizzo, stato, metratura, prezzo);
            caricaDati();
            pulisciCampi();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Metratura e Prezzo devono essere numerici!", "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void pulisciCampi() {
        txtID.setText("");
        txtIndirizzo.setText("");
        txtStato.setText("");
        txtMetratura.setText("");
        txtPrezzo.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(DatabaseImmobili::new);
    }
}
