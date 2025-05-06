package Terminal;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Tablero {
    private final int filas = 10;
    private final int columnas = 10;
    private String[][] casilla;
    private List<Barco> barcos;

    public Tablero() {
        casilla = new String[filas][columnas];
        barcos = new ArrayList<>();
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
        barcos.add(barco);
        return true;
    }

    public boolean casillaEsValida(int x, int y) {
        return x >= 0 && x < columnas && y >= 0 && y < filas;
    }

    public void imprimirTablero(boolean mostrarBarcos) {
        System.out.print("   ");
        for (char c = 'A'; c < 'A' + columnas; c++) {  // Un pequeño truco que encontre el siguiente semestre, esto hace que las letras se vayan recorriendo una por una
            System.out.print(c + " ");
        }
        System.out.println();

        for (int i = 0; i < filas; i++) {
            System.out.printf("%2d ", i + 1);
            for (int j = 0; j < columnas; j++) {
                String simbolo = casilla[i][j];
                if (!mostrarBarcos) {
                    // Mostrar impacto aunque sea un barco oculto
                    if (simbolo.equals("💥")) {
                        System.out.print("💥 ");
                    }
                    // Ocultar barcos no impactados
                    else if (simbolo.equals("🚢")) {
                        System.out.print("🌊 ");
                    }
                    // Mostrar otros símbolos normalmente
                    else {
                        System.out.print(simbolo + " ");
                    }
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

        String estadoActual = casilla[y][x];
        if (estadoActual.equals("💥") || estadoActual.equals("❌") || estadoActual.equals("🔥")) {
            return false; // Ya fue disparada
        }

        if (estadoActual.equals("🚢")) {
            casilla[y][x] = "💥";
            // Verificar si algún barco fue hundido
            for (Barco barco : barcos) {
                if (barco.recibirImpacto(x, y) && barco.estaHundido()) {
                    marcarBarcoHundido(barco);
                }
            }
            return true;
        } else if (estadoActual.equals("🌊")) {
            casilla[y][x] = "❌";
            return false;
        }
        return false;
    }

    public void actualizarCasilla(int fila, int columna, String simbolo) {
        if (!casillaEsValida(fila, columna)) {
            casilla[fila][columna] = simbolo;
        }
    }

    public String getCasilla(int fila, int columna) {
        if (!casillaEsValida(fila, columna)) {
            return "?";  // Caso de error
        }
        return casilla[fila][columna];
    }

    public void marcarBarcoHundido(Barco barco) {
        for (Point p : barco.getPosiciones()) {
            if (casillaEsValida(p.x, p.y)) {
                casilla[p.y][p.x] = "🔥"; // Asigna directamente el emoji
            }
        }
        JOptionPane.showMessageDialog(null, "Hundiste un barco!");
    }
    public boolean todosBarcosHundidos() {
        return barcos.stream().allMatch(Barco::estaHundido);
    }
}