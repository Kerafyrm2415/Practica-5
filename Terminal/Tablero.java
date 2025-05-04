package Terminal;

import java.awt.*;

public class Tablero {
    private final int filas = 10;
    private final int columnas = 10;
    private String[][] casillas;
    public Tablero() {
        casillas = new String[filas][columnas];
        inicializarTablero();
    }

    private void inicializarTablero() {
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                casillas[i][j] = "\uD83C\uDF0A";
            }
        }
    }

    public boolean colocarBarco(Barco barco) {
        for (Point p : barco.getPosiciones()) {
            int x = p.x;
            int y = p.y;
            if (!casillaEsValida(x, y) || !casillas[y][x].equals("🌊")) {
                return false;
            }
        }

        for (Point p : barco.getPosiciones()) {
            casillas[p.y][p.x] = "🚢";  // Aquí debe cambiar el agua por barco
        }

        return true;
    }

    public boolean casillaEsValida(int x, int y) {
        return x >= 0 && x < columnas && y >= 0 && y < filas;
    }

    public void imprimirTablero() {
        System.out.print("     ");
        for (char c = 'A'; c < 'A' + columnas; c++) {
            System.out.print(" " + c + "  ");
        }
        System.out.println();

        for (int i = 0; i < filas; i++) {
            System.out.printf("%2d ", i + 1);
            for (int j = 0; j < columnas; j++) {
                System.out.print(" " + casillas[i][j] + " ");
            }
            System.out.println();
        }
    }

    // Puedes agregar más métodos luego como recibirTiro, marcar impacto, etc.
    // Recibe un disparo en la coordenada (x, y)
    public boolean recibirTiro(int x, int y) {
        if (!casillaEsValida(x, y)) {
            System.out.println("Coordenada inválida.");
            return false;
        }

        String casilla = casillas[y][x];

        // Dentro de recibirTiro()
        switch (casilla) {
            case "🚢":
                casillas[y][x] = "💥";  // impacto
                System.out.println("¡Impacto!");
                return true;

            case "🌊":
                casillas[y][x] = "❌";  // fallo
                System.out.println("Agua...");
                return false;
            case "X":
            case "O":
                System.out.println("Ya disparaste aquí.");
                return false;

            default:
                System.out.println("Casilla desconocida.");
                return false;
        }
    }

    public String[][] getCasillas(int fila, int columna) {
        return casillas;
    }
}
