package GUI;

import Terminal.*;
import javax.swing.*;
import java.awt.*;
import java.util.function.BiConsumer;

public class GUITablero {
    private JPanel panel;
    private JButton[][] botones;
    int filaSeleccionada = -1;
    int columnaSeleccionada = -1;


    public GUITablero(int filas, int columnas, Tablero tableroLogico) {
        panel = new JPanel();
        panel.setLayout(new GridLayout(filas + 1, columnas + 1));
        botones = new JButton[filas][columnas];

        panel.add(new JLabel(""));

        for (int c = 0; c < columnas; c++) {
            char letra = (char) ('A' + c);
            panel.add(new JLabel(String.valueOf(letra), SwingConstants.CENTER));
        }

        // Filas con botones
        for (int f = 0; f < filas; f++) {
            panel.add(new JLabel(String.valueOf(f + 1), SwingConstants.CENTER));
            for (int c = 0; c < columnas; c++) {
                JButton boton = new JButton("🌊");
                int finalF = f;
                int finalC = c;
                boton.addActionListener(e -> {
                    filaSeleccionada = finalF;
                    columnaSeleccionada = finalC;
                    System.out.println("Casilla seleccionada: " + (char) ('A' + finalC) + (finalF + 1));
                });

                botones[f][c] = boton;
                panel.add(boton);
            }
        }
    }

    public GUITablero(int filas, int columnas, BiConsumer<Integer, Integer> manejadorCasilla){
        panel = new JPanel();
        panel.setLayout(new GridLayout(filas + 1, columnas + 1));
        botones = new JButton[filas][columnas];

        panel.add(new JLabel(""));
        for (int c = 0; c < columnas; c++) {
            char letra = (char) ('A' + c);
            panel.add(new JLabel(String.valueOf(letra), SwingConstants.CENTER));
        }

        for (int f = 0; f < filas; f++) {
            panel.add(new JLabel(String.valueOf(f + 1), SwingConstants.CENTER));
            for (int c = 0; c < columnas; c++) {
                JButton boton = new JButton("🌊");
                final int fila = f;
                final int col = c;
                boton.addActionListener(e -> {
                    filaSeleccionada = fila;
                    columnaSeleccionada = col;
                    System.out.println("Casilla seleccionada: " + (char) ('A' + col) + (fila + 1));
                    manejadorCasilla.accept(fila, col);
                });
                botones[f][c] = boton;
                panel.add(boton);
            }
        }
    }

    public JPanel getPanel() {
        return panel;
    }

    public void actualizarTableroVisual(Tablero tableroLogico) {
        for (int fila = 0; fila < 10; fila++) {
            for (int col = 0; col < 10; col++) {
                String estadoCasilla = tableroLogico.getCasilla(fila, col);
                botones[fila][col].setText(estadoCasilla);
                if (estadoCasilla.equals("🌊")) {
                    botones[fila][col].setBackground(new Color(96, 196, 225));
                } else if (estadoCasilla.equals("🚢")) {
                    botones[fila][col].setBackground(new Color(117, 124, 129));
                } else if (estadoCasilla.equals("💥")) {
                    botones[fila][col].setBackground(new Color(203, 24, 15));
                } else if (estadoCasilla.equals("❌")) {
                    botones[fila][col].setBackground(new Color(6, 84, 138));
                }
            }
        }
    }

    public int getFilaSeleccionada() {
        return filaSeleccionada;
    }

    public int getColumnaSeleccionada() {
        return columnaSeleccionada;
    }

    public void limpiarSeleccion() {
        filaSeleccionada = -2;
        columnaSeleccionada = -2;
    }

}
