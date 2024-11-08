package item;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Shield {
    private int duration; // Duration of shield in frames
    private int flickerFrequency; // Frequency of flickering
    private int counter = 0; // Counter to track the flicker effect
    private boolean isActive = false; // Shield activation state
    private int x, y, size; // Position and size of the shield

    public void setActive(boolean active) {
        isActive = active;
    }

    private BufferedImage shieldImage1, shieldImage2; // Two images for animation

    public Shield(int duration, int flickerFrequency, int size) {
        this.duration = duration;
        this.flickerFrequency = flickerFrequency;
        this.size = size;

        // Load the two shield images
        try {
            shieldImage1 = ImageIO.read(getClass().getResourceAsStream("/res/shield/shield1.png")); // Adjust path as needed
            shieldImage2 = ImageIO.read(getClass().getResourceAsStream("/res/shield/shield2.png")); // Adjust path as needed
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Activate the shield and reset counter
    public void activate(int x, int y) {
        isActive = true;
        counter = 0;
        this.x = x;
        this.y = y;
    }

    // Update shield's position, duration, and flickering
    public void update(int playerX, int playerY) {
        if (!isActive) return;

        // Update position
        x = playerX - 6;
        y = playerY - 3;

        // Increment counter and deactivate if duration is exceeded
        counter++;
        if (counter >= duration) {
            isActive = false;
        }
    }

    // Render shield with animation and flickering effect
    public void draw(Graphics2D g2) {
        if (isActive && counter / flickerFrequency % 2 == 0) {
            // Choose which image to use based on the counter for animation effect
            BufferedImage currentImage = (counter / 10 % 2 == 0) ? shieldImage1 : shieldImage2;

            if (currentImage != null) {
                // Center the shield around the player
                int offsetX = x + size / 2 - currentImage.getWidth() / 2;
                int offsetY = y + size / 2 - currentImage.getHeight() / 2;
                g2.drawImage(currentImage, offsetX, offsetY, size, size, null);
            }
        }
    }


    // Check if the shield is currently active
    public boolean isActive() {
        return isActive;
    }
}

