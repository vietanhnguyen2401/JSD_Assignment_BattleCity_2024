    package entity;

    import main.GamePanel;
    import main.KeyHandler;

    import javax.imageio.ImageIO;
    import java.awt.*;
    import java.awt.image.BufferedImage;
    import java.io.IOException;

    public class Player extends Entity{
        GamePanel gp;
        KeyHandler kh;
        public Player(GamePanel gp, KeyHandler kh){
            this.gp = gp;
            this.kh = kh;

            solidArea = new Rectangle(0,0, gp.tileSize*2 - 6, gp.tileSize*2 -6);
            setDefaultValues();
            getPlayerImage();
        }

        public void setDefaultValues(){
            x = 132;
            y = 380;
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


            }catch(IOException e){
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
