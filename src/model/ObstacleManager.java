package model;

import view.Obstacle;

import java.util.LinkedList;
import java.util.Random;

public class ObstacleManager implements Runnable {
    private LinkedList<Obstacle> obstacles;
    private int cantidadObstaculos;
    private int tiempo;
    private Random random;

    public ObstacleManager(LinkedList<Obstacle> obstacles, int cantidadObstaculos, int tiempo) {
        this.obstacles = obstacles;
        this.cantidadObstaculos = cantidadObstaculos;
        this.random = new Random();
        this.tiempo=tiempo;
    }

    @Override
    public void run() {
        while (true) {
            if (obstacles.size() < cantidadObstaculos) {
                int x = random.nextInt(50);
                int y = random.nextInt(50);
                Obstacle nuevoObstacle = new Obstacle(x, y);
                if (!obstacles.contains(nuevoObstacle)) {
                    obstacles.add(nuevoObstacle);
                }
            }
            try {
                Thread.sleep(tiempo);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
