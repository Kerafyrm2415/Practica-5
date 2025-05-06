package GUI;

import Terminal.*;

import javax.swing.*;
import java.awt.*;

public class GUIJuego {
    private final int[] tamañosBarcosJugador1 = {5, 4, 3, 3, 2, 2};
    private final int[] tamañosBarcosJugador2 = {5, 4, 3, 3, 2, 2};
    private int barcoActualJugador1 = 0;
    private int barcoActualJugador2 = 0;
    private boolean colocandoBarcosJugador1 = true;
    private boolean colocandoBarcosJugador2 = true;
    private boolean disparoRealizado = false;
    private Tablero tableroJugador1Logico;
    private Tablero tableroJugador2Logico;
    private GUITablero tableroJugador1;
    private GUITablero tableroJugador2;
    private JFrame frame;
    private GUITablero tableroVisual;
    private boolean esTurnoExtra = false;
    private boolean turnoJugador1 = true;


    public GUIJuego() {
        tableroJugador1Logico = new Tablero();
        tableroJugador2Logico = new Tablero();
        frame = new JFrame("Tablero de BattleShip");
        tableroJugador1 = new GUITablero(10, 10, tableroJugador1Logico);
        tableroJugador2 = new GUITablero(10, 10, tableroJugador2Logico);
        iniciarInterfaz();
    }

