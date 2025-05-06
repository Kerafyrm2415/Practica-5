package Terminal;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Barco {
    private List<Point> posiciones;
    private List<Boolean> impactos;
    private int tamaño;

    public Barco(int tamaño) {
        this.tamaño = tamaño;
        this.posiciones = new ArrayList<>();
        this.impactos = new ArrayList<>();
        for (int i = 0; i < tamaño; i++) {
            impactos.add(false);
        }
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
}
