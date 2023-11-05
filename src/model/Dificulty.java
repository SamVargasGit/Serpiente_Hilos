package model;
import java.io.Serializable;
public class Dificulty implements Serializable {
    private int velocidadSerpiente;
    private int tiempoReaparicionFruta;
    private int cantidadObstaculos;
    private int tiempoObstaculos;
    private int crecimiento;
    public Dificulty() {
    }
    public Dificulty(int velocidadSerpiente, int tiempoReaparicionFruta, int cantidadObstaculos, int tiempoObstaculos, int crecimiento) {
        this.velocidadSerpiente = velocidadSerpiente;
        this.tiempoReaparicionFruta = tiempoReaparicionFruta;
        this.cantidadObstaculos = cantidadObstaculos;
        this.tiempoObstaculos=tiempoObstaculos;
        this.crecimiento=crecimiento;
    }
    public int getVelocidadSerpiente() {
        return velocidadSerpiente;
    }
    public int getTiempoObstaculos() {
        return tiempoObstaculos;
    }
    public int getTiempoReaparicionFruta() {
        return tiempoReaparicionFruta;
    }
    public int getCantidadObstaculos() {
        return cantidadObstaculos;
    }
    public int getCrecimiento(){return crecimiento;}
}

