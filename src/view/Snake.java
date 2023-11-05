package view;

import com.fasterxml.jackson.databind.ObjectMapper;
import model.Dificulty;
import model.ObstacleManager;
import model.ScoreManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

public class Snake extends JFrame implements Runnable, KeyListener,MenuListener {
    private long lastDirectionChangeTime = 0;
    private static final long DIRECTION_CHANGE_DELAY = 100;
    private LinkedList<Spot> lista = new LinkedList<Spot>();
    private LinkedList<Obstacle> obstacles = new LinkedList<Obstacle>();
    private JButton volverAlMenuButton;
    private int columna, fila, colfruta, filfruta;
    private int crecimientoSerpiente = 0;
    private int crecimiento = 0;
    private int puntuacion = 0;
    private int velocidadSerpiente;
    private int tiempoReaparicionFruta;
    private int cantidadObstaculos;
    private int tiempoObstaculos;
    private boolean activo = true;
    private Direccion direccion = Direccion.DERECHA;
    private Image imagen;
    private String nombre;
    private Graphics bufferGraphics;
    private Color colorFondo = new Color(205, 205, 205);
    private ObstacleManager obstacleManager;
    private View menu = new View();
    private Font fuentePuntaje;
    private ScoreManager scoreManager;
    private Thread puntajeThread;
    private Thread hilo;
    private Thread frutaThread;
    private Thread obstaculoThread;
    private Images images = new Images();
    public int getPuntuacion() {
        return puntuacion;
    }
    private enum Direccion {
        IZQUIERDA, DERECHA, SUBE, BAJA
    }
    class Spot {
        int x, y;

