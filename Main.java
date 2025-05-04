import java.util.Scanner;
import Terminal.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int opcion;
        System.out.println(" BATTLESHIP¨\n\n\n practica realizada por: Derek Ramón Garzón Vizcarra");
        System.out.println("Seleccione una opción: \n 1.- Jugar \n 2.- Salir\n");
        opcion = sc.nextInt();
        while (opcion != 1 && opcion != 2) {
            System.out.println("Opción no valida: Escriba el numero de una de las dos opciones");
            opcion = sc.nextInt();
        }
        if (opcion == 1) {
            Tablero tablero = new Tablero();

// Simular un barco manual (3 posiciones en A1, B1, C1)
            Barco barco = new Barco(3);
            barco.colocarEn(0, 0, true, 10);  // De A1 a C1
            boolean colocado = tablero.colocarBarco(barco);
            System.out.println("¿Barco colocado?: " + colocado);

            tablero.imprimirTablero();

            System.out.println("Dispara a una coordenada (ejemplo: A1):");
            String coord = sc.next().toUpperCase();

            int x = coord.charAt(0) - 'A';        // A → 0, B → 1, ...
            int y = Integer.parseInt(coord.substring(1)) - 1; // 1 → 0

            tablero.recibirTiro(x, y);
            tablero.imprimirTablero();
        } else {
            System.out.println("Saliendo del juego...");
        }

    }
}