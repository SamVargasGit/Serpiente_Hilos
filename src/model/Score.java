package model;
import java.io.Serializable;

public class Score implements Serializable {
    private String nombreJugador;
    private int puntaje;
    public Score(){
    }
    public Score(String nombreJugador, int puntaje) {
        this.nombreJugador = nombreJugador;
        this.puntaje = puntaje;
    }

    public String getNombreJugador() {
        return nombreJugador;
    }

    public int getPuntaje() {
        return puntaje;
    }
}

