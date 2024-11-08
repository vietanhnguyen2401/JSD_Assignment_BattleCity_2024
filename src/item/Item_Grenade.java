package item;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;

public class Item_Grenade extends SuperItem{
    GamePanel gp;
    public Item_Grenade(GamePanel gp) {
        this.gp = gp;
        name = "Grenade";
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/res/items/grenade.png"));
            uTool.scaleImage(image, gp.tileSize*2 -6, gp.tileSize*2 -6);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
