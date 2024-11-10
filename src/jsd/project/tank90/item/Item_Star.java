package jsd.project.tank90.item;

import jsd.project.tank90.main.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;
/**
 * The Item_Star class represents the "Star" item that player can pick up in the game
 * 1 star: player tank's bullets fly faster
 * 2 stars: Player tank's shot cool down is lower (fire more quickly)
 * 3 stars: Player tank's bullets can destroy iron jsd.project.tank90.tile
 */
public class Item_Star extends SuperItem{
    GamePanel gp;
    public Item_Star(GamePanel gp) {
        this.gp = gp;
        name="Star";
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/jsd/project/tank90/res/items/star.png"));
            uTool.scaleImage(image, gp.TILE_SIZE*2 -6, gp.TILE_SIZE*2 -6);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
