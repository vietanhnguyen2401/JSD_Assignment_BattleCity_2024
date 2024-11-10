package entity;


import main.GamePanel;
import main.Sound;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**
 * The Base Enemy represents the enemy tanks that player has to fight with in the game.
 */
public class Enemy extends Entity {
    GamePanel gp;
    public int lives; // lives of each enemy tank
    public boolean alive = true; // alive state
    public TankType tankType; // used to define the type of each enemy tank
    private final long shotCooldown = 1000; // shoot cooldown of each enemy tank
    public int point = 0; // points the player will get if he destroys an enemy tank, each type have different points
    private long lastShotTime; // used to track the bullet
    public ArrayList<Bullet> bullets = new ArrayList<>();
    public boolean isFreezed = false; // state to execute the "Timer" item pickup

    public Enemy(GamePanel gp) {
        this.gp = gp;
        solidArea = new Rectangle(0, 0, gp.TILE_SIZE * 2 - 6, gp.TILE_SIZE * 2 - 6);
        setRandomTankType();
        setDefaultValues();
        getEnemyImage();
    }

    private void setDefaultValues() {
        x = 132;
        y = 380;
        String[] directions = {"up", "down", "left", "right"};
        Random random = new Random();
        direction = directions[random.nextInt(directions.length)];
    }

    // function triggered when "Timer" is picked up by the player
    public void setFreezed(boolean freezed) {
        isFreezed = freezed;
    }

    public void setAction() {
            actionLockCounter++;
            if (actionLockCounter == 80) {
                Random r = new Random();
                int i = r.nextInt(100) + 1;
                if (i <= 25) {
                    direction = "up";
                }
                if (i > 25 && i <= 50) {
                    direction = "down";
                }
                if (i > 50 && i <= 75) {
                    direction = "left";
                }
                if (i > 75 && i <= 100) {
                    direction = "right";
                }
                actionLockCounter = 0;
            }

    }

    public void update() {
        if (!alive) {
            System.out.println("Enemy not alive, skipping update.");
            return;
        }
        if (!isFreezed) {
            setAction();
            collisionOn = false;
            gp.cChecker.checkTile(this);
            gp.cChecker.checkPlayer(this);
            boolean baseCollision = gp.cChecker.checkBaseCollision(this);

            // IF COLLISION IS FALSE, PLAYER CAN MOVE
            if (!collisionOn && !baseCollision) {
                switch (direction) {
                    case "up":
                        this.y -= speed;
                        break;
                    case "down":
                        this.y += speed;
                        break;
                    case "left":
                        this.x -= speed;
                        break;
                    case "right":
                        this.x += speed;
                        break;
                }
            }

            // Shooting
            if (canFire()) {
                fireBullet();
            }

            // Update bullets
            for (int i = 0; i < bullets.size(); i++) {
                Bullet bullet = bullets.get(i);
                if (bullet.alive) {
                    bullet.update();
                } else {
                    bullets.remove(i);
                    i--;
                }
            }
            updateSprites();
        }
    }

    private void fireBullet() {
        int bulletWidth = gp.TILE_SIZE - 6;
        int bulletHeight = gp.TILE_SIZE - 6;

        int tankWidth = gp.TILE_SIZE * 2 - 6;
        int tankHeight = gp.TILE_SIZE * 2 - 6;

        int tankCenterX = x + tankWidth / 2;
        int tankCenterY = y + tankHeight / 2;

        int bulletX = tankCenterX - bulletWidth / 2;
        int bulletY = tankCenterY - bulletHeight / 2;

        // Adjust bullet position based on tank's direction
        switch (direction) {
            case "up":
                bulletY = y - bulletHeight; // Start just above the tank
                break;
            case "down":
                bulletY = y + tankHeight; // Start just below the tank
                break;
            case "left":
                bulletX = x - bulletWidth; // Start just left of the tank
                break;
            case "right":
                bulletX = x + tankWidth; // Start just right of the tank
                break;
        }

        // Play firing sound
//        sound.setFile(1); // Adjust the index to match the firing sound
//        sound.play();
        bullets.add(new Bullet(gp, bulletX, bulletY, direction, true));
        lastShotTime = System.currentTimeMillis();
    }

    public void updateSprites() {
        spriteCounter++;
        if (spriteCounter > 10) {
            if (spriteNum == 1) {
                spriteNum = 2;
            } else if (spriteNum == 2) {
                spriteNum = 1;
            }
            spriteCounter = 0;
        }
    }

    private boolean canFire() {
        return System.currentTimeMillis() - lastShotTime >= shotCooldown;
    }

    private void setRandomTankType() {
        TankType[] types = TankType.values();
        Random random = new Random();
        this.tankType = types[random.nextInt(types.length)];
        this.speed = tankType.speed; // Use speed based on tank type
        this.lives = tankType.lives;
        this.point = tankType.point;
    }
    public void getEnemyImage(){
            String basePath = tankType.imagePath;
            up1 = setup(basePath + " (1).png");
            up2 = setup(basePath + " (2).png");
            left1 = setup(basePath + " (3).png");
            left2 = setup(basePath + " (4).png");
            down1 = setup(basePath + " (5).png");
            down2 = setup(basePath + " (6).png");
            right1 = setup(basePath + " (7).png");
            right2 = setup(basePath + " (8).png");
    }
    public BufferedImage setup(String imagePath){
        UtilityTool uTool = new UtilityTool();
        BufferedImage image = null;

        try{
            image = ImageIO.read(getClass().getResourceAsStream(imagePath));
            image = uTool.scaleImage(image, gp.TILE_SIZE * 2 - 6, gp.TILE_SIZE * 2 - 6);
        } catch(IOException e){
            e.printStackTrace();
        }
        return image;
    }


    public void draw(Graphics2D g2) {
        if (!alive) return; // Don't draw if the enemy is not alive

        BufferedImage image = null;

        switch (direction) {
            case "up":
                if (spriteNum == 1) {
                    image = up1;
                }
                if (spriteNum == 2) {
                    image = up2;
                }
                break;
            case "down":
                if (spriteNum == 1) {
                    image = down1;
                }
                if (spriteNum == 2) {
                    image = down2;
                }
                break;
            case "left":
                if (spriteNum == 1) {
                    image = left1;
                }
                if (spriteNum == 2) {
                    image = left2;
                }
                break;
            case "right":
                if (spriteNum == 1) {
                    image = right1;
                }
                if (spriteNum == 2) {
                    image = right2;
                }
                break;
        }

        g2.drawImage(image, x, y, gp.TILE_SIZE * 2 - 6, gp.TILE_SIZE * 2 - 6, null);
    }
}
