package Terminal;

import java.util.ArrayList;
import java.util.List;

public class Jugador {
    private String nombre;
    private Tablero tableroPropio;
    private Tablero tableroRival;
    private List<Barco> barcos;
    private int barcosHundidos;

    public Jugador(String nombre) {
        this.nombre = nombre;
        this.tableroPropio = new Tablero();
        this.tableroRival = new Tablero(); // Para registrar disparos al oponente
        this.barcos = new ArrayList<>();
        this.barcosHundidos = 0;
    }

    // Métodos para gestionar barcos
    public boolean agregarBarco(Barco barco) {
        if (tableroPropio.colocarBarco(barco)) {
            barcos.add(barco);
            return true;
        }
        return false;
    }

    public boolean recibirDisparo(int x, int y) {
        // Verificar si el disparo impactó en algún barco
        for (Barco barco : barcos) {
            if (barco.recibirImpacto(x, y)) {
                tableroPropio.recibirTiro(x, y);

                if (barco.estaHundido()) {
                    barcosHundidos++;
                    tableroPropio.marcarBarcoHundido(barco);
                    System.out.println(nombre + ": ¡Barco hundido!");
                }
                return true;
            }
        }
        // Si no impactó en ningún barco
        tableroPropio.recibirTiro(x, y);
        return false;
    }

    public boolean realizarDisparo(int x, int y, Jugador oponente) {
        boolean impacto = oponente.recibirDisparo(x, y);
        // Registrar el resultado en el tablero rival
        if (impacto) {
            tableroRival.recibirTiro(x, y);
        } else {
            tableroRival.recibirTiro(x, y);
        }
        return impacto;
    }

    public boolean haPerdido() {
        return barcosHundidos == barcos.size();
    }

    // Getters
    public String getNombre() {
        return nombre;
    }

    public Tablero getTableroPropio() {
        return tableroPropio;
    }

    public Tablero getTableroRival() {
        return tableroRival;
    }

    public List<Barco> getBarcos() {
        return barcos;
    }

    public int getBarcosHundidos() {
        return barcosHundidos;
    }
}
