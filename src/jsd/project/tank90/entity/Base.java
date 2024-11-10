package jsd.project.tank90.entity;

import jsd.project.tank90.main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * The Base class represents the base that player has to protect in the game.
 */
public class Base extends Entity {
    GamePanel gp;
    private boolean exploding = false;  // Tracks if explosion is in progress
    private int explosionCounter = 0;   // Counter for explosion animation frames
    public BufferedImage normalBase, destroyedBase, explosion1, explosion2;

    public Base(GamePanel gp){
        this.gp = gp;
        solidArea = new Rectangle(0,0, gp.TILE_SIZE*2 - 6, gp.TILE_SIZE*2 - 6);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        setDefaultValues();
        getBaseImage();
    }

    public void setDefaultValues(){
        x = gp.screenWidth / 2 - (gp.TILE_SIZE*2 - 6) / 2;
        y = 404;

    }

    // Function to get all the image needed for base displaying
    public void getBaseImage(){
        try{
            normalBase = ImageIO.read(getClass().getResourceAsStream("/jsd/project/tank90/res/base/base.png"));
            destroyedBase = ImageIO.read(getClass().getResourceAsStream("/jsd/project/tank90/res/base/destroyed_base.png"));
            explosion1 = ImageIO.read(getClass().getResourceAsStream("/jsd/project/tank90/res/explosion/small.png"));
            explosion2 = ImageIO.read(getClass().getResourceAsStream("/jsd/project/tank90/res/explosion/big.png"));
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void update(){
        if (gp.gameState == gp.GAME_OVER_STATE && !exploding) {
            // Trigger explosion when game over
            exploding = true;
            explosionCounter = 0;
        }

        // If explosion is in progress, increment the counter
        if (exploding) {
            explosionCounter++; // Explosion duration (in frames)

            if (explosionCounter > 60) {// Explosion duration (in frames)
                exploding = false; // Stop explosion after duration
            }
        }
    }
    public void draw(Graphics2D g2){
        BufferedImage image = normalBase;

        if (gp.gameState == gp.GAME_OVER_STATE) {
            image = destroyedBase;
            if (exploding) {
                // Alternate explosion frames for animation effect
                BufferedImage explosionImage = (explosionCounter / 10 % 2 == 0) ? explosion1 : explosion2;
                g2.drawImage(explosionImage, x, y, gp.TILE_SIZE * 2 - 6, gp.TILE_SIZE * 2 - 6, null);
            }
        }

        g2.drawImage(image, x, y, gp.TILE_SIZE * 2 - 6, gp.TILE_SIZE * 2 - 6, null);
    }

}

