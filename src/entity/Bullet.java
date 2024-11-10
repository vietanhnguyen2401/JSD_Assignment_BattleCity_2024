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
    public int speed;
    private boolean isEnemyBullet;
    private boolean canDestroySteel;
    Sound sound = new Sound();

    public Bullet(GamePanel gp, int x, int y, String direction, boolean isEnemyBullet) {
        this.gp = gp;
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.speed = 1; // Default bullet speed
        this.isEnemyBullet = isEnemyBullet;
        this.canDestroySteel = !isEnemyBullet && gp.player.starCount >= 3;

        // Define bullet collision area
        solidArea = new Rectangle(0, 0, 6, 6);

        // Load the bullet image
        getBulletImage();
    }

    public void update() {
        if (gp.gameState == gp.PLAY_STATE) {
            switch (direction) {
                case "up" -> y -= speed;
                case "down" -> y += speed;
                case "left" -> x -= speed;
                case "right" -> x += speed;
            }

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
        BufferedImage bulletImage = switch (direction) {
            case "up" -> upImage;
            case "down" -> downImage;
            case "left" -> leftImage;
            case "right" -> rightImage;
            default -> null;
        };

        if (bulletImage != null) {
            g2.drawImage(bulletImage, x, y, gp.TILE_SIZE - 10, gp.TILE_SIZE - 10, null);
        } else {
            // Fallback if image fails to load
            g2.setColor(Color.YELLOW);
            g2.fillOval(x, y, 6, 6);
        }
    }

    public void checkTileInteraction() {
        int margin = 4;
        int centerX = x + (gp.TILE_SIZE - 8) / 2;
        int centerY = y + (gp.TILE_SIZE - 8) / 2;

        int col = centerX / gp.TILE_SIZE;
        int row = centerY / gp.TILE_SIZE;

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int checkCol = col + i;
                int checkRow = row + j;

                if (checkCol < 0 || checkCol >= gp.maxScreenCol || checkRow < 0 || checkRow >= gp.maxScreenRow) {
                    continue;
                }

                int tileNum = gp.drawer.mapTileNum[checkCol][checkRow];
                Rectangle tileRect = new Rectangle(checkCol * gp.TILE_SIZE, checkRow * gp.TILE_SIZE, gp.TILE_SIZE, gp.TILE_SIZE);
                Rectangle expandedBulletRect = new Rectangle(centerX - margin, centerY - margin, 6 + 2 * margin, 6 + 2 * margin);

                if (gp.drawer.tile[tileNum].collision && expandedBulletRect.intersects(tileRect)) {
                    if (tileNum == 1) { // Brick tile
                        System.out.println("Bullet hit brick. Breaking it.");
                        gp.drawer.mapTileNum[checkCol][checkRow] = 0;
                        sound.setFile(2);
                        sound.play();
                        alive = false;
                        return;
                    } else if (tileNum == 2) { // Steel tile
                        handleSteelTileInteraction(tileNum, checkCol, checkRow);
                    }
                }
            }
        }
    }

    private void handleSteelTileInteraction(int tileNum, int checkCol, int checkRow) {
        if (canDestroySteel) {
            System.out.println("Bullet hit steel. Destroying it.");
            gp.drawer.mapTileNum[checkCol][checkRow] = 0;
            sound.setFile(2);
            sound.play();
            alive = false;
        } else if (isEnemyBullet || !canDestroySteel) {
            System.out.println("Bullet hit steel but cannot destroy.");
            alive = false;
        }
    }

    private void checkCollisionWithTarget() {
        Rectangle bulletRect = new Rectangle(x, y, 6, 6);

        if (isEnemyBullet) {
            handlePlayerCollision(bulletRect);
            handleBaseCollision(bulletRect);
        } else {
            handleEnemyCollision(bulletRect);
        }
    }

    private void handlePlayerCollision(Rectangle bulletRect) {
        Rectangle playerRect = new Rectangle(gp.player.x, gp.player.y, gp.TILE_SIZE * 2 - 6, gp.TILE_SIZE * 2 - 6);
        if (bulletRect.intersects(playerRect)) {
            if (!gp.player.getShield().isActive()) {
                System.out.println("Enemy bullet hit the player!");
                alive = false;
                gp.player.die();
                gp.explosions.add(new Explosion(gp, gp.player.x, gp.player.y));
            } else {
                System.out.println("Enemy bullet hit the shield - no damage to player.");
                alive = false;
            }
        }
    }

    private void handleBaseCollision(Rectangle bulletRect) {
        Rectangle baseRect = new Rectangle(gp.base.x, gp.base.y, gp.TILE_SIZE * 2 - 6, gp.TILE_SIZE * 2 - 6);
        if (bulletRect.intersects(baseRect)) {
            System.out.println("Enemy bullet hit the base!");
            alive = false;
            gp.gameState = gp.GAME_OVER_STATE;
        }
    }

    private void handleEnemyCollision(Rectangle bulletRect) {
        for (int i = 0; i < gp.npc.length; i++) {
            Enemy enemy = gp.npc[i];
            if (enemy != null && enemy.alive) {
                Rectangle enemyRect = new Rectangle(enemy.x, enemy.y, gp.TILE_SIZE * 2 - 6, gp.TILE_SIZE * 2 - 6);
                if (bulletRect.intersects(enemyRect)) {
                    System.out.println("Player bullet hit an enemy!");
                    alive = false;
                    enemy.lives--;

                    System.out.println("Enemy tank " + enemy.tankType + " live: " + enemy.lives);

                    if (enemy.lives <= 0) {
                        enemy.alive = false;
                        sound.setFile(2);
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