    public void iniciarInterfaz() {
        SwingUtilities.invokeLater(() -> {
            disparoRealizado = false;
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 800);
            JPanel contenedor = new JPanel();
            contenedor.setLayout(new BoxLayout(contenedor, BoxLayout.Y_AXIS));

            JLabel labelDisparos = new JLabel("Tablero de disparos", SwingConstants.CENTER);
            labelDisparos.setAlignmentX(Component.CENTER_ALIGNMENT);

            JLabel labelPropio = new JLabel("Tu tablero", SwingConstants.CENTER);
            labelPropio.setAlignmentX(Component.CENTER_ALIGNMENT);


            // Panel central: Donde están los dos tableros
            JPanel panelTableros = new JPanel();
            panelTableros.setBorder(BorderFactory.createTitledBorder("Tableros"));
            panelTableros.add(tableroJugador1.getPanel());
            panelTableros.add(tableroJugador2.getPanel());

            tableroJugador2.getPanel().setVisible(false);

            tableroVisual = tableroJugador1;

            // Panel Inferior: Donde estan los botones para las acciones
            JPanel panelBotones = new JPanel(new FlowLayout());
            JButton botonDisparar = new JButton("Disparar");
            botonDisparar.addActionListener(e -> {
                if (colocandoBarcosJugador1 || colocandoBarcosJugador2) {
                    JOptionPane.showMessageDialog(null, "Ambos jugadores deben colocar todos sus barcos antes de disparar.");
                    return;
                }

                GUITablero tableroVisual = turnoJugador1 ? tableroJugador1 : tableroJugador2;
                Tablero tableroEnemigo = turnoJugador1 ? tableroJugador2Logico : tableroJugador1Logico;

                int fila = tableroVisual.getFilaSeleccionada();
                int columna = tableroVisual.getColumnaSeleccionada();

                if (fila == -1 || columna == -1) {
                    JOptionPane.showMessageDialog(null, "Selecciona una casilla primero");
                } else {
                    // Disparamos al tablero del oponente
                    tableroEnemigo = turnoJugador1 ? tableroJugador2Logico : tableroJugador1Logico;
                    GUITablero tableroVisualEnemigo = turnoJugador1 ? tableroJugador2 : tableroJugador1;

                    String estadoActual = tableroEnemigo.getCasilla(fila, columna);
                    if (estadoActual.equals("💥") || estadoActual.equals("❌")) {
                        JOptionPane.showMessageDialog(null, "Ya disparaste a esta casilla. Intenta con otra.");
                        esTurnoExtra = true;
                        return;
                    }

                    boolean acierto = tableroEnemigo.recibirTiro(columna, fila);
                    if (acierto) {
                        esTurnoExtra = true;
                        JOptionPane.showMessageDialog(null, "¡Impacto! Puedes volver a disparar.");
                        // No cambiamos de turno aquí
                        tableroVisual.limpiarSeleccion();
                        return;
                    }
                    String estado = tableroEnemigo.getCasilla(fila, columna);

                    if (estado.equals("💥")) {
                        System.out.println("Disparo en " + (char) ('A' + columna) + (fila + 1) + ": ¡Impacto! 💥");
                        JOptionPane.showMessageDialog(null, "¡Impacto!");
                    } else if (estado.equals("❌")) {
                        System.out.println("Disparo en " + (char) ('A' + columna) + (fila + 1) + ": Agua ❌");
                        JOptionPane.showMessageDialog(null, "Agua.");
                        cambiarTurno(false);
                    }
                    disparoRealizado = true;
                    tableroVisualEnemigo.actualizarTableroVisual(tableroEnemigo);
                    tableroVisual.limpiarSeleccion(); // limpia selección del jugador actual
                }
            });
            JButton botonTerminar = new JButton("Terminar turno");
            botonTerminar.addActionListener(e -> {
                if (colocandoBarcosJugador1 || colocandoBarcosJugador2) {
                    // Solo permitimos cambiar turno si el jugador actual ya colocó todos sus barcos
                    if ((turnoJugador1 && !colocandoBarcosJugador1) || (!turnoJugador1 && !colocandoBarcosJugador2)) {
                        cambiarTurno(false);  // Para que el otro jugador coloque sus barcos
                    } else {
                        JOptionPane.showMessageDialog(null, "Debes colocar todos tus barcos antes de pasar el turno.");
                    }
                } else if (!disparoRealizado) {
                    JOptionPane.showMessageDialog(null, "Debes disparar antes de terminar tu turno.");
                } else if (esTurnoExtra) {
                    JOptionPane.showMessageDialog(null, "Tienes un disparo extra. Usalo para poder cambiar de turno");
                }else {
                    disparoRealizado = false;
                    cambiarTurno(false);  // Ahora sí pasamos el turno normalmente
                }
            });
            JButton botonColocarBarco = new JButton("Colocar Barco");
            botonColocarBarco.addActionListener(e -> {
                GUITablero tableroVisual = turnoJugador1 ? tableroJugador1 : tableroJugador2;
                Tablero tableroLogico = turnoJugador1 ? tableroJugador1Logico : tableroJugador2Logico;

                int fila = tableroVisual.getFilaSeleccionada();
                int columna = tableroVisual.getColumnaSeleccionada();

                if (fila == -1 || columna == -1) {
                    JOptionPane.showMessageDialog(null, "Selecciona una casilla primero.");
                    return;
                }


                int barcoActual = turnoJugador1 ? barcoActualJugador1 : barcoActualJugador2;
                int[] tamaños = turnoJugador1 ? tamañosBarcosJugador1 : tamañosBarcosJugador2;
                boolean colocandoBarco = turnoJugador1 ? colocandoBarcosJugador1 : colocandoBarcosJugador2;
                if (!colocandoBarco) {
                    JOptionPane.showMessageDialog(null, "Ya colocaste todos los barcos.");
                    return;
                }

                String[] opciones = {"Horizontal", "Vertical"};
                int orientacion = JOptionPane.showOptionDialog(null, "¿En qué orientación quieres poner tu barco?",
                        "Colocar Barco", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);
                if (orientacion == -1) return;

                boolean horizontal = orientacion == 0;
                Barco barco = new Barco(tamaños[barcoActual]);
                barco.colocarEn(columna, fila, horizontal, 10);
                int tamaño = tamaños[barcoActual];
// Verificar que no se salga del tablero
                if ((horizontal && columna + tamaño > 10) || (!horizontal && fila + tamaño > 10)) {
                    JOptionPane.showMessageDialog(null, "El barco se sale del tablero.");
                    return;
                }

                if (tableroLogico.colocarBarco(barco)) {
                    tableroVisual.actualizarTableroVisual(tableroLogico);
                    if (turnoJugador1) {
                        barcoActualJugador1++;
                        if (barcoActualJugador1 >= tamañosBarcosJugador1.length) {
                            colocandoBarcosJugador1 = false;
                            JOptionPane.showMessageDialog(null, "Jugador 1 ya colocó todos sus barcos.");
                            cambiarTurno(false);
                        }
                    } else {
                        barcoActualJugador2++;
                        if (barcoActualJugador2 >= tamañosBarcosJugador2.length) {
                            colocandoBarcosJugador2 = false;
                            JOptionPane.showMessageDialog(null, "¡Todos los barcos han sido colocados! ¡Empieza la batalla!");
                            cambiarTurno(false);
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "No se pudo colocar el barco. Revise que en la posición ingresada no esté superpuesto el barco.");
                }

                tableroVisual.actualizarTableroVisual(tableroLogico);
                tableroVisual.limpiarSeleccion();
            });
            panelBotones.add(botonDisparar);
            panelBotones.add(botonColocarBarco);


            // Agregamos los frames aqui
            frame.add(panelTableros, BorderLayout.CENTER);
            frame.add(panelBotones, BorderLayout.SOUTH);
            frame.setVisible(true);

            tableroJugador1.actualizarTableroVisual(tableroJugador1Logico);
            tableroJugador2.actualizarTableroVisual(tableroJugador2Logico);
        });
    }

    public void cambiarTurno(boolean esTurnoExtra) {
        esTurnoExtra = false;
        turnoJugador1 = !turnoJugador1;
        tableroVisual = turnoJugador1 ? tableroJugador1 : tableroJugador2;

        tableroJugador1.getPanel().setVisible(turnoJugador1);
        tableroJugador2.getPanel().setVisible(!turnoJugador1);

        tableroVisual.getPanel().revalidate();
        tableroVisual.getPanel().repaint();

        JOptionPane.showMessageDialog(null, "Turno del " + (turnoJugador1 ? "Jugador 1" : "Jugador 2"));
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