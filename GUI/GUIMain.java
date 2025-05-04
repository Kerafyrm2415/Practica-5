package GUI;

import GUI.*;
import javax.swing.*;
import java.awt.*;

public class GUIMain {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
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
        });
    }
}