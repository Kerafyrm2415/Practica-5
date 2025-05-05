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
            nombrarJugadores(sc);
        } else {
            System.out.println("Saliendo del juego...");
        }
        sc.close();
    }

    public static void nombrarJugadores(Scanner sc) {
        System.out.print("Nombre del Jugador 1: ");
        String nombreJ1 = sc.nextLine();

        System.out.print("Nombre del Jugador 2: ");
        String nombreJ2 = sc.nextLine();

        Juego juego = new Juego(nombreJ1, nombreJ2);
        juego.iniciarJuego(sc);
    }
}