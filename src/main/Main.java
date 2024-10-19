package main;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("demo");

        GamePanel gamePanel = new GamePanel();

        // Create the custom background panel with the GamePanel centered
        BackgroundPanel backgroundPanel = new BackgroundPanel(gamePanel);

        window.add(backgroundPanel);
        window.setSize(1000, 488); // Set window size according to your needs
        window.setLocationRelativeTo(null);
        window.setVisible(true);

        gamePanel.setupGame();
        gamePanel.startGameThread();
    }
}
