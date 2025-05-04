package GUI;
import javax.swing.*;
import java.awt.*;

public class GUITablero {
    private JPanel panel;
    private JButton[][] botones;

    public GUITablero(int filas, int columnas) {
        panel = new JPanel();
        panel.setLayout(new GridLayout(filas + 1, columnas + 1)); // +1 para encabezados
        botones = new JButton[filas][columnas];

        panel.add(new JLabel("")); // esquina vacía

        // Encabezados A-J
        for (int c = 0; c < columnas; c++) {
            char letra = (char) ('A' + c);
            panel.add(new JLabel(String.valueOf(letra), SwingConstants.CENTER));
        }

        // Filas con números + botones
        for (int f = 0; f < filas; f++) {
            panel.add(new JLabel(String.valueOf(f + 1), SwingConstants.CENTER));
            for (int c = 0; c < columnas; c++) {
                JButton boton = new JButton("🌊");
                botones[f][c] = boton;
                panel.add(boton);
            }
        }
    }
    public JPanel getPanel() {
        return panel;
    }
}
