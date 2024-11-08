package item;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;

public class Item_Shovel extends SuperItem {
    GamePanel gp;
    public Item_Shovel(GamePanel gp) {
        this.gp = gp;
        name="Shovel";
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/res/items/shovel.png"));
            uTool.scaleImage(image, gp.tileSize*4 , gp.tileSize*4 );

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
