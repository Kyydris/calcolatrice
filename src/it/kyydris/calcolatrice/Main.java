package it.kyydris.calcolatrice;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Main {
    private JFrame finestra;
    private JTextField schermo;

    private double valoreSalvato = 0;
    private String operatore = "";
    private boolean nuovoNumero = true;

    public Main() {
        finestra = new JFrame("Calcolatrice");
        finestra.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        finestra.setSize(320, 420);
        finestra.setLayout(new BorderLayout(6, 6));

        schermo = new JTextField("0");
        schermo.setEditable(false);
        schermo.setHorizontalAlignment(JTextField.RIGHT);
        schermo.setFont(new Font("Arial", Font.PLAIN, 28));
        schermo.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        finestra.add(schermo, BorderLayout.NORTH);

        JPanel pulsanti = new JPanel(new GridLayout(5, 4, 6, 6));
        String[] etichette = {
                "C", "÷", "×", "←",
                "7", "8", "9", "-",
                "4", "5", "6", "+",
                "1", "2", "3", "=",
                "0", ".", "", ""
        };

        for (String testo : etichette) {
            if (testo.equals("")) {
                pulsanti.add(new JLabel());
                continue;
            }
            JButton bottone = new JButton(testo);
            bottone.setFont(new Font("Arial", Font.PLAIN, 20));
            bottone.addActionListener(creaAzione(testo));
            pulsanti.add(bottone);
        }

        finestra.add(pulsanti, BorderLayout.CENTER);

        finestra.setLocationRelativeTo(null);
        finestra.setVisible(true);
    }

    private ActionListener creaAzione(String testo) {
        return e -> {
            switch (testo) {
                case "C":
                    schermo.setText("0");
                    valoreSalvato = 0;
                    operatore = "";
                    nuovoNumero = true;
                    break;
                case "←":
                    cancellaUltimo();
                    break;
                case "+": case "-": case "×": case "÷":
                    applicaOperatore(testo);
                    break;
                case "=":
                    calcolaRisultato();
                    operatore = "";
                    nuovoNumero = true;
                    break;
                default:
                    aggiungiNumeroOPunto(testo);
            }
        };
    }

    private void aggiungiNumeroOPunto(String s) {
        if (nuovoNumero) {
            if (s.equals(".")) schermo.setText("0.");
            else schermo.setText(s);
            nuovoNumero = false;
        } else {
            String attuale = schermo.getText();
            if (s.equals(".") && attuale.contains(".")) return;
            if (attuale.equals("0") && !s.equals(".")) attuale = "";
            schermo.setText(attuale + s);
        }
    }

    private void cancellaUltimo() {
        String attuale = schermo.getText();
        if (attuale.length() <= 1) {
            schermo.setText("0");
            nuovoNumero = true;
        } else {
            schermo.setText(attuale.substring(0, attuale.length() - 1));
        }
    }

    private void applicaOperatore(String op) {
        double valoreAttuale = Double.parseDouble(schermo.getText());
        if (!operatore.isEmpty() && !nuovoNumero) {
            valoreSalvato = calcola(valoreSalvato, valoreAttuale, operatore);
            schermo.setText(formattaNumero(valoreSalvato));
        } else if (operatore.isEmpty()) {
            valoreSalvato = valoreAttuale;
        }
        operatore = op;
        nuovoNumero = true;
    }

    private void calcolaRisultato() {
        if (operatore.isEmpty()) return;
        double valoreAttuale = Double.parseDouble(schermo.getText());
        double risultato = calcola(valoreSalvato, valoreAttuale, operatore);
        schermo.setText(formattaNumero(risultato));
        valoreSalvato = risultato;
    }

    private double calcola(double a, double b, String op) {
        switch (op) {
            case "+": return a + b;
            case "-": return a - b;
            case "×": return a * b;
            case "÷":
                if (b == 0) {
                    JOptionPane.showMessageDialog(finestra, "Non puoi dividere per zero!", "Babbo", JOptionPane.ERROR_MESSAGE);
                    return a;
                }
                return a / b;
            default: return b;
        }
    }

    private String formattaNumero(double v) {
        if (v == (long) v) return String.format("%d", (long) v);
        else return String.valueOf(v);
    }

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception ignored) {}
        SwingUtilities.invokeLater(Main::new);
    }
}