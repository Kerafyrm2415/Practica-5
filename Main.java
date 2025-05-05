import java.util.Scanner;
import Terminal.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int opcion;
        System.out.println("  ______     ______     ______   ______   __         ______     ______     __  __     __     ______  \n" +
                "/\\  == \\   /\\  __ \\   /\\__  _\\ /\\__  _\\ /\\ \\       /\\  ___\\   /\\  ___\\   /\\ \\_\\ \\   /\\ \\   /\\  == \\ \n" +
                "\\ \\  __<   \\ \\  __ \\  \\/_/\\ \\/ \\/_/\\ \\/ \\ \\ \\____  \\ \\  __\\   \\ \\___  \\  \\ \\  __ \\  \\ \\ \\  \\ \\  _-/ \n" +
                " \\ \\_____\\  \\ \\_\\ \\_\\    \\ \\_\\    \\ \\_\\  \\ \\_____\\  \\ \\_____\\  \\/\\_____\\  \\ \\_\\ \\_\\  \\ \\_\\  \\ \\_\\   \n" +
                "  \\/_____/   \\/_/\\/_/     \\/_/     \\/_/   \\/_____/   \\/_____/   \\/_____/   \\/_/\\/_/   \\/_/   \\/_/   \n" +
                "                                                                                                    \n\n\n practica realizada por: Derek Ramón Garzón Vizcarra");
        System.out.println("Seleccione una opción: \n 1.- Jugar \n 2.- Salir\n");
        opcion = sc.nextInt();
        while (opcion != 1 && opcion != 2) {
            System.out.println("Opción no valida: Escriba el numero de una de las dos opciones");
            opcion = sc.nextInt();
        }
        if (opcion == 1) {
            System.out.println("Ingrese el nombre del jugador 1");
            String nombreJ1 = sc.nextLine();

            System.out.println("Ingrese el nombre del jugador 2");
            String nombreJ2 = sc.nextLine();

            Juego juego = new Juego(nombreJ1, nombreJ2);

            colocarBarcos(sc, juego.getJugador1(), nombreJ1);
            colocarBarcos(sc, juego.getJugador2(), nombreJ2);

            faseDeDisparos(sc, juego);

            sc.close();
        } else {
            System.out.println("Saliendo del juego...");
        }
    }
    private static void colocarBarcos(Scanner scanner, Jugador jugador, String nombre) {
        System.out.println("\n" + nombre + ", es tu turno de colocar barcos");

        int[] tamaniosBarcos = {5, 4, 3, 3, 2}; // Ejemplo: Portaaviones(5), Acorazado(4), etc.
        String[] nombresBarcos = {"Portaaviones", "Acorazado", "Submarino", "Destructor", "Fragata"};

        for (int i = 0; i < tamaniosBarcos.length; i++) {
            while (true) {
                System.out.println("\nColoca tu " + nombresBarcos[i] + " (tamaño " + tamaniosBarcos[i] + ")");
                jugador.getTableroPropio().imprimirTablero(true); // Mostrar barcos

                System.out.print("Coordenada inicial (ej. A1): ");
                String coordenada = scanner.next().toUpperCase();
                System.out.print("Orientación (H)orizontal o (V)ertical: ");
                char orientacion = scanner.next().toUpperCase().charAt(0);

                int x = coordenada.charAt(0) - 'A';
                int y = Integer.parseInt(coordenada.substring(1)) - 1;

                Barco barco = new Barco(tamaniosBarcos[i], nombresBarcos[i]);
                barco.colocarEn(x, y, orientacion == 'H', 10);

                if (jugador.agregarBarco(barco)) {
                    break;
                } else {
                    System.out.println("¡Posición inválida! Intenta nuevamente.");
                }
            }
        }
    }
    private static void faseDeDisparos(Scanner scanner, Juego juego) {
        System.out.println("\n¡COMIENZA LA BATALLA!");

        while (!juego.isJuegoTerminado()) {
            Jugador jugadorActual = juego.getJugadorActual();
            Jugador oponente = (jugadorActual == juego.getJugador1()) ?
                    juego.getJugador2() : juego.getJugador1();

            System.out.println("\n" + jugadorActual.getNombre() + ", es tu turno");
            System.out.println("Tu tablero:");
            jugadorActual.getTableroPropio().imprimirTablero(true);

            System.out.println("\nTablero de disparos:");
            jugadorActual.getTableroRival().imprimirTablero(false);

            System.out.print("Coordenada para disparar (ej. B5): ");
            String coordenada = scanner.next().toUpperCase();
            int x = coordenada.charAt(0) - 'A';
            int y = Integer.parseInt(coordenada.substring(1)) - 1;

            boolean impacto = juego.realizarDisparo(x, y);

            if (impacto) {
                System.out.println("¡IMPACTO!");
                if (oponente.haPerdido()) {
                    System.out.println("\n¡" + jugadorActual.getNombre() + " ha ganado la partida!");
                }
            } else {
                System.out.println("Agua...");
            }
        }
    }
}