package entity;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Base extends Entity {
    GamePanel gp;

    public BufferedImage normalBase, destroyedBase, explosion1, explosion2;

    public Base(GamePanel gp){
        this.gp = gp;


        solidArea = new Rectangle(0,0, gp.tileSize*2 - 6, gp.tileSize*2 - 6);

        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        setDefaultValues();
        getBaseImage();
    }

    public void setDefaultValues(){
        x = gp.screenWidth / 2 - (gp.tileSize*2 - 6) / 2;
        y = 404;

    }

    public void getBaseImage(){
        try{
            normalBase = ImageIO.read(getClass().getResourceAsStream("/res/base/base.png"));
            destroyedBase = ImageIO.read(getClass().getResourceAsStream("/res/base/destroyed_base.png"));
            explosion1 = ImageIO.read(getClass().getResourceAsStream("/res/explosion/small.png"));
            explosion2 = ImageIO.read(getClass().getResourceAsStream("/res/explosion/big.png"));
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void update(){
        if (gp.gameState == gp.GAME_OVER_STATE){

        }
    }
    public void draw(Graphics2D g2){
        BufferedImage image = null;
        if (gp.gameState == gp.GAME_OVER_STATE){
            image = destroyedBase;
        } else if (gp.gameState == gp.PLAY_STATE || gp.gameState == gp.PAUSE_STATE){
            image = normalBase;
        }
        g2.drawImage(image, x, y, gp.tileSize*2 - 6, gp.tileSize*2 - 6, null);

    }


}

