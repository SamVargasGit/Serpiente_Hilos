package view;
import java.awt.*;
public class Images {
    public static void dibujarSerpiente(Graphics g, int x, int y) {
        g.setColor(Color.BLACK);
        g.fillRect(x, y, 10, 10);
    }
    public static void dibujarFruta(Graphics g, int x, int y) {
        g.setColor(Color.red);
        g.fillOval(x, y, 10, 10);
    }
}

