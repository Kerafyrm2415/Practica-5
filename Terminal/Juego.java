package Terminal;

public class Juego {
    private Jugador jugador1;
    private Jugador jugador2;
    private Jugador turnoActual;
    private boolean juegoTerminado;

    public Juego(String nombreJugador1, String nombreJugador2) {
        this.jugador1 = new Jugador(nombreJugador1);
        this.jugador2 = new Jugador(nombreJugador2);
        this.turnoActual = jugador1;
        this.juegoTerminado = false;
    }

    public boolean colocarBarco(Jugador jugador, Barco barco) {
        return jugador.agregarBarco(barco);
    }

    public boolean realizarDisparo(int x, int y) {
        if (juegoTerminado) return false;

        Jugador atacante = turnoActual;
        Jugador defensor = (turnoActual == jugador1) ? jugador2 : jugador1;

        boolean impacto = atacante.realizarDisparo(x, y, defensor);

        if (defensor.haPerdido()) {
            juegoTerminado = true;
            System.out.println(atacante.getNombre() + " ha ganado el juego!");
        } else if (!impacto) {
            cambiarTurno();
        }

        return impacto;
    }

    private void cambiarTurno() {
        turnoActual = (turnoActual == jugador1) ? jugador2 : jugador1;
    }

    public Jugador getJugadorActual() {
        return turnoActual;
    }

    public boolean isJuegoTerminado() {
        return juegoTerminado;
    }

    // Getters para los jugadores
    public Jugador getJugador1() {
        return jugador1;
    }

    public Jugador getJugador2() {
        return jugador2;
    }
}