        public Spot(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
    public Snake(String dificultadSeleccionada) {
        fuentePuntaje = new Font("Arial", Font.PLAIN, 18);
        this.addKeyListener(this);
        lista.add(new Spot(4, 25));
        lista.add(new Spot(3, 25));
        lista.add(new Spot(2, 25));
        lista.add(new Spot(1, 25));
        columna = 4;
        fila = 25;
        Dificulty dificulty = cargarDificultad(dificultadSeleccionada);
        velocidadSerpiente = dificulty.getVelocidadSerpiente();
        hilo = new Thread(this);
        hilo.start();
        frutaThread = new Thread(() -> {
            while (activo) {
                colfruta = (int) (Math.random() * 50);
                filfruta = (int) (Math.random() * 50);
                repaint();
                try {
                    Thread.sleep(tiempoReaparicionFruta);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        frutaThread.start();
        this.getContentPane().setBackground(colorFondo);
        ajustarParametrosDificultad(dificulty);
        obstacleManager = new ObstacleManager(obstacles, cantidadObstaculos,tiempoObstaculos);
        obstaculoThread = new Thread(obstacleManager);
        obstaculoThread.start();
        scoreManager = new ScoreManager(this);
        puntajeThread = new Thread(scoreManager);
        puntajeThread.start();
        volverAlMenuButton = new JButton("Volver al Menú");
        volverAlMenuButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                volverAlMenu();
            }
        });
        volverAlMenuButton.setVisible(false);
        this.getContentPane().add(BorderLayout.SOUTH, volverAlMenuButton);
    }
    private void mostrarMensajePerdiste(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje);
    }
    private Dificulty cargarDificultad(String nombreDificultad) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(new File(nombreDificultad + ".json"), Dificulty.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    private void sePisa() {
        for (Spot p : lista) {
            if (p.x == columna && p.y == fila) {
                activo = false;
                mostrarMensajePerdiste("Perdiste: No debiste intentar comerte");
                scoreManager.agregarPuntaje(nombre,puntuacion);
                volverAlMenuButton.setVisible(true);
            }
        }
        Obstacle obstacle = null;
        for (Obstacle o : obstacles) {
            if (columna == o.x && fila == o.y) {
                obstacle = o;
                break;
            }
        }
        if (obstacle != null) {
            activo = false;
            mostrarMensajePerdiste("Perdiste: recuerda siempre mirar por donde pisas¡");
            scoreManager.agregarPuntaje(nombre, puntuacion);
            volverAlMenuButton.setVisible(true);
        }
    }
    public void volverAlMenu() {
        this.setVisible(false);
        menu.menu();
    }
    private boolean verificarComeFruta() {
        if (columna == colfruta && fila == filfruta) {
            colfruta = (int) (Math.random() * 50);
            filfruta = (int) (Math.random() * 50);
            crecimiento = crecimientoSerpiente;
            incrementarPuntuacion(10);
            return true;
        } else {
            return false;
        }
    }
    public void keyPressed(KeyEvent arg0) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastDirectionChangeTime < DIRECTION_CHANGE_DELAY) {
            return;
        }
        if (arg0.getKeyCode() == KeyEvent.VK_RIGHT && direccion != Direccion.IZQUIERDA) {
            direccion = Direccion.DERECHA;
        }
        if (arg0.getKeyCode() == KeyEvent.VK_LEFT && direccion != Direccion.DERECHA) {
            direccion = Direccion.IZQUIERDA;
        }
        if (arg0.getKeyCode() == KeyEvent.VK_UP && direccion != Direccion.BAJA) {
            direccion = Direccion.SUBE;
        }
        if (arg0.getKeyCode() == KeyEvent.VK_DOWN && direccion != Direccion.SUBE) {
            direccion = Direccion.BAJA;
        }
        lastDirectionChangeTime = currentTime;
    }
    public void keyReleased(KeyEvent arg0) {
    }
    public void keyTyped(KeyEvent arg0) {
    }
    public void dibujarObstaculos(Graphics g) {
        g.setColor(Color.WHITE);
        for (Obstacle obstacle : obstacles) {
            g.fillRect(obstacle.x * 10 + 20, obstacle.y * 10 + 50, 10, 10);
        }
    }
    @Override
    public void paint(Graphics g) {
        if (imagen == null) {
            imagen = createImage(this.getSize().width, this.getSize().height);
            bufferGraphics = imagen.getGraphics();
        }
        bufferGraphics.setColor(colorFondo);
        bufferGraphics.fillRect(0, 0, getWidth(), getHeight());
        images.dibujarFruta(bufferGraphics, colfruta * 10 + 20, filfruta * 10 + 50);
        for (Spot spot : lista) {
            images.dibujarSerpiente(bufferGraphics, spot.x * 10 + 20, spot.y * 10 + 50);
        }
        dibujarObstaculos(bufferGraphics);
        bufferGraphics.setColor(Color.BLACK);
        bufferGraphics.setFont(fuentePuntaje);
        bufferGraphics.drawString("Puntaje: " + getPuntuacion(), getWidth() - 150, 50);
        g.drawImage(imagen, 0, 0, this);
    }
    public synchronized void incrementarPuntuacion(int puntos) {
        puntuacion += puntos;
    }
    private void ajustarParametrosDificultad(Dificulty dificulty) {
        tiempoReaparicionFruta = dificulty.getTiempoReaparicionFruta();
        cantidadObstaculos = dificulty.getCantidadObstaculos();
        tiempoObstaculos = dificulty.getTiempoObstaculos();
        crecimientoSerpiente= dificulty.getCrecimiento();
    }
    @Override
    public void run() {
        while (activo) {
            try {
                Thread.sleep(velocidadSerpiente);
                actualizarPosicion();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    public void setNombre(String nombre){
        this.nombre=nombre;
    }
    private void actualizarPosicion() {
        switch (direccion) {
            case DERECHA:
                columna = (columna + 1) % 50;
                break;
            case IZQUIERDA:
                columna = (columna - 1 + 50) % 50;
                break;
            case SUBE:
                fila = (fila - 1 + 50) % 50;
                break;
            case BAJA:
                fila = (fila + 1) % 50;
                break;
        }
        repaint();
        sePisa();
        lista.addFirst(new Spot(columna, fila));
        if (!verificarComeFruta() && crecimiento == 0) {
            lista.removeLast();
        } else if (crecimiento > 0) {
            crecimiento--;
        }
    }
}
