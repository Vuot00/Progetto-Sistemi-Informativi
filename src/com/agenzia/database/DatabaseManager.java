package com.agenzia.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private static final String URL = "jdbc:mysql://localhost:3306/agenzia";
    private static final String USER = "root"; // Modifica con il tuo username MySQL
    private static final String PASSWORD = "12234"; // Modifica con la tua password MySQL

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void createTables() {
        String createCommunicationsTable = "CREATE TABLE IF NOT EXISTS communications (id VARCHAR(255) PRIMARY KEY, text TEXT)";
        String createDocumentsTable = "CREATE TABLE IF NOT EXISTS documents (id VARCHAR(255) PRIMARY KEY, document TEXT)";
        String createImmobiliTable = "CREATE TABLE IF NOT EXISTS immobili (" +
                "id VARCHAR(255) PRIMARY KEY, " +
                "indirizzo VARCHAR(255), " +
                "stato VARCHAR(50), " +
                "metratura INT, " +
                "prezzo DECIMAL(10,2))";
        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(createCommunicationsTable);
            stmt.execute(createDocumentsTable);
            stmt.execute(createImmobiliTable);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Metodi per la gestione delle comunicazioni
    public static void addCommunication(String id, String text) {
        String sql = "INSERT INTO communications (id, text) VALUES (?, ?)";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id);
            pstmt.setString(2, text);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateCommunication(String id, String newText) {
        String sql = "UPDATE communications SET text = ? WHERE id = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newText);
            pstmt.setString(2, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteCommunication(String id) {
        String sql = "DELETE FROM communications WHERE id = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ResultSet getAllCommunications() {
        String sql = "SELECT * FROM communications";
        try {
            Connection conn = connect();
            Statement stmt = conn.createStatement();
            return stmt.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Metodi per la gestione dei documenti
    public static void addDocument(String id, String document) {
        String sql = "INSERT INTO documents (id, document) VALUES (?, ?)";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id);
            pstmt.setString(2, document);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateDocument(String id, String newDocument) {
        String sql = "UPDATE documents SET document = ? WHERE id = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newDocument);
            pstmt.setString(2, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteDocument(String id) {
        String sql = "DELETE FROM documents WHERE id = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ResultSet getAllDocuments() {
        String sql = "SELECT * FROM documents";
        try {
            Connection conn = connect();
            Statement stmt = conn.createStatement();
            return stmt.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Metodi per la gestione degli immobili
    public static void addImmobile(String id, String indirizzo, String stato, int metratura, double prezzo) {
        String sql = "INSERT INTO immobili (id, indirizzo, stato, metratura, prezzo) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id);
            pstmt.setString(2, indirizzo);
            pstmt.setString(3, stato);
            pstmt.setInt(4, metratura);
            pstmt.setDouble(5, prezzo);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateImmobile(String id, String indirizzo, String stato, int metratura, double prezzo) {
        String sql = "UPDATE immobili SET indirizzo = ?, stato = ?, metratura = ?, prezzo = ? WHERE id = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, indirizzo);
            pstmt.setString(2, stato);
            pstmt.setInt(3, metratura);
            pstmt.setDouble(4, prezzo);
            pstmt.setString(5, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteImmobile(String id) {
        String sql = "DELETE FROM immobili WHERE id = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<String[]> getAllImmobili() {
        List<String[]> immobili = new ArrayList<>();
        String sql = "SELECT * FROM immobili";
        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String[] immobile = {
                        rs.getString("id"),
                        rs.getString("indirizzo"),
                        rs.getString("stato"),
                        String.valueOf(rs.getInt("metratura")),
                        String.valueOf(rs.getDouble("prezzo"))
                };
                immobili.add(immobile);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return immobili;
    }
}