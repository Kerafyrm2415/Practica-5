package GUI;

import Terminal.*;

import javax.swing.*;
import java.awt.*;

public class GUIMain {
    public static void main(String[] args) {
        // Crear un barco (por ejemplo, uno de 3 casillas) y colocarlo en el tablero
        Tablero tableroLogico = new Tablero();
        GUITablero tableroVisual = new GUITablero(10, 10);
        SwingUtilities.invokeLater(() -> {
            // Crear y colocar un barco
            Barco barco = new Barco(2);
            barco.colocarEn(0, 0, false, 10); // Barco horizontal en (2,3) de tamaño 3
            boolean colocado = tableroLogico.colocarBarco(barco);
            if (!colocado) {
                System.out.println("No se pudo colocar el barco en esa posición");
            }

            JFrame frame = new JFrame("Tablero de BattleShip");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 700);
            frame.setLayout(new BorderLayout());

            // Panel central: Donde están los dos tableros
            JPanel panelTableros = new JPanel();
            GUITablero tableroJugador1 = new GUITablero(10,10);
            GUITablero tableroJugador2 = new GUITablero(10,10);

            panelTableros.setBorder(BorderFactory.createTitledBorder("Tablero"));
            panelTableros.add(tableroJugador1.getPanel());
            panelTableros.add(tableroJugador2.getPanel());

            // Panel Inferior: Donde estan los botones para las acciones
            JPanel panelBotones = new JPanel(new FlowLayout());
            JButton botonDisparar = new JButton("Disparar");
            JButton botonTerminar = new JButton("Terminar turno");
            panelBotones.add(botonDisparar);
            panelBotones.add(botonTerminar);

            // Agregamos los frames aqui
            frame.add(panelTableros, BorderLayout.CENTER);
            frame.add(panelBotones, BorderLayout.SOUTH);

            frame.setVisible(true);

            tableroJugador2.actualizarTableroVisual(tableroLogico);

            tableroLogico.recibirTiro(0,0);
            tableroJugador1.actualizarTableroVisual(tableroLogico);
            tableroLogico.imprimirTablero();
        });
    }

    public static void imprimirTableroTerminal(Tablero logico) {
        for (int fila = 0; fila < 10; fila++) {
            for (int col = 0; col < 10; col++) {
                char estado = logico.getCasilla(fila, col);
                switch (estado) {
                    case 'A': System.out.print("🌊 "); break;
                    case 'B': System.out.print("🚢 "); break;
                    case 'X': System.out.print("💥 "); break;
                    case 'O': System.out.print("❌ "); break;
                }
            }
            System.out.println();
        }
    }
}