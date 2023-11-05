package view;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.Score;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
public class View {
    private JFrame menu = new JFrame("Snake");
    private JFrame menuDificulty = new JFrame("Selecciona la dificultad");
    private Color backgroundColor = new Color(255, 255, 255, 255);
    private Color backgroundColor2 = new Color(205, 205, 205);
    private TextField nickname = new TextField(10);
    private void showHistorial() {
        JFrame ventanaHistorial = new JFrame("Historial de Puntajes");
        ventanaHistorial.setSize(400, 400);
        ventanaHistorial.setResizable(false);
        ventanaHistorial.setLocationRelativeTo(null);
        ventanaHistorial.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        String[] columnas = {"Jugador", "Puntaje"};
        List<Score> scores = loadScores();
        Object[][] datos = new Object[scores.size()][2];

        for (int i = 0; i < scores.size(); i++) {
            datos[i][0] = scores.get(i).getNombreJugador();
            datos[i][1] = scores.get(i).getPuntaje();
        }

        JTable tabla = new JTable(datos, columnas);
        JScrollPane scrollPane = new JScrollPane(tabla);
        ventanaHistorial.add(scrollPane);

        ventanaHistorial.setVisible(true);
    }
    private void showCredits() {
        JOptionPane.showMessageDialog(null,
                "Nombre Estudiante: Samuel David Vargas Millan\n" +
                        "Código: 202128171\n" +
                        "Facultad: Ingeniería\n"+
                        "Escuela: Ingenieria de sistemas\n"+
                        "Espero que disfruten del juego :)",
                "Créditos",
                JOptionPane.INFORMATION_MESSAGE);
    }
    public List<Score> loadScores() {
        try {
            File archivoPuntajes = new File("Utilities/puntajes.json");
            if (!archivoPuntajes.exists()) {
                return new ArrayList<>();
            }

            ObjectMapper objectMapper = new ObjectMapper();
            Score[] puntajesArray = objectMapper.readValue(archivoPuntajes, Score[].class);
            return Arrays.asList(puntajesArray);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,"No hay puntajes registrados, Se el primero¡");
            return new ArrayList<>();
        }
    }
    public void initGame(Snake snake) {
        snake.setSize(600, 600);
        snake.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        snake.setVisible(true);
    }
    public void openMenuDifilcuties() {
        menuDificulty.setSize(400, 400);
        menuDificulty.setResizable(false);
        menuDificulty.setLocationRelativeTo(null);
        menuDificulty.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panelMenuDificultad = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        panelMenuDificultad.setBackground(backgroundColor);
        JButton principiante = new JButton("Principiante");
        gbc.gridx = 0;
        gbc.gridy = 0;
        principiante.setBackground(backgroundColor2);
        panelMenuDificultad.add(principiante, gbc);
        JButton medio = new JButton("Medio");
        gbc.gridx = 1;
        gbc.gridy = 0;
        medio.setBackground(backgroundColor2);
        panelMenuDificultad.add(medio, gbc);
        JButton avanzado = new JButton("Avanzado");
        gbc.gridx = 2;
        gbc.gridy = 0;
        avanzado.setBackground(backgroundColor2);
        panelMenuDificultad.add(avanzado, gbc);
        menuDificulty.getContentPane().add(BorderLayout.CENTER, panelMenuDificultad);
        menuDificulty.setVisible(true);

        principiante.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                menuDificulty.setVisible(false);
                Snake snake = new Snake("Utilities/Beginner");
                snake.setNombre(nickname.getText());
                initGame(snake);
            }
        });

        medio.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                menuDificulty.setVisible(false);
                Snake snake = new Snake("Utilities/Medium");
                snake.setNombre(nickname.getText());
                initGame(snake);
            }
        });
        avanzado.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                menuDificulty.setVisible(false);
                Snake snake = new Snake("Utilities/Advanced");
                snake.setNombre(nickname.getText());
                initGame(snake);
            }
        });
    }
    public void menu() {
        menu.setSize(400, 400);
        menu.setResizable(false);
        menu.setLocationRelativeTo(null);
        menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panelmenu = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        JPanel panelTittle = new JPanel();
        panelTittle.setBackground(backgroundColor2);
        panelmenu.setBackground(backgroundColor);
        JLabel tittle = new JLabel("SNAKE");
        tittle.setForeground(Color.BLACK);

        JLabel nick = new JLabel("Nick name");
        gbc.gridx = 0;
        gbc.gridy = 0;
        panelmenu.add(nick, gbc);
        gbc.gridx = 1;
        gbc.gridy = 0;
        panelmenu.add(nickname, gbc);
        JButton play = new JButton("Jugar");
        gbc.gridx = 0;
        gbc.gridy = 1;
        play.setBackground(backgroundColor2);
        panelmenu.add(play, gbc);
        JButton verHistorial = new JButton("Ver Historial");
        gbc.gridx = 0;
        gbc.gridy = 2;
        verHistorial.setBackground(backgroundColor2);
        panelmenu.add(verHistorial, gbc);
        JButton creditosButton = new JButton("Créditos");
        gbc.gridx = 0;
        gbc.gridy = 3;
        creditosButton.setBackground(backgroundColor2);
        panelmenu.add(creditosButton, gbc);
        panelTittle.add(tittle);
        menu.getContentPane().add(BorderLayout.NORTH, panelTittle);
        menu.getContentPane().add(BorderLayout.CENTER, panelmenu);
        menu.setVisible(true);
        play.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nombreJugador = nickname.getText();
                if (nombreJugador.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Debes ingresar un nickname antes de jugar.");
                } else {
                    menu.setVisible(false);
                    openMenuDifilcuties();
                }
            }
        });
        verHistorial.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showHistorial();
            }
        });
        creditosButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showCredits();
            }
        });
    }
}
