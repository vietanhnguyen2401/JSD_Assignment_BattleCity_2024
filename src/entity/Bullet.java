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
    private int damage;
    private boolean isEnemyBullet;
    Sound sound = new Sound();
    public Bullet(GamePanel gp, int x, int y, String direction, boolean isEnemyBullet) {
        this.gp = gp;
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.speed = 2; // Adjust bullet speed as needed
        this.isEnemyBullet = isEnemyBullet;
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
        checkCollisionWithTarget();
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
            g2.drawImage(bulletImage, x, y, gp.tileSize - 10, gp.tileSize - 10, null);
        } else {
            // Fallback if image fails to load
            g2.setColor(Color.YELLOW);
            g2.fillOval(x, y, 6, 6);
        }
    }

    public void checkTileInteraction() {
        // Define a small margin to expand the collision area
        int margin = 1; // Check for tiles within 1 pixel around the bullet

        // Calculate the bullet's center position
        int centerX = x + (gp.tileSize - 8) / 2;
        int centerY = y + (gp.tileSize - 8) / 2;

        // Determine the primary tile the bullet is over
        int col = centerX / gp.tileSize;
        int row = centerY / gp.tileSize;

        // Check primary tile and surrounding tiles within the margin
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int checkCol = col + i;
                int checkRow = row + j;

                // Skip if the tile is out of map bounds
                if (checkCol < 0 || checkCol >= gp.maxScreenCol || checkRow < 0 || checkRow >= gp.maxScreenRow) {
                    continue;
                }

                int tileNum = gp.TManager.mapTileNum[checkCol][checkRow];

                // Define a slightly larger rectangle for bullet detection
                Rectangle tileRect = new Rectangle(checkCol * gp.tileSize, checkRow * gp.tileSize, gp.tileSize, gp.tileSize);
                Rectangle expandedBulletRect = new Rectangle(centerX - margin, centerY - margin, 6 + 2 * margin, 6 + 2 * margin);

                // Check if the expanded bullet rectangle intersects with the tile's rectangle
                if (gp.TManager.tile[tileNum].collision && expandedBulletRect.intersects(tileRect)) {
                    if (tileNum == 1) { // Brick tile
                        System.out.println("Bullet hit brick. Breaking it.");
                        gp.TManager.mapTileNum[checkCol][checkRow] = 0; // Replace brick with non-collidable tile (e.g., grass)
                        sound.setFile(2);
                        sound.play();
                        alive = false; // Bullet is destroyed upon collision
                        return; // Stop after breaking the brick
                    } else if (tileNum == 2) { // Steel tile
                        System.out.println("Bullet hit steel. Bullet disappears.");
                        alive = false; // Bullet disappears on hitting steel, but steel remains unbroken
                        return; // Stop further checks as the bullet is destroyed
                    }
                }
            }
        }
    }




    private void checkCollisionWithTarget() {
        Rectangle bulletRect = new Rectangle(x, y, 6, 6); // Bounding box for the bullet

        if (isEnemyBullet) {
            // Enemy bullet: check collision with the player and consider the shield
            Rectangle playerRect = new Rectangle(gp.player.x, gp.player.y, gp.tileSize * 2 - 6, gp.tileSize * 2 - 6);

            if (bulletRect.intersects(playerRect)) {
                if (!gp.player.getShield().isActive()) { // Damage player only if shield is not active
                    System.out.println("Enemy bullet hit the player!");
                    alive = false;
                    gp.player.die(); // Decrease player life and set to respawn if necessary
                    gp.explosions.add(new Explosion(gp, gp.player.x, gp.player.y)); // Trigger explosion effect on player
                } else {
                    System.out.println("Enemy bullet hit the shield - no damage to player.");
                    alive = false; // Bullet is destroyed, but no damage to the player
                }
            }
        } else {
            // Player bullet: check collision with enemies
            for (Enemy enemy : gp.npc) {
                if (enemy != null && enemy.alive) {
                    Rectangle enemyRect = new Rectangle(enemy.x, enemy.y, gp.tileSize * 2 - 6, gp.tileSize * 2 - 6);

                    if (bulletRect.intersects(enemyRect)) {
                        System.out.println("Player bullet hit an enemy!");
                        alive = false; // Bullet is destroyed
                        enemy.alive = false; // Enemy is destroyed
                        gp.explosions.add(new Explosion(gp, enemy.x, enemy.y)); // Trigger explosion effect on enemy
                        break; // Stop after hitting one enemy
                    }
                }
            }
        }
    }






}
