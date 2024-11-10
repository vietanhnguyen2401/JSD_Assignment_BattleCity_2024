package entity;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

import main.GamePanel;
import main.Sound;
//import main.Sound;

import javax.imageio.ImageIO;

public class Bullet extends Entity {
    GamePanel gp;
    public boolean alive = true;
    private BufferedImage upImage, downImage, leftImage, rightImage;
    private int damage;
    public int speed;
    private boolean isEnemyBullet;
    private boolean canDestroySteel;
    Sound sound = new Sound();
    public Bullet(GamePanel gp, int x, int y, String direction, boolean isEnemyBullet) {
        this.gp = gp;
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.speed = 1; // Adjust bullet speed as needed
        this.isEnemyBullet = isEnemyBullet;
        this.canDestroySteel = !isEnemyBullet && gp.player.starCount >= 3;
        // Define the bullet's collision area
        solidArea = new Rectangle(0, 0, 6, 6);
        // Load the bullet image
        getBulletImage();


    }

    public void update() {
        if(gp.gameState == gp.PLAY_STATE) {
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
            checkTileInteraction();
            checkCollisionWithTarget();
            if (x < 0 || x > gp.screenWidth || y < 0 || y > gp.screenHeight) {
                alive = false;
            }
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

        int margin = 4; // Check for tiles within 1 pixel around the bullet

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
                    }
                    else if (tileNum == 2 && canDestroySteel) { // Steel wall
                        gp.TManager.mapTileNum[checkCol][checkRow] = 0; // Destroy steel wall if player has this ability
                        sound.setFile(2);
                        sound.play();
                        alive = false;
                        return;
                    } else if (tileNum == 2 && isEnemyBullet) {
                        alive = false;
                    } else if (tileNum == 2 && !canDestroySteel) {
                        alive = false;
                    }
                }
            }
        }
    }




    private void checkCollisionWithTarget() {
        Rectangle bulletRect = new Rectangle(x, y, 6, 6); // Bounding box for the bullet

        if (isEnemyBullet) {
            // Check collision with the player
            Rectangle playerRect = new Rectangle(gp.player.x, gp.player.y, gp.tileSize * 2 - 6, gp.tileSize * 2 - 6);
            if (bulletRect.intersects(playerRect)) {
                if (!gp.player. getShield().isActive()) {
                    System.out.println("Enemy bullet hit the player!");
                    alive = false;
                    gp.player.die();
                    gp.explosions.add(new Explosion(gp, gp.player.x, gp.player.y));
                } else {
                    System.out.println("Enemy bullet hit the shield - no damage to player.");
                    alive = false;
                }
            }

            // Check collision with the base
            Rectangle baseRect = new Rectangle(gp.base.x, gp.base.y, gp.tileSize * 2 - 6, gp.tileSize * 2 - 6);
            if (bulletRect.intersects(baseRect)) {
                System.out.println("Enemy bullet hit the base!");
                alive = false; // Bullet is destroyed on impact
                gp.gameState = gp.GAME_OVER_STATE; // Set game state to GAME OVER
                return;
            }
        } else {
            // Check collision with enemies (for player bullets)
            for (int i = 0; i < gp.npc.length; i++) {
                Enemy enemy = gp.npc[i];
                if (enemy != null && enemy.alive) {
                    Rectangle enemyRect = new Rectangle(enemy.x, enemy.y, gp.tileSize * 2 - 6, gp.tileSize * 2 - 6);
                    if (bulletRect.intersects(enemyRect)) {
                        System.out.println("Player bullet hit an enemy!");
                        alive = false;
                        enemy.lives--;
                        System.out.println("enemy tank " + enemy.tankType + i + " " + " Live: " + enemy.lives);

                        if(enemy.lives <= 0) {
                            enemy.alive = false; sound.setFile(2);
                        sound.play();
                        gp.totalPoint += enemy.point;
                        }
                        gp.explosions.add(new Explosion(gp, enemy.x, enemy.y));

  
                        break;
                    }
                }
            }
        }
    }






}
