package GUI;

import Terminal.*;

import javax.swing.*;
import java.awt.*;

public class GUIJuego {
    private static int[] tamañoBarcos = {5, 4, 3, 3, 2};
    private static int barcoActual = 0;
    private static boolean modoColocacion = true;
    private static JLabel etiquetaTurno;
    private static Tablero tableroLogico = new Tablero();
    private static GUITablero tableroVisual;


    public static void main(String[] args) {
        //Colocacioón de barcos
        tableroLogico = new Tablero(); // usa el global ya existente
        tableroVisual = new GUITablero(10, 10, GUIJuego::casillaSeleccionada);
        //Mi tablero donde tengo todo organizado
        etiquetaTurno = new JLabel();
        SwingUtilities.invokeLater(() -> {
            String nombreJugador = JOptionPane.showInputDialog("Nombre del jugador:");
            if (nombreJugador == null || nombreJugador.isEmpty()) nombreJugador = "Jugador";

            JFrame frame = new JFrame("Tablero de BattleShip");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 700);
            frame.setLayout(new BorderLayout());

            JLabel etiquetaTurno = new JLabel("Coloca tus barcos, " + nombreJugador, SwingConstants.CENTER);
            etiquetaTurno.setFont(new Font("Arial", Font.BOLD, 18));
            frame.add(etiquetaTurno, BorderLayout.NORTH);

            tableroVisual = new GUITablero(10, 10, (fila, columna) -> casillaSeleccionada(fila, columna));
            frame.add(tableroVisual.getPanel(), BorderLayout.CENTER);


            // Panel central: Donde están los dos tableros
            JPanel panelTableros = new JPanel();
            GUITablero tableroJugador1 = new GUITablero(10, 10, tableroLogico);
            GUITablero tableroJugador2 = new GUITablero(10, 10, tableroLogico);

            panelTableros.setBorder(BorderFactory.createTitledBorder("Tablero"));
            panelTableros.add(tableroJugador1.getPanel());
            panelTableros.add(tableroJugador2.getPanel());

            // Panel inferior con botones
            JPanel panelBotones = new JPanel(new FlowLayout());
            JButton botonDisparar = new JButton("Disparar");
            JButton botonTerminar = new JButton("Terminar turno");
            JButton botonMostrar = new JButton("Revelar Tablero en terminal");


            botonDisparar.addActionListener(e -> {
                if (modoColocacion) {
                    JOptionPane.showMessageDialog(null, "¡Termina de colocar los barcos primero!");
                    return;
                }

                int fila = tableroJugador1.getFilaSeleccionada();
                int columna = tableroJugador1.getColumnaSeleccionada();

                if (fila == -1 || columna == -1) {
                    JOptionPane.showMessageDialog(null, "Selecciona una casilla primero");
                } else if (fila == -2 && columna == -2) {
                    JOptionPane.showMessageDialog(null, "Ya disparaste, termina tu turno.");
                } else {

                    boolean acierto = tableroLogico.recibirTiro(columna, fila);
                    String estado = tableroLogico.getCasilla(fila, columna);
                    String mensaje = "Disparo en " + (char) ('A' + columna) + (fila + 1) + ": ";

                    if (estado.equals("💥")) {
                        mensaje += "¡Impacto! 💥";
                    } else if (estado.equals("❌")) {
                        mensaje += "Agua ❌";
                    }
                    JOptionPane.showMessageDialog(null, mensaje);
                    tableroVisual.actualizarTableroVisual(tableroLogico);
                    tableroVisual.limpiarSeleccion();
                }
            });
            botonTerminar.addActionListener(e -> {
                int fila = tableroJugador1.getFilaSeleccionada();
                int columna = tableroJugador1.getColumnaSeleccionada();
                if (fila == -1 || columna == -1) {
                    JOptionPane.showMessageDialog(null, "Selecciona una casilla primero");
                    return;
                }
            });
            botonMostrar.addActionListener(e -> {
                tableroLogico.imprimirTablero(true);
            });
            panelBotones.add(botonDisparar);
            panelBotones.add(botonTerminar);
            panelBotones.add(botonMostrar);

            // Agregamos los frames aqui
            frame.add(panelTableros, BorderLayout.CENTER);
            frame.add(panelBotones, BorderLayout.SOUTH);
            frame.setVisible(true);

            tableroJugador2.actualizarTableroVisual(tableroLogico);

            tableroLogico.recibirTiro(0, 0);
            tableroJugador1.actualizarTableroVisual(tableroLogico);
            tableroLogico.imprimirTablero(true);
        });
    }

    private static void casillaSeleccionada(int fila, int columna) {
        if (!modoColocacion) {
            return;
        } else if (barcoActual >= tamañoBarcos.length) {
            JOptionPane.showMessageDialog(null, "Todos los barcos han sido colocados.");
            modoColocacion = false;
            etiquetaTurno.setText("comienza la batalla!");
            return;
        }
        String opciones[] = {"Horizontal", "Vertical"};
        int orientacion = JOptionPane.showOptionDialog(null, "¿En que orientación quieres poner tu barco?",
                "Colocar Barco", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);
        if (orientacion == -1) {
            return;
        }

        boolean horizontal = (orientacion == 0);
        Barco barco = new Barco(tamañoBarcos[barcoActual]);
        barco.colocarEn(columna, fila, horizontal, 10);

        if (tableroLogico.colocarBarco(barco)) {
            tableroVisual.actualizarTableroVisual(tableroLogico);
            barcoActual++;
            if (barcoActual < tamañoBarcos.length) {
                etiquetaTurno.setText("Coloca el barco de tamaño " + tamañoBarcos[barcoActual]);
            } else {
                etiquetaTurno.setText("¡Comienza la batalla!");
                modoColocacion = false;
            }
        } else {
            JOptionPane.showMessageDialog(null, "No se pudo colocar el barco!!");
        }
    }
}