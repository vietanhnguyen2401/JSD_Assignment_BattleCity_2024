package item;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;
/**
 * The Item_Helmet class represents the "Helmet" item that player can pick up in the game
 * Give the player tank a Shield in 3 seconds
 */
public class Item_Helmet extends SuperItem{
    GamePanel gp;
    public Item_Helmet(GamePanel gp) {
        this.gp = gp;
        name="Helmet";
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/res/items/helmet.png"));
            uTool.scaleImage(image, gp.TILE_SIZE*2 -6, gp.TILE_SIZE*2 -6);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
