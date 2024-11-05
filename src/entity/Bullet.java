package entity;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

import main.GamePanel;
import main.Sound;

import javax.imageio.ImageIO;

public class Bullet extends Entity {
    GamePanel gp;
    public boolean alive = true;
    private BufferedImage upImage, downImage, leftImage, rightImage;
    Sound sound = new Sound();
    public Bullet(GamePanel gp, int x, int y, String direction) {
        this.gp = gp;
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.speed = 2; // Adjust bullet speed as needed

        // Define the bullet's collision area
        solidArea = new Rectangle(0, 0, 6, 6);
        // Load the bullet image
        getBulletImage();


    }

    public void update() {
        switch (direction) {
            case "up":
                y -= speed;
                break;
            case "down":
                y += speed;
                break;
            case "left":
                x -= speed;
                break;
            case "right":
                x += speed;
                break;
        }

        // Print the position of the bullet
        System.out.println("Bullet moved to (" + x + ", " + y + ")");
        checkTileInteraction();

        if (x < 0 || x > gp.screenWidth || y < 0 || y > gp.screenHeight) {
            alive = false;
        }
    }



    public void getBulletImage() {
        try {
            upImage = ImageIO.read(getClass().getResourceAsStream("/res/bullets/up.png"));
            downImage = ImageIO.read(getClass().getResourceAsStream("/res/bullets/down.png"));
            leftImage = ImageIO.read(getClass().getResourceAsStream("/res/bullets/left.png"));
            rightImage = ImageIO.read(getClass().getResourceAsStream("/res/bullets/right.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void draw(Graphics2D g2) {
        BufferedImage bulletImage = null;

        switch (direction) {
            case "up":
                bulletImage = upImage;
                break;
            case "down":
                bulletImage = downImage;
                break;
            case "left":
                bulletImage = leftImage;
                break;
            case "right":
                bulletImage = rightImage;
                break;
        }

        System.out.println("Bullet moved to (" + x + ", " + y + ")");
        checkTileInteraction();
        if (x < 0 || x > gp.screenWidth || y < 0 || y > gp.screenHeight) {
            alive = false;
        }

        if (bulletImage != null) {
            g2.drawImage(bulletImage, x, y, gp.tileSize - 8, gp.tileSize - 8, null);
        } else {
            // Fallback if image fails to load
            g2.setColor(Color.YELLOW);
            g2.fillOval(x, y, 6, 6);
        }
    }

    public void checkTileInteraction() {
        // Use the center of the bullet for collision detection
        int centerX = x + (gp.tileSize - 8) / 2;
        int centerY = y + (gp.tileSize - 8) / 2;

        int col = centerX / gp.tileSize;
        int row = centerY / gp.tileSize;

        if(col < 0 || col >= gp.maxScreenCol || row < 0 || row >= gp.maxScreenRow) {
            System.out.println("Bullet out of bounds at (" + col + ", " + row + ")");
            return; // Prevent out-of-bounds errors
        }

        int tileNum = gp.TManager.mapTileNum[col][row];

        System.out.println("Bullet at (" + centerX + ", " + centerY + "), Tile: " + tileNum + " at (" + col + ", " + row + ")");

        if (gp.TManager.tile[tileNum].collision) {
            if (tileNum == 1 || tileNum == 2) { // Brick or Iron tile
                System.out.println("Bullet hit wall or iron. Breaking it.");
                gp.TManager.mapTileNum[col][row] = 0; // Change to grass tile
                sound.setFile(2);
                sound.play();
                alive = false;

            }
            // No action needed for water tile as bullets pass through
        }
    }



}
