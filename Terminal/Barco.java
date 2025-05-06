package Terminal;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Barco {
    private int tamaño;
    private List<Point> posiciones;  // coordenadas en el tablero
    private List<Boolean> impactos;
    private String nombre;

    public Barco(int tamaño) {
        this.tamaño = tamaño;
        this.posiciones = new ArrayList<>();
        this.impactos = new ArrayList<>();
        // Inicializar impactos
        for (int i = 0; i < tamaño; i++) {
            impactos.add(false);
        }
    }

    public Barco(int tamaño, String nombre) {
        this.tamaño = tamaño;
        this.nombre = nombre;
        this.posiciones = new ArrayList<>();
        this.impactos = new ArrayList<>();

        for (int i = 0; i < tamaño; i++) {
            impactos.add(false);
        }
    }

    // Colocamos el barco a partir de una posición y orientación
    public void colocarEn(int x, int y, boolean horizontal, int limite) {
        posiciones.clear();
        for (int i = 0; i < tamaño; i++) {
            int posX = horizontal ? x + i : x;
            int posY = horizontal ? y : y + i;
            if (posX < limite && posY < limite) {
                posiciones.add(new Point(posX, posY));
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
            if (p.x == x && p.y == y && !impactos.get(i)) {
                impactos.set(i, true);
                return true;
            }
        }
        return false;
    }

    public boolean estaHundido() {
        return impactos.stream().allMatch(hit -> hit);
    }

    // Getters
    public int getTamaño() {
        return tamaño;
    }

    public List<Point> getPosiciones() {
        return posiciones;
    }

    public String getNombre() {
        return nombre;
    }

}
