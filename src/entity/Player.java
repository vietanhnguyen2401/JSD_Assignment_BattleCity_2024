    package entity;

    import item.Shield;
    import main.GamePanel;
    import main.KeyHandler;
    import main.Sound;

    import javax.imageio.ImageIO;
    import java.awt.*;
    import java.awt.image.BufferedImage;
    import java.io.IOException;
    import java.util.ArrayList;

    public class Player extends Entity{
        GamePanel gp;
        KeyHandler kh;
        Shield shield;
        Sound sound = new Sound();

        // Bullet Assist
        public ArrayList<Bullet> bullets = new ArrayList<>();
        private long lastShotTime;
        private final long shotCooldown = 300;
        // Revive Assist
        public int lives = 3; // Number of lives the player has
        private boolean isDead = false; // Tracks if the player is currently dead
        private long respawnTime = 500; // 2-second respawn delay
        private long deathTime; // Time at which player died
        private boolean isFlickering = false; // To track flickering state
        private int flickerCounter = 0; // Counter to control flicker effect
        private final int flickerDuration = 60; // Total duration for flickering


        public Player(GamePanel gp, KeyHandler kh){
            this.gp = gp;
            this.kh = kh;

            solidArea = new Rectangle(0,0, gp.tileSize*2 - 6, gp.tileSize*2 - 6);

            solidAreaDefaultX = solidArea.x;
            solidAreaDefaultY = solidArea.y;
            setDefaultValues();
            getPlayerImage();
        }

        public void setDefaultValues(){
            x = 132;
            y = 400;
            speed = 1;
            direction = "up";
            shield = new Shield(120, 10, gp.tileSize * 2 + 5);
        }

        public void getPlayerImage(){
            try{
                up1 = ImageIO.read(getClass().getResourceAsStream("/res/player/yellow_small (1).png"));
                up2 = ImageIO.read(getClass().getResourceAsStream("/res/player/yellow_small (2).png"));

                left1 = ImageIO.read(getClass().getResourceAsStream("/res/player/yellow_small (3).png"));
                left2 = ImageIO.read(getClass().getResourceAsStream("/res/player/yellow_small (4).png"));

                down1 = ImageIO.read(getClass().getResourceAsStream("/res/player/yellow_small (5).png"));
                down2 = ImageIO.read(getClass().getResourceAsStream("/res/player/yellow_small (6).png"));

                right1 = ImageIO.read(getClass().getResourceAsStream("/res/player/yellow_small (7).png"));
                right2 = ImageIO.read(getClass().getResourceAsStream("/res/player/yellow_small (8).png"));


            } catch(IOException e) {
                e.printStackTrace();
            }
        }

        public void updateSprites(){
            spriteCounter++;
            if (spriteCounter > 10) {
                if (spriteNum == 1){
                    spriteNum = 2;
                } else if (spriteNum == 2){
                    spriteNum = 1;
                }
                spriteCounter=0;
            }
        }
        public void update(){
            if (isDead) {
                if (System.currentTimeMillis() - deathTime >= respawnTime) {
                    revive();
                }

                if (isFlickering) {
                    flickerCounter++;
                    if (flickerCounter >= flickerDuration) {
                        isFlickering = false;
                    }
                }
                return;
            }

            shield.update(x, y);

            if (kh.downPressed || kh.upPressed || kh.leftPressed || kh.rightPressed){
                if (kh.downPressed){
                    direction = "down";
                    updateSprites();

                }
                else if (kh.upPressed){
                    direction = "up";
                    updateSprites();

                }
                else if (kh.leftPressed){
                    direction = "left";
                    updateSprites();

                }
                else if (kh.rightPressed){
                    direction = "right";

                    updateSprites();
                }


                // CHECK TILE COLLISION
                collisionOn = false;
                gp.cChecker.checkTile(this);
                boolean baseCollision = gp.cChecker.checkBaseCollision(this);
                // CHECK NPC COLLISION
                int npcIndex = gp.cChecker.checkEntity(this, gp.npc);
                interactNPC(npcIndex);
                // CHECK ITEM COLLISION
                int itemIndex = gp.cChecker.checkItem(this, true);
                // IF COLLISION IS FALSE, PLAYER CAN MOVE
                if (!collisionOn && !baseCollision){
                    switch (direction){
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
            }

            // Shooting
            if (kh.shootPressed && canFire()) {
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


        }

        public void die() {
            lives--;
            isDead = true;
            deathTime = System.currentTimeMillis();
            System.out.println("Player has died! Lives left: " + lives);

            if (lives <= 0) {
                System.out.println("Game Over! No lives left.");
                // Optionally, set game over state here if needed
            }
        }

        public void revive() {
            if (lives > 0) {
                System.out.println("Player is reviving...");
                isDead = false;
                isFlickering = true;
                shield.activate(x, y); // Activate shield on revive
                x = 132;
                y = 400;
            } else {
                System.out.println("No lives left. Cannot revive.");
            }
        }


        private void fireBullet() {
            int bulletWidth = gp.tileSize - 10;
            int bulletHeight = gp.tileSize - 10;

            int tankWidth = gp.tileSize * 2 - 6;
            int tankHeight = gp.tileSize * 2 - 6;

            int tankCenterX = x + tankWidth / 2;
            int tankCenterY = y + tankHeight / 2;

            int bulletX = tankCenterX - bulletWidth / 2;
            int bulletY = tankCenterY - bulletHeight / 2;

            // Adjust bullet position based on tank's direction
            switch (direction) {
                case "up":
                    bulletY = y - bulletHeight/2 - 5; // Start just above the tank
                    break;
                case "down":
                    bulletY = y + tankHeight/2 - 5; // Start just below the tank
                    break;
                case "left":
                    bulletX = x - bulletWidth/2 - 5; // Start just left of the tank
                    break;
                case "right":
                    bulletX = x + tankWidth/2 - 5; // Start just right of the tank
                    break;
            }

            // Play firing sound

            sound.setFile(1); // Adjust the index to match the firing sound
            sound.play();
            bullets.add(new Bullet(gp, bulletX, bulletY, direction, false));
            lastShotTime = System.currentTimeMillis();
        }

        private void interactNPC(int i) {
            if(i != 999){
                System.out.println("Hitting enemy");
            }
        }

        public Shield getShield() {
            return shield;
        }


        private boolean canFire() {
            return System.currentTimeMillis() - lastShotTime >= shotCooldown;
        }


        public void draw(Graphics2D g2) {
            boolean shouldDraw = !isDead || (isDead && flickerCounter % 10 < 5);

            if (shouldDraw) {
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
            shield.draw(g2);
        }
    }
