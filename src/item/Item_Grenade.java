package item;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;
/**
 * The Item_Grenade class represents the "Grenade" item that player can pick up in the game
 * destroy all enemy tanks existing on the Game screen, not giving points for player
 */
public class Item_Grenade extends SuperItem{
    GamePanel gp;
    public Item_Grenade(GamePanel gp) {
        this.gp = gp;
        name = "Grenade";
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/res/items/grenade.png"));
            uTool.scaleImage(image, gp.TILE_SIZE*2 -6, gp.TILE_SIZE*2 -6);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
