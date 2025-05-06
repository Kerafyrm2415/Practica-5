package GUI;

import Terminal.*;

import javax.swing.*;
import java.awt.*;

public class GUIJuego {
    private int[] tamañobarcos = {5, 4, 3, 3, 2, 2};
    private int barcoActual = 0;
    private Tablero tableroLogico;
    private GUITablero tableroJugador1;
    private GUITablero tableroJugador2;
    private JFrame frame;
    private GUITablero tableroVisual;
    private boolean colocandoBarco = true;
    private boolean turnoJugador1 = true;


    public GUIJuego() {
        tableroLogico = new Tablero();
        iniciarInterfaz();
    }


    public void iniciarInterfaz() {
        //Colocacioón de barcos
        SwingUtilities.invokeLater(() -> {


            frame = new JFrame("Tablero de BattleShip");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 700);
            frame.setLayout(new BorderLayout());

            // Panel central: Donde están los dos tableros
            JPanel panelTableros = new JPanel();
            tableroJugador1 = new GUITablero(10, 10, tableroLogico);
            tableroJugador2 = new GUITablero(10, 10, tableroLogico);

            tableroVisual = tableroJugador1;

            panelTableros.setBorder(BorderFactory.createTitledBorder("Tablero"));
            panelTableros.add(tableroJugador1.getPanel());
            panelTableros.add(tableroJugador2.getPanel());

            // Panel Inferior: Donde estan los botones para las acciones
            JPanel panelBotones = new JPanel(new FlowLayout());
            JButton botonDisparar = new JButton("Disparar");
            botonDisparar.addActionListener(e -> {
                int fila = tableroJugador1.getFilaSeleccionada();
                int columna = tableroJugador1.getColumnaSeleccionada();

                if (fila == -1 || columna == -1) {
                    JOptionPane.showMessageDialog(null, "Selecciona una casilla primero");
                } else if (fila == -2 && columna == -2) {
                    JOptionPane.showMessageDialog(null, "Ya disparaste, termina tu turno.");
                } else {

                    boolean acierto = tableroLogico.recibirTiro(columna, fila);
                    String estado = tableroLogico.getCasilla(fila, columna);

                    if (estado.equals("💥")) {
                        System.out.println("Disparo en " + (char) ('A' + columna) + (fila + 1) + ": ¡Impacto! 💥");
                    } else if (estado.equals("❌")) {
                        System.out.println("Disparo en en " + (char) ('A' + columna) + (fila + 1) + ": Agua ❌");
                    }

                    tableroJugador1.actualizarTableroVisual(tableroLogico);
                    tableroJugador1.limpiarSeleccion();
                }
            });
            JButton botonTerminar = new JButton("Terminar turno");
            botonTerminar.addActionListener(e -> {
                    // Cambiar de turno
                    turnoJugador1 = !turnoJugador1;

                    if (turnoJugador1) {
                        tableroVisual = tableroJugador1;
                        tableroJugador1.getPanel().setVisible(true);
                        tableroJugador2.getPanel().setVisible(false);
                        JOptionPane.showMessageDialog(null, "Turno del Jugador 1");
                    } else {
                        tableroVisual = tableroJugador2;
                        tableroJugador1.getPanel().setVisible(false);
                        tableroJugador2.getPanel().setVisible(true);
                        JOptionPane.showMessageDialog(null, "Turno del Jugador 2");
                    }
            });
            JButton botonMostrar = new JButton("Revelar Tablero en terminal");
            botonMostrar.addActionListener(e -> {
                tableroLogico.imprimirTablero(true);
            });
            JButton botonColocarBarco = new JButton("Colocar Barco");
            botonColocarBarco.addActionListener(e -> {
                if (!colocandoBarco) {
                    JOptionPane.showMessageDialog(null, "Ya colocaste todos los barcos.");
                    return;
                }

                int fila = tableroJugador1.getFilaSeleccionada();
                int columna = tableroJugador1.getColumnaSeleccionada();

                if (fila == -1 || columna == -1) {
                    JOptionPane.showMessageDialog(null, "Selecciona una casilla primero.");
                    return;
                }

                String[] opciones = {"Horizontal", "Vertical"};
                int orientacion = JOptionPane.showOptionDialog(null, "¿En qué orientación quieres poner tu barco?",
                        "Colocar Barco", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);
                if (orientacion == -1) return;

                boolean horizontal = orientacion == 0;
                Barco barco = new Barco(tamañobarcos[barcoActual]);
                barco.colocarEn(columna, fila, horizontal, 10);

                if (tableroLogico.colocarBarco(barco)) {
                    tableroVisual.actualizarTableroVisual(tableroLogico);
                    barcoActual++;

                    if (barcoActual >= tamañobarcos.length) {
                        colocandoBarco = false;
                        JOptionPane.showMessageDialog(null, "Todos los barcos han sido colocados.");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "No se pudo colocar el barco.");
                }

                tableroJugador1.limpiarSeleccion();  // Opcional: para limpiar el cuadro seleccionado
            });
            panelBotones.add(botonDisparar);
            panelBotones.add(botonTerminar);
            panelBotones.add(botonMostrar);
            panelBotones.add(botonColocarBarco);

            tableroJugador2.getPanel().setVisible(false);


            // Agregamos los frames aqui
            frame.add(panelTableros, BorderLayout.CENTER);
            frame.add(panelBotones, BorderLayout.SOUTH);
            frame.setVisible(true);

            tableroJugador2.actualizarTableroVisual(tableroLogico);
            tableroJugador1.actualizarTableroVisual(tableroLogico);
        });
    }

    private void colocarBarcos(int fila, int columna) {
        if (!colocandoBarco){
            return;
        }
        if (barcoActual >= tamañobarcos.length) {
            JOptionPane.showMessageDialog(null, "Todos los barcos han sido colocados.");
            colocandoBarco = false;
            return;
        }
        String opciones[] = {"Horizontal", "Vertical"};
        int orientacion = JOptionPane.showOptionDialog(null, "¿En que orientación quieres poner tu barco?",
                "Colocar Barco", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);
        if (orientacion == -1) {
            return;
        }

        boolean horizontal = orientacion == 0;
        Barco barco = new Barco(tamañobarcos[barcoActual]);
        barco.colocarEn(columna, fila, horizontal, 10);

        if (tableroLogico.colocarBarco(barco)) {
            tableroVisual.actualizarTableroVisual(tableroLogico);
            barcoActual++;
            if (barcoActual >= tamañobarcos.length) {
                colocandoBarco = false;
                JOptionPane.showMessageDialog(null, "Todos los barcos han sido colocados.");
            }
        } else {
            JOptionPane.showMessageDialog(null, "No se pudo colocar el barco!!");
        }
    }


    public void imprimirTableroTerminal(Tablero logico) {
        for (int fila = 0; fila < 10; fila++) {
            for (int col = 0; col < 10; col++) {
                String estado = logico.getCasilla(fila, col);
                switch (estado) {
                    case "A": System.out.print("🌊 "); break;
                    case "B": System.out.print("🚢 "); break;
                    case "X": System.out.print("💥 "); break;
                    case "O": System.out.print("❌ "); break;
                }
            }
            System.out.println();
        }
    }
}