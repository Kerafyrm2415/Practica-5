package Terminal;

import java.awt.*;
import java.util.Scanner;

public class Juego {
    private Jugador jugador1;
    private Jugador jugador2;
    private Jugador turnoActual;
    private boolean juegoTerminado;

    public Juego(String nombreJugador1, String nombreJugador2) {
        this.jugador1 = new Jugador(nombreJugador1);
        this.jugador2 = new Jugador(nombreJugador2);
        this.turnoActual = jugador1;
        this.juegoTerminado = false;
    }

    public void iniciarJuego(Scanner scanner) {
        colocarBarcos(scanner, jugador1);
    }
    public boolean colocarBarco(Jugador jugador, Barco barco) {
        return jugador.agregarBarco(barco);
    }

    public boolean realizarDisparo(int x, int y) {
        if (juegoTerminado) return false;

        Jugador atacante = turnoActual;
        Jugador defensor = (turnoActual == jugador1) ? jugador2 : jugador1;

        boolean impacto = atacante.realizarDisparo(x, y, defensor);

        if (defensor.haPerdido()) {
            juegoTerminado = true;
            System.out.println(atacante.getNombre() + " ha ganado el juego!");
        } else if (!impacto) {
            cambiarTurno();
        }

        return impacto;
    }

    private void cambiarTurno() {
        turnoActual = (turnoActual == jugador1) ? jugador2 : jugador1;
    }


    private void colocarBarcos(Scanner scanner, Jugador jugador) {
        System.out.println("\n" + jugador.getNombre() + ", es tu turno de colocar barcos");

        int[] tamañosBarcos = {5, 4, 3, 3, 2};
        String[] nombresBarcos = {"Portaaviones", "Acorazado", "Submarino", "Destructor", "Fragata"};

        for (int i = 0; i < tamañosBarcos.length; i++) {
            while (true) {
                System.out.println("\nColoca tu " + nombresBarcos[i] + " (tamaño " + tamañosBarcos[i] + ")");
                jugador.getTableroPropio().imprimirTablero(true);

                System.out.print("Coordenada inicial (ej. A1): ");
                String coordenada = scanner.next().toUpperCase();
                System.out.print("Orientación (H)orizontal o (V)ertical: ");
                char orientacion = scanner.next().toUpperCase().charAt(0);

                int x = coordenada.charAt(0) - 'A';
                int y = Integer.parseInt(coordenada.substring(1)) - 1;

                Barco barco = new Barco(tamañosBarcos[i], nombresBarcos[i]);
                barco.colocarEn(x, y, orientacion == 'H', 10);

                if (jugador.agregarBarco(barco)) {
                    break;
                } else {
                    System.out.println("¡Posición inválida! Intenta nuevamente.");
                }
            }
        }
    }

    private void faseDeDisparos(Scanner scanner) {
        System.out.println("\n¡COMIENZA LA BATALLA!");

        while (!juegoTerminado) {
            Jugador atacante = turnoActual;
            Jugador defensor = (turnoActual == jugador1) ? jugador2 : jugador1;

            System.out.println("\n" + atacante.getNombre() + ", es tu turno");
            mostrarTableros(atacante);

            Point coordenada = obtenerCoordenadaDisparo(scanner, atacante);
            boolean impacto = realizarDisparo(coordenada.x, coordenada.y);

            if (impacto && defensor.haPerdido()) {
                juegoTerminado = true;
            }
        }
    }

    private void mostrarTableros(Jugador jugador) {
        System.out.println("Tu tablero:");
        jugador.getTableroPropio().imprimirTablero(true);

        System.out.println("\nTablero de disparos:");
        jugador.getTableroRival().imprimirTablero(false);
    }

    private Point obtenerCoordenadaDisparo(Scanner scanner, Jugador jugador) {
        while (true) {
            System.out.print("Coordenada para disparar (ej. B5): ");
            String input = scanner.next().toUpperCase();

            try {
                int x = input.charAt(0) - 'A';
                int y = Integer.parseInt(input.substring(1)) - 1;

                if (x >= 0 && x < 10 && y >= 0 && y < 10) {
                    return new Point(x, y);
                } else {
                    System.out.println("Coordenadas fuera de rango. Usa letras A-J y números 1-10.");
                }
            } catch (Exception e) {
                System.out.println("Formato inválido. Usa formato como A1 o J10.");
            }
        }
    }

    private void mostrarGanador() {
        Jugador ganador = jugador1.haPerdido() ? jugador2 : jugador1;
        System.out.println("\n¡FELICIDADES " + ganador.getNombre() + "! ¡HAS GANADO!");
        System.out.println("Barcos hundidos:");
        System.out.println("- " + jugador1.getNombre() + ": " + jugador1.getBarcosHundidos());
        System.out.println("- " + jugador2.getNombre() + ": " + jugador2.getBarcosHundidos());
    }

    public Jugador getJugadorActual() {
        return turnoActual;
    }

    public boolean isJuegoTerminado() {
        return juegoTerminado;
    }

    // Getters para los jugadores
    public Jugador getJugador1() {
        return jugador1;
    }

    public Jugador getJugador2() {
        return jugador2;
    }
}