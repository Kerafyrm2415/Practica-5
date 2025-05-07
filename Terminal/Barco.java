package Terminal;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Barco {
    private List<Point> posiciones;
    private List<Boolean> impactos;
    private int tamaño;
    private List<int[]> partes;
    private List<int[]> partesImpactadas;

    public Barco(int tamaño) {
        this.tamaño = tamaño;
        this.posiciones = new ArrayList<>();
        this.impactos = new ArrayList<>();
        for (int i = 0; i < tamaño; i++) {
            impactos.add(false);
        }
        this.partes = new ArrayList<>();
        this.partesImpactadas = new ArrayList<>();
    }

    public Barco(int tamaño, String nombre) {
        this.tamaño = tamaño;
        this.posiciones = new ArrayList<>();
        this.impactos = new ArrayList<>();

        for (int i = 0; i < tamaño; i++) {
            impactos.add(false);
        }
    }

    public void colocarEn(int x, int y, boolean horizontal, int tamañoTablero) {
        posiciones.clear();
        if (horizontal) {
            for (int i = 0; i < tamaño; i++) {
                posiciones.add(new Point(x + i, y));
            }
        } else {
            for (int i = 0; i < tamaño; i++) {
                posiciones.add(new Point(x, y + i));
            }
        }

        partes.clear();
        for (int i = 0; i < this.tamaño; i++) {
            int c = horizontal ? x + i : x;
            int f = horizontal ? y : y + i;
            partes.add(new int[]{c, f});
        }
    }


    public boolean ocupaCoordenada(int x, int y) {
        for (Point p : posiciones) {
            if (p.x == x && p.y == y)
                return true;
        }
        return false;
    }

    public boolean recibirImpacto(int x, int y) {
        for (int i = 0; i < posiciones.size(); i++) {
            Point p = posiciones.get(i);
            if (p.x == x && p.y == y) {
                impactos.set(i, true);
                return true;
            }
        }
        return false;
    }


    public boolean estaHundido() {
        return impactos.stream().allMatch(b -> b);
    }

    // Getters
    public int getTamaño() {
        return tamaño;
    }

    public List<Point> getPosiciones() {
        return posiciones;
    }

    public boolean contieneCasilla(int columna, int fila) {
        for (int[] parte : partes) {
            if (parte[0] == columna && parte[1] == fila) {
                return true;
            }
        }
        return false;
    }

    public void marcarImpacto(int columna, int fila) {
        for (int[] parte : partes) {
            if (parte[0] == columna && parte[1] == fila) {
                partesImpactadas.add(parte);
                break;
            }
        }
    }

    public List<int[]> getPartes() {
        return new ArrayList<>(partes);
    }

}
