package Terminal;

import java.awt.*;

public class Tablero {
    private final int filas = 10;
    private final int columnas = 10;
    private String[][] casillasLogicas;
    private char[][] casillas;
    public Tablero() {
        casillasLogicas = new String[filas][columnas];
        inicializarTablero();
    }

    private void inicializarTablero() {
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                casillasLogicas[i][j] = "🌊"; // Agua
            }
        }
    }

    public boolean colocarBarco(Barco barco) {
        // Verificar que todas las posiciones son válidas
        for (Point p : barco.getPosiciones()) {
            if (!casillaEsValida(p.x, p.y) || !casillasLogicas[p.y][p.x].equals("🌊")) {
                return false;
            }
        }
        // Colocar el barco
        for (Point p : barco.getPosiciones()) {
            casillasLogicas[p.y][p.x] = "🚢";
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
                System.out.print(" " + casillasLogicas[i][j] + " ");
            }
            System.out.println();
        }
    }

    public boolean recibirTiro(int x, int y) {
        if (!casillaEsValida(x, y)) {
            System.out.println("Coordenada inválida.");
            return false;
        }

        String casilla = casillasLogicas[y][x];

        switch (casilla) {
            case "🚢": // Barco
                casillasLogicas[y][x] = "💥"; // Impacto
                System.out.println("¡Impacto!");
                return true;
            case "🌊": // Agua
                casillasLogicas[y][x] = "❌"; // Fallo
                System.out.println("Agua...");
                return false;
            case "💥": // Ya impactado
            case "❌": // Ya disparado aquí (agua)
                System.out.println("Ya disparaste aquí antes.");
                return false;
            default:
                System.out.println("Casilla desconocida.");
                return false;
        }
    }

    public void disparar(int fila, int columna) {
        if (casillas[fila][columna] == 'B') {
            casillas[fila][columna] = 'X'; // tocado
        } else if (casillas[fila][columna] == 'A') {
            casillas[fila][columna] = 'O'; // fallido
        }
    }

    public char getCasilla(int fila, int columna) {
        return casillas[fila][columna];
    }
    public String getCasillaVisual(int fila, int columna) {
        return casillasLogicas[fila][columna];
    }
}
