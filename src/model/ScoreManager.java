package model;
import com.fasterxml.jackson.databind.ObjectMapper;
import view.Snake;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
public class ScoreManager implements Runnable {
    private Snake snake;
    private List<Score> scores;
    private ObjectMapper objectMapper;
    public ScoreManager(Snake snake) {
        this.snake = snake;
        this.scores = new ArrayList<>();
        this.objectMapper = new ObjectMapper();
        cargarPuntajes();
    }
    public void agregarPuntaje(String nombreJugador, int puntaje) {
        Score nuevoScore = new Score(nombreJugador, puntaje);
        scores.add(nuevoScore);
        guardarPuntajes();
    }
    public List<Score> getPuntajes() {
        return scores;
    }
    private void guardarPuntajes() {
        try {
            objectMapper.writeValue(new File("Utilities/puntajes.json"), scores);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void cargarPuntajes() {
        try {
            Score[] puntajesArray = objectMapper.readValue(new File("Utilities/puntajes.json"), Score[].class);
            scores.clear();
            for (Score score : puntajesArray) {
                scores.add(score);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(10000);
                guardarPuntajes();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}



