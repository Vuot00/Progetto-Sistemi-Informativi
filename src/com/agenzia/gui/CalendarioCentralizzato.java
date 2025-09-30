package com.agenzia.gui;


import com.agenzia.models.Acquirente;
import com.agenzia.models.Agente;
import com.agenzia.models.Appuntamento;
import com.agenzia.models.Immobile;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CalendarioCentralizzato {
    private JFrame frame;
    private JPanel calendarioPanel;
    private JLabel meseAnnoLabel;
    private JButton addButton;
    private JButton removeButton;
    private JButton changeMonthButton;
    private List<Appuntamento> appuntamenti;

    public CalendarioCentralizzato() {
        appuntamenti = new ArrayList<>();
        loadAppuntamenti();

        frame = new JFrame("Calendario Appuntamenti");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setResizable(false);

        frame.getContentPane().setBackground(Color.LIGHT_GRAY); // Cambia il colore dello sfondo


        meseAnnoLabel = new JLabel("", SwingConstants.CENTER);
        meseAnnoLabel.setFont(new Font("Arial", Font.BOLD, 24));
        frame.add(meseAnnoLabel, BorderLayout.NORTH);

        calendarioPanel = new JPanel(new GridLayout(0, 7));
        frame.add(new JScrollPane(calendarioPanel), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        addButton = new JButton("Aggiungi Appuntamento");
        removeButton = new JButton("Rimuovi Appuntamento");
        changeMonthButton = new JButton("Cambia Data");

        addButton.addActionListener(e -> mostraAggiungiDialogo());
        removeButton.addActionListener(e -> mostraRimuoviDialogo());
        changeMonthButton.addActionListener(e -> mostraCambiaDataDialogo());

        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(changeMonthButton);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        aggiornaCalendario(LocalDate.now().getYear(), LocalDate.now().getMonthValue());

        frame.setVisible(true);
    }

    private void aggiornaCalendario(int anno, int mese) {
        calendarioPanel.removeAll();
        meseAnnoLabel.setText("Calendario - " + Month.of(mese) + " " + anno);

        String[] giorniSettimana = {"Lun", "Mar", "Mer", "Gio", "Ven", "Sab", "Dom"};
        for (String giorno : giorniSettimana) {
            JLabel giornoLabel = new JLabel(giorno, SwingConstants.CENTER);
            giornoLabel.setFont(new Font("Arial", Font.BOLD, 16));
            calendarioPanel.add(giornoLabel);
        }

        YearMonth yearMonth = YearMonth.of(anno, mese);
        LocalDate primoGiorno = yearMonth.atDay(1);
        int offset = primoGiorno.getDayOfWeek().getValue() % 7;

        for (int i = 0; i < offset; i++) {
            calendarioPanel.add(new JLabel(""));
        }

        int giorniNelMese = yearMonth.lengthOfMonth();
        for (int giorno = 1; giorno <= giorniNelMese; giorno++) {
            LocalDate dataCorrente = LocalDate.of(anno, mese, giorno);
            JButton giornoButton = new JButton(String.valueOf(giorno));
            giornoButton.setFont(new Font("Arial", Font.PLAIN, 14));

            long appuntamentiCount = appuntamenti.stream()
                    .filter(a -> a.getData().equals(dataCorrente))
                    .count();

            if (appuntamentiCount == 0) {
                giornoButton.setBackground(Color.GREEN);
            } else if (appuntamentiCount == 1) {
                giornoButton.setBackground(Color.ORANGE);
            } else {
                giornoButton.setBackground(Color.RED);
                giornoButton.setForeground(Color.WHITE);
            }

            giornoButton.addActionListener(e -> mostraInfoGiorno(dataCorrente));
            calendarioPanel.add(giornoButton);
        }

        calendarioPanel.revalidate();
        calendarioPanel.repaint();
    }

    private void mostraAggiungiDialogo() {
        JTextField dataField = new JTextField(10);

        JComboBox<String> oraComboBox = new JComboBox<>();
        oraComboBox.addItem("08:00");
        oraComboBox.addItem("09:00");
        oraComboBox.addItem("10:00");
        oraComboBox.addItem("11:00");
        oraComboBox.addItem("12:00");
        oraComboBox.addItem("13:00");
        oraComboBox.addItem("14:00");
        oraComboBox.addItem("15:00");
        oraComboBox.addItem("16:00");
        oraComboBox.addItem("17:00");
        oraComboBox.addItem("18:00");

        // Liste di esempio
        List<Agente> agentiEsempio = List.of(new Agente("CF123", "Mario", "Rossi", "Vendite"),
                new Agente("CF124", "Luigi", "Bianchi", "Affitti"));
        List<Immobile> immobiliEsempio = List.of(new Immobile("ID001", "Via Roma, 10", "Disponibile", 100, 200000),
                new Immobile("ID002", "Via Milano, 20", "Venduto", 80, 150000));

        JComboBox<Agente> agenteComboBox = new JComboBox<>(agentiEsempio.toArray(new Agente[0]));
        JComboBox<Immobile> immobileComboBox = new JComboBox<>(immobiliEsempio.toArray(new Immobile[0]));

        JTextField codiceFiscaleField = new JTextField(16); // Campo per il codice fiscale

        JPanel panel = new JPanel(new GridLayout(8, 2));
        panel.add(new JLabel("Data (YYYY-MM-DD):"));
        panel.add(dataField);
        panel.add(new JLabel("Ora:"));
        panel.add(oraComboBox);
        panel.add(new JLabel("Agente:"));
        panel.add(agenteComboBox);
        panel.add(new JLabel("Immobile:"));
        panel.add(immobileComboBox);
        panel.add(new JLabel("Cliente (Codice Fiscale):"));
        panel.add(codiceFiscaleField);

        int result = JOptionPane.showConfirmDialog(frame, panel, "Aggiungi Appuntamento",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                LocalDate data = LocalDate.parse(dataField.getText());
                LocalTime ora = LocalTime.parse((String) oraComboBox.getSelectedItem());

                Agente agente = (Agente) agenteComboBox.getSelectedItem();
                Immobile immobile = (Immobile) immobileComboBox.getSelectedItem();

                String codiceFiscale = codiceFiscaleField.getText().strip();
                if (codiceFiscale.isEmpty() || codiceFiscale.length() != 16) {
                    throw new IllegalArgumentException("Il codice fiscale è obbligatorio e deve essere di 16 caratteri.");
                }
                Acquirente cliente = new Acquirente(codiceFiscale); // Creazione cliente con codice fiscale

                Appuntamento appuntamento = new Appuntamento(data, ora, agente, cliente, immobile);

                boolean esisteConflitto = appuntamenti.stream().anyMatch(a ->
                        (a.getData().equals(data) && a.getOra().equals(ora) &&
                                a.getAgente().equals(agente)) || (a.getImmobile().equals(immobile) && a.getData().equals(data) && a.getOra().equals(ora)));

                if (!esisteConflitto) {
                    appuntamenti.add(appuntamento);
                    saveAppuntamenti();
                    aggiornaCalendario(data.getYear(), data.getMonthValue());
                } else {
                    JOptionPane.showMessageDialog(frame, "Conflitto di appuntamento!", "Errore", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Errore nei dati inseriti. Verifica i campi.", "Errore", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    private void mostraRimuoviDialogo() {
        if (appuntamenti.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Nessun appuntamento da rimuovere.", "Informazione", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        Object[] opzioni = appuntamenti.stream().map(Appuntamento::toString).toArray();
        String selezionato = (String) JOptionPane.showInputDialog(
                frame,
                "Seleziona l'appuntamento da rimuovere:",
                "Rimuovi Appuntamento",
                JOptionPane.PLAIN_MESSAGE,
                null,
                opzioni,
                null);

        if (selezionato != null) {
            appuntamenti.removeIf(a -> a.toString().equals(selezionato));
            saveAppuntamenti();
            aggiornaCalendario(LocalDate.now().getYear(), LocalDate.now().getMonthValue());
        }
    }

    private void mostraInfoGiorno(LocalDate data) {
        List<Appuntamento> appuntamentiGiorno = appuntamenti.stream()
                .filter(a -> a.getData().equals(data))
                .collect(Collectors.toList());

        if (appuntamentiGiorno.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Nessun appuntamento in questa data.", "Informazione", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(frame, "Appuntamenti:\n" + appuntamentiGiorno, "Appuntamenti del giorno", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void mostraCambiaDataDialogo() {
        // JComboBox per l'anno
        Integer[] anni = {2023, 2024, 2025, 2026, 2027}; // Intervallo di anni
        JComboBox<Integer> annoComboBox = new JComboBox<>(anni);

        // JComboBox per il mese
        String[] mesi = {"Gennaio", "Febbraio", "Marzo", "Aprile", "Maggio", "Giugno",
                "Luglio", "Agosto", "Settembre", "Ottobre", "Novembre", "Dicembre"};
        JComboBox<String> meseComboBox = new JComboBox<>(mesi);

        JPanel panel = new JPanel(new GridLayout(3, 1));
        panel.add(new JLabel("Seleziona l'anno:"));
        panel.add(annoComboBox);
        panel.add(new JLabel("Seleziona il mese:"));
        panel.add(meseComboBox);

        int result = JOptionPane.showConfirmDialog(frame, panel, "Cambia Data",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                int anno = (Integer) annoComboBox.getSelectedItem();
                int mese = meseComboBox.getSelectedIndex() + 1; // Indice da 0, quindi sommiamo 1
                aggiornaCalendario(anno, mese);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Errore nella selezione della data.", "Errore", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void saveAppuntamenti() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("appuntamenti.dat"))) {
            out.writeObject(appuntamenti);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Errore nel salvataggio degli appuntamenti.", "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadAppuntamenti() {
        File file = new File("appuntamenti.dat");
        if (!file.exists()) {
            appuntamenti = new ArrayList<>(); // File assente: lista vuota
            return;
        }
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            appuntamenti = (List<Appuntamento>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            appuntamenti = new ArrayList<>(); // File corrotto: inizializza lista vuota
            JOptionPane.showMessageDialog(frame, "Impossibile caricare gli appuntamenti, lista inizializzata vuota.",
                    "Avviso", JOptionPane.INFORMATION_MESSAGE);
        }
    }


    public static void main(String[] args) {
        new CalendarioCentralizzato();
    }
}

/*class Appuntamento implements Serializable {
    private LocalDate data;
    private LocalTime ora;
    private Agente agente;
    private Acquirente acquirente;
    private Immobile immobile;

    public Appuntamento(LocalDate data, LocalTime ora, Agente agente, Acquirente acquirente, Immobile immobile) {
        this.data = data;
        this.ora = ora;
        this.agente = agente;
        this.acquirente = acquirente;
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

    public Acquirente getAcquirente() {
        return acquirente;
    }

    public Immobile getImmobile() {
        return immobile;
    }

    @Override
    public String toString() {
        return "Appuntamento {" +
                "data=" + data +
                ", ora=" + ora +
                ", agente=" + agente.getNome() + " " + agente.getCognome() +
                ", acquirente=" + acquirente.getCf() + " " +
                ", immobile=" + immobile.getId() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Appuntamento that = (Appuntamento) o;
        return data.equals(that.data) && ora.equals(that.ora) && agente.equals(that.agente);
    }

    @Override
    public int hashCode() {
        return Objects.hash(data, ora, agente.getCf());
    }
}

class Agente implements Serializable {
    private String cf;
    private String nome;
    private String cognome;
    private String specializzazione;

    public Agente(String cf, String nome, String cognome, String specializzazione) {
        this.cf = cf;
        this.nome = nome;
        this.cognome = cognome;
        this.specializzazione = specializzazione;
    }

    public Agente(String nome) {
        this(null, nome, null, null);
    }

    public String getCf() {
        return cf;
    }

    public String getNome() {
        return nome;
    }

    public String getCognome() {
        return cognome;
    }

    public String getSpecializzazione() {
        return specializzazione;
    }

    @Override
    public String toString() {
        return "Agente {" +
                "cf='" + cf + '\'' +
                ", nome='" + nome + '\'' +
                ", cognome='" + cognome + '\'' +
                ", specializzazione='" + specializzazione + '\'' +
                '}';
    }

    @Override
public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Agente agente = (Agente) o;
    return cf.equals(agente.cf);
}

@Override
public int hashCode() {
    return Objects.hash(cf);
}
}

// Classe Acquirente
class Acquirente implements Serializable {
    private String cf;
    private String nome;
    private String cognome;
    private String contatti;
    private List<String> preferenze;

    public Acquirente(String cf, String nome, String cognome, String contatti) {
        this.cf = cf;
        this.nome = nome;
        this.cognome = cognome;
        this.contatti = contatti;
        this.preferenze = new ArrayList<>();
    }

    public String getCf() {
        return cf;
    }

    public Acquirente(String cf) {
        this(cf, "", "", "");
    }


    public String getNome() {
        return nome;
    }

    public String getCognome() {
        return cognome;
    }

    public String getContatti() {
        return contatti;
    }

    public List<String> getPreferenze() {
        return preferenze;
    }

    public void aggiungiPreferenza(String preferenza) {
        preferenze.add(preferenza);
    }

    @Override
    public String toString() {
        return "Acquirente {" +
                "cf='" + cf + '\'' +
                ", nome='" + nome + '\'' +
                ", cognome='" + cognome + '\'' +
                ", contatti='" + contatti + '\'' +
                ", preferenze=" + preferenze +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Acquirente that = (Acquirente) o;
        return cf.equals(that.cf);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cf);
    }
}

// Classe Immobile
class Immobile implements Serializable {
    private String id;
    private String indirizzo;
    private String stato;
    private double metratura;
    private double prezzo;

    public Immobile(String id, String indirizzo, String stato, double metratura, double prezzo) {
        this.id = id;
        this.indirizzo = indirizzo;
        this.stato = stato;
        this.metratura = metratura;
        this.prezzo = prezzo;
    }

    public Immobile(String indirizzo) {
        this(null, indirizzo, "", 0.0, 0.0); // Altri valori predefiniti
    }


    public String getId() {
        return id;
    }

    public String getIndirizzo() {
        return indirizzo;
    }

    public String getStato() {
        return stato;
    }

    public double getMetratura() {
        return metratura;
    }

    public double getPrezzo() {
        return prezzo;
    }

    @Override
    public String toString() {
        return "Immobile {" +
                "id='" + id + '\'' +
                ", indirizzo='" + indirizzo + '\'' +
                ", stato='" + stato + '\'' +
                ", metratura=" + metratura +
                ", prezzo=" + prezzo +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Immobile immobile = (Immobile) o;
        return id.equals(immobile.id) && indirizzo.equals(immobile.indirizzo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, indirizzo);
    }
}*/