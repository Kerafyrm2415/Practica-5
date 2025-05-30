package GUI;

import Terminal.*;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.Random;

public class GUIJuego {
    private final int[] tamañosBarcosJugador1 = {5, 4, 3, 3, 2, 2}; // tamaño de los barcos para pruebas: {5, 4, 3, 3, 2, 2} y {1}
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
    private boolean vsCPU = false;


    public GUIJuego(String nombreJugador1, String nombreJugador2) {
        this.nombreJugador1 = nombreJugador1;
        this.nombreJugador2 = nombreJugador2;

        tableroJugador1Logico = new Tablero();
        tableroJugador2Logico = new Tablero();
        frame = new JFrame("Tablero de BattleShip");
        tableroJugador1 = new GUITablero(10, 10, tableroJugador1Logico);
        tableroJugador2 = new GUITablero(10, 10, tableroJugador2Logico);
    }

    public GUIJuego(String nombreJugador1) {
        this(nombreJugador1, "CPU");
        this.vsCPU = true;

        colocarBarcosAleatorios(tableroJugador2Logico, tamañosBarcosJugador2);
    }

    public void iniciarInterfaz() {
        SwingUtilities.invokeLater(() -> {
            disparoRealizado = false;
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(700, 400);
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
                    if (estadoActual.equals("💥") || estadoActual.equals("❌") || estadoActual.equals("🔥")) {
                        JOptionPane.showMessageDialog(null, "Ya disparaste a esta casilla. Intenta con otra.");
                        esTurnoExtra = true;
                        return;
                    }

                    boolean acierto = tableroEnemigo.recibirTiro(columna, fila);
                    if (acierto) {
                        esTurnoExtra = true;
                        boolean hayGanador = verificarGanador();
                        if (hayGanador){
                            return;
                        } else {
                            JOptionPane.showMessageDialog(null, "¡Impacto! Puedes volver a disparar.");
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
                if (turnoJugador1) {
                    System.out.println("====== TABLERO DE " + nombreJugador1 + " ======");
                    imprimirTableroTerminal(tableroJugador1Logico);
                } else {
                    System.out.println("====== TABLERO DE " + nombreJugador2 + " ======");
                    imprimirTableroTerminal(tableroJugador2Logico);
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
                // Verificamos que no se salga del tablero
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
                            JOptionPane.showMessageDialog(null, nombreJugador1 + " ya colocó todos sus barcos.");
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
                if (vsCPU) {
                    // Si es modo CPU, deshabilitamos el botón de colocar barcos para el jugador 2
                    if (!turnoJugador1) {
                        botonColocarBarco.setEnabled(false);
                    }
                }

                tableroVisual.actualizarTableroVisual(tableroLogico);
                tableroVisual.limpiarSeleccion();
            });
            JButton botonGuardar = new JButton("Guardar Partida");
            botonGuardar.addActionListener(e -> {
                guardarPartida("savefile" + ".txt");
            });
            JButton botonCargar = new JButton("Cargar Partida");
            botonCargar.addActionListener(e -> {
                GUIJuego juegoCargado = cargarPartida("savefile" + ".txt");
                if (juegoCargado != null) {
                    frame.dispose(); // Cierra la ventana actual
                    juegoCargado.iniciarInterfaz(); // Abre la partida cargada
                    JOptionPane.showMessageDialog(null, "¡Partida cargada exitosamente!");
                }
            });

            panelBotones.add(botonDisparar);
            panelBotones.add(botonColocarBarco);
            panelBotones.add(botonImprimir);
            panelBotones.add(botonGuardar);
            panelBotones.add(botonCargar);


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
        // Si es el turno de la CPU, ejecutamos sus acciones automáticas
        if (vsCPU && !turnoJugador1) {
            ejecutarTurnoCPU();
        }
    }

    private boolean verificarGanador() {
        boolean huboGanador = false;
        // Verificamos si el jugador 2 ha perdido (si todos sus barcos fueron hundidos)
        if (tableroJugador2Logico.todosBarcosHundidos()) {
            JOptionPane.showMessageDialog(frame,
                    "¡Felicidades " + nombreJugador1 + "! ¡Has hundido todos los barcos de " + nombreJugador2 + "!",
                    "¡Juego Terminado!",
                    JOptionPane.INFORMATION_MESSAGE);
            frame.dispose();
            return !huboGanador;
        }
        // En caso de que no, se verifica ahora si el jugador 1 perdio
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

    private void colocarBarcosAleatorios(Tablero tablero, int[] tamañosBarcos) {
        Random random = new Random();
        for (int tamaño : tamañosBarcos) {
            boolean colocado = false;
            int intentos = 0;

            while (!colocado && intentos < 100) { // Para evitar un bucle infinito, marcara un error si despues de 100 intentos se siguiera colocando mal
                intentos++;

                int fila = random.nextInt(10);
                int columna = random.nextInt(10);
                boolean horizontal = random.nextBoolean();

                // Verificar que el barco no se salga del tablero
                if (horizontal && columna + tamaño > 10) continue;
                if (!horizontal && fila + tamaño > 10) continue;

                Barco barco = new Barco(tamaño);
                barco.colocarEn(columna, fila, horizontal, 10);

                if (tablero.colocarBarco(barco)) {
                    colocado = true;
                }
            }

            if (!colocado) {
                System.err.println("No se pudo colocar barco de tamaño " + tamaño + " después de " + 100 + " intentos");
            }
        }
        colocandoBarcosJugador2 = false;
    }

    private void disparoAleatorioCPU() {
        Random random = new Random();
        int fila, columna;
        String estado;

        // Buscamos una casilla no disparada
        do {
            fila = random.nextInt(10);  // 0-9
            columna = random.nextInt(10); // 0-9
            estado = tableroJugador1Logico.getCasilla(fila, columna);
        } while (estado.equals("💥") || estado.equals("❌") || estado.equals("🔥")); // Repetir si ya fue disparada

        // Realizamos el disparo
        boolean acierto = tableroJugador1Logico.recibirTiro(columna, fila);
        tableroJugador1.actualizarTableroVisual(tableroJugador1Logico);

        // Mostramos mensaje
        String coordenada = (char)('A' + columna) + "" + (fila + 1);
        if (acierto) {
            JOptionPane.showMessageDialog(frame, "CPU disparó a " + coordenada + " ¡y acertó!", "Turno de CPU", JOptionPane.INFORMATION_MESSAGE);
            cambiarTurno(true); // Cambiar turno después del disparo
            esTurnoExtra = false;
        } else {
            JOptionPane.showMessageDialog(frame, "CPU disparó a " + coordenada + " ¡y falló!", "Turno de CPU", JOptionPane.INFORMATION_MESSAGE);
            cambiarTurno(false); // Cambiar turno después del disparo
        }
    }

    private void ejecutarTurnoCPU() {
        // Pequeño retraso para mejor experiencia (opcional)
        Timer timer = new Timer(1000, e -> {
            disparoAleatorioCPU();
        });
        timer.setRepeats(false);
        timer.start();
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

    public void guardarPartida(String nombreArchivo) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(nombreArchivo))) {
            // Guardar información básica
            writer.println("Nombres:");
            writer.println(nombreJugador1);
            writer.println(nombreJugador2);
            writer.println("vsCPU:" + vsCPU);

            // Guardar estado del juego
            writer.println("Estado:");
            writer.println("turnoJugador1:" + turnoJugador1);
            writer.println("esTurnoExtra:" + esTurnoExtra);
            writer.println("colocandoBarcosJugador1:" + colocandoBarcosJugador1);
            writer.println("colocandoBarcosJugador2:" + colocandoBarcosJugador2);
            writer.println("barcoActualJugador1:" + barcoActualJugador1);
            writer.println("barcoActualJugador2:" + barcoActualJugador2);

            // Guardar tableros
            writer.println("MostrarBarcos:true"); // Forzar a mostrar barcos al guardar
            guardarTablero(writer, "TableroJugador1", tableroJugador1Logico, true);
            guardarTablero(writer, "TableroJugador2", tableroJugador2Logico, true);

            JOptionPane.showMessageDialog(frame, "Partida guardada correctamente en " + nombreArchivo);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Error al guardar la partida: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void guardarTablero(PrintWriter writer, String nombreTablero, Tablero tablero, boolean mostrarBarcos) {
        writer.println(nombreTablero + ":");
        for (int fila = 0; fila < 10; fila++) {
            for (int col = 0; col < 10; col++) {
                String estado = tablero.getCasilla(fila, col);
                // Guardar siempre el estado real, mostrando barcos
                writer.print(estado + " ");
            }
            writer.println();
        }
    }

    public static GUIJuego cargarPartida(String nombreArchivo) {
        try (BufferedReader reader = new BufferedReader(new FileReader(nombreArchivo))) {
            String linea;
            String nombreJugador1 = "", nombreJugador2 = "";
            boolean vsCPU = false;
            boolean turnoJugador1 = true;
            boolean esTurnoExtra = false;
            boolean colocandoBarcosJugador1 = false, colocandoBarcosJugador2 = false;
            int barcoActualJugador1 = 0, barcoActualJugador2 = 0;
            Tablero tableroJ1 = new Tablero();
            Tablero tableroJ2 = new Tablero();

            while ((linea = reader.readLine()) != null) {
                if (linea.equals("Nombres:")) {
                    nombreJugador1 = reader.readLine();
                    nombreJugador2 = reader.readLine();
                    vsCPU = Boolean.parseBoolean(reader.readLine().split(":")[1]);
                } else if (linea.equals("Estado:")) {
                    turnoJugador1 = Boolean.parseBoolean(reader.readLine().split(":")[1]);
                    esTurnoExtra = Boolean.parseBoolean(reader.readLine().split(":")[1]);
                    colocandoBarcosJugador1 = Boolean.parseBoolean(reader.readLine().split(":")[1]);
                    colocandoBarcosJugador2 = Boolean.parseBoolean(reader.readLine().split(":")[1]);
                    barcoActualJugador1 = Integer.parseInt(reader.readLine().split(":")[1]);
                    barcoActualJugador2 = Integer.parseInt(reader.readLine().split(":")[1]);
                } else if (linea.equals("TableroJugador1:")) {
                    cargarTablero(reader, tableroJ1);
                } else if (linea.equals("TableroJugador2:")) {
                    cargarTablero(reader, tableroJ2);
                }
            }

            GUIJuego juego = new GUIJuego(nombreJugador1, nombreJugador2);
            juego.vsCPU = vsCPU;
            // Mostrar advertencia solo para modo dos jugadores
            if (vsCPU) {
                juego = new GUIJuego(nombreJugador1); // Usar constructor para CPU
            } else {
                int respuesta = JOptionPane.showConfirmDialog(
                        null,
                        "AVISO: La función de cargado de partida en el modo de dos jugadores es experimental.\n"
                                + "Puede haber errores inesperados al disparar o en el estado de los barcos.\n\n"
                                + "¿Deseas continuar de todos modos?",
                        "Confirmación de carga",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                );

                if (respuesta != JOptionPane.YES_OPTION) {
                    return null; // El usuario canceló la carga
                } else {
                    juego = new GUIJuego(nombreJugador1, nombreJugador2);
                }
            }
            juego.tableroJugador1Logico = tableroJ1;
            juego.tableroJugador2Logico = tableroJ2;

            juego.tableroJugador1 = new GUITablero(10, 10, tableroJ1);
            juego.tableroJugador2 = new GUITablero(10, 10, tableroJ2);


            juego.turnoJugador1 = turnoJugador1;
            juego.esTurnoExtra = esTurnoExtra;
            juego.colocandoBarcosJugador1 = colocandoBarcosJugador1;
            juego.colocandoBarcosJugador2 = colocandoBarcosJugador2;
            juego.barcoActualJugador1 = barcoActualJugador1;
            juego.barcoActualJugador2 = barcoActualJugador2;
            juego.tableroJugador1.actualizarTableroVisual(tableroJ1);
            juego.tableroJugador2.actualizarTableroVisual(tableroJ2);

            return juego;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al cargar la partida: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    private static void cargarTablero(BufferedReader reader, Tablero tablero) throws IOException {
        for (int fila = 0; fila < 10; fila++) {
            String[] casillas = reader.readLine().trim().split(" ");
            for (int col = 0; col < 10; col++) {
                tablero.actualizarCasilla(fila, col, casillas[col]);
            }
        }
        tablero.reconstruirBarcosDesdeGuardado(); // Reconstruye la lista de barcos
    }
}