package jsd.project.tank90.item;

import jsd.project.tank90.main.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;
/**
 * The Item_Tank class represents the "Tank" item that player can pick up in the game
 * give an extra live
 */
public class Item_Tank extends SuperItem{
    GamePanel gp;
    public Item_Tank(GamePanel gp) {
        this.gp = gp;
        name="Tank";
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/jsd/project/tank90/res/items/tank.png"));
            uTool.scaleImage(image, gp.TILE_SIZE*2 -6, gp.TILE_SIZE*2 -6);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
