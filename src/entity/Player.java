    package entity;

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

        Sound sound = new Sound();
        public ArrayList<Bullet> bullets = new ArrayList<>();
        private long lastShotTime;
        private final long shotCooldown = 1000;
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
                // CHECK NPC COLLISION
                int npcIndex = gp.cChecker.checkEntity(this, gp.npc);
                interactNPC(npcIndex);
                // CHECK ITEM COLLISION
                int itemIndex = gp.cChecker.checkItem(this, true);
                // IF COLLISION IS FALSE, PLAYER CAN MOVE
                if (!collisionOn){
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

            sound.setFile(1); // Adjust the index to match the firing sound
            sound.play();
            bullets.add(new Bullet(gp, bulletX, bulletY, direction));
            lastShotTime = System.currentTimeMillis();
        }

        private void interactNPC(int i) {
            if(i != 999){
                System.out.println("Hitting enemy");
            }
        }


        private boolean canFire() {
            return System.currentTimeMillis() - lastShotTime >= shotCooldown;
        }


        public void draw(Graphics2D g2){

            BufferedImage image = null;

            switch(direction){
                case "up":
                    if (spriteNum == 1){
                        image = up1;
                    } if (spriteNum == 2) {
                        image = up2;
                    }
                    break;
                case "down":
                    if (spriteNum == 1){
                        image = down1;
                    } if (spriteNum == 2) {
                    image = down2;
                }
                    break;
                case "left":
                    if (spriteNum == 1){
                        image = left1;
                    } if (spriteNum == 2) {
                    image = left2;
                }
                    break;
                case "right":
                    if (spriteNum == 1){
                        image = right1;
                    } if (spriteNum == 2) {
                    image = right2;
                }
                    break;
            }

            g2.drawImage(image, x, y, gp.tileSize*2 - 6, gp.tileSize*2 - 6, null);
        }
    }
