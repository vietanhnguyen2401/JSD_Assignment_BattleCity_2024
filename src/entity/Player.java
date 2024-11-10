package entity;

import item.Shield;
import main.GamePanel;
import main.KeyHandler;
import main.UtilityTool;
import main.Sound;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Represents the player entity with movement, shooting, and power-up abilities.
 */
public class Player extends Entity implements Runnable {

    // Directional sprites
    public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;
    private BufferedImage shield1, shield2;

    // References and helpers
    private final GamePanel gp;
    private final KeyHandler kh;
    private final Sound sound = new Sound();
    private Shield shield;

    // Player stats and status
    public int starCount = 3;
    public int lives = 3;
    private long lastShotTime;
    private long shotCooldown = 1000;
    private boolean isDead = false;
    private boolean canDestroySteel = false;

    // Timing and effects
    private final long respawnTime = 500;
    private long deathTime;
    private boolean isFlickering = false;
    private int flickerCounter = 0;
    private final int flickerDuration = 60;

    // Movement and bullets
    private boolean running = true;
    public ArrayList<Bullet> bullets = new ArrayList<>();

    public Player(GamePanel gp, KeyHandler kh) {
        this.gp = gp;
        this.kh = kh;
        this.shield = new Shield(4 * 60, 10, gp.TILE_SIZE * 2 - 5);

        solidArea = new Rectangle(0, 0, gp.TILE_SIZE * 2 - 6, gp.TILE_SIZE * 2 - 6);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        setDefaultValues();
        loadPlayerImages();
    }

