package entity;

import main.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * The Entity class represents any object or character in the game world.
 * It contains properties for position, movement, appearance, collision detection, and more.
 */
public class Entity {

    // Position of the entity on the game panel
    public int x, y;

    // Speed at which the entity moves
    public int speed;

    // Buffered images for different sprite animations (e.g., walking up, down, left, right)
    public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;

    // Direction the entity is facing (up, down, left, right)
    public String direction;

    // Sprite animation counters
    public int spriteCounter = 0;  // Counter for sprite switching speed
    public int spriteNum = 1;      // Current sprite being displayed

    // Rectangle used for collision detection
    public Rectangle solidArea;

    // Default position of the solid area (for resetting after movement)
    public int solidAreaDefaultX, solidAreaDefaultY;

    // Flag indicating if a collision is occurring
    public boolean collisionOn = false;

    // Flag indicating if the entity is alive
    public boolean alive = true;

    // Counter to prevent frequent actions (used for action lock)
    public int actionLockCounter = 0;
}
