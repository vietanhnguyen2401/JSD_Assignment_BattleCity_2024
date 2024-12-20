package jsd.project.tank90.main;

import javax.swing.*;
import java.awt.*;
/**
 * The BackgroundPanel class is used for displaying the
 * instruction and some statistics on both sides of the game screen
 */
public class BackgroundPanel extends JPanel {
    private GamePanel gp;
    private final Image sideImage;
    Font gameFont;

    public BackgroundPanel(GamePanel gamePanel) {
        this.gp = gamePanel;
        setLayout(null); // Absolute positioning
        gameFont = new Font("Font 7x7 Regular", Font.PLAIN, 40);

        // Load the image from the resources
        sideImage = new ImageIcon(getClass().getResource("/jsd/project/tank90/res/instruction/sideInstruction.png")).getImage();

        // Add the GamePanel to the BackgroundPanel and center it
        add(gamePanel);
        gamePanel.setBounds(getCenteredX(), getCenteredY(), gamePanel.screenWidth, gamePanel.screenHeight);
        Timer timer = new Timer(16, e -> repaint());  // 16 ms for ~60 FPS
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw a wider black rectangle (the background)
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.black);

        // Define the size of the wider rectangle
        int rectWidth = 1140;
        int rectHeight = 488;
        int x = (getWidth() - rectWidth) / 2; // Center horizontally
        int y = (getHeight() - rectHeight) / 2; // Center vertically

        g2.fillRect(x, y, rectWidth, rectHeight);
        // Draw the image in the left side space only
        drawImageInLeftSpace(g2);
        // Update GamePanel's position to keep it centered
        gp.setBounds(getCenteredX(), getCenteredY(), gp.screenWidth, gp.screenHeight);
        drawGameInformationRightSide(g2);    }

    // Method to draw the image in the left side space only
    private void drawImageInLeftSpace(Graphics2D g2) {
        if (sideImage != null) {
            int leftX = getCenteredX() - sideImage.getWidth(null) - 20; // Left side with some margin
            int imageY = (getHeight() - sideImage.getHeight(null)) / 2; // Vertically center the image
            // Draw the image on the left side of the GamePanel
            g2.drawImage(sideImage, leftX, imageY, null);  // Left side
        } else {
            System.err.println("Image not found or failed to load.");
        }
    }

    private void drawGameInformationRightSide(Graphics2D g2) {
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 14));
        if (gp.gameState == gp.PLAY_STATE){

            String text = "BATTLE CITY";
            int x = 900; // center x for right panel
            int y = gp.TILE_SIZE*5;
            g2.setColor(Color.WHITE);
            g2.drawString(text, x, y);

            y = gp.TILE_SIZE*7;
            String enemy = "ENEMY: ";
            g2.drawString(enemy, x, y);
            g2.drawString(gp.enemyCount+"", x + 100, y);


            y = gp.TILE_SIZE*9;
                String player = "PLAYER: ";
                g2.drawString(player, x, y);
                g2.drawString(gp.player.lives+"", x + 100, y);

            y = gp.TILE_SIZE*14;
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 16));

            String score = "SCORE: ";
            g2.drawString(score, x, y);
            g2.drawString(gp.totalPoint+"", x + 100, y);
            }

    }

    // Get X coordinate to center GamePanel horizontally
    private int getCenteredX() {
        return (getWidth() - gp.screenWidth) / 2;
    }

    // Get Y coordinate to center GamePanel vertically
    private int getCenteredY() {
        return (getHeight() - gp.screenHeight) / 2;
    }
}
