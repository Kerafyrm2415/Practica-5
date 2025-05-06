package GUI;

import Terminal.*;

import javax.swing.*;
import javax.swing.border.Border;
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
    private String nombreJugador1;
    private String nombreJugador2;
    private JLabel labelTurno;


    public GUIJuego(String nombreJugador1, String nombreJugador2) {
        this.nombreJugador1 = nombreJugador1;
        this.nombreJugador2 = nombreJugador2;

        tableroJugador1Logico = new Tablero();
        tableroJugador2Logico = new Tablero();
        frame = new JFrame("Tablero de BattleShip");
        tableroJugador1 = new GUITablero(10, 10, tableroJugador1Logico);
        tableroJugador2 = new GUITablero(10, 10, tableroJugador2Logico);
    }

    public void iniciarInterfaz() {
        SwingUtilities.invokeLater(() -> {
            disparoRealizado = false;
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(600, 400);
            JPanel contenedor = new JPanel();
            contenedor.setLayout(new BoxLayout(contenedor, BoxLayout.Y_AXIS));
            labelTurno = new JLabel("Turno de: " + nombreJugador1, SwingConstants.CENTER);
            labelTurno.setFont(new Font("Arial", Font.BOLD, 16));
            labelTurno.setAlignmentX(Component.CENTER_ALIGNMENT);
            contenedor.add(labelTurno);


            // Panel central: Donde están los dos tableros
            JPanel panelTableros = new JPanel();
            panelTableros.setBorder(BorderFactory.createEmptyBorder());
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
                        verificarGanador();
                        if (verificarGanador() == true){
                            return;
                        } else {

                            JOptionPane.showMessageDialog(null, "¡Impacto! Puedes volver a disparar.");
                            // No cambiamos de turno aquí
                            tableroVisual.limpiarSeleccion();
                            return;
                        }

                    }
                    String estado = tableroEnemigo.getCasilla(fila, columna);

                    if (estado.equals("💥")) {
                        JOptionPane.showMessageDialog(null, "¡Impacto!");
                        esTurnoExtra = true;
                    } else if (estado.equals("❌")) {
                        JOptionPane.showMessageDialog(null, "Agua.");
                        esTurnoExtra = false;
                    }
                    disparoRealizado = true;
                    tableroVisualEnemigo.actualizarTableroVisual(tableroEnemigo);
                    tableroVisual.limpiarSeleccion(); // limpia selección del jugador actual
                    cambiarTurno(esTurnoExtra);
                }
            });
            JButton botonImprimir = new JButton("Imprimir Terminal");
            botonImprimir.addActionListener(e -> {
                imprimirTableroTerminal(tableroJugador1Logico);
                imprimirTableroTerminal(tableroJugador2Logico);
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
            panelBotones.add(botonImprimir);


            // Agregamos los frames aqui
            frame.add(contenedor, BorderLayout.NORTH);
            frame.add(panelTableros, BorderLayout.CENTER);
            frame.add(panelBotones, BorderLayout.SOUTH);
            frame.setVisible(true);

            tableroJugador1.actualizarTableroVisual(tableroJugador1Logico);
            tableroJugador2.actualizarTableroVisual(tableroJugador2Logico);
        });
    }

    public void cambiarTurno(boolean esTurnoExtra) {
        // Solo cambiamos de turno si no es un turno extra
        if (!esTurnoExtra) {
            turnoJugador1 = !turnoJugador1;
        }

        // Solo verificamos ganador si ambos jugadores han terminado de colocar barcos
        if (!colocandoBarcosJugador1 && !colocandoBarcosJugador2) {
            verificarGanador();
        }

        tableroVisual = turnoJugador1 ? tableroJugador1 : tableroJugador2;

        tableroJugador1.getPanel().setVisible(turnoJugador1);
        tableroJugador2.getPanel().setVisible(!turnoJugador1);

        tableroVisual.getPanel().revalidate();
        tableroVisual.getPanel().repaint();

        String nombreTurno = turnoJugador1 ? nombreJugador1 : nombreJugador2;
        labelTurno.setText("Turno de: " + nombreTurno);

        // Solo mostramos el mensaje de cambio de turno si no estamos colocando barcos
        if (!colocandoBarcosJugador1 && !colocandoBarcosJugador2) {
            JOptionPane.showMessageDialog(null, "Turno de " + nombreTurno);
        }
    }

    private boolean verificarGanador() {
        boolean huboGanador = false;
        // Verificar si el jugador 2 ha perdido (todos sus barcos hundidos)
        if (tableroJugador2Logico.todosBarcosHundidos()) {
            JOptionPane.showMessageDialog(frame,
                    "¡Felicidades " + nombreJugador1 + "! ¡Has hundido todos los barcos de " + nombreJugador2 + "!",
                    "¡Juego Terminado!",
                    JOptionPane.INFORMATION_MESSAGE);
            frame.dispose();
            return !huboGanador;
        }
        // Verificar si el jugador 1 ha perdido
        else if (tableroJugador1Logico.todosBarcosHundidos()) {
            JOptionPane.showMessageDialog(frame,
                    "¡Felicidades " + nombreJugador2 + "! ¡Has hundido todos los barcos de " + nombreJugador1 + "!",
                    "¡Juego Terminado!",
                    JOptionPane.INFORMATION_MESSAGE);
            frame.dispose();
            return !huboGanador;
        }
        return huboGanador;
    }

    public void imprimirTableroTerminal(Tablero logico) {
        System.out.println("   A B C D E F G H I J");
        for (int fila = 0; fila < 10; fila++) {
            System.out.printf("%2d ", fila + 1);
            for (int col = 0; col < 10; col++) {
                String estado = logico.getCasilla(fila, col);
                switch (estado) {
                    case "🌊": System.out.print("🌊 "); break;
                    case "🚢": System.out.print("🚢 "); break;
                    case "💥": System.out.print("💥 "); break;
                    case "❌": System.out.print("❌ "); break;
                    case "🔥": System.out.print("🔥 "); break;
                    default: System.out.print("? "); break;
                }
            }
            System.out.println();
        }
        System.out.println();
    }
}