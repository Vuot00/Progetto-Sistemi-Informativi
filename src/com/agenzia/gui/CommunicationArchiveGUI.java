package com.agenzia.gui;

import com.agenzia.database.DatabaseManager;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CommunicationArchiveGUI {
    private JFrame frame;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField idField, textField;

    public CommunicationArchiveGUI() {
        frame = new JFrame("Archivio Comunicazioni");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);
        frame.setLocationRelativeTo(null);

        tableModel = new DefaultTableModel(new String[]{"ID", "Comunicazione"}, 0);
        table = new JTable(tableModel);
        loadDataFromDatabase();

        JScrollPane scrollPane = new JScrollPane(table);
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        inputPanel.add(new JLabel("ID:"), gbc);

        gbc.gridx = 1;
        idField = new JTextField(15);
        inputPanel.add(idField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        inputPanel.add(new JLabel("Comunicazione:"), gbc);

        gbc.gridx = 1;
        textField = new JTextField(15);
        inputPanel.add(textField, gbc);

        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Aggiungi");
        JButton updateButton = new JButton("Modifica");
        JButton deleteButton = new JButton("Elimina");

        addButton.setBackground(new Color(144, 238, 144));
        deleteButton.setBackground(new Color(255, 99, 71));
        updateButton.setBackground(new Color(100, 149, 237));

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);

        addButton.addActionListener(e -> addCommunication());
        updateButton.addActionListener(e -> updateCommunication());
        deleteButton.addActionListener(e -> deleteCommunication());

        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(inputPanel, BorderLayout.NORTH);
        frame.add(buttonPanel, BorderLayout.SOUTH);
        frame.setVisible(true);
    }

    private void loadDataFromDatabase() {
        DatabaseManager.createTables();
        tableModel.setRowCount(0); // Pulisce la tabella prima di caricare i dati
        try (ResultSet rs = DatabaseManager.getAllCommunications()) {
            while (rs != null && rs.next()) {
                tableModel.addRow(new Object[]{rs.getString("id"), rs.getString("text")});
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Errore nel caricamento dati dal database!", "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void addCommunication() {
        String id = idField.getText().trim();
        String text = textField.getText().trim();
        if (!id.isEmpty() && !text.isEmpty()) {
            DatabaseManager.addCommunication(id, text);
            loadDataFromDatabase();  // Ricarica la tabella dopo l'aggiunta
            idField.setText("");
            textField.setText("");
        } else {
            JOptionPane.showMessageDialog(frame, "Compila entrambi i campi!", "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void updateCommunication() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            String id = (String) tableModel.getValueAt(selectedRow, 0);
            String newText = textField.getText().trim();
            if (!newText.isEmpty()) {
                DatabaseManager.updateCommunication(id, newText);
                loadDataFromDatabase();  // Ricarica la tabella dopo la modifica
            } else {
                JOptionPane.showMessageDialog(frame, "Inserisci un nuovo testo!", "Errore", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Seleziona una riga da modificare!", "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void deleteCommunication() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            String id = (String) tableModel.getValueAt(selectedRow, 0);
            DatabaseManager.deleteCommunication(id);
            loadDataFromDatabase();  // Ricarica la tabella dopo la cancellazione
        } else {
            JOptionPane.showMessageDialog(frame, "Seleziona una riga da eliminare!", "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(CommunicationArchiveGUI::new);
    }
}
