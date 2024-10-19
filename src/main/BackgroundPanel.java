package main;

import javax.swing.*;
import java.awt.*;

public class BackgroundPanel extends JPanel {
    private GamePanel gamePanel;
    private Image sideImage;

    public BackgroundPanel(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        setLayout(null); // Absolute positioning

        // Load the image to be displayed in the side spaces
        // You can load the image from a file or resource
        // Assuming you have an image named "side_image.png" in your project folder
        sideImage = new ImageIcon("res/").getImage();

        // Add the GamePanel to the BackgroundPanel and center it
        add(gamePanel);
        gamePanel.setBounds(getCenteredX(), getCenteredY(), gamePanel.screenWidth, gamePanel.screenHeight);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw a wider black rectangle (the background)
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.black);

        // Define the size of the wider rectangle
        int rectWidth = 1000;
        int rectHeight = 500;
        int x = (getWidth() - rectWidth) / 2; // Center horizontally
        int y = (getHeight() - rectHeight) / 2; // Center vertically

        g2.fillRect(x, y, rectWidth, rectHeight);

        // Draw the image in the left and right empty spaces
        drawImageInSideSpaces(g2);

        // Update GamePanel's position to keep it centered
        gamePanel.setBounds(getCenteredX(), getCenteredY(), gamePanel.screenWidth, gamePanel.screenHeight);
    }

    // Method to draw the image in the side spaces
    private void drawImageInSideSpaces(Graphics2D g2) {
        int leftX = getCenteredX() - sideImage.getWidth(null) - 20; // Left side with some margin
        int rightX = getCenteredX() + gamePanel.screenWidth + 20;   // Right side with some margin
        int imageY = (getHeight() - sideImage.getHeight(null)) / 2; // Vertically center the image

        // Draw the image on the left and right sides of the GamePanel
        g2.drawImage(sideImage, leftX, imageY, null);  // Left side
        g2.drawImage(sideImage, rightX, imageY, null); // Right side
    }

    // Get X coordinate to center GamePanel horizontally
    private int getCenteredX() {
        return (getWidth() - gamePanel.screenWidth) / 2;
    }

    // Get Y coordinate to center GamePanel vertically
    private int getCenteredY() {
        return (getHeight() - gamePanel.screenHeight) / 2;
    }
}
