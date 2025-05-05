package Terminal;

import java.awt.*;

public class Tablero {
    private final int filas = 10;
    private final int columnas = 10;
    private String[][] casilla;
    public Tablero() {
        casilla = new String[filas][columnas];
        inicializarTablero();
    }

    private void inicializarTablero() {
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                casilla[i][j] = "🌊"; // Agua
            }
        }
    }

    public boolean colocarBarco(Barco barco) {
        // Primero verificar que todas las posiciones son válidas
        for (Point p : barco.getPosiciones()) {
            if (!casillaEsValida(p.x, p.y) || !casilla[p.y][p.x].equals("🌊")) {
                return false;
            }
        }

        // Luego colocar el barco
        for (Point p : barco.getPosiciones()) {
            casilla[p.y][p.x] = "🚢";
        }
        return true;
    }

    public boolean casillaEsValida(int x, int y) {
        return x >= 0 && x < columnas && y >= 0 && y < filas;
    }

    public void imprimirTablero(boolean mostrarBarcos) {
        System.out.print("   ");
        for (char c = 'A'; c < 'A' + columnas; c++) {
            System.out.print(c + " ");
        }
        System.out.println();

        for (int i = 0; i < filas; i++) {
            System.out.printf("%2d ", i + 1);
            for (int j = 0; j < columnas; j++) {
                String simbolo = casilla[i][j];

                if (!mostrarBarcos && simbolo.equals("🚢")) {
                    System.out.print("🌊 ");
                } else {
                    System.out.print(simbolo + " ");
                }
            }
            System.out.println();
        }
    }

    public boolean recibirTiro(int x, int y) {
        if (!casillaEsValida(x, y)) {
            return false;
        }

        switch (casilla[y][x]) {
            case "🚢":
                casilla[y][x] = "💥";
                return true;
            case "🌊":
                casilla[y][x] = "❌";
                return false;
            default:
                return false; // Ya fue disparada
        }
    }

    public String getCasilla(int fila, int columna) {
        return casilla[fila][columna];
    }
}
