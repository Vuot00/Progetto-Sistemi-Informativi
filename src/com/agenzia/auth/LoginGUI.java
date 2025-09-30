package com.agenzia.auth;

import javax.swing.*;
import java.awt.*;

import com.agenzia.gui.MainMenuGUI;
import com.agenzia.models.Utente;

public class LoginGUI {
    private JFrame frame;
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;

    public LoginGUI() {
        frame = new JFrame("Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 250);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new GridLayout(4, 2, 10, 10));

        JLabel lblUsername = new JLabel("Username:");
        txtUsername = new JTextField();
        JLabel lblPassword = new JLabel("Password:");
        txtPassword = new JPasswordField();

        btnLogin = new JButton("Accedi");
        btnLogin.setBackground(new Color(144, 238, 144));

        frame.add(lblUsername);
        frame.add(txtUsername);
        frame.add(lblPassword);
        frame.add(txtPassword);
        frame.add(new JLabel());  // Spazio vuoto
        frame.add(btnLogin);

        btnLogin.addActionListener(e -> {
            String username = txtUsername.getText();
            String password = new String(txtPassword.getPassword());

            // Simuliamo un controllo credenziali (da sostituire con il backend)
            Utente utente = autenticaUtente(username, password);

            if (utente != null) {
                JOptionPane.showMessageDialog(frame, "Accesso riuscito!");
                frame.dispose(); // Chiude la finestra di login
                new MainMenuGUI(utente); // Apre il menu principale
            } else {
                JOptionPane.showMessageDialog(frame, "Credenziali errate", "Errore", JOptionPane.ERROR_MESSAGE);
            }
        });

        frame.setVisible(true);
    }

    private Utente autenticaUtente(String username, String password) {
        // Simuliamo un database di utenti (da collegare al backend in futuro)
        if (username.equals("admin") && password.equals("admin")) return new Utente(username, "Amministratore");
        if (username.equals("agente") && password.equals("1234")) return new Utente(username, "Agente");
        return null;
    }

    public static void main(String[] args) {
        new LoginGUI();
    }
}