    @Override
    public void run() {
        while (running) {
            update();
            try {
                Thread.sleep(16); // Approx 60 updates per second
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void stop() {
        running = false; // To stop the player thread when the game ends
    }

    public void setDefaultValues() {
        x = 132;
        y = 400;
        starCount = 0;
        lives = 3;
        shotCooldown = 1000;
        canDestroySteel = false;
        speed = 1;
        direction = "up";
        shield.activate(x, y);
    }

    private void loadPlayerImages() {
        up1 = loadImage("/res/player/yellow_small (1).png");
        up2 = loadImage("/res/player/yellow_small (2).png");
        down1 = loadImage("/res/player/yellow_small (5).png");
        down2 = loadImage("/res/player/yellow_small (6).png");
        left1 = loadImage("/res/player/yellow_small (3).png");
        left2 = loadImage("/res/player/yellow_small (4).png");
        right1 = loadImage("/res/player/yellow_small (7).png");
        right2 = loadImage("/res/player/yellow_small (8).png");
        shield1 = loadImage("/res/shield/shield1.png");
        shield2 = loadImage("/res/shield/shield2.png");
    }

    private BufferedImage loadImage(String imagePath) {
        UtilityTool uTool = new UtilityTool();
        BufferedImage image = null;
        try {
            image = ImageIO.read(getClass().getResourceAsStream(imagePath));
            image = uTool.scaleImage(image, gp.TILE_SIZE * 2 - 6, gp.TILE_SIZE * 2 - 6);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    public void updateSprites() {
        spriteCounter++;
        if (spriteCounter > 10) {
            spriteNum = (spriteNum == 1) ? 2 : 1;
            spriteCounter = 0;
        }
    }

    public void update() {
        if (isDead) {
            handleRevival();
            return;
        }

        shield.update(x, y);
        handleMovement();
        handleShooting();
        updateBullets();
        checkCheatCode();
    }

    private void handleRevival() {
        if (System.currentTimeMillis() - deathTime >= respawnTime) {
            revive();
        }
        if (isFlickering) {
            flickerCounter++;
            if (flickerCounter >= flickerDuration) {
                isFlickering = false;
            }
        }
    }

    private void handleMovement() {
        if (kh.downPressed || kh.upPressed || kh.leftPressed || kh.rightPressed) {
            if (kh.downPressed) direction = "down";
            if (kh.upPressed) direction = "up";
            if (kh.leftPressed) direction = "left";
            if (kh.rightPressed) direction = "right";

            updateSprites();
            checkCollisions();
        }
    }

    private void checkCollisions() {
        collisionOn = false;
        gp.cChecker.checkTile(this);
        boolean baseCollision = gp.cChecker.checkBaseCollision(this);
        int npcIndex = gp.cChecker.checkEntity(this, gp.npc);
        interactWithNPC(npcIndex);

        int itemIndex = gp.cChecker.checkItem(this, true);
        pickUpItem(itemIndex);

        if (!collisionOn && !baseCollision) {
            move();
        }
    }

    private void move() {
        switch (direction) {
            case "up" -> y -= speed;
            case "down" -> y += speed;
            case "left" -> x -= speed;
            case "right" -> x += speed;
        }
    }

    private void handleShooting() {
        if (kh.shootPressed && canFire()) {
            fireBullet();
        }
    }

    private void fireBullet() {
        int bulletX = x + (gp.TILE_SIZE * 2 - 6) / 2 - (gp.TILE_SIZE - 10) / 2;
        int bulletY = y + (gp.TILE_SIZE * 2 - 6) / 2 - (gp.TILE_SIZE - 10) / 2;

        switch (direction) {
            case "up" -> bulletY = y - (gp.TILE_SIZE - 10) / 2 - 5;
            case "down" -> bulletY = y + (gp.TILE_SIZE * 2 - 6) / 2 - 5;
            case "left" -> bulletX = x - (gp.TILE_SIZE - 10) / 2 - 5;
            case "right" -> bulletX = x + (gp.TILE_SIZE * 2 - 6) / 2 - 5;
        }

        sound.setFile(1);
        sound.play();
        bullets.add(new Bullet(gp, bulletX, bulletY, direction, false));
        lastShotTime = System.currentTimeMillis();
    }

    private void updateBullets() {
        for (int i = 0; i < bullets.size(); i++) {
            Bullet bullet = bullets.get(i);
            if (bullet.alive) {
                bullet.speed = (starCount >= 1) ? 3 : bullet.speed;
                shotCooldown = (starCount >= 2) ? 400 : shotCooldown;
                bullet.update();
            } else {
                bullets.remove(i);
                i--;
            }
        }
    }

    public void die() {
        lives--;
        isDead = true;
        deathTime = System.currentTimeMillis();
        resetPowerLevel();
        System.out.println("Player has died! Lives left: " + lives);

        if (lives <= 0) {
            System.out.println("Game Over! No lives left.");
        }
    }

    public void revive() {
        if (lives > 0) {
            System.out.println("Player is reviving...");
            isDead = false;
            isFlickering = true;
            shield.activate(x, y);
            x = 132;
            y = 400;
        } else {
            System.out.println("No lives left. Cannot revive.");
        }
    }

    public void resetPowerLevel() {
        speed = 1;
        starCount = 0;
        shotCooldown = 1000;
        for (Bullet bullet : bullets) {
            bullet.speed = 2;
        }
        canDestroySteel = false;
        shield.setDuration(4 * 60);
    }
    private void checkCheatCode() {
        if (kh.cheatPressed) {  // Assume `kh.cheatPressed` is set when the player types "cheat"
            starCount = 3;
            shield.setDuration(10000 * 60);
            this.shield.activate(x, y);
            canDestroySteel = true;
            speed = 5;
        }else if (kh.normalPressed) {
           resetPowerLevel();
            kh.normalPressed = false;
        }
    }

    private void interactWithNPC(int i) {
        if (i != 999) {
            if (!shield.isActive()) {
                die();
                gp.explosions.add(new Explosion(gp, x, y));
                System.out.println("Hitting enemy - Player dies");
            } else {
                System.out.println("Hitting enemy - Shield is active, no damage to player");
            }
        }
    }

    public Shield getShield() {
        return shield;
    }

    private boolean canFire() {
        return System.currentTimeMillis() - lastShotTime >= shotCooldown;
    }

    public void pickUpItem(int i) {
        if (i != 999) {
            gp.item[i] = null;
        }
    }

    public void draw(Graphics2D g2) {
        boolean shouldDraw = !isDead || (isDead && flickerCounter % 10 < 5);

        if (shouldDraw) {
            BufferedImage image = switch (direction) {
                case "up" -> (spriteNum == 1) ? up1 : up2;
                case "down" -> (spriteNum == 1) ? down1 : down2;
                case "left" -> (spriteNum == 1) ? left1 : left2;
                case "right" -> (spriteNum == 1) ? right1 : right2;
                default -> null;
            };
            g2.drawImage(image, x, y, gp.TILE_SIZE * 2 - 6, gp.TILE_SIZE * 2 - 6, null);
        }
        shield.draw(g2);
    }
}
