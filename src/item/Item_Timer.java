package item;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;
/**
 * The Item_Timer class represents the "Timer" item that player can pick up in the game
 * freeze all enemy tanks on the screen for 3 seconds
 */
public class Item_Timer extends SuperItem{
    GamePanel gp;
    public Item_Timer(GamePanel gp) {
        this.gp = gp;
        name="Timer";
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/res/items/timer.png"));
            uTool.scaleImage(image, gp.TILE_SIZE*2 -6, gp.TILE_SIZE*2 -6);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
