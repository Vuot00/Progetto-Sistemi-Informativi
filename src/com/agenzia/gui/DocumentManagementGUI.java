package com.agenzia.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.agenzia.database.DatabaseManager;

public class DocumentManagementGUI {
    private final JFrame frame;
    private final JTable table;
    private final DefaultTableModel tableModel;
    private final JTextField idField;
    private final JTextField documentField;

    public DocumentManagementGUI() {
        frame = new JFrame("Gestione Documenti");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);
        frame.setLocationRelativeTo(null);

        tableModel = new DefaultTableModel(new String[]{"ID", "Documento"}, 0);
        table = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(table);

        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(new JLabel("ID:"), gbc);

        gbc.gridx = 1;
        idField = new JTextField(15);
        inputPanel.add(idField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        inputPanel.add(new JLabel("Documento:"), gbc);

        gbc.gridx = 1;
        documentField = new JTextField(15);
        inputPanel.add(documentField, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton addButton = new JButton("Aggiungi");
        JButton updateButton = new JButton("Modifica");
        JButton deleteButton = new JButton("Elimina");

        addButton.setBackground(new Color(144, 238, 144));
        deleteButton.setBackground(new Color(255, 99, 71));
        updateButton.setBackground(new Color(100, 149, 237));

        addButton.addActionListener(e -> addDocument());
        updateButton.addActionListener(e -> updateDocument());
        deleteButton.addActionListener(e -> deleteDocument());

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);

        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(inputPanel, BorderLayout.NORTH);
        frame.add(buttonPanel, BorderLayout.SOUTH);
        frame.setVisible(true);

        loadDataFromDatabase();
    }

    private void loadDataFromDatabase() {
        tableModel.setRowCount(0);
        try (ResultSet rs = DatabaseManager.getAllDocuments()) {
            while (rs != null && rs.next()) {
                tableModel.addRow(new Object[]{rs.getString("id"), rs.getString("text")});
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Errore nel caricamento dati dal database!", "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addDocument() {
        String id = idField.getText().trim();
        String document = documentField.getText().trim();
        if (!id.isEmpty() && !document.isEmpty()) {
            DatabaseManager.addDocument(id, document);
            loadDataFromDatabase();
            idField.setText("");
            documentField.setText("");
        } else {
            JOptionPane.showMessageDialog(frame, "Compila entrambi i campi!", "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateDocument() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            String id = (String) tableModel.getValueAt(selectedRow, 0);
            String newText = documentField.getText().trim();
            if (!newText.isEmpty()) {
                DatabaseManager.updateDocument(id, newText);
                loadDataFromDatabase();
            } else {
                JOptionPane.showMessageDialog(frame, "Inserisci un nuovo testo!", "Errore", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Seleziona una riga da modificare!", "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteDocument() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            String id = (String) tableModel.getValueAt(selectedRow, 0);
            DatabaseManager.deleteDocument(id);
            loadDataFromDatabase();
        } else {
            JOptionPane.showMessageDialog(frame, "Seleziona una riga da eliminare!", "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(DocumentManagementGUI::new);
    }
}