package Terminal;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
        if (casillaEsValida(fila, columna)) {
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
    public void reconstruirBarcosDesdeGuardado() {
        barcos.clear(); // Limpiar lista existente

        // Reconstruir barcos basado en las casillas con "🚢" o "💥"
        for (int fila = 0; fila < 10; fila++) {
            for (int col = 0; col < 10; col++) {
                String estado = getCasilla(fila, col);
                if (estado.equals("🚢") || estado.equals("💥")) {
                    // Verificar si esta casilla ya pertenece a algún barco reconstruido
                    boolean perteneceABarcoExistente = false;
                    for (Barco barco : barcos) {
                        if (barco.contieneCasilla(col, fila)) {
                            perteneceABarcoExistente = true;
                            // Si está impactado, marcar esa parte del barco
                            if (estado.equals("💥")) {
                                barco.marcarImpacto(col, fila);
                            }
                            break;
                        }
                    }

                    if (!perteneceABarcoExistente) {
                        // Reconstruir el barco completo
                        Barco barco = reconstruirBarcoDesdeCasilla(col, fila);
                        barcos.add(barco);
                        // Marcar impactos si corresponde
                        for (int[] parte : barco.getPartes()) {
                            int c = parte[0], f = parte[1];
                            if (getCasilla(f, c).equals("💥")) {
                                barco.marcarImpacto(c, f);
                            }
                        }
                    }
                }
            }
        }
    }
    private Barco reconstruirBarcoDesdeCasilla(int colInicial, int filaInicial) {
        // Primero determinamos el tamaño y orientación del barco
        int tamaño = 1;
        boolean horizontal = false;
        boolean vertical = false;

        // Verificar hacia la derecha (horizontal)
        int c = colInicial + 1;
        while (c < 10 && (getCasilla(filaInicial, c).equals("🚢") || getCasilla(filaInicial, c).equals("💥"))) {
            tamaño++;
            horizontal = true;
            c++;
        }

        // Si no es horizontal, verificar hacia abajo (vertical)
        if (tamaño == 1) {
            int f = filaInicial + 1;
            while (f < 10 && (getCasilla(f, colInicial).equals("🚢") || getCasilla(f, colInicial).equals("💥"))) {
                tamaño++;
                vertical = true;
                f++;
            }
        }

        // Crear el barco con el tamaño determinado
        Barco barco = new Barco(tamaño);

        // Determinar la orientación
        boolean esHorizontal = horizontal || (tamaño == 1 && new Random().nextBoolean());

        // Colocar el barco en la posición inicial con la orientación determinada
        barco.colocarEn(colInicial, filaInicial, esHorizontal, 10);

        // Marcar las partes impactadas
        for (int[] parte : barco.getPartes()) {
            int col = parte[0];
            int fila = parte[1];
            if (getCasilla(fila, col).equals("💥")) {
                barco.marcarImpacto(col, fila);
            }
        }

        return barco;
    }
}