package entity;

import main.GamePanel;
import main.Sound;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Enemy extends Entity {
    GamePanel gp;
    public boolean alive = true; // Add this attribute
    private TankType tankType;
    private final long shotCooldown = 1000;

public int point = 0;

    public void setFreezed(boolean freezed) {
        isFreezed = freezed;
    }

    private long lastShotTime;
    public ArrayList<Bullet> bullets = new ArrayList<>();
    Sound sound = new Sound();

    public boolean isFreezed = false;

    public Enemy(GamePanel gp) {
        this.gp = gp;
        solidArea = new Rectangle(0, 0, gp.tileSize * 2 - 6, gp.tileSize * 2 - 6);
        setRandomTankType();
        setDefaultValues();
        getPlayerImage();
    }

    private void setDefaultValues() {
        x = 132;
        y = 380;
        String[] directions = {"up", "down", "left", "right"};
        Random random = new Random();
        direction = directions[random.nextInt(directions.length)];
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
            return;  }
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
        int bulletWidth = gp.tileSize - 6;
        int bulletHeight = gp.tileSize - 6;

        int tankWidth = gp.tileSize * 2 - 6;
        int tankHeight = gp.tileSize * 2 - 6;

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
        this.point = tankType.point;
    }
    public void getPlayerImage(){
        try{
            String basePath = tankType.imagePath;
            up1 = ImageIO.read(getClass().getResourceAsStream(basePath + " (1).png"));
            up2 = ImageIO.read(getClass().getResourceAsStream(basePath + " (2).png"));
            left1 = ImageIO.read(getClass().getResourceAsStream(basePath + " (3).png"));
            left2 = ImageIO.read(getClass().getResourceAsStream(basePath + " (4).png"));
            down1 = ImageIO.read(getClass().getResourceAsStream(basePath + " (5).png"));
            down2 = ImageIO.read(getClass().getResourceAsStream(basePath + " (6).png"));
            right1 = ImageIO.read(getClass().getResourceAsStream(basePath + " (7).png"));
            right2 = ImageIO.read(getClass().getResourceAsStream(basePath + " (8).png"));


        }catch(IOException e){
            e.printStackTrace();
        }
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

        g2.drawImage(image, x, y, gp.tileSize * 2 - 6, gp.tileSize * 2 - 6, null);
    }
}
